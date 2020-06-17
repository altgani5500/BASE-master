package com.parttime.enterprise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.parttime.enterprise.R;
import com.parttime.enterprise.uicomman.BaseActivity;

import java.util.List;

/**
 * Created by vikash on 5/7/17.
 */

public class CityRowListItemAdapter extends RecyclerView.Adapter<CityRowListItemAdapter.MyHolder> {


    private final BaseActivity activity;
    private final List<String> mResources;
    private final LayoutInflater mLayoutInflater;
    private CityRowItemClick rowItemClick;


    public CityRowListItemAdapter(BaseActivity activity, List<String> mResources, CityRowItemClick clickListner) {
        this.activity = activity;
        this.mResources = mResources;
        this.rowItemClick = clickListner;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selectable_item_inflator_with_exit, parent, false));

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.title.setText(mResources.get(position));
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

    }

    private void removeItem(int position) {
        mResources.remove(position);
        if (mResources.size() == 0) {
            rowItemClick.cityRowItemClick(position);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mResources.size();
    }


    public interface CityRowItemClick {
        void cityRowItemClick(int pos);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView iv_delete;
        private TextView title;

        public MyHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtItem);
            iv_delete = itemView.findViewById(R.id.imgItemCancel);
        }
    }
}
