package product.prison.view;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.LiveAdapter;
import product.prison.model.LiveData;
import product.prison.utils.Utils;

public class LiveApkActivity extends BaseActivity implements LiveAdapter.OnItemClickListener {

    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private List<LiveData> list = new ArrayList<>();
    private LiveAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        mainrecyle = f(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);


        mainrecyle.setLayoutManager(layoutManager);
    }

    @Override
    public void loadData() {
        try {
            list = (List<LiveData>) getIntent().getExtras().get("key");
            adapter = new LiveAdapter(this, list);
            mainrecyle.setAdapter(adapter);
            adapter.setOnItemClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentId() {
        return R.layout.activity_main;
    }


    @Override
    public void onItemClick(View view, int position) {
        if (Utils.isInstall(this, list.get(position).getPackage_name())) {
            Utils.startApk(this, list.get(position).getPackage_name(), "");
        } else {
            if (!list.get(position).getPath().startsWith("h")) {
                Toast.makeText(getApplicationContext(), list.get(position).getName() + " " + getString(R.string.urlerror), Toast.LENGTH_SHORT).show();
                return;
            }
            new Utils().Download(this, list.get(position).getPath(), true);
        }
    }
}
