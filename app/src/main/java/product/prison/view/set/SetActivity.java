package product.prison.view.set;

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.CommonAdapter;
import product.prison.adapter.SetAdapter;
import product.prison.app.MyApp;
import product.prison.utils.SpUtils;

public class SetActivity extends BaseActivity implements SetAdapter.OnItemClickListener {
    private String[] list = {"WIFI", "USB", "浏览器",
            "网络设置", "IP设置"};
    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private SetAdapter adapter;


    @Override
    public void initView(Bundle savedInstanceState) {
        mainrecyle = f(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);


        mainrecyle.setLayoutManager(layoutManager);
    }

    @Override
    public void loadData() {
        adapter = new SetAdapter(this, list);
        mainrecyle.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public int getContentId() {
        return R.layout.activity_main;
    }

    //判断点击功能
    private void toFram(int position) {

        try {
            switch (position) {
                case 0://WIFI
                    startActivity(new Intent(SetActivity.this, WifiActivity.class));
                    break;
                case 1://USB
                    startActivity(new Intent(SetActivity.this, UsbActivity.class));
                    break;
                case 2://浏览器
                    startActivity(new Intent(SetActivity.this, WebActivity.class));
                    break;
                case 3://设置
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    break;
                case 4://设置IP
                    if (serverip_dialog != null && serverip_dialog.isShowing())
                        return;
                    setip();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //点击事件
    @Override
    public void onItemClick(View view, int position) {
        toFram(position);
    }


    private AlertDialog serverip_dialog;

    private void setip() {
        // TODO Auto-generated method stub
        try {
            serverip_dialog = new AlertDialog.Builder(this).create();
            serverip_dialog.setView(new EditText(this));
            serverip_dialog.show();
            serverip_dialog.setContentView(R.layout.serverip_dialog);
            final EditText serverip = serverip_dialog
                    .findViewById(R.id.serverip);
            serverip.setText(MyApp.ip);
            serverip.requestFocus();
            ImageButton serverip_ok = serverip_dialog
                    .findViewById(R.id.serverip_ok);
            serverip_ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    serverip_dialog.dismiss();
                    SpUtils.putString(getApplicationContext(), "ip", serverip.getText().toString());
                    Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }

    }


}
