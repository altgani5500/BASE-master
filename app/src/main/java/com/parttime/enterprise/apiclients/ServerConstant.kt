package com.parttime.enterprise.apiclients

object ServerConstant {

   /* const val BASE_URL = "http://13.232.62.239/parttime/public/index.php/enterprise/v1/"
    const val BASE_URL_GET = "http://13.232.62.239/parttime/public/index.php/enterprise/v1/"*/
    const val BASE_URL = "https://www.partime.org/partime/public/index.php/enterprise/v1/"
    const val BASE_URL_GET ="https://www.partime.org/partime/public/index.php/enterprise/v1/"
 const val SOCKET_BASE_URL = "https://www.partime.org"
    //const val SOCKET_BASE_URL = "http://13.232.62.239:6262"
    const val USER_LOGIN = "enterpriseLogin"
    const val USER_FORGOT = "forgotPassword"
    const val USER_DEVICE_TOKEN = "updateDeviceToken"
    const val USER_JOB_LIST = "getJobList"
    const val USER_JOB_PARTTIME_GET = "getParttimeType"
    const val USER_CURRANCY_MODEL = "getCurrency"
    const val USER_INDUSTRY = "getCategoryList"
    const val USER_ADD_JOBS = "addJob"
    const val USER_PROFILE_UPDATE = "editProfile"
    const val USER_DELETE_JOBS = "deleteJob"
    const val USER_DELETE_APPLICANT = "deleteApplicants"
    const val USER_DETAILS_JOBS = "getJobDetail"
    const val USER_APPLICANT_JOB = "getJobApplicants"
    const val USER_PROFILE_VIEW = "getProfile"
    const val USER_APPLICANT_PROFILE_VIEW = "applicantsProfile"
    const val USER_EDIT_JOBS = "editJob"
    const val USER_JOB_IMG_DELETE = "deleteJobImage"
    const val USER_GET_EDUCATION_LEVEL = "getEducationList"
    const val USER_GET_NATIONALITY = "getNationality"
    const val USER_GET_EDUCATION_DETAILS_LIST = "getEducationDetailList"
    const val USER_GET_ACCOUNT_SETTING = "changeAccountSetting"
    const val USER_PASS_CHANGE = "changePassword"
    const val USER_ENROLL_ACCEPT = "enrolUser"
    const val USER_ENROLL_LIST = "getEnrolledUser"
    const val USER_ACCEPT_INTERVIEW = "acceptForInterview"
    const val USER_GET_SUB_USER_LIST = "getSubUser"
    const val USER_ADD_USER_SUB = "addSubUser"
    const val USER_EDIT_USER_SUB = "editSubUser"
    const val USER_ADVANCE_SETTING = "advanceSetting"
    const val USER_MESSAGE_LIST = "getAllChatMessages"
    const val USER_SEARCH_API = "searchApplicant"
    const val COUNTRY_LIST = "getCountry"
    const val STATE_LIST = "getState"
    const val CITY_LIST = "getCity"
    const val USER_NOTIFICATION_LIST = "notificationList"
    const val USER_SENDCHAT_MSG = "sendChatMessage"
    const val USER_APPSETTING = "getAdvanceSetting"
    const val WORKER_PROFILE_LIST = "getEnrolledUserProfile"
    const val SUPER_EVALUATOR_LIST = "getSuperEvaluator"
    const val CREATE_TASK= "createTask"
    const val TASK_DETAILS = "getTaskDetail"
    const val TASK_DETAILS_DIALOG_EVOLUTION = "evaluateTask"
    const val USER_ENROLL_LIST_ALL = "allEnrollUserList"
    const val UPDATE_TASK= "updateTask"
    const val CREATE_SCHEDUL="createSchedule"
    const val EDIT_SCHEDUL="editSchedule"
    const val CALANDER_SCHEDULE="getScheduleData"
    const val GET_MONTH_DETAILS="getUserWorkReport"
    const val UPDATE_ACCEPT_TEJECT_WOTRK_HISTORY="acceptRejectTiming"
    const val GET_PERTICULAR_SCHEDULE_LIST="getPerticularScheduleList"

    /*For Socket Related Url*/
    const val SOCKET_SAVE = "save_socket"
    const val SOCKET_UPDATE_CHAT_LIST_UPDATE = "chat_list_update"


    // default language Setting
    const val USER_LANG = "en"
    // other required que
    const val NETWORK_CHECK = "network_check"


    // app Api Key
    const val API_KEY_USERNAME = "userName"
    const val API_KYE_PASSWORD = "password"
    const val API_KEY_FORGOT_TYPE = "forgotType"
    const val API_KEY_EMAIL = "email"
    const val API_KEY_DEVICE_TOKEN = "deviceToken"
    const val API_KEY_DEVICE_TYPE = "deviceType"

    // app Constant Key Word
    const val FORGOTTYPE_INTENT = "forgotType"

    const val LOCATION_REQUEST = 1000
    const val GPS_REQUEST = 1001




}