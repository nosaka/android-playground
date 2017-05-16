package me.ns.lib;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;

/**
 * Animation Generator
 * <p>
 * Created by shintaro.nosaka on 2017/05/15.
 */
public class AnimationGenerator {

    /**
     * View State
     */
    public static class ViewState {
        /**
         * 高
         */
        int height;
        /**
         * 幅
         */
        int width;
        /**
         * X位置
         */
        float x;
        /**
         * y位置
         */
        float y;
    }

    /**
     * {@link Bitmap}メモリキャッシュ
     */
    private static LruCache<Integer, ViewState> sViewStateCache = new LruCache<>(CacheUtil.MAX_MEMORY);

    /**
     * アニメーションリストア処理
     *
     * @param view {@link View}
     */
    public static void restoreAnimatorIfNecessary(View view) {
        ViewState viewState = sViewStateCache.get(view.hashCode());
        if (viewState != null) {
            ValueAnimator width = width(view, viewState.width);
            ValueAnimator height = height(view, viewState.height);
            ValueAnimator x = x(view, viewState.x);
            ValueAnimator y = y(view, viewState.y);
            AnimatorSet set = new AnimatorSet();
            set.play(width).with(height).with(x).with(y);
            set.start();
        }
    }

    /**
     * 幅{@link ValueAnimator}アニメーション生成処理
     *
     * @param view {@link View}
     * @param to   アニメーション後幅
     * @return {@link ValueAnimator}
     */
    public static ValueAnimator width(final View view, int to) {
        final int from = view.getWidth();
        final ValueAnimator animator = ValueAnimator.ofInt(from, to);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.width = (Integer) animation.getAnimatedValue();
                view.setLayoutParams(lp);
            }
        });
        cacheViewState(view);
        return animator;
    }

    /**
     * 高{@link ValueAnimator}アニメーション生成処理
     *
     * @param view {@link View}
     * @param to   アニメーション後高
     * @return {@link ValueAnimator}
     */
    public static ValueAnimator height(final View view, int to) {
        final int from = view.getHeight();
        final ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = (Integer) animation.getAnimatedValue();
                view.setLayoutParams(lp);
            }
        });
        cacheViewState(view);
        return animator;
    }

    /**
     * X位置{@link ValueAnimator}アニメーション生成処理
     *
     * @param view {@link View}
     * @param to   アニメーション後X位置
     * @return {@link ValueAnimator}
     */
    public static ValueAnimator x(final View view, float to) {
        final float from = view.getX();
        final ValueAnimator animator = ValueAnimator.ofFloat(from, to);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setX((Float) animation.getAnimatedValue());
            }
        });
        cacheViewState(view);
        return animator;
    }

    /**
     * Y位置{@link ValueAnimator}アニメーション生成処理
     *
     * @param view {@link View}
     * @param to   アニメーション後X位置
     * @return {@link ValueAnimator}
     */
    public static ValueAnimator y(final View view, float to) {
        final float from = view.getY();
        final ValueAnimator animator = ValueAnimator.ofFloat(from, to);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setY((Float) animation.getAnimatedValue());
            }
        });
        cacheViewState(view);
        return animator;
    }

    /**
     * {@link ViewState}キャッシュ処理
     *
     * @param view {@link View}
     */
    private static void cacheViewState(View view) {
        ViewState viewState = new ViewState();
        viewState.height = view.getHeight();
        viewState.width = view.getWidth();
        viewState.x = view.getX();
        viewState.y = view.getY();
        sViewStateCache.put(view.hashCode(), viewState);
    }
}
