package com.parttime.enterprise.uicomman.zoomviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.parttime.enterprise.R;
import com.parttime.enterprise.uicomman.BaseActivity;

import java.io.File;
import java.util.ArrayList;

public class TouchImageViewActivityFile extends BaseActivity {

    ArrayList<File> imgList = new ArrayList<File>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_zoom_activity);
        imgList = (ArrayList<File>) getIntent().getSerializableExtra("IMGLIST");
        ViewPager mViewPager = findViewById(R.id.pager);
        // setContentView(mViewPager);
        mViewPager.setAdapter(new TouchImageAdapter());

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class TouchImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TouchImageView imgDisplay;
            ImageView btnClose;

            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.view_pager_img_zoom, container,
                    false);

            imgDisplay = viewLayout.findViewById(R.id.pinch_image_view_touch);
            btnClose = viewLayout.findViewById(R.id.imageView_pinch_image_view_touch);

            if (imgList.get(position).exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgList.get(position).getAbsolutePath());
                imgDisplay.setImageBitmap(myBitmap);
            }

            // close button click event
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            container.addView(viewLayout);

            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}