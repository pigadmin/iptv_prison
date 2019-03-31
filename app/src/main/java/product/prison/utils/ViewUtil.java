package product.prison.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import product.prison.model.MaterialVO;
import product.prison.model.ProgramContentVO;
import product.prison.model.ViewSizeAndMargins;

public class ViewUtil {

    // flash
    public static WebView createWebView(Context context, ProgramContentVO vo,
                                        HashMap<String, String> matrailMap) {

        ViewSizeAndMargins vv = new ViewSizeAndMargins(vo);

        WebView webView = new WebView(context);

        android.widget.FrameLayout.LayoutParams fparams = new android.widget.FrameLayout.LayoutParams(
                vv.getWith(), vv.getHeight());
        fparams.setMargins(vv.getLeft(), vv.getTop(), vv.getRight(),
                vv.getBottom());
        webView.setLayoutParams(fparams);
        // webView.getSettings().setPluginsEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.setBackgroundColor(0);

        List<MaterialVO> list = vo.getMaterials();
        if (list != null && !list.isEmpty()) {
            String key = list.get(0).getPath().split("/")[list.get(0).getPath()
                    .split("/").length - 1];
            webView.loadUrl("file://" + matrailMap.get(key));
            // System.out.println("file://"
            // + matrailMap.get(list.get(0).getPath()));
        } else {
            // content null
        }
        return webView;
    }

    public static WebView createWebView2(Context context, ProgramContentVO vo,
                                         HashMap<String, String> matrailMap) {

        ViewSizeAndMargins vv = new ViewSizeAndMargins(vo);

        WebView webView = new WebView(context);

        android.widget.FrameLayout.LayoutParams fparams = new android.widget.FrameLayout.LayoutParams(
                vv.getWith(), vv.getHeight());
        fparams.setMargins(vv.getLeft(), vv.getTop(), vv.getRight(),
                vv.getBottom());
        webView.setLayoutParams(fparams);
        // webView.getSettings().setPluginsEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.setBackgroundColor(0);
        webView.loadDataWithBaseURL(null, vo.getTxt(), "text/html", "utf-8",
                null);

        return webView;
    }

    // TXT格式文档
    public static TextView createTextView(Context context, ProgramContentVO vo,
                                          HashMap<String, String> matrailMap) {
        // TextureView
        ViewSizeAndMargins vv = new ViewSizeAndMargins(vo);

        TextView textView = new TextView(context);
        android.widget.FrameLayout.LayoutParams fparams = new android.widget.FrameLayout.LayoutParams(
                vv.getWith(), vv.getHeight());
        fparams.setMargins(vv.getLeft(), vv.getTop(), vv.getRight(),
                vv.getBottom());
        textView.setLayoutParams(fparams);

        // 明确
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        // textView.setText(Html
        // .fromHtml("<html><head><title>TextView使用HTML</title></head><body><p><strong>强调</strong></p><p><em>斜体</em></p>"
        // +
        // "<p><a href=\"http://www.dreamdu.com/xhtml/\">超链接HTML入门</a>学习HTML!</p><p><font color=\"#aabb00\">颜色1"
        // +
        // "</p><p><font color=\"#00bbaa\">颜色2</p><h1>标题1</h1><h3>标题2</h3><h6>标题3</h6><p>大于>小于<</p><p>"
        // +
        // "下面是网络图片</p><img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></body></html>"));

        textView.setText(Html.fromHtml("<html><head></head><body>"
                + vo.getTxt() + "</body></html>"));

        return textView;
    }

    // HTML 格式
    public static TextView createHtmlView(Context context, ProgramContentVO vo,
                                          HashMap<String, String> matrailMap) {
        ViewSizeAndMargins vv = new ViewSizeAndMargins(vo);

        TextView textView = new TextView(context);
        android.widget.FrameLayout.LayoutParams fparams = new android.widget.FrameLayout.LayoutParams(
                vv.getWith(), vv.getHeight());
        fparams.setMargins(vv.getLeft(), vv.getTop(), vv.getRight(),
                vv.getBottom());
        textView.setLayoutParams(fparams);

        // 明确
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setText(Html.fromHtml(vo.getTxt()));
        return textView;
    }

