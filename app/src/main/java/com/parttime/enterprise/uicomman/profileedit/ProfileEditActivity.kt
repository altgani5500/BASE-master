package com.parttime.enterprise.uicomman.profileedit

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.darsh.multipleimageselect.helpers.Constants
import com.darsh.multipleimageselect.models.Image
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.ImagePickerUtil
import com.parttime.enterprise.helpers.PermissionsUtil
import com.parttime.enterprise.helpers.PermissionsUtil.askPermissions
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.profileedit.indusrtymodel.IndustryModel
import com.parttime.enterprise.uicomman.profileedit.indusrtymodel.UpdateProfileResponse
import com.parttime.enterprise.viewmodels.ProfileEditViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_edit.*
import java.io.File

class ProfileEditActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v == tvCamera) {
            pickImage()
        } else if (v == save_profile) {

            if (enter_priseName_profile.text.toString().length < 3) {
                showMessage(activity, enter_priseName_profile, getString(R.string.enter_name_please))
            } else {
                if (enter_priseName_profile_gen.text.toString().length > 2) {
                    if (isNetworkAvailable(activity)) {
                        showProgressBarNew()
                        viewModel.editProfileApi(
                            enter_priseName_profile.text.toString(),
                            getIndustryId().toString(),
                            enter_priseName_profile_gen.text.toString(),
                            file,
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                            isNetworkAvailable(activity),
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                        )
                    } else {
                        showMessage(activity, enter_priseName_profile, getString(R.string.network_error))
                    }
                } else {
                    showMessage(activity, enter_priseName_profile, getString(R.string.profile_info))
                }
            }

        } else if (v == profile_backedt) {
            onBackPressed()
        }
    }

    var fileList = ArrayList<File>()
    private var file: File? = null
    val JsonArrayIndustryType = ArrayList<com.parttime.enterprise.uicomman.profileedit.indusrtymodel.Message>()
    lateinit var viewModel: ProfileEditViewModel
    var industryTypeSpinnerAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            profile_backedt.rotation = 180F
        } else {
            profile_backedt.rotation = 0F
        }
        viewModel = ViewModelProviders.of(this@ProfileEditActivity).get(ProfileEditViewModel::class.java)
        if (isNetworkAvailable(activity)) {
            viewModel.getPartTimeList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        } else {
            showMessage(this@ProfileEditActivity, spn_industry, getString(R.string.network_error))
        }
        getIndustryTypeSet()
        tvCamera.setOnClickListener(this@ProfileEditActivity)
        save_profile.setOnClickListener(this@ProfileEditActivity)
        // setP prev value for view profile
        txt_profileId.text = getString(R.string.enterprise_id) + " " + intent.getStringExtra("ID")
        txt_createdjob.text = intent.getStringExtra("CREATED")
        enter_priseName_profile.setText(intent.getStringExtra("NAME"))
        //setIndustryId(intent.getStringExtra("CAT"))
        Picasso.get().load(intent.getStringExtra("IMG")).placeholder(R.drawable.others).into(imgUser_edtProfile)
        enter_priseName_profile_gen.setText(intent.getStringExtra("GEN"))

        profile_backedt.setOnClickListener {
            onBackPressed()
        }

    }


    /*Mehtod to implement view model
    */
    private fun setViewModel() {
        viewModel = ViewModelProviders.of(activity).get(ProfileEditViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showToast(getString(msg))
        })

        viewModel.profileUpdate.observe(this, Observer { response: UpdateProfileResponse ->
            hideProgressBarNew()
            if (response.success) {
                showMessage(this@ProfileEditActivity, spn_industry, response.message)
                onBackPressed()

            } else {
                showToast(response.errorMessage.toString())
            }
        })
    }


    /*Get IndustryType Type From Api*/
    fun getIndustryTypeSet() {

        viewModel.getPartTimeList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showMessage(this@ProfileEditActivity, spn_industry, getString(msg))
        })
        viewModel.industryModelResponse.observe(this, Observer { response: IndustryModel ->
            if (response.success) {
                if (response.code == 200) {
                    // Success Response
                    var list: MutableList<String> = mutableListOf<String>()
                    JsonArrayIndustryType.addAll(response.message)
                    val itr = response.message.iterator()
                    while (itr.hasNext()) {
                        list.add(itr.next().category)
                        // arrayListPartimeType.add(itr.next().parttimeTypeId)

                    }

                    industryTypeSpinnerAdapter =
                        ArrayAdapter<String>(this, R.layout.spinner_item_text, R.id.spinText, list)
                    //partTimeSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_industry.adapter = industryTypeSpinnerAdapter
                    val st: String = intent.getStringExtra("CAT")
                    for (i in 0 until list.size) {
                        if (list[i].contentEquals(st)) {
                            spn_industry.setSelection(i)
                        }
                    }
                    setViewModel()
                } else if (response.code == 204) {
                    showMessage(this@ProfileEditActivity, spn_industry, response.errorMessage.toString())
                } else if (response.code == 401) {
                    showMessage(this@ProfileEditActivity, spn_industry, response.errorMessage.toString())
                }
            } else {
                showMessage(this@ProfileEditActivity, spn_industry, response.errorMessage.toString())

            }
        })

    }


    /*
   Method to pick image
    */
    private fun pickImage() {
        askPermissions(
            this@ProfileEditActivity,
            PermissionsUtil.CAMERA,
            PermissionsUtil.STORAGE,
            object : PermissionsUtil.PermissionListener {
                override fun onPermissionResult(isGranted: Boolean) {
                    if (isGranted) {
                        ImagePickerUtil.selectImage(
                            this@ProfileEditActivity,
                            object : ImagePickerUtil.ImagePickerListener {
                                override fun onImagePicked(imageFile: File?, tag: String?) {
                                    file = imageFile
                                    val imgBitMap = BitmapFactory.decodeFile(imageFile?.absolutePath)
                                    imgUser_edtProfile.setImageBitmap(imgBitMap)
                                    // tvCamera.setImageBitmap(imgBitMap);
                                    //setImageBitmap(BitmapFactory().de(BitmapFactory.decodeFile(imageFile?.absolutePath)))
                                }

                            },
                            "user",
                            false
                        )
                    }
                }

            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Constants.INTENT_EXTRA_IMAGES)
            for (myImages in images) {
                fileList.add(File(myImages.path))
            }

        } else {
            ImagePickerUtil.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /*Get Part Time Id*/
    private fun getIndustryId(): Int {
        var parTimeType: Int = 0
        for (i in 0 until JsonArrayIndustryType.size) {
            val item = JsonArrayIndustryType[i]
            if (item.category == spn_industry.selectedItem) {
                parTimeType = item.categoryId
            }
            // Your code here
        }
        return parTimeType
    }


    override fun onBackPressed() {
        finish()
    }
}