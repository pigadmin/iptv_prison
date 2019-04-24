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
import product.prison.model.LivePreView;
import product.prison.utils.Utils;


public class LivePreListAdapter extends BaseAdapter {
    private Context context;

    private List<LivePreView> list = new ArrayList<>();

    public LivePreListAdapter(Context context, List<LivePreView> list) {
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
                    R.layout.adapter_livelist, null);
            holder.livelist_no = convertView
                    .findViewById(R.id.livelist_no);
            holder.livelist_name = convertView
                    .findViewById(R.id.livelist_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.livelist_no.setText(Utils.formatMyTime("HH:mm", list.get(position).getStartTime()) + "-" + Utils.formatMyTime("HH:mm", list.get(position).getEndTime()));

        holder.livelist_name.setText(list.get(position).getProgramName());

        return convertView;

    }


    public class ViewHolder {
        private TextView livelist_no;
        private TextView livelist_name;

    }

}