    // private static Bitmap bit2 =null;
    // 图片
    public static ImageView createImageView(Context context,
                                            ProgramContentVO vo, HashMap<String, String> matrailMap) {
        ImageView imageView = null;
        try {
            Bitmap bit2 = null;
            // 大小
            ViewSizeAndMargins vv = new ViewSizeAndMargins(vo);

            // ViewFlipper imageView=new ViewFlipper(context);
            imageView = new ImageView(context);
            // 控件大小位置
            android.widget.FrameLayout.LayoutParams fparams = new android.widget.FrameLayout.LayoutParams(
                    vv.getWith(), vv.getHeight());
            fparams.setMargins(vv.getLeft(), vv.getTop(), vv.getRight(),
                    vv.getBottom());

            imageView.setLayoutParams(fparams);

            AnimationDrawable ani = new AnimationDrawable();

            List<MaterialVO> list = vo.getMaterials();
            if (list != null && !list.isEmpty()) {
                 System.out.println("图片的张数==" + list.size());

                BitmapDrawable bitdra = null;
                for (MaterialVO m : list) {
                    // 图片压缩
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inJustDecodeBounds = true;

                    // http:\/\/192.168.2.101:8080\/krizerad\/upload\/materialfile\/d538e6ec863a486c9446796d7c8129cb_1.png
//                     System.out.println(matrailMap
//                     .get("d538e6ec863a486c9446796d7c8129cb_2.png"));

                    BitmapFactory.decodeFile(matrailMap.get(m.getPath().split(
                            "/")[m.getPath().split("/").length - 1]), option);

                     System.out.println("图片的路径==" + m.getPath());

                    option.inSampleSize = calculateImageRatio(option,
                            vv.getWith(), vv.getHeight());

                    option.inJustDecodeBounds = false;

                    bit2 = BitmapFactory.decodeFile(matrailMap.get(m.getPath()
                                    .split("/")[m.getPath().split("/").length - 1]),
                            option);

                    if (bit2 != null) {
                        bitdra = new BitmapDrawable(bit2);
                    }

                    ani.addFrame(bitdra, vo.getContentInterval() * 1000);

                }
            }
            ani.setOneShot(false);
            imageView.setImageDrawable(ani);
            imageView.setScaleType(ScaleType.FIT_XY);
            ani.start();

        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            // bit2.recycle();
        } catch (Exception e) {
            // TODO: handle exception
        }

        // AnimationSet aset = new AnimationSet(true);
        // AlphaAnimation aa = new AlphaAnimation(1, 0);
        // aa.setDuration(2000);
        // aset.addAnimation(aa);
        // imageView.startAnimation(aset);
        return imageView;
    }

    public static int calculateImageRatio(BitmapFactory.Options option,
                                          int reqWidth, int reqHeigh) {
        int inSampleSize = 1;

        int imageWidth = option.outWidth;
        int imageHeight = option.outHeight;

        if (imageWidth > reqWidth || imageHeight > reqHeigh) {
            int wRatio = Math.round((float) imageWidth / (float) reqWidth);
            int hRatio = Math.round((float) imageHeight / (float) reqHeigh);
            if (hRatio < wRatio) {
                inSampleSize = hRatio;
            } else {
                inSampleSize = wRatio;
            }
        }
        return inSampleSize;
    }

    // SurfaceView
    public static SurfaceView createSurfaceView(Context context,
                                                ProgramContentVO vo, HashMap<String, String> matrailMap) {

        ViewSizeAndMargins vv = new ViewSizeAndMargins(vo);

        SurfaceView surfaceView = new SurfaceView(context);
        android.widget.FrameLayout.LayoutParams fparams = new android.widget.FrameLayout.LayoutParams(
                vv.getWith(), vv.getHeight());
        fparams.setMargins(vv.getLeft(), vv.getTop(), vv.getRight(),
                vv.getBottom());
        surfaceView.setLayoutParams(fparams);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        return surfaceView;

    }

