package product.prison;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import product.prison.app.MyApp;
import product.prison.model.Backs;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;

public abstract class BaseActivity extends Activity implements IBaseView {

    public static Activity activity;

    public <T, View> T f(int id) {
        return (T) super.findViewById(id);
    }

    private MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initView(savedInstanceState);
        loadData();
        activity = this;
        app = (MyApp) getApplication();

        setBg();
    }

    private void setBg() {
        try {
            if (app.getLogoData() == null) return;
            if (app.getBg() != null) {
                getWindow().getDecorView().setBackground(app.getBg());
                return;
            }
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setFailureDrawableId(R.drawable.bg)
                    .setLoadingDrawableId(R.drawable.bg)
                    .build();
            String url = "";
            for (Backs backs : app.getLogoData().getBacks()) {
                if (backs.getType() == 1) {
                    url = backs.getPath();
                }
            }
            Logs.e("bg" + url);
            x.image().loadDrawable(url, imageOptions, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable bg) {
                    getWindow().getDecorView().setBackground(bg);
                    app.setBg(bg);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
