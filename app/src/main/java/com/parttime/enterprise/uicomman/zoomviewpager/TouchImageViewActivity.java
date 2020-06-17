package com.parttime.enterprise.uicomman.zoomviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.parttime.enterprise.R;
import com.parttime.enterprise.uicomman.BaseActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class TouchImageViewActivity extends BaseActivity {

    ArrayList<String> imgList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_zoom_activity);
        imgList = getIntent().getExtras().getStringArrayList("IMGLIST");
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
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
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
            if (imgList.get(position).contains("http")) {
                Picasso.get()
                        .load(imgList.get(position))
                        .placeholder(R.drawable.b_scrren)
                        .into(imgDisplay);
            } else {
                if (new File(imgList.get(position)).exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(new File(imgList.get(position)).getAbsolutePath());
                    imgDisplay.setImageBitmap(myBitmap);
                }
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


    }
}