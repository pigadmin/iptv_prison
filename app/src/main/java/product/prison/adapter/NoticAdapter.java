package product.prison.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import product.prison.R;
import product.prison.model.Nt;
import product.prison.view.ad.RsType;

public class NoticAdapter extends BaseAdapter {
    private Context context;
    private List<Nt> list;

    public NoticAdapter(Context context, List<Nt> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub

        return position;

    }

    private int current;

    public void change(int current) {
        // TODO Auto-generated method stub
        this.current = current;
        this.notifyDataSetChanged();

    }

    View view;
    TextView email_time, email_title, email_content;
    TextView email_status;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        view = LayoutInflater.from(context).inflate(R.layout.adapter_msg,
                null);

        try {
            email_status = view.findViewById(R.id.email_status);

            if (!list.get(position).isRead()) {
                email_status.setBackgroundResource(R.drawable.msg_ico_r);
            } else {
                email_status.setBackgroundResource(R.drawable.msg_ico_p);
            }

            email_time = view.findViewById(R.id.email_time);

            email_time.setText(date(list.get(position).getCtiem()));

            email_title = view.findViewById(R.id.email_title);
//            email_title.setText((position + 1) + "");
            String url = list.get(position).getContent();
            if (url.startsWith("h")) {
                String temp = url.substring(url.lastIndexOf("."));
                switch (RsType.type.get(temp.toLowerCase())) {
                    case 1:
                        email_title.setText("图片");
                        break;
                    case 2:
                    case 3:
                        email_title.setText("视频");
                        break;
                }
            } else {
                email_title.setText("文字");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return view;
    }

    private String date(long time) {
        // TODO Auto-generated method stub
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd");
            return dateFormat.format(new Date(time));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

}
