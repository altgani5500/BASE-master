package com.parttime.enterprise.uicomman.subuser

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.darsh.multipleimageselect.helpers.Constants
import com.darsh.multipleimageselect.models.Image
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.ImagePickerUtil
import com.parttime.enterprise.helpers.PermissionsUtil
import com.parttime.enterprise.helpers.PermissionsUtil.askPermissions
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.subuser.createuser.CreateUserResponse
import com.parttime.enterprise.validations.CreateSubUserValidation
import com.parttime.enterprise.viewmodels.CreateSubUserViewModel
import kotlinx.android.synthetic.main.create_sub_user_activity.*
import java.io.File

class CreateSubUserActivity : BaseActivity(), View.OnClickListener {

    //  var fileList = ArrayList<File>()
    private var file: File? = null
    //lateinit var viewModel: CreateSubUserViewModel
    private var FLAG=" "

    // onclick functionality
    override fun onClick(p0: View?) {
        when (p0) {
            create_sub_backedt -> {
                onBackPressed()
            }
            imgUser_edtProfile_createUser -> {
                pickImage()
            }
            tvCamera_sub -> {
                pickImage()
            }
            save_createUser -> {
                var selectedRoles :Int=0
                if (inflatorCheckboxOne.isChecked) {
                    selectedRoles=1
                } else if (inflatorCheckboxTwo.isChecked) {
                    selectedRoles=2
                } else  if (inflatorCheckboxThree.isChecked) {
                    selectedRoles=3
                } else  if (inflatorCheckboxFour.isChecked) {
                    selectedRoles=4
                }
               /* var value: String
                var finalValue = StringBuilder()
                for (i in 0 until selectedRoles.size) {
                    finalValue.append(selectedRoles[i]).append(",")

                }
                // its remove lst index comma
                value = finalValue.toString()
                value = value.substring(0, value.length - 1)*/
                var validResponse = CreateSubUserValidation().isInputValidAdduSer(
                    this@CreateSubUserActivity,
                    enter_priseName_profile.text.toString(),
                    create_userName.text.toString(),
                    create_password.text.toString(),
                    selectedRoles.toString(), Utilities.isNetworkAvailable(this@CreateSubUserActivity)
                )
                if (validResponse.status == 0) {
                    showMessage(this@CreateSubUserActivity, enter_priseName_profile, validResponse.errMessage)
                } else {
                    //api hit
                    showProgressBarNew()
                    var viewModel = ViewModelProviders.of(this).get(CreateSubUserViewModel::class.java)
                    viewModel.createSubUser(
                        enter_priseName_profile.text.toString(),
                        create_userName.text.toString(),
                        create_password.text.toString(),
                        selectedRoles.toString(),
                        file,
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                        Utilities.isNetworkAvailable(this@CreateSubUserActivity),
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                    )
                    addUserResponse()

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_sub_user_activity)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            create_sub_backedt.rotation = 180F
        } else {
            create_sub_backedt.rotation = 0F
        }

        viewClickInitilise()
    }

    private fun viewClickInitilise() {
        create_sub_backedt.setOnClickListener(this@CreateSubUserActivity)
        imgUser_edtProfile_createUser.setOnClickListener(this@CreateSubUserActivity)
        tvCamera_sub.setOnClickListener(this@CreateSubUserActivity)
        save_createUser.setOnClickListener(this@CreateSubUserActivity)
    }


    private fun addUserResponse() {
        hideProgressBarNew()
        var viewModel = ViewModelProviders.of(this@CreateSubUserActivity).get(CreateSubUserViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@CreateSubUserActivity, create_password, getString(msg))
        })

        viewModel.createUserResponseViewModels.observe(this@CreateSubUserActivity, Observer { response: CreateUserResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    //showMessage(this@CreateSubUserActivity, create_password, response.message)
                    showToast(response.message)
                    onBackPressed()
                } else {
                   // if (viewModel != null && viewModel.createUserResponseViewModels.hasObservers()) {
                    //viewModel.createUserResponseViewModels.removeObservers(this);
                   // }
                   if(!FLAG.contentEquals(response.errorMessage.toString().trim())) {
                        FLAG=response.errorMessage.toString().trim()
                        showToast(response.errorMessage.toString())
                    }
                }
            } else {
              //  if (viewModel != null && viewModel.createUserResponseViewModels.hasObservers()) {
                 //   viewModel.createUserResponseViewModels.removeObservers(this);
               // }
               if(!FLAG.contentEquals(response.errorMessage.toString().trim())) {
                   FLAG=response.errorMessage.toString().trim()
                    showToast(response.errorMessage.toString())
                }
            }
        })

    }

    /*
Method to pick image
*/
    private fun pickImage() {
        askPermissions(
            this@CreateSubUserActivity,
            PermissionsUtil.CAMERA,
            PermissionsUtil.STORAGE,
            object : PermissionsUtil.PermissionListener {
                override fun onPermissionResult(isGranted: Boolean) {
                    if (isGranted) {
                        ImagePickerUtil.selectImage(
                            this@CreateSubUserActivity,
                            object : ImagePickerUtil.ImagePickerListener {
                                override fun onImagePicked(imageFile: File?, tag: String?) {
                                    file = imageFile
                                    val imgBitMap = BitmapFactory.decodeFile(imageFile?.absolutePath)
                                    imgUser_edtProfile_createUser.setImageBitmap(imgBitMap)
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
                file = File(myImages.path)
            }
        } else {
            ImagePickerUtil.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        finish()
    }
}