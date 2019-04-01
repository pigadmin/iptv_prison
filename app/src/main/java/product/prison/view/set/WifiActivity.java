package product.prison.view.set;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.lang.reflect.Method;

import product.prison.BaseActivity;
import product.prison.R;

/**
 * Created by zhu on 2017/9/26.
 */

public class WifiActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private CheckBox showpsw;
    private EditText wifi_ssid, wifi_psw;
    private ImageButton wifi_start, wifi_stop;
    private WifiManager wifiManager;
    private boolean flag = false;


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.wifi_start:
                flag = true;
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_onscuess), Toast.LENGTH_SHORT).show();
                break;
            case R.id.wifi_stop:
                flag = false;
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_ofscuess), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        try {
            if (!wifi_ssid.getText().toString().equals("")
                    && !wifi_psw.getText().toString().equals("")) {
                setWifiApEnabled(flag);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_fail), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean setWifiApEnabled(boolean enabled) {

        if (enabled) { // disable WiFi in any case
            wifiManager.setWifiEnabled(false);
        }
        try {
            WifiConfiguration apConfig = new WifiConfiguration();
            apConfig.SSID = wifi_ssid.getText().toString();
            apConfig.preSharedKey = wifi_psw.getText().toString();

            apConfig.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);

            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            boolean status = (Boolean) method.invoke(wifiManager, apConfig,
                    enabled);

            if (enabled && status) {
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_onscuess), Toast.LENGTH_SHORT).show();
            } else if (!enabled && status) {
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_ofscuess), Toast.LENGTH_SHORT).show();

            }
            return status;
        } catch (Exception e) {

            if (enabled) {
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_onerror), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_oferror), Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        wifi_ssid = f(R.id.wifi_ssid);
        wifi_ssid.setSelection(wifi_ssid.length());
        wifi_psw = f(R.id.wifi_psw);
        wifi_psw.setSelection(wifi_psw.length());

        showpsw = f(R.id.showpsw);
        showpsw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    wifi_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    wifi_psw.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        wifi_start = f(R.id.wifi_start);
        wifi_stop = f(R.id.wifi_stop);
        wifi_start.setOnClickListener(this);
        wifi_stop.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        try {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public int getContentId() {
        return R.layout.activity_wifi;
    }
}
