package com.parttime.enterprise.uicomman.homes

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.fragments.SettingFragments
import com.parttime.enterprise.uicomman.fragments.enroll.EnrollWorkerFragment
import com.parttime.enterprise.uicomman.fragments.jobslist.JobsFragment
import com.parttime.enterprise.uicomman.fragments.messages.MessageListFragment
import com.parttime.enterprise.uicomman.fragments.profileview.ProfileFragment
import com.parttime.enterprise.uicomman.homes.homesmodels.HomesModels
import com.parttime.enterprise.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), JobsFragment.OnNotifyRelodPage {
    lateinit var viewModel: HomeViewModel
    // val navView: BottomNavigationView?=null


    override fun reloadPage(position: Int) {
        replaceFragment(R.id.homeContainer, JobsFragment())
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is JobsFragment) {
            fragment.setOnHeadlineSelectedListener(this)
        }
    }

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (enterPriseAdminRole) {
                        replaceFragment(R.id.homeContainer, JobsFragment())
                        return@OnNavigationItemSelectedListener true
                    } else if (appRecruiterRole) {
                        replaceFragment(R.id.homeContainer, JobsFragment())
                        return@OnNavigationItemSelectedListener true
                    } else if (appSupervisorRole || appSchedulerRole || appSupervisorEvaluatorRole) {
                        showMessage(this@HomeActivity, nav_view, getString(R.string.role_msg))
                        return@OnNavigationItemSelectedListener false
                    } else {
                        replaceFragment(R.id.homeContainer, JobsFragment())
                        return@OnNavigationItemSelectedListener true
                    }


                }
                R.id.navigation_message -> {
                    if (enterPriseAdminRole) {
                        replaceFragment(R.id.homeContainer, MessageListFragment())
                        return@OnNavigationItemSelectedListener true
                    } else if (appRecruiterRole) {
                        replaceFragment(R.id.homeContainer, MessageListFragment())
                        return@OnNavigationItemSelectedListener true
                    } else if (appSupervisorRole || appSchedulerRole || appSupervisorEvaluatorRole) {
                        showMessage(this@HomeActivity, nav_view, getString(R.string.role_msg))
                        return@OnNavigationItemSelectedListener false
                    } else {
                        replaceFragment(R.id.homeContainer, MessageListFragment())
                        return@OnNavigationItemSelectedListener true
                    }

                }
                R.id.navigation_worker -> {
                    replaceFragment(R.id.homeContainer, EnrollWorkerFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    replaceFragment(
                        R.id.homeContainer,
                        ProfileFragment()
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_setting -> {
                    //  homeContainer.setBackgroundColor(resources.getColor(R.color.white))
                    replaceFragment(R.id.homeContainer, SettingFragments())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // val navView: BottomNavigationView = findViewById(R.id.nav_view)
        //clear filter prefence
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.FILTER.toString(), " ")
        // load first jobs fragment
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        replaceFragment(R.id.homeContainer, JobsFragment())
        nav_view.itemIconTintList = null
        appApiSettingCall()
        userRole()
    }

    override fun onResume() {
        super.onResume()
        appApiSettingCall()
        // check role for the user
        //userRole()


    }

    private fun userRole() {
        if (enterPriseAdminRole) {
            replaceFragment(R.id.homeContainer, JobsFragment())
            nav_view.selectedItemId = R.id.navigation_home
        } else if (appRecruiterRole) {
            replaceFragment(R.id.homeContainer, JobsFragment())
            nav_view.selectedItemId = R.id.navigation_worker
        } else if (!appSchedulerRole || !appSupervisorRole || !appSupervisorEvaluatorRole) {
            replaceFragment(R.id.homeContainer, EnrollWorkerFragment())
            nav_view.selectedItemId = R.id.navigation_worker
        } else {
            replaceFragment(R.id.homeContainer, JobsFragment())
            nav_view.selectedItemId = R.id.navigation_home
        }

    }

    private fun appApiSettingCall() {
        if (Utilities.isNetworkAvailable(this@HomeActivity)) {
            viewModel = ViewModelProviders.of(this@HomeActivity).get(HomeViewModel::class.java)
            viewModel.deviceSetting(
                Utilities.isNetworkAvailable(this@HomeActivity),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
            )
            appSetting()
        } else {
            showToast(getString(R.string.network_error))

        }
    }

    private fun appSetting() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showToast(getString(msg))
        })

        // get Response through observer
        viewModel.settingResponse.observe(this, Observer { response: HomesModels ->
            if (response.success) {
                //   Log.e("HOme Setting ", response.toString())
                if (response.code == 200) {
                    if (response.success) {
                        //  showMessage(this@HomeActivity, navView, response.message.toString())
                        // showToast(response.message.toString())
                        appPrefence.setString(
                            AppPrefence.SharedPreferncesKeys.hourlyRateSetting.toString(),
                            response.hourlyRate
                        )
                        appPrefence.setString(
                            AppPrefence.SharedPreferncesKeys.enrollWorkerSetting.toString(),
                            response.enrolledWorker
                        )
                        if (response.isNotification.toString().contentEquals("1")) {
                            appPrefence.setString(
                                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                                response.isNotification.toString()
                            )
                        } else {
                            appPrefence.setString(
                                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                                "0"
                            )
                        }
                        appPrefence.setInt(AppPrefence.SharedPreferncesKeys.ACCOUNT_TYPE.toString(),response.accountType)
                        // app Role  Set into prefence for future
                        /* Schedular  1
                            Supervisor 2
                            Super Evaluator 3
                            Recruiter 4
                            else Enterprise Admin 5
                        * */
                        for (i in 0 until response.enterpriseRole.size) {
                            when (response.enterpriseRole[i]) {

                                "1" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        1
                                    )

                                    // remove
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        0
                                    )

                                }

                                "2" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        2
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        2
                                    )
                                    //  remove
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        0
                                    )


                                }
                                "3" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        3
                                    )
                                    // remove

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        0
                                    )
                                }

                                "4" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        4
                                    )
                                    // remove
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        0
                                    )
                                }

                            }


                        }


                    } else {
                        // hideProgressBarNew()
                        showToast(response.errorMessage.toString())
                    }
                } else {
                    showToast(response.errorMessage.toString())
                }
            } else {
                showToast(response.errorMessage.toString())
            }
        })
    }

}
