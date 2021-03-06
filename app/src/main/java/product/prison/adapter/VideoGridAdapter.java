package product.prison.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import product.prison.R;
import product.prison.model.VodData;
import product.prison.utils.ImageUtils;

public class VideoGridAdapter extends BaseAdapter {
    private Activity activity;
    private List<VodData> list = new ArrayList<>();

    public VideoGridAdapter(Activity activity, List<VodData> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.adapter_vod, null);
            holder.vod_bg = convertView
                    .findViewById(R.id.vod_bg);
            holder.vod_name = convertView
                    .findViewById(R.id.vod_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.vod_name.setText(list.get(position).getName());
        ImageUtils.display(holder.vod_bg, list.get(position).getIcon());
        return convertView;

    }

    public class ViewHolder {
        private ImageView vod_bg;
        private TextView vod_name;

    }

}
