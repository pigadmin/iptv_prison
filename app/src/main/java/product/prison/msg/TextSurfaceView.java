package product.prison.msg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import product.prison.msg.IScrollState;

/**
 * 锟斤拷幕锟斤拷
 */
public class TextSurfaceView extends SurfaceView implements Callback, Runnable {
    /**
     * 锟角凤拷锟斤拷锟?
     */
    private boolean isMove = true;
    /**
     * 锟狡讹拷锟斤拷锟斤拷
     */
    private int orientation = 0;
    /**
     * 锟斤拷锟斤拷锟狡讹拷
     */
    public final static int MOVE_LEFT = 1;
    /**
     * 锟斤拷锟斤拷锟狡讹拷
     */
    public final static int MOVE_RIGHT = 0;
    /**
     * 锟狡讹拷锟劫度★拷10锟斤拷锟诫　锟狡讹拷一锟斤拷
     */
    private long speed = 15;
    /**
     * 锟斤拷幕锟斤拷锟斤拷
     */
    private String content;
    /**
     * 锟斤拷幕锟斤拷锟斤拷色
     */
    private String bgColor = "#80000000";

    /**
     * 锟斤拷幕透锟斤拷锟饺★拷默锟较ｏ拷60
     */
    private int bgalpha = 255;

    /**
     * 锟斤拷锟斤拷锟斤拷色 锟斤拷默锟较ｏ拷锟斤拷色 (#FFFFFF)
     */
    private String fontColor = "#FFFFFF";
    /**
     * 锟斤拷锟斤拷透锟斤拷锟饺★拷默锟较ｏ拷锟斤拷透锟斤拷(255)
     */
    private int fontAlpha = 255;
    /**
     * 锟斤拷锟斤拷锟叫?锟斤拷默锟较ｏ拷20
     */
    private float fontSize = 25f;
    /**
     * 锟斤拷锟斤拷
     */
    private SurfaceHolder mSurfaceHolder;
    /**
     * 锟竭程匡拷锟斤拷
     */
    private boolean loop = true;
    /**
     * 锟斤拷锟捷癸拷锟斤拷位锟斤拷锟斤拷始锟斤拷锟?
     */
    private float x = 0;

    IScrollState scroolState;

    /**
     * @param context <see>默锟较癸拷锟斤拷</see>
     */
    public TextSurfaceView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        // 锟斤拷锟矫伙拷锟斤拷锟斤拷锟斤拷锟斤拷为锟斤拷色锟斤拷锟教筹拷Sureface时锟斤拷锟斤拷锟斤拷锟斤拷锟酵革拷锟?
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        // 锟斤拷锟斤拷色
        setBackgroundColor(Color.parseColor(bgColor));
        // 锟斤拷锟斤拷透锟斤拷
        getBackground().setAlpha(bgalpha);
    }

    public TextSurfaceView(Context context, IScrollState scroolState) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        // 锟斤拷锟矫伙拷锟斤拷锟斤拷锟斤拷锟斤拷为锟斤拷色锟斤拷锟教筹拷Sureface时锟斤拷锟斤拷锟斤拷锟斤拷锟酵革拷锟?
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        // 锟斤拷锟斤拷色
        setBackgroundColor(Color.parseColor(bgColor));
        // 锟斤拷锟斤拷透锟斤拷
        getBackground().setAlpha(bgalpha);
        this.scroolState = scroolState;
        bounds = new Rect();
        bounds = new Rect();

    }

    Rect bounds;

    /**
     * @param context
     * @param move    <see>锟角凤拷锟斤拷锟?/see>
     */
    public TextSurfaceView(Context context, boolean move) {
        this(context);
        this.isMove = move;
        setLoop(isMove());
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {

        // Log.d("WIDTH:", "" + getWidth());
        if (isMove) {// 锟斤拷锟斤拷效锟斤拷
            if (orientation == MOVE_LEFT) {
                x = getWidth();
            } else {
                x = -(content.length() * 10);
            }
            new Thread(this).start();
        } else {// 锟斤拷锟斤拷锟斤拷只锟斤拷一锟斤拷
            draw();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        loop = false;
    }

    /**
     * 锟斤拷图
     */
    private void draw() {
        // 锟斤拷锟斤拷

        try {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (mSurfaceHolder == null || canvas == null) {
                return;
            }
            Paint paint = new Paint();
            // 锟斤拷锟斤拷
            canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
            // 锟斤拷锟?
            paint.setAntiAlias(true);
            // 锟斤拷锟斤拷
            paint.setTypeface(Typeface.SANS_SERIF);
            // 锟斤拷锟斤拷锟叫?
            paint.setTextSize(fontSize);
            // 锟斤拷锟斤拷锟斤拷色
            paint.setColor(Color.parseColor(fontColor));
            // 锟斤拷锟斤拷透锟斤拷锟斤拷
            paint.setAlpha(fontAlpha);
            // 锟斤拷锟斤拷锟斤拷

            paint.getTextBounds(content, 0, content.length(), bounds);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top)
                    / 2 - fontMetrics.top;
            canvas.drawText(content, x, baseline, paint);
            // 锟斤拷锟斤拷锟斤拷示
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            // 锟斤拷锟斤拷效锟斤拷
            if (isMove) {
                // 锟斤拷锟斤拷锟斤拷占锟斤拷锟斤拷
                float conlen = paint.measureText(content);
                // 锟斤拷锟斤拷锟斤拷
                int w = getWidth();
                // 锟斤拷锟斤拷
                if (orientation == MOVE_LEFT) {// 锟斤拷锟斤拷
                    if (x < -conlen) {
                        x = w;
                        if (this.scroolState != null)
                            this.scroolState.stop();
                    } else {
                        x -= 2;
                    }
                } else if (orientation == MOVE_RIGHT) {// 锟斤拷锟斤拷
                    if (x >= w) {
                        x = -conlen;
                        if (this.scroolState != null)
                            this.scroolState.stop();
                    } else {
                        x += 2;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void run() {
        while (loop) {
            synchronized (mSurfaceHolder) {
                draw();
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                Log.e("TextSurfaceView", ex.getMessage() + "\n" + ex);
            }
        }
        content = null;
    }

    /****************************** set get method ***********************************/

    private int getOrientation() {
        return orientation;
    }

    /**
     * @param orientation <li>锟斤拷锟斤拷选锟斤拷锟洁静态锟斤拷锟斤拷</li> <li>0.MOVE_RIGHT 锟斤拷锟斤拷 (默锟斤拷)</li>
     *                    <li>1.MOVE_LEFT 锟斤拷锟斤拷</li>
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    private long getSpeed() {
        return speed;
    }

    /**
     * @param speed <li>锟劫讹拷锟皆猴拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷贫锟街拷锟斤拷时锟斤拷锟斤拷</li> <li>默锟斤拷为 1500
     *              锟斤拷锟斤拷</li>
     */
    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public boolean isMove() {
        return isMove;
    }

    /**
     * @param isMove <see>默锟较癸拷锟斤拷</see>
     */
    public void setMove(boolean isMove) {
        this.isMove = isMove;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public void setBgalpha(int bgalpha) {
        this.bgalpha = bgalpha;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setFontAlpha(int fontAlpha) {
        this.fontAlpha = fontAlpha;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

}
