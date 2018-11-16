#### 零、前言

>1.本篇原型是慕课网的教程，但`是用JavaScript实现在浏览器上`的，[详见](https://note.youdao.com/)  
2.最近感觉安卓Canvas不比html5的canvas差，使用想`复刻一下到Android上`  
3.本篇并不止于教程，而是以知其所以然来运用字符点阵及动效，这也是从JavaScript移植到安卓的必要条件    
4.本篇会深入地分析整体思路与逻辑，为大家提供一种canvas绘制的思路，相信会给你带来收获。

##### 最终效果

![最终效果.gif](https://upload-images.jianshu.io/upload_images/9414344-89dae20bef66956a.gif?imageMogr2/auto-orient/strip)

#### 一、字符的点阵显示

>第一个问题是如何字符的点阵显示，以及点阵形状的自定义：效果如下：

![字符的点阵显示.png](https://upload-images.jianshu.io/upload_images/9414344-65c335d09c06ce53.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 1.从`1`开始分析:先看一个数组：

```
/**
 * 用来显示点阵的二维数组
 */
public static final int[][] digit_test = new int[][]
        {
                {0, 0, 0, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0},
                {1, 1, 1, 1, 1, 1, 1}
        };//1
```


```
/**
 * 颜色数组
 */
public static final int[] colors = new int[]{
        0xff33B5E5, 0xff0099CC, 0xffAA66CC, 0xff9933CC,
        0xff99CC00, 0xff669900, 0xffFFBB33, 0xffFF8800, 0xffFF4444, 0xffCC0000};
```


>如果你高度散光，估计能一眼看出来些什么，看不出来的话，看下图：  
你也许会说：晕，这么简单粗暴。----没错，就是这么粗暴

![](https://upload-images.jianshu.io/upload_images/9414344-309ff130c10f4d16.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



##### 2.其实分析出来也不麻烦
>就是二维数组一行一行遍历，遇到1就画图形，这里是圆形，你可以自己画任意的形状  
补充一下，`这里的1和0都是自定义的标示符`，你可以任意,但绘制的逻辑也要同步修改

![点阵分析.png](https://upload-images.jianshu.io/upload_images/9414344-53fdf637601d7ed9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


```
private void renderDigitTest(Canvas canvas) {
    for (int i = 0; i < Cons.digit_test.length; i++) {
        for (int j = 0; j < Cons.digit_test[j].length; j++) {//一行一行遍历，遇到1就画
            if (Cons.digit_test[i][j] == 1) {
                canvas.save();
                float rX = (j * 2 + 1) * (mRadius + 1);//第(i，j)个点圆心横坐标
                float rY = (i * 2 + 1) * (mRadius + 1);//第(i，j)个点圆心纵坐标
                canvas.translate(rX, rY);
                canvas.drawCircle(rX, rY, mRadius, mPaint);//画圆
                int color = Cons.colors[(int) (Math.random() * Cons.colors.length)];
                mPaint.setColor(color);
                canvas.restore();
            }
        }
    }
}
```

##### 3.如何多个数字呢?
>`上古有道，道生一，一生二`，会一个自然第二个也就不远了  
既然一个二维数组可以表示一个数，那么用一个三维数组再成放10个二维数组不就表示数字了吗!  
三维数组太长了，也没有什么技术含量，贴这里影响市容，放在文尾，自行拷贝。

##### 4.数字0~9的渲染函数：(这里就画五角星吧，路径绘制[可详见Path篇](https://www.jianshu.com/p/d080579ae048))

```
    /**
     *
     * @param pos 偏移量 x,y
     * @param num 要显示的数字
     * @param canvas 画布
     */
    private void renderDigit(PointF pos, int num, Canvas canvas) {
        for (int i = 0; i < Cons.digit[num].length; i++) {
            for (int j = 0; j < Cons.digit[num][j].length; j++) {
                if (Cons.digit[num][i][j] == 1) {
                    canvas.save();
                    float rX = pos.x + j * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心横坐标
                    float rY = pos.y + i * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心纵坐标
                    canvas.translate(rX, rY);
                    Path path = CommonPath.nStarPath(5, mRadius, mRadius / 2);
                    int color = Cons.colors[(int) (Math.random() * Cons.colors.length)];
                    mPaint.setColor(color);
                    canvas.drawPath(path, mPaint);
                    canvas.restore();
                }
            }
        }
    }
```

OnDraw()中：

```
canvas.save();
renderDigit( 1, canvas);
canvas.translate(150, 0);
renderDigit( 9, canvas);
canvas.translate(150, 0);
renderDigit(9, canvas);
canvas.translate(150, 0);
renderDigit(4, canvas);
canvas.restore();
```

![字符的点阵显示.png](https://upload-images.jianshu.io/upload_images/9414344-65c335d09c06ce53.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>OK,第一步完成，万事开头难，理解之后，后面就很简单了

---

#### 二、静态时间显示与倒计时：
##### 1.静态时间显示

>只要动态获取时间即可，在onDraw中将时间解析成响应数字绘制出来，是不是很简单

```
mCalendar = Calendar.getInstance();
int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
int min = mCalendar.get(Calendar.MINUTE);
int sec = mCalendar.get(Calendar.SECOND);
canvas.save();
//绘制小时
renderDigit(hour / 10, canvas);
canvas.translate(17*mRadius, 0);
renderDigit(hour % 10, canvas);
//绘制冒号：
canvas.translate(17*mRadius, 0);
renderDigit(10, canvas);
//绘制分钟
canvas.translate(11*mRadius, 0);
renderDigit(min / 10, canvas);
canvas.translate(17*mRadius, 0);
renderDigit(min % 10, canvas);
//绘制冒号：
canvas.translate(17*mRadius, 0);
renderDigit(10, canvas);
//绘制秒：
canvas.translate(11*mRadius, 0);
renderDigit(sec / 10, canvas);
canvas.translate(17*mRadius, 0);
renderDigit(sec % 10, canvas);
canvas.restore();
```

![时钟静态效果.png](https://upload-images.jianshu.io/upload_images/9414344-63245ec70d2a7d46.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>通过Handler定时发送消息更新视图：

![时钟动态效果.gif](https://upload-images.jianshu.io/upload_images/9414344-e3ac6e66d1e851d7.gif?imageMogr2/auto-orient/strip)

---
##### 倒计时处理

```
//初始时设定截止日期
mCalendar = Calendar.getInstance();
mCalendar.set(2018, 11-1, 11, 21, 21, 21);
mDeadTime = mCalendar.getTimeInMillis();
//OnDraw中获取之间的毫秒数，处理
mCalendar = Calendar.getInstance();
long lastSec = (mDeadTime - mCalendar.getTimeInMillis()) / 1000;//持续时间

int hour = (int) (lastSec / 3600);
int min = (int) (lastSec - hour * 3600) / 60;
int sec = (int) (lastSec % 60);
Log.d(TAG, "render: "+ hour+","+min+","+sec);
//接下来的和上面一样
```
![倒计时处理.gif](https://upload-images.jianshu.io/upload_images/9414344-f0ceef3835ec5ed5.gif?imageMogr2/auto-orient/strip)

---

#### 4.重点：小球的运动

>有点小复杂，简单地画了一个流程图

![分析.png](https://upload-images.jianshu.io/upload_images/9414344-9ee3903d5e2ad4b0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>先把小球封装一下：

```
/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/11/11 0011:6:13<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：小球封装类
 */
public class Ball {
    public float a;//加速度
    public float vX;//速度X
    public float vY;//速度Y
    public float pX;//点位X
    public float pY;//点位Y
    public int color;//颜色
}

```

>准备工作

```
//成员变量
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

//初始化：init()
mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
mPaint.setStyle(Paint.Style.FILL);
mLimitY = winSize.y - 200;
Calendar calendar = Calendar.getInstance();
calendar.set(2018, 11 - 1, 11, 21, 21, 21);
mDeadTime = calendar.getTimeInMillis();
mCurDeadTime = getLifeSec();
```

---


##### 1.使用ValueAnimator不断重绘界面

```
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
```

##### 2.小球集合的更新总函数

```
/**
 * 小球集合的更新总函数
 */
private void update() {
    addBalls();//添加小球
    updateBalls();//更新小球
}
```

##### 3.添加倒计时中改动的点
>核心是：判断当前时间是否改变,再将点位放到集合中  
这是要注意我绘制小球放在绘制秒的前面，所以根据绘制小球时的画布原点，在加入是对小球的点位进行偏移

```
    /**
     * 添加倒计时中改动的点
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
                    ball.a = BALLS_A;
                    ball.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * BALLS_ABS_VX * Math.random());
                    ball.vY = (float) (BALLS_VY * Math.random());
                    ball.pX = offsetX + j * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心横坐标
                    ball.pY = i * 2 * (mRadius + 1) + (mRadius + 1);//第(i，j)个点圆心纵坐标
                    ball.color = Cons.colors[(int) (Math.random() * Cons.colors.length)];
                    mBalls.add(ball);
                }
            }
        }
    }
```

##### 4.这里是让小球运动的核心：
>更新所有球的位置---让球运动,并且越界移除  
根据速度和加速度的公式，每次刷新的时间作为单位时间  
安卓的ValueAnimator是0.167秒,由于代码的执行效率，会有一点偏差,大太多就会造成不流畅，即视觉卡顿  


```
    /**
     * 更新所有球的位置---让球运动
     * 并且越界移除
     */
    private void updateBalls() {
        int minX = (-17 * 5 - 11 * 2) * mRadius;//限定x范围最小值
        int maxX = 400;//限定x范围大值

        for (Ball ball : mBalls) {
            ball.pX += ball.vX;//x=xo+v*t
            ball.pY += ball.vY;
            ball.pY += ball.a;//v=vo+a*t

            if (ball.pY >= mLimitY - mRadius) {//超过Y底线，反弹
                ball.pY = mLimitY - mRadius;
                ball.vY = -ball.vY * 0.99f;
            }

            if (ball.pX > maxX) {//超过X最大值，反弹
                ball.pX = maxX - mRadius;
                ball.vX = -ball.vX * 0.99f;
            }
        }

        for (int i = 0; i < mBalls.size(); i++) {//删除越界的点
            if (mBalls.get(i).pX + mRadius < minX || mBalls.get(i).pY + mRadius < 0
                    || mBalls.get(i).pX - mRadius > maxX) {
                mBalls.remove(i);
            }
        }
    }
```

##### 5.获取倒计时剩余秒数


```
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
```

##### 6.绘制的方法:
和上面比就多了个画圆球的方法，把化数字的方法抽取了一下。

```

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
            canvas.drawCircle(ball.pX, ball.pY, mRadius, mPaint);
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
```

![最终效果.gif](https://upload-images.jianshu.io/upload_images/9414344-89dae20bef66956a.gif?imageMogr2/auto-orient/strip)


>到这里就OK了，是不是比想象中的要简单

---

#### 后记：捷文规范
##### 1.本文成长记录及勘误表
项目源码 | 日期|备注
---|---|---
V0.1--无|2018-11-11|[Android原生绘图之炫酷倒计时](https://www.jianshu.com/p/dd8e325b2ae3)


##### 2.更多关于我

笔名 | QQ|微信|爱好
---|---|---|---|
张风捷特烈 | 1981462002|zdl1994328|语言
 [我的github](https://github.com/toly1994328)|[我的简书](https://www.jianshu.com/u/e4e52c116681)|[我的CSDN](https://blog.csdn.net/qq_30447263)|[个人网站](http://www.toly1994.com)

##### 3.声明
>1----本文由张风捷特烈原创,转载请注明  
2----欢迎广大编程爱好者共同交流  
3----个人能力有限，如有不正之处欢迎大家批评指证，必定虚心改正   
4----看到这里，我在此感谢你的喜欢与支持



#### 附录、静态常量

```
/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/11/11 0011:6:02<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：常量
 */
public class Cons {

    /**
     * 颜色数组
     */
    public static final int[] colors = new int[]{
            0xff33B5E5, 0xff0099CC, 0xffAA66CC, 0xff9933CC,
            0xff99CC00, 0xff669900, 0xffFFBB33, 0xffFF8800, 0xffFF4444, 0xffCC0000};

    /**
     * 用来显示点阵的三维数组
     */
    public static final int[][] digit_test = new int[][]

            {
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 1, 1, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {1, 1, 1, 1, 1, 1, 1}
            };//1




    /**
     * 用来显示点阵的三维数组
     */
    public static final int[][][] digit = new int[][][]{
            {
                    {0, 0, 1, 1, 1, 0, 0},
                    {0, 1, 1, 0, 1, 1, 0},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 1, 1, 0, 1, 1, 0},
                    {0, 0, 1, 1, 1, 0, 0}
            },//0
            {
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 1, 1, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {1, 1, 1, 1, 1, 1, 1}
            },//1
            {
                    {0, 1, 1, 1, 1, 1, 0},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 1, 1, 0, 0, 0},
                    {0, 1, 1, 0, 0, 0, 0},
                    {1, 1, 0, 0, 0, 0, 0},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1}
            },//2
            {
                    {1, 1, 1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 1, 1, 1, 0, 0},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0}
            },//3
            {
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 1, 1, 1, 0},
                    {0, 0, 1, 1, 1, 1, 0},
                    {0, 1, 1, 0, 1, 1, 0},
                    {1, 1, 0, 0, 1, 1, 0},
                    {1, 1, 1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 1, 1, 1, 1}
            },//4
            {
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 0, 0, 0, 0, 0},
                    {1, 1, 0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1, 1, 0},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0}
            },//5
            {
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 1, 1, 0, 0, 0},
                    {0, 1, 1, 0, 0, 0, 0},
                    {1, 1, 0, 0, 0, 0, 0},
                    {1, 1, 0, 1, 1, 1, 0},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0}
            },//6
            {
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 1, 1, 0, 0, 0}
            },//7
            {
                    {0, 1, 1, 1, 1, 1, 0},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0}
            },//8
            {
                    {0, 1, 1, 1, 1, 1, 0},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1},
                    {0, 1, 1, 1, 0, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 1, 1, 0},
                    {0, 0, 0, 1, 1, 0, 0},
                    {0, 1, 1, 0, 0, 0, 0}
            },//9
            {
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 1, 1, 0},
                    {0, 1, 1, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 1, 1, 0},
                    {0, 1, 1, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            }//:
    };
}

```
