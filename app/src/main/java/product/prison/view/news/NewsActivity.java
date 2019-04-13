package product.prison.view.news;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.CommonListAdapter;
import product.prison.adapter.NewsGridAdapter;
import product.prison.adapter.TeleplayGridAdapter;
import product.prison.adapter.VideoGridAdapter;
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.Details;
import product.prison.model.News;
import product.prison.model.TGson;
import product.prison.model.Vod;
import product.prison.model.VodData;
import product.prison.model.VodTypeData;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.set.WebActivity;
import product.prison.view.video.PlayerActivity;

public class NewsActivity extends BaseActivity implements CommonListAdapter.OnItemClickListener, AdapterView.OnItemClickListener {

    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private CommonListAdapter listAdapter;
    private GridView right_grid;
    private List<News> grid = new ArrayList<>();

    private GridView teleplay_grid;
    private TeleplayGridAdapter teleplayadapter;
    String[] list = {"头条", "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};
    String[] newscat2 = {"top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};
    private NewsGridAdapter gridAdapter;
    private MyApp app;

    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);
        right_grid = f(R.id.right_grid);
        right_grid.setOnItemClickListener(NewsActivity.this);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);

    }


    @Override
    public void loadData() {
        try {
            app = (MyApp) getApplication();
            if (app.getInfoData() != null) {
                list = new String[]{app.getInfoData().getName(), "头条", "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};
                newscat2 = new String[]{"", "top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};
            }

            listAdapter = new CommonListAdapter(getApplicationContext(), list);
            left_list.setAdapter(listAdapter);
            listAdapter.setOnItemClickListener(NewsActivity.this);

            getNews(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getContentId() {
        return R.layout.activity_video;
    }

    @Override
    public void onItemClick(View view, int position) {
        getNews(position);
        listAdapter.update(position);
    }


    private void getNews(int position) {
        if (newscat2[position].equals("")) {
            grid = new ArrayList<>();

            Logs.e(Utils.gson.toJson(app.getInfoData()));

            for (Details details : app.getInfoData().getDetails()) {
                News news = new News();
                news.setTitle(details.getName());
                news.setThumbnail_pic_s(details.getPics().get(0).getFilePath());
                news.setUrl(details.getPics().get(0).getFilePath());
                grid.add(news);
            }
            Logs.e(grid.size() + "");
            gridAdapter = new NewsGridAdapter(NewsActivity.this, grid);
            right_grid.setAdapter(gridAdapter);
            return;
        }
        RequestParams params = new RequestParams(MyApp.apiurl + "getNews");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("type", newscat2[position]);
        params.addBodyParameter("pageNo", "1");
        params.addBodyParameter("pageSize", "40");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<News>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<News>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    grid = json.getData();
                    Logs.e(grid.size() + "");
                    gridAdapter = new NewsGridAdapter(NewsActivity.this, grid);
                    right_grid.setAdapter(gridAdapter);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String path = grid.get(position).getUrl();


            Intent intent = new Intent(NewsActivity.this, WebActivity.class);
            intent.putExtra("key", path);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
