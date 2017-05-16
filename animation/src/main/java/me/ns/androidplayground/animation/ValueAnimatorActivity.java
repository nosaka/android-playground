package me.ns.androidplayground.animation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.AnimationGenerator;

public class ValueAnimatorActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * Animation State
     */
    private enum AnimationState {
        NONE, ANIMATED
    }

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.valueAnimator_Toolbar)
    Toolbar mToolbar;

    @BindView(R.id.valueAnimator_Container)
    ViewGroup mContainer;

    @BindView(R.id.valueAnimator_BounceImageView)
    ImageView mBounceImageView;

    @BindView(R.id.valueAnimator_ExpandCardView)
    CardView mExpandCardView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animator);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mExpandCardView.setTag(AnimationState.NONE);

        mBounceImageView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(ValueAnimatorActivity.this, R.anim.bounce);
                animation.setInterpolator(new BounceInterpolator());
                mBounceImageView.startAnimation(animation);
            }
        });
        mExpandCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AnimationState.NONE.equals(mExpandCardView.getTag())) {
                    mExpandCardView.setTag(AnimationState.ANIMATED);
                    final int toWidth = (int) Math.round(mContainer.getWidth() * 0.8);
                    final int toHeight = (int) Math.round(mContainer.getHeight() * 0.8);
                    final float toX = (mContainer.getWidth() - toWidth) / 2;
                    final float toY = (mContainer.getHeight() - toHeight) / 2;

                    final ValueAnimator widthAnimator = AnimationGenerator.width(mExpandCardView, toWidth);
                    final ValueAnimator heightAnimator = AnimationGenerator.height(mExpandCardView, toHeight);
                    final ValueAnimator xAnimator = AnimationGenerator.x(mExpandCardView, toX);
                    final ValueAnimator yAnimator = AnimationGenerator.y(mExpandCardView, toY);

                    final AnimatorSet set = new AnimatorSet();

                    set.setTarget(mExpandCardView);
                    set.play(widthAnimator).with(heightAnimator).with(xAnimator).with(yAnimator);
                    set.start();
                } else {
                    mExpandCardView.setTag(AnimationState.NONE);

                    AnimationGenerator.restoreAnimatorIfNecessary(mExpandCardView);
                }
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
     * {@link Intent}生成
     *
     * @param context {@link Context}
     * @return {@link Intent}
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, ValueAnimatorActivity.class);
    }

}
