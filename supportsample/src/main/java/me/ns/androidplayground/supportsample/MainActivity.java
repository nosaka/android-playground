package me.ns.androidplayground.supportsample;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.LogUtil;

public class MainActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * リクエストコード：ギャラリーオープン
     */
    private static final int REQUEST_OPEN_GALLERY = 0x001;

    /**
     * カスタムタグアクション BroadcastReceiver
     */
    private static class CustomTabActionReceiver extends BroadcastReceiver {

        static final String ACTION_CUSTOM_ID_1 = "ACTION_CUSTOM_ID_1";

        static final String ACTION_CUSTOM_ID_2 = "ACTION_CUSTOM_ID_2";

        static final String ACTION_CUSTOM_ID_3 = "ACTION_CUSTOM_ID_3";

        static final String ACTION_CUSTOM_ID_4 = "ACTION_CUSTOM_ID_4";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CUSTOM_ID_1.equals(intent.getAction())) {
                Toast.makeText(context, "TEST1", Toast.LENGTH_SHORT).show();
            } else if (ACTION_CUSTOM_ID_2.equals(intent.getAction())) {
                Toast.makeText(context, "TEST2", Toast.LENGTH_SHORT).show();
            } else if (ACTION_CUSTOM_ID_3.equals(intent.getAction())) {
                Toast.makeText(context, "TEST3", Toast.LENGTH_SHORT).show();
            } else if (ACTION_CUSTOM_ID_4.equals(intent.getAction())) {
                Toast.makeText(context, "TEST4", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_ExampleCardView)
    CardView mExampleCardView;

    @BindView(R.id.main_ExampleImageView)
    ImageView mImageView;

    @BindView(R.id.main_ExampleTitleTextView)
    TextView mExampleTitleTextView;

    @BindView(R.id.main_ExampleBodyTextView)
    TextView mExampleBodyTextView;

    @BindView(R.id.main_ExtractImageButton)
    Button mExtractImageButton;

    @BindView(R.id.main_CustomTabButton)
    Button mCustomTabButton;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * 現在画像Swatch一覧
     */
    private List<Palette.Swatch> mCurrentImageSwatchList;

    /**
     * 現在Swatch
     */
    private Palette.Swatch mCurrentSwatch;

    /**
     * {@link CustomTabActionReceiver}
     */
    private CustomTabActionReceiver mCustomTabActionReceiver;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mExtractImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT)
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("image/*"), REQUEST_OPEN_GALLERY);
            }
        });
        mExampleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSwatch();
            }
        });
        mCustomTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChrome();
            }
        });

        // CustomTabActionReceiverの登録
        registerCustomTabActionReceiver();
    }

    @Override
    protected void onDestroy() {
        // CustomTabActionReceiverの解除
        unregisterCustomTabActionReceiver();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_OPEN_GALLERY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null && data.getData() != null) {
                            Bitmap bitmap = getBitmap(data.getData());
                            mImageView.setImageBitmap(bitmap);
                            decodePalette(bitmap);
                            blurBitmap(bitmap);
                        }
                        break;
                    default:
                        // 処理なし
                }
                break;
            default:
                // 処理なし
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link Bitmap}取得処理
     *
     * @param uri {@link Uri}
     * @return {@link Bitmap}
     */
    private Bitmap getBitmap(Uri uri) {

        ParcelFileDescriptor descriptor = null;
        try {
            descriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (descriptor == null) {
                return null;
            }
            FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
            return BitmapFactory.decodeFileDescriptor(fileDescriptor);
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            if (descriptor != null) {
                try {
                    descriptor.close();
                } catch (IOException ignored) {
                    LogUtil.e(ignored);
                }
            }
        }

        return null;
    }


    /**
     * Paletteデコード処理
     *
     * @param bitmap {@link Bitmap}
     */
    private void decodePalette(Bitmap bitmap) {
        mCurrentImageSwatchList = null;
        mCurrentSwatch = null;

        Palette.Builder builder = Palette.from(bitmap);
        Palette palette = builder.generate();
        mCurrentImageSwatchList = palette.getSwatches();

        // 切替処理
        switchSwatch();
    }

    /**
     * {@link Palette.Swatch}切替処理
     */
    private void switchSwatch() {
        if (mCurrentImageSwatchList != null && mCurrentImageSwatchList.size() > 0) {
            int index = mCurrentImageSwatchList.indexOf(mCurrentSwatch);
            if (index != -1 && index < mCurrentImageSwatchList.size() - 1) {
                index++;
            } else {
                index = 0;
            }
            mCurrentSwatch = mCurrentImageSwatchList.get(index);
            mExampleCardView.setBackgroundColor(mCurrentSwatch.getRgb());
            mExampleTitleTextView.setTextColor(mCurrentSwatch.getTitleTextColor());
            mExampleBodyTextView.setTextColor(mCurrentSwatch.getBodyTextColor());
        }

    }

    /**
     * Bitmapのブラー処理
     *
     * @param bitmap {@link Bitmap}
     */
    private void blurBitmap(Bitmap bitmap) {
        final RenderScript rs = RenderScript.create(this);
        final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(20.f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
    }

    /**
     * Chrome表示
     */
    private void openChrome() {
        final CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .addMenuItem("ACTION_SEND", PendingIntent.getActivity(this, 0,
                        new Intent(Intent.ACTION_SEND).setType("text/plain"),
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        tabsIntent.launchUrl(this, Uri.parse("http:google.com"));
    }

    /**
     * {@link #mCustomTabActionReceiver}登録処理
     */
    private void registerCustomTabActionReceiver() {
        mCustomTabActionReceiver = new CustomTabActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomTabActionReceiver.ACTION_CUSTOM_ID_1);
        filter.addAction(CustomTabActionReceiver.ACTION_CUSTOM_ID_2);
        filter.addAction(CustomTabActionReceiver.ACTION_CUSTOM_ID_3);
        filter.addAction(CustomTabActionReceiver.ACTION_CUSTOM_ID_4);
        registerReceiver(mCustomTabActionReceiver, filter);
    }

    /**
     * {@link #mCustomTabActionReceiver}解除処理
     */
    private void unregisterCustomTabActionReceiver() {
        if (mCustomTabActionReceiver != null) {
            unregisterReceiver(mCustomTabActionReceiver);
        }
    }

}
