package me.ns.androidplayground.appshortcuts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Activity
 * <p>
 * Created by shintaro.nosaka on 2017/05/02.
 */
public class MainActivity extends Activity {

    /**
     * メニューID：ショートカット追加
     */
    private static final int MENU_ADD_SHORT_CUT = 0x001;

    @BindView(R.id.main_Toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_ListView)
    ListView mListView;

    /**
     * ショートカット選択アダプタ
     */
    private ArrayAdapter<ShortcutItem> mShortcutsAdapter;

    /**
     * メイン画面リストアダプタ
     */
    private ArrayAdapter<AdapterItem> mListAdapter;

    /**
     * {@link ShortcutManager}
     */
    private ShortcutManager mShortcutManager;

    /**
     * アダプタ要素
     */
    private static class AdapterItem {

        ShortcutInfo shortcutInfo;

        AdapterItem(ShortcutInfo value) {
            shortcutInfo = value;
        }

        @Override
        public String toString() {
            return shortcutInfo != null && shortcutInfo.getShortLabel() != null ? shortcutInfo.getShortLabel().toString() : "NONE";
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setActionBar(mToolbar);

        mShortcutManager = getSystemService(ShortcutManager.class);

        mShortcutsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mShortcutsAdapter.addAll(ShortcutItem.items());

        mListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mListAdapter);
        // アダプタ同期
        syncAdapter();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AdapterItem item = mListAdapter.getItem(position);
                if (item != null) {
                    confirmRemoveShortcut(item.shortcutInfo);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ADD_SHORT_CUT, Menu.NONE, "追加");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ADD_SHORT_CUT:
                confirmAddShortcut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * ショートカット削除確認
     */
    private void confirmRemoveShortcut(final ShortcutInfo shortcut) {
        new AlertDialog.Builder(this)
                .setMessage("このショートカットを削除しますか")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mShortcutManager.removeDynamicShortcuts(Collections.singletonList(shortcut.getId()));
                        syncAdapter();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    /**
     * ショートカット追加確認
     */
    private void confirmAddShortcut() {
        new AlertDialog.Builder(this)
                .setAdapter(mShortcutsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShortcutItem item = mShortcutsAdapter.getItem(which);
                        if (item == null) {
                            return;
                        }
                        ShortcutInfo shortcut = new ShortcutInfo.Builder(MainActivity.this, item.id)
                                .setShortLabel(item.shortLabel)
                                .setLongLabel(item.longLabel)
                                .setIcon(Icon.createWithResource(MainActivity.this, item.icon))
                                .setIntent(item.getIntent())
                                .build();
                        mShortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
                        // アダプタ同期
                        syncAdapter();
                    }
                })
                .create()
                .show();
    }

    /**
     * アダプタ同期処理
     */
    private void syncAdapter() {
        mListAdapter.clear();
        List<ShortcutInfo> shortcuts = mShortcutManager.getDynamicShortcuts();
        List<AdapterItem> items = new ArrayList<>();
        for (ShortcutInfo shortcut : shortcuts) {
            items.add(new AdapterItem(shortcut));
        }
        mListAdapter.addAll(items);
    }
}
