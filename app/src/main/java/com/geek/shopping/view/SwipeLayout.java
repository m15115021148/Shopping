package com.geek.shopping.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


public class SwipeLayout extends FrameLayout {

    private View mBackView; // 后布局
    private View mFrontView; // 前布局
    private int mWidth; // 控件宽度
    private int mHeight; // 控件高度
    private int mRange; // 拖拽范围
    private ViewDragHelper dragHelper; // 拖拽辅助类

    private Status status = Status.Close; // 默认状态为关闭状态
    private OnSwipeListener onSwipeListener; // 滑动监听
    private float mDownX;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OnSwipeListener getOnSwipeListener() {
        return onSwipeListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public static enum Status {
        Close,  // 关闭
        Open,   // 打开
        Swiping // 滑动中..
    }

    public interface OnSwipeListener {

        void onOpen(SwipeLayout layout);

        void onClose(SwipeLayout layout);

        // 将要打开
        void onStartOpen(SwipeLayout layout);

        // 将要关闭
        void onStartClose(SwipeLayout layout);

    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 1. 创建dragHelper对象
        dragHelper = ViewDragHelper.create(this, callback);
    }

    // 3. 重写事件回调
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View arg0, int arg1) {
            return true;
        }

        // 设置 大于 0 的值
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        ;

        // 修正将要移动到的位置, 还未发生移动
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            // left 建议移动到的位置
            if (child == mFrontView) {
                if (left < -mRange) {
                    // 限定前布局左边界
                    left = -mRange;
                } else if (left > 0) {
                    // 限定前布局右边界
                    left = 0;
                }
            } else if (child == mBackView) {
                if (left < mWidth - mRange) {
                    // 限定后布局左边界
                    left = mWidth - mRange;
                } else if (left > mWidth) {
                    // 限定后布局右边界
                    left = mWidth;
                }
            }
            return left;
        }

        ;

        // 位置发生变化之后调用, 转交变化量
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mFrontView) {
                // 将前布局的水平方向刚刚发生的瞬时变化量, 转交给后布局, 使其移动
                mBackView.offsetLeftAndRight(dx);
            } else if (changedView == mBackView) {
                // 将后布局的水平方向刚刚发生的瞬时变化量, 转交给前布局, 使其移动
                mFrontView.offsetLeftAndRight(dx);
            }

            dispathSwipeEvent();

            invalidate(); // 重绘界面.
        }

        ;

        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            // xvel 松手时, 水平方向的速度, 向左 - , 向右 +
            // 判断所有的开启情况
            if (xvel == 0 && mFrontView.getLeft() < -mRange / 2.0f) {
                open(); // 打开
            } else if (xvel < 0) {
                open(); // 打开
            } else {
                close();// 关闭
            }

        }

        ;
    };

    // 3. 转交拦截判断, 触摸事件
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 决定当前的SwipeLayout是否要把touch事件拦截下来，直接交由自己的onTouchEvent处理
        // 返回true则为拦截
        //return  dragHelper.shouldInterceptTouchEvent(ev) & mGestureDetector.onTouchEvent(ev);
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    ;

    // 交由onTouchEvent处理触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//		try {
//			dragHelper.processTouchEvent(event);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return true;
        // 当处理touch事件时，不希望被父类onInterceptTouchEvent的代码所影响。
        // 比如处理向右滑动关闭已打开的条目时，如果进行以下逻辑，则不会在关闭的同时引发左边菜单的打开。

        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = event.getRawX() - mDownX;
                if (deltaX > dragHelper.getTouchSlop()) {
                    // 请求父级View不拦截touch事件
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                mDownX = 0;
            default:
                break;
        }

        try {
            dragHelper.processTouchEvent(event);
        } catch (IllegalArgumentException e) {
        }

        return true;
    }

    /**
     * 在动画执行过程中更新不断执行此方法, 更新状态
     */
    protected void dispathSwipeEvent() {
        // 更新状态, 执行监听

        Status lastStatus = status; // 修改之前的状态
        status = updateStatus();

        // 当状态发生变化的时候, 调用监听里的方法. 调用者可以收到事件.
        if (lastStatus != status && onSwipeListener != null) {
            if (status == Status.Open) {
                // 指定打开回调
                onSwipeListener.onOpen(this);
            } else if (status == Status.Close) {
                onSwipeListener.onClose(this);
            } else if (status == Status.Swiping) {
                if (lastStatus == Status.Close) {
                    // 指定将要打开回调
                    onSwipeListener.onStartOpen(this);
                } else if (lastStatus == Status.Open) {
                    // 指定将要关闭回调
                    onSwipeListener.onStartClose(this);
                }
            }
        }
    }

    // 获取最新状态
    private Status updateStatus() {
        int left = mFrontView.getLeft();
        if (left == 0) {
            return Status.Close;
        } else if (left == -mRange) {
            return Status.Open;
        }
        return Status.Swiping;
    }

    public void close() {
        close(true);
    }

    public void close(boolean isSmooth) {
        if (isSmooth) {
            // 1. 触发一个平滑动画
            if (dragHelper.smoothSlideViewTo(mFrontView, 0, 0)) {
                // 动画还没有移动到指定位置, 需要重绘界面
                ViewCompat.postInvalidateOnAnimation(this);
            }
            ;
        } else {
            layoutContent(true);
        }
    }

    public void open() {
        open(true);
    }

    // 执行平滑的开启动画
    public void open(boolean isSmooth) {
        if (isSmooth) {
            // 1. 触发一个平滑动画
            if (dragHelper.smoothSlideViewTo(mFrontView, -mRange, 0)) {
                // 动画还没有移动到指定位置, 需要重绘界面
                ViewCompat.postInvalidateOnAnimation(this);
            }
            ;
        } else {
            layoutContent(true);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //2. 维持平滑动画的继续
        if (dragHelper.continueSettling(true)) {
            // 动画还没有移动到指定位置, 需要重绘界面
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
    }

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//	}

    // 经过测量之后, 如果发现控件内部宽高发生了变化, 此方法会被调用. 此时获取宽高比较合适
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mRange = mBackView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 摆放内容
        layoutContent(false);
    }

    /**
     * 根据开启状态摆放内容
     *
     * @param isOpen
     */
    private void layoutContent(boolean isOpen) {

        // 计算得到前布局的矩形区域
        Rect frontRect = computeFrontRect(isOpen);
        mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);

        // 通过前布局获取后布局的矩形区域
        Rect backRect = computeBackRectViaFront(frontRect);
        mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

    }

    private Rect computeBackRectViaFront(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left, 0, left + mRange, 0 + mHeight);
    }

    // 计算得到前布局的矩形区域
    private Rect computeFrontRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mRange;
        }
        return new Rect(left, 0, left + mWidth, 0 + mHeight);
    }


}
