package product.prison.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import product.prison.R;
import product.prison.model.SongsData;


public class SongListAdapter extends BaseAdapter {
    private Context activity;

    private List<SongsData> list = new ArrayList<>();

    public SongListAdapter(Context activity, List<SongsData> list) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
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

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.adapter_musiclist, null);

            holder.livelist_name = convertView
                    .findViewById(R.id.livelist_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.livelist_name.setText(list.get(position).getName());
//        holder.livelist_name.setTextColor(activity.getResources().getColor(R.drawable.text2));

        holder.livelist_name.setTextColor(activity.getResources().getColor(R.color.white));
        if (chk == position) {
            holder.livelist_name.setTextColor(activity.getResources().getColor(R.color.musicbar));
        }
        return convertView;

    }

    private String liveno(int position) {
        if ((position + 1) < 10)
            return "0" + (position + 1);
        return (position + 1) + "";
    }

    private int chk = -1;

    public void update(int chk) {
        this.chk = chk;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        private TextView livelist_no;
        private TextView livelist_name;

    }

}
