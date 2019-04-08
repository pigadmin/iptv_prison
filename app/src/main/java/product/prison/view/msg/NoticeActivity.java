package product.prison.view.msg;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.NoticAdapter;
import product.prison.app.MyApp;
import product.prison.model.Nt;
import product.prison.model.TGson;
import product.prison.model.TMenu;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class NoticeActivity extends BaseActivity implements OnItemClickListener {
    private List<Nt> list = new ArrayList<>();

    private ListView notice_list;
    private TextView notice_content;
    private ImageButton bottom_msg;
    private TextView bottom_msg_tips;
    private NoticAdapter adapter;

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        try {

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        notice_list = f(R.id.notice_list);
        notice_list.setOnItemClickListener(this);

        notice_content = f(R.id.notice_content);
        notice_list.setOnItemClickListener(this);
    }

    @Override
    public void loadData() {
        getNotice();
    }

    private void getNotice() {
        try {
            list = MyApp.db.findAll(Nt.class);
            Logs.e(list.size() + "@@@" + list.get(0).getContent());

            if (list.isEmpty())
                return;
            adapter = new NoticAdapter(this, list);
            notice_list.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getContentId() {
        return R.layout.activity_notice;
    }
}
