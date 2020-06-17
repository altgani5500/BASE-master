package com.parttime.enterprise.adapters


/*
class FragmentPagerAdapter(context: Context, private val imageModelArrayList: ArrayList<model_class>, promotionUrl: String) : PagerAdapter() {
    private val inflater: LayoutInflater
    lateinit var mContext: Context
    lateinit var mpromotionUrl :String


    init {
        inflater = LayoutInflater.from(context)
        mContext = context
        mpromotionUrl=promotionUrl
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.item_image_row, view, false)!!

        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView
        Utilities.setImagePicasso(mContext, imageView, mpromotionUrl + "" + imageModelArrayList[position].promotionImage)
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
*/

//}