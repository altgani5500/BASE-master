package com.parttime.enterprise.adapters

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
import com.parttime.enterprise.uicomman.fragments.jobsDetails.models.JobImage
import com.parttime.enterprise.uicomman.zoomviewpager.TouchImageViewActivity
import com.squareup.picasso.Picasso

/*this adapter class is for view page slider */

class MyFragmentPagerAdapterStringType(
    context: Activity,
    private val imageModelArrayList: ArrayList<JobImage>
) : PagerAdapter() {
    private val inflater: LayoutInflater
    var mContext: Context


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
            .load(imageModelArrayList[position].jobImage.toString())
            .resize(900, 600)
            .onlyScaleDown()
            .placeholder(R.drawable.b_scrren)
            // .fit()
            .centerCrop()
            .into(imageView)
        view.addView(imageLayout, 0)

        imageView.setOnClickListener {
            var imgList = ArrayList<String>()
            for (i in 0 until imageModelArrayList.size) {
                imgList.add(imageModelArrayList[i].jobImage)
            }
            if (imgList.isNotEmpty()) {

                val intent = Intent(mContext, TouchImageViewActivity::class.java)
                intent.putStringArrayListExtra("IMGLIST", imgList)
                activity.startActivity(intent)
            }
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