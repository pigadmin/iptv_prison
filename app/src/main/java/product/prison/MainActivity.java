package product.prison;

import android.os.Bundle;

import com.github.nkzawa.emitter.Emitter;

import product.prison.app.MyApp;
import product.prison.utils.Logs;
import product.prison.utils.SocketIO;

public class MainActivity extends BaseActivity {
    private MyApp app;

    @Override
    public void initView(Bundle savedInstanceState) {
        app = (MyApp) getApplication();
    }

    @Override
    public void loadData() {
//        Thread.
        Logs.e(app.gettMenu().size()+"");
    }


    @Override
    public int getContentId() {
        return R.layout.activity_main;
    }
}