    public static MediaPlayer initMediaPlayer11(String materialPath,
                                                SurfaceView surfaceView) {
        try {
            MediaPlayer media = new MediaPlayer();
            media.reset();
            media.setAudioStreamType(AudioManager.STREAM_MUSIC);
            media.setDataSource(materialPath);
            media.setDisplay(surfaceView.getHolder());
            media.prepareAsync();
            media.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mdplayer) {
                    // TODO Auto-generated method stub
                    mdplayer.start();
                    mdplayer.setLooping(true);
                }
            });

            media.setOnErrorListener(new OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });
            return media;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    // 截屏
    public static Bitmap screenShot(Activity act) {
        try {
            View view = act.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();

            Bitmap bitmap = view.getDrawingCache();

            Rect rct = new Rect();
            act.getWindow().getDecorView().getWindowVisibleDisplayFrame(rct);

            int barHeight = rct.top;
            int width = act.getWindowManager().getDefaultDisplay().getWidth();
            int height = act.getWindowManager().getDefaultDisplay().getHeight();

            Bitmap bit = Bitmap.createBitmap(bitmap, 0, barHeight, width,
                    height - barHeight);

            view.destroyDrawingCache();
            return bit;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;

    }

    private static float getDegreesForRotation(int value) {
        switch (value) {
            case Surface.ROTATION_90:
                return 360f - 90f;
            case Surface.ROTATION_180:
                return 360f - 180f;
            case Surface.ROTATION_270:
                return 360f - 270f;
        }
        return 0f;
    }

    // private static Display mDisplay;
    // private static DisplayMetrics mDisplayMetrics;
    // private static Matrix mDisplayMatrix;
    // private static Bitmap mScreenBitmap;
    // static WindowManager mWindowManager;
    // static NotificationManager mNotificationManager;
    //
    // public static Bitmap sc(Context context) {
    // // TODO Auto-generated method stub
    // mDisplayMatrix = new Matrix();
    // mWindowManager = (WindowManager) context
    // .getSystemService(Context.WINDOW_SERVICE);
    // mNotificationManager = (NotificationManager) context
    // .getSystemService(Context.NOTIFICATION_SERVICE);
    // mDisplay = mWindowManager.getDefaultDisplay();
    // mDisplayMetrics = new DisplayMetrics();
    // mDisplay.getRealMetrics(mDisplayMetrics);
    //
    // mDisplay.getRealMetrics(mDisplayMetrics);
    // float[] dims = { mDisplayMetrics.widthPixels,
    // mDisplayMetrics.heightPixels };
    // float degrees = getDegreesForRotation(mDisplay.getRotation());
    // boolean requiresRotation = (degrees > 0);
    // if (requiresRotation) {
    // // Get the dimensions of the device in its native orientation
    // mDisplayMatrix.reset();
    // mDisplayMatrix.preRotate(-degrees);
    // mDisplayMatrix.mapPoints(dims);
    // dims[0] = Math.abs(dims[0]);
    // dims[1] = Math.abs(dims[1]);
    // }
    //
    // Log.d("tests", "takeScreenshot, dims, w-h: " + dims[0] + "-" + dims[1]
    // + "; dm w-h: " + mDisplayMetrics.widthPixels
    // + mDisplayMetrics.heightPixels);
    // // Take the screenshot
    // mScreenBitmap = SurfaceControl.screenshot((int) dims[0], (int) dims[1]);
    //
    // return mScreenBitmap;
    // }

//	public static void screenShots(Activity activity) {
//		// 获取windows中最顶层的view
//
//		IDevice device;
//
//		AndroidDebugBridge bridge = AndroidDebugBridge.createBridge();
//
//		waitDeviceList(bridge);
//
//		IDevice devices[] = bridge.getDevices();
//
//		device = devices[0];
//
//		try {
//			RawImage rawScreen = device.getScreenshot();
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//		}
//
//	}
//
//	private static void waitDeviceList(AndroidDebugBridge bridge) {
//
//		int count = 0;
//
//		while (bridge.hasInitialDeviceList() == false) {
//
//			try {
//
//				Thread.sleep(100); // 如果没有获得设备列表，则等待
//
//				count++;
//
//			} catch (InterruptedException e) {
//			}
//
//			if (count > 300) {
//
//				// 设定时间超过300×100 ms的时候为连接超时
//
//				System.err.print("Time out");
//
//				break;
//
//			}
//
//		}
//
//	}

    private static byte[] BitmapToByte(Bitmap bit) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, bout);
        return bout.toByteArray();
    }

    public static File BitmapToFile(Bitmap bit) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(ZipUtil.basePath + File.separator + "a123.png");
            FileOutputStream fout = new FileOutputStream(file);

            stream = new BufferedOutputStream(fout);
            stream.write(BitmapToByte(bit));
        } catch (Exception e) {
            // TODO: handle exception
            // e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    // e2.printStackTrace();
                }
            }
        }
        return file;
    }

    // private void scview(Context context) {
    // // TODO Auto-generated method stub
    // MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    // try {
    // retriever.setDataSource(mFilePath);
    //
    // Bitmap bitmap = retriever.getFrameAtTime(miCurPos * 1000);
    //
    // String path = Environment.getExternalStorageDirectory()
    // + File.separator + Environment.DIRECTORY_DCIM + "/Camera";
    //
    // path += Tools.getDisplayName(mFilePath) + "_"
    // + Tools.IntToTimeString(miCurPos) + "_" + ".png";
    //
    // Tools.saveBitmap(bitmap, path);
    //
    // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
    // Uri.fromFile(new File(path))));
    // } catch (IllegalArgumentException e) {
    // e.printStackTrace();
    // // VideoLogger.e(TAG, "MediaMetadataRetriever.setDataSource Failed: "
    // // + mFilePath);
    // // ShowToast(mResources.getString(R.string.info_screen_shot_failed), 0);
    // }
    // }
}
