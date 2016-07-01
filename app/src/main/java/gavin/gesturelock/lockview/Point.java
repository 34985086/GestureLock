package gavin.gesturelock.lockview;

public class Point {
    public Point(float x, float y){
        mX = x;
        mY = y;
        mState = State.Normal;
        mIndex = "";
    }

    public Point() {
        this(0, 0);
    }

    public void setX(float x) {
        mX = x;
    }

    public void setY(float y) {
        mY = y;
    }

    public float x() {
        return mX;
    }

    public float y() {
        return mY;
    }

    public void setState(State state) {
        mState = state;
    }

    public State state() {
        return mState;
    }

    public void setIndex(String index) {
        mIndex = index;
    }

    public String index() {
        return mIndex;
    }

    private float mX;
    private float mY;
    private State mState;
    private String mIndex;
}
