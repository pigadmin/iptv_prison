//package product.prison;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import com.tencent.smtt.sdk.QbSdk;
//import com.tencent.smtt.sdk.ValueCallback;
//
//public class TestActivity extends BaseActivity implements QbSdk.PreInitCallback, ValueCallback<String> {
//
//
//    @Override
//    public void loadData() {
//
//
//    }
//
//
//    @Override
//    public void initView(Bundle savedInstanceState) {
//
//    }
//
//
//    @Override
//    public void onCoreInitFinished() {
//        Log.d("test", "onCoreInitFinished");
//    }
//
//    @Override
//    public void onViewInitFinished(boolean isX5Core) {
//        Log.d("test", "onViewInitFinished,isX5Core =" + isX5Core);
//    }
//
//    @Override
//    public void onReceiveValue(String val) {
//        Log.d("test", "onReceiveValue,val =" + val);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//    }
//
//
//
//
//    @Override
//    public int getContentId() {
//        return R.layout.activity_test;
//    }
//
//
//}