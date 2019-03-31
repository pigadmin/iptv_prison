package product.prison.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import product.prison.R;



public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> implements View.OnClickListener {
    String[] menus;
    Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView left_list_name;

        public ViewHolder(View v) {
            super(v);

            left_list_name = v.findViewById(R.id.left_list_name);
            v.setOnClickListener(SetAdapter.this);
        }
    }

    public SetAdapter(Context context, String[] menus) {
        this.context = context;
        this.menus = menus;
    }

    private OnItemClickListener mOnItemClickListener = null;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_text, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.left_list_name.setText(menus[position]);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return menus.length;
    }


}
