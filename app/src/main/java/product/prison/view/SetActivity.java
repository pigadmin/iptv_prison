package product.prison.view;

import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.SetAdapter;

public class SetActivity extends BaseActivity implements SetAdapter.OnItemClickListener {

    private String[] set_name = {"WIFI", "USB", "浏览器",
            "网络设置", "IP设置"};
    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private SetAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);
    }

    @Override
    public void loadData() {

        adapter = new SetAdapter(this, set_name);
        left_list.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        toFram(0);
    }

    @Override
    public int getContentId() {
        return R.layout.activity_list;
    }

    //判断点击功能
    private void toFram(int position) {

        try {
            switch (position) {
                case 0://WIFI
//                    FragmentTools.Replace(getFragmentManager(),
//                            new WifiActivity());
                    break;
                case 1://USB
//                    FragmentTools.Replace(getFragmentManager(),
//                            new USBFr());
                    break;
                case 2://浏览器
//                    FragmentTools.Replace(getFragmentManager(),
//                            new WebActivity());
                    break;
                case 3://设置
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    break;
                case 4://设置IP
//                    new ServerIpDialog(new Handler(), SetActivity.this).crt();
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
}
