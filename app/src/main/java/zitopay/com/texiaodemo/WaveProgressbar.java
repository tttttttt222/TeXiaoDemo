package zitopay.com.texiaodemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/10/21.
 */
public class WaveProgressbar extends View {

    private int radius = dp2px(55);
    private Paint mCyclePaint;
    private Path path  = new Path();
    private Path path2 = new Path();
    private Paint  mPathPaint;
    private Paint  mPathPaint2;
    private Paint  textPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private int mY = -30;
    private int mYBefor=-30;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:

                    if ((mYBefor >= mY || mY == 30)  && mY != -30) {
                        mYBefor = mY;
                        mY = mY - 10;
                    } else if((mYBefor < mY || mY == -30) && mY!=30){
                        mYBefor = mY;
                        mY = mY + 10;
                    }


                    invalidate();
                    break;

                default:
                    break;
            }
        }
    };


    public WaveProgressbar(Context context) {
        this(context, null);
    }

    public WaveProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveProgressbar);
        radius = typedArray.getInteger(R.styleable.WaveProgressbar_radius, radius);
        typedArray.recycle();

        mCyclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCyclePaint.setColor(Color.WHITE);
        mCyclePaint.setStrokeWidth(6);
        mCyclePaint.setDither(true);

        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setColor(0x992898E0);
        mPathPaint.setDither(true);
        mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mPathPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint2.setColor(0xFF2898E0);
        mPathPaint2.setDither(true);
        mPathPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setDither(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(36);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = resolveSize(2 * radius, widthMeasureSpec);
        int height = resolveSize(2 * radius, heightMeasureSpec);
        int min = Math.min(width, height);
        width=height=min;

        radius=min/2;
        setMeasuredDimension(width,height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
        }
        bitmapCanvas.save();

        bitmapCanvas.drawCircle(radius, radius, radius, mCyclePaint);


        path2.reset();
        path2.moveTo(radius * 2, radius+mY);
        path2.lineTo(radius * 2, radius * 2);
        path2.lineTo(0, radius * 2);
        path2.lineTo(0, radius-mY);

        path2.rQuadTo(radius / 2, -mY, radius, 0);
        path2.rQuadTo(radius / 2, mY, radius, 0);

        path2.close();
        bitmapCanvas.drawPath(path2, mPathPaint2);



        path.reset();
        path.moveTo(radius * 2, radius+mY);
        path.lineTo(radius * 2, radius * 2);
        path.lineTo(0, radius * 2);
        path.lineTo(0, radius-mY);

        path.rQuadTo(radius / 2, mY, radius, 0);
        path.rQuadTo(radius / 2, -mY, radius, 0);
//        path.quadTo(radius * 2,radius-mY,radius,radius);
//        path.quadTo(0,radius+mY,radius * 2,radius+mY);

        path.close();
        bitmapCanvas.drawPath(path, mPathPaint);

        String text="Loading...";
        int textWidth =(int) textPaint.measureText(text);
        Rect textrect = new Rect();
        textPaint.getTextBounds(text,0,text.length(),textrect);
        bitmapCanvas.drawText(text,radius-textWidth/2,radius+textrect.height()/2,textPaint);


        canvas.drawBitmap(bitmap, 0, 0, null);


        myHandler.sendEmptyMessageDelayed(101, 200);


    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    public void show(){
        this.setVisibility(VISIBLE);
    }

    public void dismss(){
         this.setVisibility(GONE);
    }


}
