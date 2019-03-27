package product.prison;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends Activity implements IBaseView {

    public static Activity activity;

    public <T, View> T f(int id) {
        return (T) super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initView(savedInstanceState);
        loadData();
        activity = this;
    }


}
