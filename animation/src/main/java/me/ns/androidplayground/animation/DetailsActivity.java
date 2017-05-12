package me.ns.androidplayground.animation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.CacheUtil;

public class DetailsActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * Shared Transition Name：タイトル
     */
    public static final String TRANSITION_NAME_TITLE = "title";

    /**
     * Shared Transition Name：画像
     */
    public static final String TRANSITION_NAME_IMAGE = "image";

    /**
     * Bundleキー：画像キャッシュキー
     */
    public static final String KEY_BUNDLE_BITMAP_CACHE_KEY = "image_bitmap";

    /**
     * Bundleキー：要素インデックス
     */
    public static final String KEY_BUNDLE_ITEM_INDEX = "item_index";

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.details_Toolbar)
    Toolbar mToolbar;

    @BindView(R.id.details_ImageView)
    ImageView mImageView;

    @BindView(R.id.details_BodyTextView)
    TextView mBodyTextView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * 画像
     */
    private Bitmap mBitmap;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(KEY_BUNDLE_BITMAP_CACHE_KEY)) {
            mBitmap = CacheUtil.getBitmapCache(this, getIntent().getStringExtra(KEY_BUNDLE_BITMAP_CACHE_KEY));
        }

        if (getIntent() != null) {
            Constants.Item item = Constants.ITEMS[getIntent().getIntExtra(KEY_BUNDLE_ITEM_INDEX, -1)];
            mToolbar.setTitle(item.title);
            mBodyTextView.setText(item.body);
        }
        mImageView.setImageBitmap(mBitmap);

        if (mBitmap != null) {
            Palette.Builder builder = Palette.from(mBitmap);
            Palette palette = builder.generate();
            Palette.Swatch swatch = palette.getDominantSwatch();
            if (swatch != null) {
                mToolbar.setTitleTextColor(swatch.getTitleTextColor());
                mToolbar.setBackgroundColor(swatch.getRgb());
            }
        }
        setSupportActionBar(mToolbar);

        mToolbar.setTransitionName(TRANSITION_NAME_TITLE);
        mImageView.setTransitionName(TRANSITION_NAME_IMAGE);
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link Intent}生成
     *
     * @param context {@link Context}
     * @return {@link Intent}
     */
    public static Intent newIntent(Context context, String bitmapCacheKey, int itemIndex) {
        return new Intent(context, DetailsActivity.class)
                .putExtra(KEY_BUNDLE_BITMAP_CACHE_KEY, bitmapCacheKey)
                .putExtra(KEY_BUNDLE_ITEM_INDEX, itemIndex);
    }

    /**
     * {@link ActivityOptionsCompat}{@link Bundle}生成
     *
     * @param activity  {@link Activity}
     * @param titleView タイトル{@link View}
     * @param imageView 画像{@link View}
     * @return {@link ActivityOptionsCompat}{@link Bundle}
     */
    public static Bundle newActivityOptions(Activity activity, View titleView, View imageView) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair<>(titleView, TRANSITION_NAME_TITLE),
                new Pair<>(imageView, TRANSITION_NAME_IMAGE)).toBundle();
    }

}
