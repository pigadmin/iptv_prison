package product.prison;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.x;

import product.prison.app.MyApp;
import product.prison.utils.ImageUtils;
import product.prison.utils.Utils;

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
//        x.image().bind("", app.getLogoData().getBacks().get(0).getPath(), new Callback.CacheCallback<Drawable>() {
//            @Override
//            public boolean onCache(Drawable result) {
//                return false;
//            }
//
//            @Override
//            public void onSuccess(Drawable result) {
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
    }


}
