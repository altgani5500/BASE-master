package com.parttime.enterprise.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.UserDocument
import com.parttime.enterprise.uicomman.applicantprofile.webview.ActivityWebView
import com.parttime.enterprise.uicomman.pinchzoom.PinchZoomActivity
import kotlinx.android.synthetic.main.url_view_inflator.view.*

class DocumentProfileAdapter(context: BaseActivity, list: List<UserDocument>) :
    RecyclerView.Adapter<DocumentProfileAdapter.ViewHolder>() {


    var context: BaseActivity


    var list: List<UserDocument>
    var finalList = ArrayList<UserDocument>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.url_view_inflator, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size

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
            if (list[position].documentName.isNullOrBlank()) {
                view.webText.text = "--"
            } else {
                view.webText.text = list[position].documentName
            }

            //Log.e("FILE_LINK",list[position].docUrl)
            // view click functionality
            view.webText.setOnClickListener {
                if (list[position].docUrl.contains(".pdf")
                    || list[position].docUrl.contains(".PDF")
                ) {
                    val intent = Intent(context, ActivityWebView::class.java)
                    intent.putExtra("LINK", list[position].docUrl)
                    context.startActivity(intent)
                } else if (list[position].docUrl.contains(".png")
                    || list[position].docUrl.contains(".JPG") ||
                    list[position].docUrl.contains(".jpg")
                    || list[position].docUrl.contains(".jpeg")
                    || list[position].docUrl.contains(".JPEG")
                ) {
                    // pass to image
                    val intent = Intent(context, PinchZoomActivity::class.java)
                    intent.putExtra("IMGURL", list[position].docUrl)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.file_format),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: ArrayList<UserDocument>)

    }


}