package com.parttime.enterprise.uicomman.pinchzoom;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parttime.enterprise.R;
import com.parttime.enterprise.uicomman.BaseActivity;

public class PinchZoomActivity extends BaseActivity {
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.axtivity_pinchzoom);

        // initialize the view and the gesture detector
        mImageView = findViewById(R.id.pinch_image_view);
        /* new UnsafeOkHttpClient().getPicasoObject(this).load(getIntent().getStringExtra("IMGURL")).into(mImageView);*/
        Glide.with(this).load(getIntent().getStringExtra("IMGURL")).placeholder(R.drawable.b_scrren).into(mImageView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        findViewById(R.id.imageView4).setOnClickListener(v -> onBackPressed());

    }


    // this redirects all touch events in the activity to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        // when a scale gesture is detected, use it to resize the image
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}