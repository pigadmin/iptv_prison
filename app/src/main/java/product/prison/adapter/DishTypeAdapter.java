package product.prison.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import product.prison.R;
import product.prison.model.FoodCat;
import product.prison.model.VodTypeData;
import product.prison.utils.ImageUtils;

/**
 * Created by zhu on 2017/9/26.
 */

public class DishTypeAdapter extends RecyclerView.Adapter<DishTypeAdapter.ViewHolder> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private  List<FoodCat> list;
    private  Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        RelativeLayout menu_linearlayout;

        public ViewHolder(View v) {
            super(v);

            mImageView = v.findViewById(R.id.main_menu_icon);
            mTextView = v.findViewById(R.id.main_menu_text);
            menu_linearlayout = v.findViewById(R.id.menu_linearlayout);
            v.setOnClickListener(DishTypeAdapter.this);
        }
    }

    public DishTypeAdapter(Context context, List<FoodCat> list) {
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
    public DishTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position % 6 == 0) {
            holder.menu_linearlayout.setBackgroundResource(R.drawable.main_menu_line3);
            StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(242, 485);
            lp.setFullSpan(true);
            holder.menu_linearlayout.setLayoutParams(lp);
        } else if (position % 6 == 2) {
            holder.menu_linearlayout.setBackgroundResource(R.drawable.main_menu_line);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(485, 242);
            holder.menu_linearlayout.setLayoutParams(lp);
        } else {
            holder.menu_linearlayout.setBackgroundResource(R.drawable.main_menu_line2);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(242, 242);
            holder.menu_linearlayout.setLayoutParams(lp);
        }
        ImageUtils.display(holder.mImageView, list.get(position).getIcon());
        holder.mTextView.setText(list.get(position).getName());

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
