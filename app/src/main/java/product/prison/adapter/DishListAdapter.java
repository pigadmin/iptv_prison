package product.prison.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import product.prison.R;
import product.prison.model.Details;
import product.prison.model.Food;


public class DishListAdapter extends RecyclerView.Adapter<DishListAdapter.ViewHolder> implements View.OnClickListener {
    private  List<Food> list;
    private  Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView left_list_icon;
        TextView left_list_name;

        public ViewHolder(View v) {
            super(v);

            left_list_name = v.findViewById(R.id.left_list_name);
            v.setOnClickListener(DishListAdapter.this);
        }
    }

    public DishListAdapter(Context context, List<Food> list) {
        this.context = context;
        this.list = list;
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
        holder.left_list_name.setText(list.get(position).getName());

        if (chk == position) {
            holder.itemView.requestFocus();
            holder.itemView.setBackgroundResource(R.drawable.left_list_f);
        }
        holder.itemView.setTag(position);
    }

    private int chk = -1;

    public void update(int chk) {
        this.chk = chk;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}