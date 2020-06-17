package com.parttime.enterprise.apiclients


import com.parttime.enterprise.uicomman.accountsettings.accountsetting.AccountSettingResponse
import com.parttime.enterprise.uicomman.accountsettings.changepass.ChangePasswordModel
import com.parttime.enterprise.uicomman.addjobs.addJob.AddJobsModelsResponse
import com.parttime.enterprise.uicomman.addjobs.parttimemodel.PartTime
import com.parttime.enterprise.uicomman.advancesetting.advancesettingmodel.AdvanceSettingResponse
import com.parttime.enterprise.uicomman.applicantfilter.educationdetails.EducationDetails
import com.parttime.enterprise.uicomman.applicantfilter.educationlevel.EducationLevel
import com.parttime.enterprise.uicomman.applicantfilter.nationality.NationalityResponse
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.ApplicantProfileModelResponse
import com.parttime.enterprise.uicomman.appsearch.searchresults.SearchResponse
import com.parttime.enterprise.uicomman.auth.authmodels.AuthLoginModel
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import com.parttime.enterprise.uicomman.chat.singlechat.dialogmodels.ChatDialogResponse
import com.parttime.enterprise.uicomman.editjobs.deleteimage.ImageDeleteResponse
import com.parttime.enterprise.uicomman.editjobs.detailsmodels.EditDetailsData
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels.ScheduleTimeResponse
import com.parttime.enterprise.uicomman.enrollcalander.schedulecalander.ScheduleCalenderResponseModel
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.createtask.CreateTaskResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel.SuperEvaluatorModelsResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.detail.TaskDetailResponseModel
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.dialogevolution.DialogEvolutionModelRes
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist.WorkerProfileListModelResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.AcceptRejectWorkResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.MonthDetailsModelResponse
import com.parttime.enterprise.uicomman.forgots.forgotModel.ForgotModelResponse
import com.parttime.enterprise.uicomman.fragments.applicant.acceptfinal.AcceptInterviewResponse
import com.parttime.enterprise.uicomman.fragments.applicant.applicantmodel.ApplicantModelResponse
import com.parttime.enterprise.uicomman.fragments.applicant.enrollaccept.EnrollAcceptModel
import com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.response.EnrollWorkerListResponse
import com.parttime.enterprise.uicomman.fragments.jobsDetails.models.JobDetailsModelResponse
import com.parttime.enterprise.uicomman.fragments.jobslist.currencyModel.CurrancyModel
import com.parttime.enterprise.uicomman.fragments.jobslist.jobsdelete.DeleteJobResponse
import com.parttime.enterprise.uicomman.fragments.jobslist.jobslistmodels.JobListResponseModel
import com.parttime.enterprise.uicomman.fragments.messages.messagemodel.MessageListResponse
import com.parttime.enterprise.uicomman.fragments.profileview.profilemodel.ProfileViewModelResponse
import com.parttime.enterprise.uicomman.homes.homesmodels.HomesModels
import com.parttime.enterprise.uicomman.locationaddress.citymodel.CityModelResponseX
import com.parttime.enterprise.uicomman.locationaddress.countrymodel.CountryListResponse
import com.parttime.enterprise.uicomman.locationaddress.statemodel.StateModelResponse
import com.parttime.enterprise.uicomman.notifications.notificationmodels.NotificationResponseModels
import com.parttime.enterprise.uicomman.profileedit.indusrtymodel.IndustryModel
import com.parttime.enterprise.uicomman.profileedit.indusrtymodel.UpdateProfileResponse
import com.parttime.enterprise.uicomman.schedules.CreateScheduleResponseModel
import com.parttime.enterprise.uicomman.schedules.allenrolllist.GetAllEnrollUserListResponse
import com.parttime.enterprise.uicomman.subuser.adduserlist.GetSubUserListResponse
import com.parttime.enterprise.uicomman.subuser.createuser.CreateUserResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit


interface ApiService {


    @POST(ServerConstant.USER_LOGIN)
    fun userLogin(@Header("lang") header: String, @Body map: HashMap<String, String>): Call<AuthLoginModel>

    @POST(ServerConstant.USER_FORGOT)
    fun userForgotPass(@Header("lang") header: String, @Body map: HashMap<String, String>): Call<ForgotModelResponse>

