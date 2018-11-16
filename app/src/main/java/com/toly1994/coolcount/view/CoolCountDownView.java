package com.toly1994.coolcount.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/11/11 0011:5:53<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class CoolCountDownView extends View {
    private static final String TAG = "CoolCountDown";

    private int mRadius = 12;//半径
    private int mLimitY;//Y边界
    private final static int BALLS_VY = 15;//y方向速度
    private final static int BALLS_ABS_VX = 20;//x方向速度数值
    private final static float BALLS_A = 0.98f;//小球的加速度

    private List<Ball> mBalls = new ArrayList<>();//小球集合
    private long mDeadTime;//剩余时间
    private int mCurDeadTime;//当前剩余时间

    private Paint mPaint;
    private Path mStarPath;

    public CoolCountDownView(Context context) {
        this(context, null);
    }

    public CoolCountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoolCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Point winSize = new Point();
        loadWinSize(getContext(), winSize);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mLimitY = winSize.y - 200;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mDeadTime = calendar.getTimeInMillis();
        mCurDeadTime = getLifeSec();

        mStarPath = nStarPath(5, mRadius, mRadius / 2);


        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                update();
                invalidate();
            }
        });
        animator.start();

    }

    /**
     * 小球集合的更新总函数
     */
    private void update() {
        addBalls();//添加小球
        updateBalls();//更新小球
    }

    /**
     * 添加倒计时中改动得点
     */
    private void addBalls() {
        int nextSec = getLifeSec();
        int nextHours = nextSec / 3600;
        int nextMinutes = (nextSec - nextHours * 3600) / 60;
        int nextSeconds = nextSec % 60;

        int curHours = mCurDeadTime / 3600;
        int curMinutes = (mCurDeadTime - curHours * 3600) / 60;
        int curSeconds = mCurDeadTime % 60;

        if (nextSeconds != curSeconds) {//判断当前时间是否改变,再将点位放到集合中
            if ((curHours / 10) != (nextHours / 10)) {
                addBalls((-17 * 5 - 11 * 2) * mRadius, curHours / 10);
            }
            if ((curHours % 10) != (nextHours % 10)) {
                addBalls((-17 * 4 - 11 * 2) * mRadius, curHours % 10);
            }
            if ((curMinutes / 10) != (nextMinutes / 10)) {
                addBalls((-17 * 3 - 11) * mRadius, curMinutes / 10);
            }
            if ((curMinutes % 10) != (nextMinutes % 10)) {
                addBalls((-17 * 2 - 11) * mRadius, curMinutes % 10);
            }
            if ((curSeconds / 10) != (nextSeconds / 10)) {
                addBalls(-17 * mRadius, curSeconds / 10);
            }
            if ((curSeconds % 10) != (nextSeconds % 10)) {
                addBalls(0, nextSeconds % 10);
            }
            mCurDeadTime = nextSec;
        }
    }

    /**
     * 添加小球的核心逻辑
     *
     * @param offsetX x方向偏移
     * @param num     改变的时间数字
     */
    private void addBalls(int offsetX, int num) {
        for (int i = 0; i < Cons.digit[num].length; i++) {
            for (int j = 0; j < Cons.digit[num][i].length; j++) {
                if (Cons.digit[num][i][j] == 1) {
                    Ball ball = new Ball();
                    ball.aY = BALLS_A;
                    ball.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * BALLS_ABS_VX * Math.random());
                    ball.vY = (float) (BALLS_VY * Math.random());
                    ball.x = offsetX + j * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心横坐标
                    ball.y = i * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心纵坐标
                    ball.color = Cons.colors[(int) (Math.random() * Cons.colors.length)];
                    mBalls.add(ball);
                }
            }
        }
    }


    /**
     * 更新所有球的位置---让球运动
     * 并且越界移除
     */
    private void updateBalls() {
        int minX = (-17 * 5 - 11 * 2) * mRadius;//限定x范围最小值
        int maxX = 400;//限定x范围大值

        for (Ball ball : mBalls) {
            ball.x += ball.vX;//x=xo+v*t-----t=1
            ball.y += ball.vY;
            ball.y += ball.aY;//v=vo+a*t-----t=1

            if (ball.y >= mLimitY - mRadius) {//超过Y底线，反弹
                ball.y = mLimitY - mRadius;
                ball.vY = -ball.vY * 0.99f;
            }

            if (ball.x > maxX) {//超过X最大值，反弹
                ball.x = maxX - mRadius;
                ball.vX = -ball.vX * 0.99f;
            }
        }

        for (int i = 0; i < mBalls.size(); i++) {//删除越界的点
            if (mBalls.get(i).x + mRadius < minX || mBalls.get(i).y + mRadius < 0
                    || mBalls.get(i).x - mRadius > maxX) {
                mBalls.remove(i);
            }
        }
    }


    /**
     * 获取倒计时剩余秒数
     *
     * @return 倒计时剩余秒数
     */
    private int getLifeSec() {
        int life = Math.round((mDeadTime - System.currentTimeMillis()) / 1000);
        life = life > 0 ? life : 0;
        return life;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int hour = getLifeSec() / 3600;
        int min = (getLifeSec() - hour * 3600) / 60;
        int sec = getLifeSec() % 60;

        canvas.save();
        //绘制小时
        drawHour(canvas, hour);
        //绘制冒号：
        drawDot(canvas);
        //绘制分钟
        drawMin(canvas, min);
        //绘制冒号：
        drawDot(canvas);
        //绘制秒：
        drawSec(canvas, sec);
        canvas.restore();
    }


    /**
     * 绘制秒
     *
     * @param canvas
     * @param sec
     */
    private void drawSec(Canvas canvas, int sec) {
        canvas.translate(11 * mRadius, 0);
        drawBall(canvas);//绘制小球
        renderDigit(sec / 10, canvas);
        canvas.translate(17 * mRadius, 0);
        renderDigit(sec % 10, canvas);
    }

    /**
     * 绘制小球
     *
     * @param canvas
     */
    private void drawBall(Canvas canvas) {
        canvas.save();
        canvas.translate(17 * mRadius, 0);
        for (Ball ball : mBalls) {
            mPaint.setColor(ball.color);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(ball.x, ball.y, mRadius, mPaint);
        }
        canvas.restore();
    }

    /**
     * 绘制分钟
     * @param canvas
     * @param min
     */
    private void drawMin(Canvas canvas, int min) {
        canvas.translate(11 * mRadius, 0);
        renderDigit(min / 10, canvas);
        canvas.translate(17 * mRadius, 0);
        renderDigit(min % 10, canvas);
    }

    /**
     *  绘制冒号
     * @param canvas
     */
    private void drawDot(Canvas canvas) {
        canvas.translate(17 * mRadius, 0);
        renderDigit(10, canvas);
    }

    /**
     * 绘制小时
     * @param canvas
     * @param hour
     */
    private void drawHour(Canvas canvas, int hour) {
        renderDigit(hour / 10, canvas);
        canvas.translate(17 * mRadius, 0);
        renderDigit(hour % 10, canvas);
    }

    /**
     * 渲染数字
     * @param num    要显示的数字
     * @param canvas 画布
     */
    private void renderDigit(int num, Canvas canvas) {
        if (num > 10) {
            return;
        }

        for (int i = 0; i < Cons.digit[num].length; i++) {
            for (int j = 0; j < Cons.digit[num][j].length; j++) {
                if (Cons.digit[num][i][j] == 1) {
                    canvas.save();
                    float rX = j * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心横坐标
                    float rY = i * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心纵坐标
                    canvas.translate(rX, rY);
                    mPaint.setColor(Color.BLUE);
                    canvas.drawPath(mStarPath, mPaint);
                    canvas.restore();
                }
            }
        }
    }
    public static void loadWinSize(Context ctx, Point winSize) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        winSize.x = outMetrics.widthPixels;
        winSize.y = outMetrics.heightPixels;
    }

    /**
     * n角星路径
     *
     * @param num 几角星
     * @param R   外接圆半径
     * @param r   内接圆半径
     * @return n角星路径
     */
    public static Path nStarPath(int num, float R, float r) {
        Path path = new Path();
        float perDeg = 360 / num;
        float degA = perDeg / 2 / 2;
        float degB = 360 / (num - 1) / 2 - degA / 2 + degA;

        path.moveTo(
                (float) (Math.cos(rad(degA + perDeg * 0)) * R + R * Math.cos(rad(degA))),
                (float) (-Math.sin(rad(degA + perDeg * 0)) * R + R));
        for (int i = 0; i < num; i++) {
            path.lineTo(
                    (float) (Math.cos(rad(degA + perDeg * i)) * R + R * Math.cos(rad(degA))),
                    (float) (-Math.sin(rad(degA + perDeg * i)) * R + R));
            path.lineTo(
                    (float) (Math.cos(rad(degB + perDeg * i)) * r + R * Math.cos(rad(degA))),
                    (float) (-Math.sin(rad(degB + perDeg * i)) * r + R));
        }
        path.close();
        return path;
    }


    /**
     * 角度制化为弧度制
     *
     * @param deg 角度
     * @return 弧度
     */
    public static float rad(float deg) {
        return (float) (deg * Math.PI / 180);
    }


}
