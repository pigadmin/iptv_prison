package product.prison.view.satisfied;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.SatisfiedAdapter;
import product.prison.adapter.SatisfiedListAdapter;
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.Satisfied;
import product.prison.model.TGson;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.video.VideoActivity;

/**
 * Created by zhu on 2017/10/23.
 */

public class SatisfiedActivity extends BaseActivity implements SatisfiedListAdapter.OnItemClickListener, View.OnClickListener {
    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private GridView satisfied_grid;
    private Button satisfied_submit;

    private SatisfiedListAdapter listAdapter;
    private SatisfiedAdapter gridAdapter;
    private List<Satisfied> satisfied;
    private int id = 0;
    private int timeout = 60 * 1000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0:
                        if (satisfied_submit.isEnabled()) {
                            satisfied_submit.setEnabled(!satisfied_submit.isEnabled());
                        }
                        new CountDownTimer(timeout, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                satisfied_submit.setText(getString(R.string.Ok) + "(" + (millisUntilFinished / 1000) + ")");
                            }

                            @Override
                            public void onFinish() {
                                if (!satisfied_submit.isEnabled()) {
                                    satisfied_submit.setEnabled(!satisfied_submit.isEnabled());
                                }
                                satisfied_submit.setText(getString(R.string.Ok));
                            }
                        }.start();


                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void exam_question() {
        RequestParams params = new RequestParams(MyApp.apiurl + "exam_question");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<Satisfied>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<Satisfied>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    satisfied = json.getData();
                    listAdapter = new SatisfiedListAdapter(getApplicationContext(), satisfied);
                    left_list.setAdapter(listAdapter);
                    listAdapter.setOnItemClickListener(SatisfiedActivity.this);
                    update(0);
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



    private void update(int position) {
        try {
            id = satisfied.get(position).getId();
            gridAdapter = new SatisfiedAdapter(getApplicationContext(), satisfied.get(position).getDetails());
            satisfied_grid.setAdapter(gridAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.satisfied_submit:
                gridAdapter.post(id);
                handler.sendEmptyMessage(0);
                break;
            default:
                break;
        }

    }


    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);

        satisfied_grid = f(R.id.satisfied_grid);
        satisfied_submit = f(R.id.satisfied_submit);
        satisfied_submit.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        exam_question();
    }

    @Override
    public int getContentId() {
        return R.layout.activity_satisfied;
    }

    @Override
    public void onItemClick(View view, int position) {
        update(position);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
//            satisfied_grid.setSelection(satisfied_grid.getSelectedItemPosition() - 1);
//        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
//            satisfied_grid.setSelection(satisfied_grid.getSelectedItemPosition() + 1);
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}
