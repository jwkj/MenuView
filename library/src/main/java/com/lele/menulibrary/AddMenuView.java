package com.lele.menulibrary;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created by lele on 2017/9/28.
 */

public class AddMenuView extends FrameLayout {
    private Context context;
    //距离右边的距离
    private static final int PADDING_RIGHT = 32;
    //文字与图片距离
    private static int text_and_picture_space = 36;
    //每一个item高度
    private static final int ITEM_HIGHT = 260;

    private boolean isShow = false;
    int[] drawbleIds;
    String[] strs;
    private int topHeight = 90;

    public AddMenuView(Context context, int[] drawbleIds, String[] strs) {
        super(context);
        this.context = context;
        text_and_picture_space = dip2px(context, 18);
        this.drawbleIds = drawbleIds;
        this.strs = strs;
//        setOrientation(VERTICAL);
        init();
    }

    public AddMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isShow() {
        return isShow;
    }

    public void setTopHeight(int topHeight) {
        this.topHeight = topHeight;
    }

    private void init() {
        setClipChildren(false);
        setClipToPadding(false);
        for (int i = 0; i < drawbleIds.length; i++) {
            final int position = i;
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, dip2px(context, 35), 0, 0);
            TextView tx = new TextView(context);
            tx.setLayoutParams(params);
            tx.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(drawbleIds[i]), null);
            tx.setCompoundDrawablePadding(text_and_picture_space);
            tx.setText(strs[i]);
            tx.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tx.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
            addView(tx);
        }
        showAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void showAnimation() {
        for (int i = 0; i < drawbleIds.length; i++) {
            PropertyValuesHolder translationHolder = PropertyValuesHolder.ofFloat("translationY", i * ITEM_HIGHT + dip2px(context, 35));
            PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(getChildAt(i), translationHolder, alphaHolder);
            animator.setStartDelay(i * 100);
            animator.setDuration(300);
            animator.setInterpolator(new OvershootInterpolator());
            animator.start();
        }
        isShow = true;
    }

    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void hideAnimation() {
        for (int i = 0; i < drawbleIds.length; i++) {
            AnimatorSet animationSet = new AnimatorSet();
            ObjectAnimator translationY = ObjectAnimator.ofFloat(getChildAt(i), "translationY", i * ITEM_HIGHT + dip2px(context, 35), 0);
            ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(getChildAt(i), "alpha", 1f, 0f);
            animationSet.playTogether(translationY, alphaAnimation);
            animationSet.setDuration(300);
            animationSet.start();
            if (i == drawbleIds.length - 1) {
                animationSet.addListener(animationListener);
            }
        }
        isShow = false;
    }

    Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (listener != null) {
                listener.hideAnimationEnd();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);

        void hideAnimationEnd();
    }
}
