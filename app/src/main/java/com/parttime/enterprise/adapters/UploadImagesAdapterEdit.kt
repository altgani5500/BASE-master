package com.parttime.enterprise.adapters

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import kotlinx.android.synthetic.main.adapter_for_images.view.*

class UploadImagesAdapterEdit(
    activity: Activity,
    list: MutableList<String>/*, list:MutableList<File>*/,
    onCluckListner: OnItemClick
) : RecyclerView.Adapter<UploadImagesAdapterEdit.ViewHolder>() {

    var context: Activity
    //var list:MutableList<File>
    var list: MutableList<String>
    var mOnItemClickListener: OnItemClick? = onCluckListner

    init {
        this.context = activity
        this.list = list
        // this.listImg=listImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_for_images, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View

        init {
            this.view = view
        }

        fun bindData(position: Int) {
            val displayMetrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            //view.imgCar.layoutParams.width=width/2
            //view.iv_image_pager.layoutParams.height=width/3

            if (position < list.size) {
                //Picasso.with(context).load(list.get(position)).into(view.iv_image_pager)
                Glide.with(context).load(list.get(position)).thumbnail(.5f)
                    .into(view.iv_image_pager)
                // view.iv_image_pager.setImageBitmap(BitmapFactory.decodeFile(list.get(position).absolutePath))
                view.iv_remove.visibility = View.VISIBLE
                view.iv_image_pager.scaleType = ImageView.ScaleType.FIT_XY
                view.iv_remove.setOnClickListener {
                    mOnItemClickListener?.onRemoveImages(list[position])
                    //list.removeAt(position)
                    //notifyDataSetChanged()
                }
            } else {
                view.iv_remove.visibility = View.GONE
                view.iv_image_pager.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.test_bg
                    )
                )
                view.iv_image_pager.scaleType = ImageView.ScaleType.CENTER_INSIDE
                view.iv_image_pager.setOnClickListener {
                    mOnItemClickListener?.onClickImages(position)
                }
            }

        }

    }


    interface OnItemClick {
        fun onClickImages(value: Int)
        fun onRemoveImages(img: String)
    }


}