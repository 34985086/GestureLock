package gavin.gesturelock.lockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import gavin.gesturelock.R;

/**
 * Created by gavin on 2016-06-30.
 */

public class GestureView extends View {
    final static int RADIUS = 50;

    private boolean isInitialized = false;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //the point of touch
    private Point touchPoint = new Point();
    //use to store 9 points to draw
    private List<Point> mListAllPoints = new ArrayList<>();
    //used to store the points touched with finger
    private List<Point> mListTouchedPoints = new ArrayList<>();
    //
    private OnPasswordListener mListener;

    public GestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureView(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInitialized) {
            initPoint();
            isInitialized = true;
        }

        drawCircles(canvas, mListAllPoints, mPaint);
        drawLines(canvas, mListTouchedPoints, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchPoint.setX(event.getX());
        touchPoint.setY(event.getY());
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            reset();
            checkPoint(touchPoint);
            break;
        case MotionEvent.ACTION_MOVE:
            checkPoint(touchPoint);
            break;
        case MotionEvent.ACTION_UP:
            String password = genPassword();
            if(!password.isEmpty()) {
                if (!mListener.onPasswordChanged(password)) {
                    setState(State.Error);
                }
            }
            break;
        }
        postInvalidate();
        return true;
    }

    public void reset(){
        int index = 0;
        for(Point p : mListAllPoints){
            p.setState(State.Normal);
            p.setIndex(++index + "");
        }
        mListTouchedPoints.clear();
        postInvalidate();
    }

    private boolean contain(Point a, Point b, float radius)
    {
        float dx = a.x() - b.x();
        float dy = a.y() - b.y();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= radius;
    }

    private void checkPoint(Point moving){
        for(Point p : mListAllPoints){
            if(contain(p, moving, RADIUS) && !mListTouchedPoints.contains(p)){
                p.setState(State.Press);
                mListTouchedPoints.add(p);
            }
        }
    }

    private void drawCircles(Canvas canvas, List<Point> points, Paint paint){
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        for(Point p : points){
            if (p.state() == State.Normal) {
                paint.setColor(Color.WHITE);
            } else if (p.state() == State.Press) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.RED);
            }
            canvas.drawCircle(p.x(), p.y(), RADIUS, paint);
        }
    }

    private void drawLines(Canvas canvas, List<Point> points, Paint paint){
        if(points.size() > 0) {
            State state = points.get(0).state();

            Path path = new Path();
            path.moveTo(points.get(0).x(), points.get(0).y());
            for (Point p : points) {
                path.lineTo(p.x(), p.y());
            }
            if (state == State.Error) {
                paint.setColor(getResources().getColor(R.color.colorUnlockFailed));
            } else {
                paint.setColor(getResources().getColor(R.color.colorUnlocking));
                path.lineTo(touchPoint.x(), touchPoint.y());
            }

            paint.setAntiAlias(true);
            paint.setStrokeWidth(4);

            canvas.drawPath(path, paint);

            paint.setStrokeWidth(12);
            for (Point p : points) {
                canvas.drawPoint(p.x(), p.y(), paint);
            }
        }
    }
    private void initPoint() {
        int wView = getWidth();
        int hView = getHeight();

        int hOffSet = 0;
        int wOffSet = 0;

        if (hView > wView) {
            // portrait
            hOffSet = (hView - wView) / 2;
            hView = wView;
        } else {
            //landscape
            wOffSet = (wView - hView) / 2;
            wView = hView;
        }

        mListAllPoints.add(new Point(wOffSet + wView / 4,
                hOffSet + hView / 4));
        mListAllPoints.add(new Point(wOffSet + wView / 2,
                hOffSet + hView / 4));
        mListAllPoints.add(new Point(wOffSet + wView * 3 / 4,
                hOffSet + hView / 4));

        mListAllPoints.add(new Point(wOffSet + wView / 4,
                hOffSet + hView / 2));
        mListAllPoints.add(new Point(wOffSet + wView / 2,
                hOffSet + hView / 2));
        mListAllPoints.add(new Point(wOffSet + wView * 3 / 4,
                hOffSet + hView / 2));

        mListAllPoints.add(new Point(wOffSet + wView / 4,
                hOffSet + hView * 3 / 4));
        mListAllPoints.add(new Point(wOffSet + wView / 2,
                hOffSet + hView * 3 / 4));
        mListAllPoints.add(new Point(wOffSet + wView * 3 / 4,
                hOffSet + hView * 3 / 4));

        reset();
    }

    private void setState(State state){
        for(Point p : mListTouchedPoints){
            p.setState(state);
        }
    }

    private String genPassword(){
        String password = "";
        for(Point p : mListTouchedPoints){
            password += p.index();
        }
        return password;
    }

    public interface OnPasswordListener {
        boolean onPasswordChanged(String password);
    }


    public void setChangeListener(OnPasswordListener listener) {
        this.mListener = listener;
    }
}
