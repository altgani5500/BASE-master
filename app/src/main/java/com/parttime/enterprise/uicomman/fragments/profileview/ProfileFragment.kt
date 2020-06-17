package com.parttime.enterprise.uicomman.fragments.profileview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.koushikdutta.ion.Ion
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseFragment
import com.parttime.enterprise.uicomman.fragments.profileview.profilemodel.ProfileViewModelResponse
import com.parttime.enterprise.uicomman.pinchzoom.PinchZoomActivity
import com.parttime.enterprise.uicomman.profileedit.ProfileEditActivity
import com.parttime.enterprise.viewmodels.ProfileGetViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    lateinit var viewModel: ProfileGetViewModel
    lateinit var imgString: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity).get(ProfileGetViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        userRole()
        // call api for profile view
        if (Utilities.isNetworkAvailable(activity)) {
            activity.showProgressBarNew()
            viewModel.getProfileData(
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(activity)
            )
            // after api hit get Response and set into api
            getProfileData()
        } else {
            activity.showMessage(context, txtEditProfile, getString(R.string.network_error))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()


    }

    private fun userRole(){
        if(enterPriseAdminRole){
            txtEditProfile.visibility=View.VISIBLE

        }else if(appRecruiterRole){
            txtEditProfile.visibility = View.GONE
        }else if (appSchedulerRole || appSupervisorRole || appSupervisorEvaluatorRole) {
            txtEditProfile.visibility = View.GONE
        } else {
            txtEditProfile.visibility = View.VISIBLE
        }
    }

    private fun setOnClickListener() {
        userRole()
        txtEditProfile.setOnClickListener {
            // activity.launchActivity(ProfileEditActivity::class.java)
            val intent = Intent(activity, ProfileEditActivity::class.java)
            intent.putExtra("CREATED", txtProfileView_CreatedJob.text.toString())
            intent.putExtra("NAME", txtProfileView_Entname.text.toString())
            intent.putExtra("ID", txtProfileView_EntId1.text.toString())
            intent.putExtra("CAT", txtProfileView_EntCate.text.toString())
            intent.putExtra("GEN", txtProfileView_GEne.text.toString())
            intent.putExtra("IMG", imgString)
            activity.startActivity(intent)


        }
    }


    // Profile view get from api
    private fun getProfileData() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, txtEditProfile, getString(msg))
        })
        viewModel.profileGetResponse.observe(this, Observer { response: ProfileViewModelResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        txtProfileView_Title.text = response.message.enterpriseName.toString()
                        txtProfileView_CreatedJob.text = response.message.jobType
                        txtProfileView_EntId1.text = response.message.enterpriseId.toString()
                        txtProfileView_Entname.text = response.message.enterpriseName
                        txtProfileView_EntCate.text = response.message.industryType
                        txtProfileView_GEne.text = response.message.generalInfo
                        imgString = response.message.enterpriseLogo

                        Glide.with(context).load(response.message.enterpriseLogo).placeholder(R.drawable.b_scrren).into(imgUserPrev)
                       /* Picasso.get()
                            .load(response.message.enterpriseLogo)
                            .placeholder(R.drawable.b_scrren)*/
                    /*        .into(imgUserPrev)
                        Ion.with(context)
                            .load(response.message.enterpriseLogo)
                            .withBitmap()
                            .placeholder(R.drawable.b_scrren)
                            .error(R.drawable.b_scrren)
                            .intoImageView(imgUserPrev)*/
                        imgUserPrev.setOnClickListener {
                            val intent = Intent(activity, PinchZoomActivity::class.java)
                            intent.putExtra("IMGURL", response.message.enterpriseLogo)
                            startActivity(intent)
                        }

                    } else {
                        activity.showMessage(context, txtEditProfile, response.errorMessage.toString())
                    }

                } else {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, txtEditProfile, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, txtEditProfile, response.errorMessage.toString())

            }
        })

    }


}