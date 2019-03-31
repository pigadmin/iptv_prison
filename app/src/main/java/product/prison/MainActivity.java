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
import android.widget.Toast;


import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import product.prison.adapter.MainAdapter;
import product.prison.app.MyApp;
import product.prison.model.Live;
import product.prison.model.TGson;
import product.prison.model.TMenu;
import product.prison.model.TranscribeData;
import product.prison.model.WelcomeAd;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.LiveActivity;
import product.prison.view.LiveApkActivity;
import product.prison.view.RecordActivity;
import product.prison.view.SetActivity;
import product.prison.view.VideoActivity;
import product.prison.view.VideoTypeActivity;

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
                try {
                    Logs.e(result);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
//        Logs.e(position + "");
        try {
            int open = list.get(position).getStatus();
            if (open == 0) {
                Toast.makeText(getApplicationContext(), R.string.disable, Toast.LENGTH_SHORT).show();
                return;
            }
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
                    getTranscribe();
                    break;
                case 64://设置
                    setting();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setting() {
        startActivity(new Intent(this, SetActivity.class));
    }

    private void getTranscribe() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getTranscribe");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<TranscribeData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<TranscribeData>>>() {
                            }.getType());

                    Logs.e(json.getData().size() + "getTranscribe");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", (Serializable) json.getData());
                    startActivity(new Intent(MainActivity.this, RecordActivity.class).putExtras(bundle));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

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
                try {
                    Logs.e(result);
                    TGson<Integer> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<Integer>>() {
                            }.getType());
                    if (json.getData() == null)
                        return;
                    switch (json.getData()) {
                        case 1:
                            startActivity(new Intent(MainActivity.this, VideoTypeActivity.class));
                            break;
                        case 2:
                            break;
                    }
//                Logs.e(json.getData() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
        RequestParams params = new RequestParams(MyApp.apiurl + "live");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<Live> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<Live>>() {
                            }.getType());
                    int type = json.getData().getType();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", (Serializable) json.getData().getData());
                    switch (type) {
                        case 1:
                            break;
                        case 2:
                            startActivity(new Intent(MainActivity.this, LiveActivity.class).putExtras(bundle));
                            break;
                        case 3:
                            startActivity(new Intent(MainActivity.this, LiveApkActivity.class).putExtras(bundle));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
//
//    private void toActivity(Object o, Class<?> c) {
//        if (o != null) {
//        }
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("key", (Serializable) o);
//        startActivity(new Intent(this, c).putExtras(bundle));
//    }


}
