package com.parttime.enterprise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parttime.enterprise.R;
import com.parttime.enterprise.uicomman.BaseActivity;

import java.util.List;

/**
 * Created by vikash on 5/7/17.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyHolder> {


    private final BaseActivity activity;
    private final List<String> mResources;
    private final LayoutInflater mLayoutInflater;


    public ImagesAdapter(BaseActivity activity, List<String> mResources) {
        this.activity = activity;
        this.mResources = mResources;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_for_images, parent, false));

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Glide.with(activity).load(mResources.get(position)).thumbnail(.5f).into(holder.mImageView);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

    }

    private void removeItem(int position) {
        // mResources.remove(position);
        //if (mResources.size()==0){
        //fragment.hiderecyclerview(position);
        //}
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mResources.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView, iv_delete;

        public MyHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_image_pager);
            iv_delete = itemView.findViewById(R.id.iv_remove);
        }
    }
}
