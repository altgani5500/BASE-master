package com.cancan.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.zoomviewpager.TouchImageViewActivityFile
import com.squareup.picasso.Picasso
import java.io.File


class MyFragmentPagerAdapter(
    context: Context,
    private val imageModelArrayList: ArrayList<File>
) : PagerAdapter() {
    private val inflater: LayoutInflater
    lateinit var mContext: Context


    init {
        inflater = LayoutInflater.from(context)
        mContext = context
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val activity = mContext as Activity
        val imageLayout = inflater.inflate(R.layout.item_image_row, view, false)!!
        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView

        Picasso.get()
            .load(imageModelArrayList[position])
            .resize(900, 600)
            .onlyScaleDown()
            .placeholder(R.drawable.b_scrren)
            //.fit()
            // .centerCrop()
            .into(imageView)
        view.addView(imageLayout, 0)
        imageLayout.setOnClickListener {
            val intent = Intent(mContext, TouchImageViewActivityFile::class.java)
            intent.putExtra("IMGLIST", imageModelArrayList)
            activity.startActivity(intent)

        }
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}