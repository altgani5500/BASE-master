package com.parttime.enterprise.uicomman.subuser

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.darsh.multipleimageselect.helpers.Constants
import com.darsh.multipleimageselect.models.Image
import com.google.gson.Gson
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.ImagePickerUtil
import com.parttime.enterprise.helpers.PermissionsUtil
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.subuser.adduserlist.Message
import com.parttime.enterprise.uicomman.subuser.createuser.CreateUserResponse
import com.parttime.enterprise.validations.EditSubUserValidation
import com.parttime.enterprise.viewmodels.EditSubUserViewModel
import kotlinx.android.synthetic.main.create_sub_user_activity.*
import java.io.File


class EditSubUserActivity : BaseActivity(), View.OnClickListener {

    private var file: File? = null
    private var editProfilePrev: Message? = null
    private val gson = Gson()
    lateinit var viewModel: EditSubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_sub_user_activity)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            create_sub_backedt.rotation = 180F
        } else {
            create_sub_backedt.rotation = 0F
        }
        inflatorCheckboxTwo.isChecked = false
        sub_user_Title_edit.visibility=View.VISIBLE
        sub_user_Title.setText(resources.getString(R.string.add_sub))
        inflatorCheckboxTwo.isChecked=false
        // gson data from conver and set in to model from Intent
        editProfilePrev = gson.fromJson<Message>(intent.getStringExtra("DATA"), Message::class.java)

        setViewPrevData()
        viewClickInitilise()

    }

    private fun viewsClickable(flag:Int){
        if(flag==1){
            imgUser_edtProfile_createUser.isEnabled=true
            tvCamera_sub.isEnabled=true
            enter_priseName_profile.isEnabled=true
            create_userName.isEnabled=true
            create_password.isEnabled=true
            inflatorCheckboxOne.isEnabled=true
            inflatorCheckboxTwo.isEnabled=true
            inflatorCheckboxThree.isEnabled=true
            inflatorCheckboxFour.isEnabled=true
            save_createUser.visibility=View.VISIBLE
            save_createUser.isEnabled=true
            sub_user_Title.setText(resources.getString(R.string.edit_sub_user))
        }else{
            imgUser_edtProfile_createUser.isEnabled=false
            tvCamera_sub.isEnabled=false
            enter_priseName_profile.isEnabled=false
            create_userName.isEnabled=false
            create_password.isEnabled=false
            inflatorCheckboxOne.isEnabled=false
            inflatorCheckboxTwo.isEnabled=false
            inflatorCheckboxThree.isEnabled=false
            inflatorCheckboxFour.isEnabled=false
            save_createUser.visibility=View.GONE
            save_createUser.isEnabled=false
            sub_user_Title.setText(resources.getString(R.string.add_sub))
        }
    }

    private fun setViewPrevData() {
        add_userName.text = editProfilePrev?.name
        enter_priseName_profile.setText(editProfilePrev?.name)
        create_userName.setText(editProfilePrev?.email)
        Glide.with(this@EditSubUserActivity).load(editProfilePrev?.subUserLogo)/*.placeholder(R.drawable.b_scrren)*/
            .into(imgUser_edtProfile_createUser)
        if (!editProfilePrev?.roleId?.trim().isNullOrEmpty()) {
            var result: List<String> = editProfilePrev?.roleId?.trim()?.split(",")!!.map { it.trim() }
            result.forEach {
                when (it) {
                    "1" -> {
                        inflatorCheckboxOne.isChecked = true
                    }
                    "2" -> {
                        inflatorCheckboxTwo.isChecked = true
                    }
                    "3" -> {
                        inflatorCheckboxThree.isChecked = true
                    }
                    "4" -> {
                        inflatorCheckboxFour.isChecked = true
                    }
                }
                // println(it)
            }
        }

        viewsClickable(0)
    }


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
            sub_user_Title_edit->{
                viewsClickable(1)
            }
            save_createUser -> {
                var selectedRoles :Int =0
                if (inflatorCheckboxOne.isChecked) {
                    selectedRoles=1
                } else if (inflatorCheckboxTwo.isChecked) {
                    selectedRoles=2
                } else if (inflatorCheckboxThree.isChecked) {
                    selectedRoles=3
                } else if (inflatorCheckboxFour.isChecked) {
                    selectedRoles=4
                }
               /* var value: String
                var finalValue = StringBuilder()
                for (i in 0 until selectedRoles.size) {
                    finalValue.append(selectedRoles[i]).append(",")

                }*/
                // its remove lst index comma
              //  value = finalValue.toString()
              //  value = value.substring(0, value.length - 1)
                var validResponse = EditSubUserValidation().isInputValidAdduSer(
                    this@EditSubUserActivity,
                    enter_priseName_profile.text.toString(),
                    create_userName.text.toString(),
                    create_password.text.toString(),
                    selectedRoles.toString(), Utilities.isNetworkAvailable(this@EditSubUserActivity)
                )
                if (validResponse.status == 0) {
                    showMessage(this@EditSubUserActivity, enter_priseName_profile, validResponse.errMessage)
                } else {
                    //api hit
                    showProgressBarNew()
                    viewModel.editSubUser(
                        editProfilePrev?.subUserId.toString(),
                        enter_priseName_profile.text.toString(),
                        create_userName.text.toString(),
                        create_password.text.toString(),
                        selectedRoles.toString(),
                        file,
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                        Utilities.isNetworkAvailable(this@EditSubUserActivity),
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                    )
                    addUserResponse()
                }
            }
        }
    }

    private fun viewClickInitilise() {
        viewModel = ViewModelProviders.of(activity).get(EditSubUserViewModel::class.java)
        create_sub_backedt.setOnClickListener(this@EditSubUserActivity)
        imgUser_edtProfile_createUser.setOnClickListener(this@EditSubUserActivity)
        tvCamera_sub.setOnClickListener(this@EditSubUserActivity)
        save_createUser.setOnClickListener(this@EditSubUserActivity)
        sub_user_Title_edit.setOnClickListener(this@EditSubUserActivity)
    }


    private fun addUserResponse() {

        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@EditSubUserActivity, create_password, getString(msg))
        })

        viewModel.createUserResponseViewModels.observe(this, Observer { response: CreateUserResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    showMessage(this@EditSubUserActivity, create_password, response.message)
                    onBackPressed()
                } else {
                    showToast(response.errorMessage.toString())
                }

            } else {
                showToast(response.errorMessage.toString())
            }
        })
    }


    /*
Method to pick image
*/
    private fun pickImage() {
        PermissionsUtil.askPermissions(
            this@EditSubUserActivity,
            PermissionsUtil.CAMERA,
            PermissionsUtil.STORAGE,
            object : PermissionsUtil.PermissionListener {
                override fun onPermissionResult(isGranted: Boolean) {
                    if (isGranted) {
                        ImagePickerUtil.selectImage(
                            this@EditSubUserActivity,
                            object : ImagePickerUtil.ImagePickerListener {
                                override fun onImagePicked(imageFile: File?, tag: String?) {
                                    file = imageFile
                                    if(!imageFile?.absolutePath.isNullOrEmpty()) {
                                        val imgBitMap =
                                            BitmapFactory.decodeFile(imageFile?.absolutePath)
                                        imgUser_edtProfile_createUser.setImageBitmap(imgBitMap)
                                    }
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