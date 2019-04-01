/**/
package product.prison;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import product.prison.adapter.MainAdapter;
import product.prison.app.MyApp;
import product.prison.model.Details;
import product.prison.model.InfoData;
import product.prison.model.Live;
import product.prison.model.TGson;
import product.prison.model.TMenu;
import product.prison.model.TranscribeData;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.info.InfoActivity;
import product.prison.view.live.LiveActivity;
import product.prison.view.live.LiveApkActivity;
import product.prison.view.record.RecordActivity;
import product.prison.view.set.SetActivity;
import product.prison.view.video.VideoApkActivity;
import product.prison.view.video.VideoTypeActivity;

public class MainActivity extends BaseActivity implements MainAdapter.OnItemClickListener {
    private MyApp app;
    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private MainAdapter adapter;
    private ImageView head_logo;
    private ImageView qr_code;
    private List<TMenu> list = new ArrayList<>();
    private int menusize = 0;
    private List<InfoData> info = new ArrayList<>();

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
                    menusize = list.size();
                    getInfo();
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
            if (position > menusize - 1) {
                Logs.e(info.size() + "(position - menusize )" + (position - menusize) + "getDetails" + info.get(position - menusize).getDetails().size());
                List<Details> details = info.get(position - menusize).getDetails();
                if (details.isEmpty())
                    return;
                Bundle bundle = new Bundle();
                bundle.putSerializable("key", (Serializable) details);
                startActivity(new Intent(MainActivity.this, InfoActivity.class).putExtras(bundle));
                return;
            }
            String name = list.get(position).getName();
            int id = list.get(position).getId();
            if (name.contains("直播") || name.contains("电视") || name.contains("tv")) {
                live();
            }
            switch (id) {
                case 60://首页
                    break;
                case 61://直播
                case 47:
                    live();
                break;
                case 62://点播
                case 48:
                    vod();
                    break;
                case 63://录播
                    getTranscribe();
                    break;
                case 64://设置
                    startActivity(new Intent(this, SetActivity.class));
                    break;
                case 43://新闻通知
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getInfo() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getInfo");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("templateType", MyApp.templateType + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<InfoData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<InfoData>>>() {
                            }.getType());
                    info = json.getData();
                    for (InfoData infoData : info) {
                        TMenu tMenu = new TMenu();
                        tMenu.setId(infoData.getId());
//                        tMenu.setName(MyApp.info[MyApp.templateType - 1]);
                        tMenu.setName(infoData.getName());
                        tMenu.setStatus(1);
                        tMenu.setIcon(infoData.getPath());
                        list.add(tMenu);
                    }
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
                            startActivity(new Intent(MainActivity.this, VideoApkActivity.class));
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
