package com.wongs.openchartview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by ryze.liu on 7/19/2016.
 */
public class ChartView extends View {
    private static final String TAG = "ChartView";
    private Paint paint;//draw the X and Y
    private Paint textPaint;//draw text
    private Paint scaleLinePaint;//draw scale line of chart
    private Paint pointPaint;//draw the point
    private Paint linesPaint;//draw the lines
    private int pointColor;
    private int width;
    private int height;
    float start_x;
    float start_y;
    float end_x;
    float end_y;
    float temp_scale;
    float x_scale;
    float y_scale;

    private String temp = "";
    String y_string;
    String x_string;
    String scale_text[];


    String title;
    private ArrayList<PointView> list = new ArrayList();

    private Resources resources;
    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }
    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        y_string = "$";
        x_string = "Time";
        pointColor = Color.GREEN;
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ChartView, 0, 0);
            y_string = typedArray.getString(R.styleable.ChartView_y_text);
            x_string = typedArray.getString(R.styleable.ChartView_x_text);
            pointColor = typedArray.getColor(R.styleable.ChartView_point_color,Color.GREEN);
            typedArray.recycle();
        }

        initPaint(context);
    }
    public ChartView(Context context) {
        super(context);
        initPaint(context);


    }
    public void setScaleText(String[] texts){
        this.scale_text = texts;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    void initPaint(Context context){
        resources = context.getResources();
        int  density = resources.getDisplayMetrics().densityDpi;
        float  DGG = resources.getDisplayMetrics().density;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(16f);
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(54f);
        textPaint.setStrokeWidth(16F);
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setAntiAlias(true);


        scaleLinePaint = new Paint();
        scaleLinePaint.setColor(resources.getColor(R.color.chart_scale_color));
        scaleLinePaint.setStrokeWidth(4F);

        linesPaint = new Paint();
        linesPaint.setColor(Color.WHITE);
        linesPaint.setStrokeJoin(Paint.Join.ROUND);
        linesPaint.setStrokeCap(Paint.Cap.ROUND);
        linesPaint.setStrokeWidth(4F);
        linesPaint.setAntiAlias(true);

        pointPaint = new Paint();
        pointPaint.setColor(pointColor);
        pointPaint.setStrokeJoin(Paint.Join.ROUND);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        pointPaint.setStrokeWidth(28F);
        pointPaint.setAntiAlias(true);
    }


    public void invalidateView(double value){

        PointView point = new PointView();

        if (list.size() >= 31){
                list.remove(0);
        }
        point.set(end_x, (float) (start_y -(value* y_scale)));
        for (int i = 0;i < list.size();i++){
            list.get(i).setX(list.get(i).getX()- x_scale);
        }
        list.add(point);
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine(start_x,start_y,end_x,start_y,paint);
        canvas.drawLine(start_x, start_y, start_x, end_y, paint);
        canvas.drawText(y_string, end_x + 16, start_y + 16f, textPaint);
        canvas.drawLine(start_x + 8f, start_y - temp_scale, end_x, start_y - temp_scale, scaleLinePaint);
        canvas.drawLine(start_x + 8f, start_y - 2 * temp_scale, end_x, start_y - 2 * temp_scale, scaleLinePaint);
        canvas.drawLine(start_x+8f,start_y-3*temp_scale,end_x,start_y-3*temp_scale,scaleLinePaint);
        canvas.drawLine(start_x + 8f, start_y - 4 * temp_scale, end_x, start_y - 4 * temp_scale, scaleLinePaint);

        canvas.drawText(scale_text[0], start_x - 80, start_y - temp_scale + 20, textPaint);
        canvas.drawText(scale_text[1], start_x - 80, start_y - 2 * temp_scale + 20, textPaint);
        canvas.drawText(scale_text[2],start_x-80,start_y - 3*temp_scale+20, textPaint);
        canvas.drawText(scale_text[3], start_x - 80, start_y - 4 * temp_scale + 20, textPaint);

        canvas.drawText(y_string, start_x - 80, start_y - 5 * temp_scale - 20, textPaint);
        PointView view,next;
        if (list.size()>=2) {
            for (int i = 0; i < list.size(); i++) {

                if (i + 1 == list.size()) {
                    Log.e(TAG, "no line can be draw");
                    break;
                }
                view = list.get(i);
                next = list.get(i+1);
                canvas.drawLine(view.x,view.y,next.x,next.y,linesPaint);
            }
        }
        if (list.size() != 0)
        canvas.drawPoint(list.get(list.size()-1).getX(),list.get(list.size()-1).getY(),pointPaint);
    }

    public class PointView{

        private float x;
        private float y;

        public void set(float x,float y){
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        start_x = 280f;
        start_y = height/2+height/4;
        end_y = 100;
        end_x = start_x+800f;
        temp_scale = (start_y - end_y)/5;
        y_scale = temp_scale/scale;
        x_scale = (end_x-start_x)/30f;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
    public void setScale(int scale){
        this.scale = scale;
        Log.e(TAG, "setY_scale" + y_scale);
    }

    private int scale = 10;
    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();
    }
}