    @POST(ServerConstant.USER_DEVICE_TOKEN)
    fun userDeviceToken(@Header("Authorization") header: String, @Header("lang") header1: String, @Body map: HashMap<String, String>): Call<DeviceToken>

    @POST(ServerConstant.USER_JOB_LIST)
    fun getJobsList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<JobListResponseModel>

    @GET(ServerConstant.USER_JOB_PARTTIME_GET)
    fun getPartTimeListApi(@Header("lang") header2: String): Call<PartTime>

    @GET(ServerConstant.USER_CURRANCY_MODEL)
    fun getCurrancyApi(@Header("lang") header2: String): Call<CurrancyModel>

    @GET(ServerConstant.USER_GET_EDUCATION_LEVEL)
    fun getEducationLevelList(@Header("lang") header2: String): Call<EducationLevel>

    @GET(ServerConstant.USER_GET_NATIONALITY)
    fun getNationalList(@Header("lang") header2: String): Call<NationalityResponse>

    @GET(ServerConstant.USER_GET_EDUCATION_DETAILS_LIST)
    fun getEducationDetailsList(@Header("lang") header2: String): Call<EducationDetails>

    @GET(ServerConstant.USER_INDUSTRY)
    fun getIndustryApi(@Header("lang") header2: String): Call<IndustryModel>

    @Multipart
    @POST(ServerConstant.USER_PROFILE_UPDATE)
    fun editProfile(@Header("Authorization") header: String, @Header("lang") header2: String, @PartMap map: HashMap<String, RequestBody>, @Part part: MultipartBody.Part?): Call<UpdateProfileResponse>

    @Multipart
    @POST(ServerConstant.USER_ADD_JOBS)
    fun userAddJob(@Header("Authorization") header: String, @Header("lang") header2: String, @PartMap map: HashMap<String, RequestBody>, @Part part: ArrayList<MultipartBody.Part>): Call<AddJobsModelsResponse>

