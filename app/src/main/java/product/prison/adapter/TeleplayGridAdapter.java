package product.prison.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import product.prison.R;
import product.prison.model.Details;

/**
 * Created by zhu on 2017/10/10.
 */

public class TeleplayGridAdapter extends BaseAdapter {
    private Context context;
    private List<Details> list = new ArrayList<>();

    public TeleplayGridAdapter(Context context, List<Details> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_teleplay, null);
            holder.teleplay_name = convertView
                    .findViewById(R.id.teleplay_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.teleplay_name.setText((position + 1) + "");


        return convertView;

    }


    public class ViewHolder {
        private TextView teleplay_name;

    }

}
