/**/
package product.prison;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
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
import product.prison.broadcast.MyAction;
import product.prison.model.Backs;
import product.prison.model.Details;
import product.prison.model.InfoData;
import product.prison.model.Live;
import product.prison.model.TGson;
import product.prison.model.TMenu;
import product.prison.model.TranscribeData;
import product.prison.utils.InstallApk;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.dish.DishTypeActivity;
import product.prison.view.game.GameActivity;
import product.prison.view.info.InfoActivity;
import product.prison.view.live.LiveActivity;
import product.prison.view.msg.NoticeActivity;
import product.prison.view.music.MusicActivity;
import product.prison.view.news.NewsActivity;
import product.prison.view.record.RecordActivity;
import product.prison.view.satisfied.SatisfiedActivity;
import product.prison.view.set.SetActivity;
import product.prison.view.video.VideoTypeActivity;

public class MainActivity extends BaseActivity implements MainAdapter.OnItemClickListener {
    private MyApp app;
    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private MainAdapter adapter;
    private List<TMenu> list = new ArrayList<>();
    private int menusize = 0;
    private List<InfoData> info = new ArrayList<>();
    private MediaPlayer mediaPlayer;


    @Override
    public void initView(Bundle savedInstanceState) {
        app = (MyApp) getApplication();
        mainrecyle = f(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        mainrecyle.setLayoutManager(layoutManager);


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
//                mediaPlayer.setLooping(true);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setBgMusic();
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyAction.PAUSEBGMUSIC);
        filter.addAction(MyAction.GOONBGMUSIC);
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(MyAction.PAUSEBGMUSIC)) {
                    Logs.e("暂停----");

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                }
                if (intent.getAction().equals(MyAction.GOONBGMUSIC)) {
                    Logs.e("继续播放-----");
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void loadData() {
        try {
            getmenu();
            setBgMusic();
            if (!app.getUpdateurl().equals("")) {
                String url = app.getUpdateurl();
                Logs.e("升级" + url);
                app.setUpdateurl("");
                //            new Utils().Download(getApplicationContext(), app.getUpdateurl(), true);
                new InstallApk(MainActivity.this, url).downloadAndInstall();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getmenu() {
        RequestParams params = new RequestParams(MyApp.apiurl + "tmenu");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("templateType", MyApp.templateType + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("getmenu: " + result);
                    TGson<List<TMenu>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<TMenu>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    list = json.getData();
                    menusize = list.size();
                    getInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "访问异常", Toast.LENGTH_SHORT).show();
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
            String name = list.get(position).getName();
            int id = list.get(position).getId();
            Logs.e(id + "");
            if (open == 0) {
                Toast.makeText(getApplicationContext(), R.string.disable, Toast.LENGTH_SHORT).show();
                return;
            }
            if (position > menusize - 1) {
                Logs.e(info.size() + "长度  位置" + (position - menusize) + " getDetails长度" + info.get(position - menusize).getDetails().size());

                Logs.e("***********" + Utils.gson.toJson(info.get(position - menusize)));

                List<Details> details = info.get(position - menusize).getDetails();
                InfoData infoData = info.get(position - menusize);

                if (infoData.getType() == 2 && details.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noneconnent), Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("key", (Serializable) infoData);
                startActivity(new Intent(MainActivity.this, InfoActivity.class).putExtras(bundle));
                return;
            }

            switch (id) {
                case -60://首页
                    break;
                case 25:
                case 61://直播
                case 47:
                    live();
                    break;
                case 27:
                case 44://互动游戏
                case 64:
                    startActivity(new Intent(this, GameActivity.class));
                    break;
                case 29:
                case 45://问卷调查
                case 60:
                    startActivity(new Intent(this, SatisfiedActivity.class));
                    break;
                case 26:
                case 62://点播
                case 48:
                    vod();
                    break;
                case -63://录播
                    getTranscribe();
                    break;
                case 30:
                case 51://商品
                case 68:
                    startActivity(new Intent(this, DishTypeActivity.class));
                    break;
                case 31:
                case 59://音乐
                case 63:
                    startActivity(new Intent(this, MusicActivity.class));
                    break;
                case 32:
                case 65: //设置
                case 46:
                    startActivity(new Intent(this, SetActivity.class));
                    break;
                case 33:
                case 43://3新闻通知
                    startActivity(new Intent(this, NewsActivity.class));
                    break;
                case 40:
                case 58://查询未读消息
                case 67:
                    startActivity(new Intent(this, NoticeActivity.class));
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
                    Logs.e("getInfo: " + result);
                    TGson<List<InfoData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<InfoData>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
//                    Logs.e(json.getData().size() + "");
                    info = json.getData();

                    if (!info.isEmpty()) {
                        int n = -1;
                        for (int i = 0; i < info.size(); i++) {
//                            Logs.e(info.get(i).getName());
                            if (info.get(i).getName().equals("校内新闻")) {
                                n = i;
//                                Logs.e("校内新闻"+n);
                                app.setInfoData(info.get(i));
                            } else {
                                TMenu tMenu = new TMenu();
                                tMenu.setId(info.get(i).getId());
//                        tMenu.setName(MyApp.info[MyApp.templateType - 1]);
                                tMenu.setName(info.get(i).getName());

                                tMenu.setStatus(1);
                                tMenu.setIcon(info.get(i).getIcon());
//                                if (!info.get(i).getPics().isEmpty()) {
//                                    tMenu.setIcon(info.get(i).getPics().get(0).getFilePath());
//                                }
                                list.add(tMenu);
                            }
                        }
                        if (n > -1) {
                            Logs.e("移除校内新闻-下标" + n);
                            info.remove(n);
                        }
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
//                Logs.e("getInfo接口异常");
//                adapter = new MainAdapter(MainActivity.this, list);
//                adapter.setOnItemClickListener(MainActivity.this);
//                mainrecyle.setAdapter(adapter);
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
                    Logs.e("getTranscribe " + result);
                    TGson<List<TranscribeData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<TranscribeData>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
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
        startActivity(new Intent(MainActivity.this, VideoTypeActivity.class));
//        RequestParams params = new RequestParams(MyApp.apiurl + "vod/sel");
//        params.addBodyParameter("mac", MyApp.mac);
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                try {
//                    Logs.e("vod "+result);
//                    TGson<Integer> json = Utils.gson.fromJson(result,
//                            new TypeToken<TGson<Integer>>() {
//                            }.getType());
//                    if (!json.getCode().equals("200")) {
//                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                    if (json.getData() == null)
//                        return;
//                    switch (json.getData()) {
//                        case 1:
//                            startActivity(new Intent(MainActivity.this, VideoTypeActivity.class));
//                            break;
//                        case 2:
//                            startActivity(new Intent(MainActivity.this, VideoApkActivity.class));
//                            break;
//                    }
////                Logs.e(json.getData() + "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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


    private void live() {
        RequestParams params = new RequestParams(MyApp.apiurl + "live");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("live " + result);
                    final TGson<Live> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<Live>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    if (json.getData() == null)
                        return;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", (Serializable) json.getData());
                    startActivity(new Intent(MainActivity.this, LiveActivity.class).putExtras(bundle));
//                    startActivity(new Intent(MainActivity.this, LiveApkActivity.class).putExtras(bundle));
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
    private void setBgMusic() {
        try {
            if (app.getLogoData() == null) return;
            String url = "";
            for (Backs backs : app.getLogoData().getBacks()) {
                if (backs.getType() == 2) {
                    url = backs.getPath();
                }


            }
            if (url.isEmpty() || !url.startsWith("h"))
                return;
            Logs.e("bgmusic" + url);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(url));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Logs.e("onPause");
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Logs.e("onRestart");
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