    @POST(ServerConstant.USER_DETAILS_JOBS)
    fun detailsJobApiForEdit(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<EditDetailsData>

    @POST(ServerConstant.USER_DELETE_JOBS)
    fun deleteJobApi(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<DeleteJobResponse>

    @POST(ServerConstant.USER_DELETE_APPLICANT)
    fun deleteApplicantApi(@Header("Authorization") header: String, @Body map: HashMap<String, String>): Call<DeleteJobResponse>

    @POST(ServerConstant.USER_DETAILS_JOBS)
    fun detailsJobApi(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<JobDetailsModelResponse>

    @POST(ServerConstant.USER_APPLICANT_PROFILE_VIEW)
    fun getApplicantProfileData(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ApplicantProfileModelResponse>

    @POST(ServerConstant.USER_PROFILE_VIEW)
    fun getProfileData(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ProfileViewModelResponse>

    @POST(ServerConstant.USER_APPLICANT_JOB)
    fun getApplicantsList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ApplicantModelResponse>

    @POST(ServerConstant.USER_GET_ACCOUNT_SETTING)
    fun accountSetting(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<AccountSettingResponse>


    @POST(ServerConstant.USER_PASS_CHANGE)
    fun accountSettingPassword(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ChangePasswordModel>

    @Multipart
    @POST(ServerConstant.USER_EDIT_JOBS)
    fun userEditJob(@Header("Authorization") header: String, @Header("lang") header2: String, @PartMap map: HashMap<String, RequestBody>, @Part part: ArrayList<MultipartBody.Part>): Call<AddJobsModelsResponse>


    @POST(ServerConstant.USER_JOB_IMG_DELETE)
    fun jobImageDelet(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ImageDeleteResponse>

    @POST(ServerConstant.USER_ENROLL_ACCEPT)
    fun acceptForUserEnrollApi(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<EnrollAcceptModel>


    @POST(ServerConstant.USER_ACCEPT_INTERVIEW)
    fun acceptForInterviewApi(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<AcceptInterviewResponse>


    @POST(ServerConstant.USER_ENROLL_LIST)
    fun getEnrollWorkerList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<EnrollWorkerListResponse>


    @POST(ServerConstant.USER_GET_SUB_USER_LIST)
    fun getSubUserListApi(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<GetSubUserListResponse>

    @Multipart
    @POST(ServerConstant.USER_ADD_USER_SUB)
    fun addSubUserApi(@Header("Authorization") header: String, @Header("lang") header2: String, @PartMap map: HashMap<String, RequestBody>, @Part part: MultipartBody.Part?): Call<CreateUserResponse>

    @Multipart
    @POST(ServerConstant.USER_EDIT_USER_SUB)
    fun editSubUserApi(@Header("Authorization") header: String, @Header("lang") header2: String, @PartMap map: HashMap<String, RequestBody>, @Part part: MultipartBody.Part?): Call<CreateUserResponse>

    @POST(ServerConstant.USER_ADVANCE_SETTING)
    fun updateAdvanceSettingAPI(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<AdvanceSettingResponse>

    @POST(ServerConstant.WORKER_PROFILE_LIST)
    fun workerProfileList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<WorkerProfileListModelResponse>

    @POST(ServerConstant.SUPER_EVALUATOR_LIST)
    fun getSuperEvaluatorList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<SuperEvaluatorModelsResponse>

    @POST(ServerConstant.CREATE_TASK)
    fun createTask(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<CreateTaskResponse>


    @POST(ServerConstant.USER_MESSAGE_LIST)
    fun getMsgList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<MessageListResponse>

    @POST(ServerConstant.USER_SENDCHAT_MSG)
    fun setDialogChat(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ChatDialogResponse>

    @POST(ServerConstant.USER_SEARCH_API)
    fun applicantJobSearchApi(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<SearchResponse>

    @POST(ServerConstant.COUNTRY_LIST)
    fun getCountry(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<CountryListResponse>

    @POST(ServerConstant.STATE_LIST)
    fun getState(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<StateModelResponse>

    @POST(ServerConstant.CITY_LIST)
    fun getCity(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<CityModelResponseX>

    @POST(ServerConstant.USER_NOTIFICATION_LIST)
    fun getNotificationList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<NotificationResponseModels>

    @POST(ServerConstant.USER_APPSETTING)
    fun userRoleSetting(@Header("Authorization") header: String, @Header("lang") header1: String, @Body map: HashMap<String, String>): Call<HomesModels>

    @POST(ServerConstant.TASK_DETAILS)
    fun taskProfileDetails(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<TaskDetailResponseModel>

    @POST(ServerConstant.TASK_DETAILS_DIALOG_EVOLUTION)
    fun taskEvolutionDialog(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<DialogEvolutionModelRes>

    @POST(ServerConstant.UPDATE_TASK)
    fun updateTask(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<CreateTaskResponse>

    @POST(ServerConstant.USER_ENROLL_LIST_ALL)
    fun getAppUserEnrolList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<GetAllEnrollUserListResponse>

    @POST(ServerConstant.CREATE_SCHEDUL)
    fun createScheduls(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<CreateScheduleResponseModel>

    @POST(ServerConstant.CALANDER_SCHEDULE)
    fun getSchedulesCalander(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ScheduleCalenderResponseModel>

    @POST(ServerConstant.EDIT_SCHEDUL)
    fun editsScheduls(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<CreateScheduleResponseModel>

    @POST(ServerConstant.GET_MONTH_DETAILS)
    fun getMonthDeatilsApi(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<MonthDetailsModelResponse>

    @POST(ServerConstant.UPDATE_ACCEPT_TEJECT_WOTRK_HISTORY)
    fun acceptRejectTiming(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<AcceptRejectWorkResponse>

    @POST(ServerConstant.GET_PERTICULAR_SCHEDULE_LIST)
    fun getScheduleList(@Header("Authorization") header: String, @Header("lang") header2: String, @Body map: HashMap<String, String>): Call<ScheduleTimeResponse>

    companion object Factory {

        var interceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        /*
        * Ig SSL PATH
        * */
        private var client: OkHttpClient =
            SSLTrust.getUnsafeOkHttpClient().connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build()
        /*
        * **** for without ssl
        * */
        /* private var client: OkHttpClient =
             OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)
                 .readTimeout(5, TimeUnit.MINUTES)
                 .writeTimeout(5, TimeUnit.MINUTES)
                 .addInterceptor(interceptor)
                 .build()*/

        fun init(): ApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(ServerConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }

        fun initGet(): ApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(ServerConstant.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


            return retrofit.create(ApiService::class.java)
        }

    }


}