package zitopay.com.dragsortlistviewimpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DragSortListView extends ListView {

    private int                        mDownX;
    private int                        mDownY;
    private int                        mDragPosition;
    private boolean                    mIsDraging;
    private int                        mMoveX;
    private int                        mMoveY;
    private WindowManager              mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParmas;
    private Bitmap                     mDragPhotoBitmap;
    private int                        mRawX;
    private int                        mRawY;
    private int                        mItemOffsetY;
    private int                        mItemOffsetX;
    private ImageView                  mDragPhotoView;
    private int                        mToPosition;
    private int                        mFromPosition;

    public DragSortListView(Context context) {
        this(context, null);
    }

    public DragSortListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSortListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mDragPosition = pointToPosition(mDownX, mDownY);
                //System.out.println("position" + mDragPosition);
                // 如果触摸的坐标不在条目上，在分割线、或外部区域，则为无效值-1; 宽度3/4 以右的区域可拖拽
                if (mDragPosition == AdapterView.INVALID_POSITION || mDownX < getWidth() * 3 / 4) {
                    return super.onTouchEvent(ev);
                }
                mIsDraging = true;
                mToPosition = mFromPosition = mDragPosition;

                mRawX = (int) (ev.getRawX() - mDownX);
                mRawY = (int) (ev.getRawY() - mDownY);

                startDraging();

                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) ev.getX();
                mMoveY = (int) ev.getX();
                if (mIsDraging) {
                    updateDragView();
                    updateItemView();
                } else {
                    return super.onTouchEvent(ev);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mIsDraging) {
                    stopDraging();
                } else {
                    return super.onTouchEvent(ev);
                }

                break;

            default:
                break;
        }
        return true;
    }


    private void stopDraging() {
        // 显示坐标上的条目
        View view = getItemView(mToPosition);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        // 移除快照
        if (mDragPhotoView != null) {
            mWindowManager.removeView(mDragPhotoView);
            mDragPhotoView.setImageDrawable(null);
            mDragPhotoBitmap.recycle();
            mDragPhotoBitmap = null;
            mDragPhotoView = null;
        }
        mIsDraging = false;
    }

    //更新window位置
    private void updateDragView() {
        if (mDragPhotoView != null) {
            mWindowLayoutParmas.y = mMoveY + mRawY - mItemOffsetY;
            mWindowManager.updateViewLayout(mDragPhotoView, mWindowLayoutParmas);
        }
    }


    //更新item
    private void updateItemView() {
        int position = pointToPosition(mMoveX, mMoveY);
        if (position != AdapterView.INVALID_POSITION) {
            mToPosition = position;
        }
        //调换位置.并显示调换
        if (mFromPosition != mToPosition) {
            if (exchangePosition()) {
                View view = getItemView(mFromPosition);
                if(view !=null){
                    view.setVisibility(VISIBLE);
                }
                view = getItemView(mToPosition);
                if(view !=null){
                    view.setVisibility(INVISIBLE);
                }
                mFromPosition=mToPosition;
            }
        }
    }

    private boolean exchangePosition() {
        DragListViewAdapter adapter = (DragListViewAdapter) getAdapter();
        int itemCount = adapter.getCount();
        if (mFromPosition >= 0 && mFromPosition < itemCount && mToPosition >= 0 && mToPosition < mFromPosition) {
            adapter.swapData(mFromPosition,mToPosition);
            return true;
        }
        return false;
    }



    private Boolean startDraging() {
        View itemView = getItemView(mDragPosition);
        if (itemView == null) {
            return false;
        }
        //进行图片缓存
        itemView.setDrawingCacheEnabled(true);
        mDragPhotoBitmap = Bitmap.createBitmap(itemView.getDrawingCache());
        itemView.setDrawingCacheEnabled(false);
        itemView.setAlpha(0.7f);

        mItemOffsetX = mDownX - itemView.getLeft();
        mItemOffsetY = mDownY - itemView.getTop();


        cretaeDragView();
        return true;
    }


    /*创建windowManager*/
    private void cretaeDragView() {
        //获取窗口管理器
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //创建布局参数
        mWindowLayoutParmas = new WindowManager.LayoutParams();
        mWindowLayoutParmas.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParmas.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParmas.format = PixelFormat.TRANSLUCENT; //图片半透明
        mWindowLayoutParmas.gravity = Gravity.TOP | Gravity.START;

        mWindowLayoutParmas.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mWindowLayoutParmas.windowAnimations = 0; // 无动画
        mWindowLayoutParmas.alpha = 0.9f; // 微透明

        mWindowLayoutParmas.x = mDownX + mRawX - mItemOffsetX;
        mWindowLayoutParmas.y = mDownY + mRawY - mItemOffsetY;

        mDragPhotoView = new ImageView(getContext());
        mDragPhotoView.setImageBitmap(mDragPhotoBitmap);
        mWindowManager.addView(mDragPhotoView, mWindowLayoutParmas);

    }

    //获取itemview的位置
    private View getItemView(int positon) {
        if (positon < 0 || positon >= getAdapter().getCount()) {
            return null;
        }
        int index = positon - getFirstVisiblePosition();
        return getChildAt(index);
    }


}
