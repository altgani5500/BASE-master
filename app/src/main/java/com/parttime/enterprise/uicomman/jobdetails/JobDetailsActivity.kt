package com.parttime.enterprise.uicomman.jobdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.ApplicantFilterActivity
import com.parttime.enterprise.uicomman.fragments.applicant.JobApplicantFragment
import com.parttime.enterprise.uicomman.fragments.jobsDetails.JobDeatailsFragments
import com.parttime.enterprise.uicomman.homes.HomeActivity
import kotlinx.android.synthetic.main.activity_job_details.*


class JobDetailsActivity : BaseActivity(), View.OnClickListener,
    JobApplicantFragment.OnNotifyRelodPage {

    override fun reloadPage(position: Int) {
        setupViewPager(viewPager!!)
        viewPager!!.currentItem = position
        tabs.setupWithViewPager(viewPager)

    }


    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is JobApplicantFragment) {
            fragment.setOnHeadlineSelectedListener(this)
        }
    }


    var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            id_back_me_details.rotation = 180F
        } else {
            id_back_me_details.rotation = 0F
        }

        viewPager = findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager!!)
        tabs.setupWithViewPager(viewPager)

        id_back_me_details.setOnClickListener(this@JobDetailsActivity)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val bundle = Bundle()
        bundle.putString("JOBID", intent.getStringExtra("JOBID"))
        val fragmentsDetals = JobDeatailsFragments()
        val fragmentApplicant = JobApplicantFragment()
        fragmentsDetals.arguments = bundle
        fragmentApplicant.arguments = bundle
        adapter.addFragment(fragmentsDetals, getString(R.string.details_s1))
        adapter.addFragment(fragmentApplicant, getString(R.string.applicatnts_ss))
        viewPager.adapter = adapter

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                positionSelect(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        /*  tabs.setOnTabSelectedListener(
              object : TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                  override fun onTabSelected(tab: TabLayout.Tab) {
                      super.onTabSelected(tab)
                      positionSelect(tab.position)
                      *//*  val tabIconColor = ContextCompat.getColor(context, R.color.tabSelectedIconColor)
                      tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)*//*
                }

            }
        )*/

    }

    private fun positionSelect(position: Int) {
        if (position == 1) {
            detail_filter_icon.visibility = View.VISIBLE
            detail_filter_icon.setOnClickListener {
                // launchActivity(ApplicantFilterActivity::class.java)
                //appPrefence.setString(AppPrefence.SharedPreferncesKeys.FILTER.toString(), " ")
                if (geAccountType() == 2 || geAccountType() == 3) {
                    val intent =
                        Intent(this@JobDetailsActivity, ApplicantFilterActivity::class.java)
                    startActivityForResult(intent, 100)
                } else {
                    showMessage(
                        this@JobDetailsActivity,
                        detail_filter_icon,
                        getString(R.string.err_account_type)
                    )
                }
            }
        } else {
            detail_filter_icon.visibility = View.GONE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getIntExtra("FLAG", 0) > 1) {
                //val hashMap = data.getSerializableExtra("KEY_API") as HashMap<String, String>
                setupViewPager(viewPager!!)
                viewPager!!.currentItem = 1
                tabs.setupWithViewPager(viewPager)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }


        /*   fun replace(position: Int, fragment: Fragment) {
               // Get currently active fragment.
               val old_fragment = mFragments[position] ?: return

               // Replace the fragment using transaction and in underlaying array list.
               // NOTE .addToBackStack(null) doesn't work
               this.startUpdate(mContainer)
               mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                   .remove(old_fragment).add(mContainer.getId(), fragment)
                   .commit()
               mFragments[position] = fragment
               this.notifyDataSetChanged()
               this.finishUpdate(mContainer)
           }*/


        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    override fun onClick(v: View?) {
        if (v == id_back_me_details)
            onBackPressed()

    }

    override fun onBackPressed() {
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.FILTER.toString(), " ")
        if (intent.hasExtra("FLAGS_BACK")) {
            if (intent.getIntExtra("FLAGS_BACK", 0) == 1) {
                launchActivityMain(HomeActivity::class.java)
                finish()
            } else {
                finish()
            }
        } else
            finish()
    }

}