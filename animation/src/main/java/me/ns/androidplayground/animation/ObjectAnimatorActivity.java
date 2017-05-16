package me.ns.androidplayground.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ObjectAnimatorActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * Bounce アニメーション再生時間
     */
    private long DURATION_BOUNCE_ANIMATION = 2500L;

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.objectAnimator_Toolbar)
    Toolbar mToolbar;

    @BindView(R.id.objectAnimator_AnimationContainer)
    View mAnimationContainer;

    @BindView(R.id.objectAnimator_BallImageView)
    ImageView mBallImageView;

    @BindView(R.id.objectAnimator_GroundView)
    View mGroundView;

    @BindView(R.id.objectAnimator_RestoreButton)
    Button mRestoreButton;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    private AnimatorSet mAnimatorSet;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_animator);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mBallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBounceAnimation();
            }

        });

        mRestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBallImageView.setX(0);
                mBallImageView.setY(0);
                mAnimationContainer.invalidate();
                mRestoreButton.setVisibility(View.INVISIBLE);
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
     * Bounce アニメーション開始処理
     */
    private void startBounceAnimation() {

        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            return;
        }
        if (mRestoreButton.getVisibility() == View.VISIBLE) {
            return;
        }

        mAnimatorSet = createBounceAnimation();
        startAnimation();
    }

    /**
     * アニメーション開始処理
     */
    private void startAnimation() {

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRestoreButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mAnimatorSet.start();
    }

    /**
     * Bounce {@link AnimatorSet}生成処理
     *
     * @return Bounce {@link AnimatorSet}
     */
    private AnimatorSet createBounceAnimation() {
        ObjectAnimator translationYAnimator = new ObjectAnimator();
        translationYAnimator.setPropertyName("translationY");
        translationYAnimator.setDuration(DURATION_BOUNCE_ANIMATION);
        translationYAnimator.setTarget(mBallImageView);
        translationYAnimator.setInterpolator(new BounceInterpolator());
        float distanceFall = mGroundView.getTop() - mBallImageView.getBottom();
        translationYAnimator.setFloatValues(mBallImageView.getY(), distanceFall);

        ObjectAnimator translationXAnimator = new ObjectAnimator();
        translationXAnimator.setPropertyName("translationX");
        translationXAnimator.setDuration(DURATION_BOUNCE_ANIMATION);
        translationXAnimator.setTarget(mBallImageView);
        float distanceRight = (mAnimationContainer.getRight() - mAnimationContainer.getLeft()) - mBallImageView.getRight();
        translationXAnimator.setFloatValues(mBallImageView.getX(), distanceRight);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translationXAnimator).with(translationYAnimator);

        return animatorSet;
    }

    /**
     * {@link Intent}生成
     *
     * @param context {@link Context}
     * @return {@link Intent}
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, ObjectAnimatorActivity.class);
    }

}
