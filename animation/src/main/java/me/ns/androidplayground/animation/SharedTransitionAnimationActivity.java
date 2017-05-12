package me.ns.androidplayground.animation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.CacheUtil;

public class SharedTransitionAnimationActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * 共通Bitmapキャッシュキー
     */
    private static final String SHARED_BITMAP_CACHE_KEY = "shared_bitmap_cache_key";

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.sharedTransitionAnimation_Toolbar)
    Toolbar mToolbar;

    @BindView(R.id.sharedTransitionAnimation_TransitionRadioGroup)
    RadioGroup mTransitionRadioGroup;

    @BindView(R.id.sharedTransitionAnimation_Example1CardView)
    CardView mExample1CardView;

    @BindView(R.id.sharedTransitionAnimation_Example1ImageView)
    ImageView mExample1ImageView;

    @BindView(R.id.sharedTransitionAnimation_Example1TitleTextView)
    TextView mExample1TitleTextView;

    @BindView(R.id.sharedTransitionAnimation_Example1BodyTextView)
    TextView mExample1BodyTextView;

    @BindView(R.id.sharedTransitionAnimation_Example2CardView)
    CardView mExample2CardView;

    @BindView(R.id.sharedTransitionAnimation_Example2ImageView)
    ImageView mExample2ImageView;

    @BindView(R.id.sharedTransitionAnimation_Example2TitleTextView)
    TextView mExample2TitleTextView;

    @BindView(R.id.sharedTransitionAnimation_Example2BodyTextView)
    TextView mExample2BodyTextView;

    @BindView(R.id.sharedTransitionAnimation_Example3CardView)
    CardView mExample3CardView;

    @BindView(R.id.sharedTransitionAnimation_Example3ImageView)
    ImageView mExample3ImageView;

    @BindView(R.id.sharedTransitionAnimation_Example3TitleTextView)
    TextView mExample3TitleTextView;

    @BindView(R.id.sharedTransitionAnimation_Example3BodyTextView)
    TextView mExample3BodyTextView;

    @BindView(R.id.sharedTransitionAnimation_Example4CardView)
    CardView mExample4CardView;

    @BindView(R.id.sharedTransitionAnimation_Example4ImageView)
    ImageView mExample4ImageView;

    @BindView(R.id.sharedTransitionAnimation_Example4TitleTextView)
    TextView mExample4TitleTextView;

    @BindView(R.id.sharedTransitionAnimation_Example4BodyTextView)
    TextView mExample4BodyTextView;


    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_transition_animation);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mExample1ImageView.setImageResource(Constants.ITEMS[0].imageResourceId);
        mExample1TitleTextView.setText(Constants.ITEMS[0].title);
        mExample1BodyTextView.setText(Constants.ITEMS[0].body);
        mExample2ImageView.setImageResource(Constants.ITEMS[1].imageResourceId);
        mExample2TitleTextView.setText(Constants.ITEMS[1].title);
        mExample2BodyTextView.setText(Constants.ITEMS[1].body);
        mExample3ImageView.setImageResource(Constants.ITEMS[2].imageResourceId);
        mExample3TitleTextView.setText(Constants.ITEMS[2].title);
        mExample3BodyTextView.setText(Constants.ITEMS[2].body);
        mExample4ImageView.setImageResource(Constants.ITEMS[3].imageResourceId);
        mExample4TitleTextView.setText(Constants.ITEMS[3].title);
        mExample4BodyTextView.setText(Constants.ITEMS[3].body);

        onChangeTransition();

        mTransitionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                onChangeTransition();
            }
        });
        mExample1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) mExample1ImageView.getDrawable()).getBitmap();
                String key = CacheUtil.saveCache(SharedTransitionAnimationActivity.this, bitmap, SHARED_BITMAP_CACHE_KEY);
                startActivity(DetailsActivity.newIntent(SharedTransitionAnimationActivity.this, key, 0),
                        DetailsActivity.newActivityOptions(SharedTransitionAnimationActivity.this, mExample1TitleTextView, mExample1ImageView));
            }
        });
        mExample2CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) mExample2ImageView.getDrawable()).getBitmap();
                String key = CacheUtil.saveCache(SharedTransitionAnimationActivity.this, bitmap, SHARED_BITMAP_CACHE_KEY);
                startActivity(DetailsActivity.newIntent(SharedTransitionAnimationActivity.this, key, 1),
                        DetailsActivity.newActivityOptions(SharedTransitionAnimationActivity.this, mExample2TitleTextView, mExample2ImageView));
            }
        });
        mExample3CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) mExample3ImageView.getDrawable()).getBitmap();
                String key = CacheUtil.saveCache(SharedTransitionAnimationActivity.this, bitmap, SHARED_BITMAP_CACHE_KEY);
                startActivity(DetailsActivity.newIntent(SharedTransitionAnimationActivity.this, key, 2),

                        DetailsActivity.newActivityOptions(SharedTransitionAnimationActivity.this, mExample3TitleTextView, mExample3ImageView));
            }
        });
        mExample4CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) mExample4ImageView.getDrawable()).getBitmap();
                String key = CacheUtil.saveCache(SharedTransitionAnimationActivity.this, bitmap, SHARED_BITMAP_CACHE_KEY);
                startActivity(DetailsActivity.newIntent(SharedTransitionAnimationActivity.this, key, 3),
                        DetailsActivity.newActivityOptions(SharedTransitionAnimationActivity.this, mExample4TitleTextView, mExample4ImageView));
            }
        });
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * Transition変更時処理
     */
    private void onChangeTransition() {
        switch (mTransitionRadioGroup.getCheckedRadioButtonId()) {
            case R.id.sharedTransitionAnimation_TransitionExplodeRadioButton:
                getWindow().setExitTransition(new Explode());
                getWindow().setEnterTransition(new Explode());
                break;
            case R.id.sharedTransitionAnimation_TransitionFadeRadioButton:
                getWindow().setExitTransition(new Fade());
                getWindow().setEnterTransition(new Fade());
                break;
            case R.id.sharedTransitionAnimation_TransitionSlideRadioButton:
                getWindow().setExitTransition(new Slide(Gravity.START));
                getWindow().setEnterTransition(new Slide(Gravity.END));
                break;
            case R.id.sharedTransitionAnimation_TransitionChangeImageAndBoundsTransformRadioButton:
                TransitionSet set = new TransitionSet();
                set.addTransition(new ChangeBounds());
                set.addTransition(new ChangeImageTransform());
                getWindow().setExitTransition(set);
                getWindow().setEnterTransition(set);
                break;
        }
    }

    /**
     * {@link Intent}生成
     *
     * @param context {@link Context}
     * @return {@link Intent}
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, SharedTransitionAnimationActivity.class);
    }

}
