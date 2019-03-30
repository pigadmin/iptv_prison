package product.prison;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;


import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.adapter.MainAdapter;
import product.prison.app.MyApp;
import product.prison.model.TGson;
import product.prison.model.TMenu;
import product.prison.model.WelcomeAd;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.SetActivity;

public class MainActivity extends BaseActivity implements MainAdapter.OnItemClickListener {
    private MyApp app;
    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private MainAdapter adapter;
    private ImageView head_logo;
    private ImageView qr_code;
    private List<TMenu> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        app = (MyApp) getApplication();
        mainrecyle = f(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        mainrecyle.setLayoutManager(layoutManager);
        head_logo = f(R.id.head_logo);
        qr_code = f(R.id.qr_code);
    }

    @Override
    public void loadData() {
        getmenu();
    }

    private void getmenu() {
        RequestParams params = new RequestParams(MyApp.apiurl + "tmenu");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("templateType", MyApp.templateType + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logs.e("**********" + result);
                TGson<List<TMenu>> json = Utils.gson.fromJson(result,
                        new TypeToken<TGson<List<TMenu>>>() {
                        }.getType());
                list = json.getData();
                if (list.isEmpty())
                    return;
                app.settMenu(list);

                Logs.e(app.gettMenu().size() + "");
                adapter = new MainAdapter(MainActivity.this, list);
                adapter.setOnItemClickListener(MainActivity.this);
                mainrecyle.setAdapter(adapter);
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
    }


    @Override
    public int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItemClick(View view, int position) {
        Logs.e(position + "");
        int id = list.get(position).getId();
        switch (id) {
            case 60://首页
                break;
            case 61://直播
                live();
                break;
            case 62://点播
                vod();
                break;
            case 63://录播
                recode();
                break;
            case 64://设置
                setting();
                break;
        }


    }

    private void setting() {
        startActivity(new Intent(this, SetActivity.class));
    }

    private void recode() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getTranscribe");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logs.e(result);
//                    TGson<List<WelcomeAd>> json = Utils.gson.fromJson(result,
//                            new TypeToken<TGson<List<WelcomeAd>>>() {
//                            }.getType());

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
    }

    private void vod() {//     （1.本地点播 2.第三方点播）
        RequestParams params = new RequestParams(MyApp.apiurl + "vod/sel");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logs.e(result);
                    TGson<List<WelcomeAd>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<WelcomeAd>>>() {
                            }.getType());

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
    }

    private void live() {
//        type: 1   (1.模拟直播。2.数字直播 3.第三方直播)
        RequestParams params = new RequestParams(MyApp.apiurl + "live");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("type", "3");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logs.e(result);
//                    TGson<List<WelcomeAd>> json = Utils.gson.fromJson(result,
//                            new TypeToken<TGson<List<WelcomeAd>>>() {
//                            }.getType());

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

    }
}
