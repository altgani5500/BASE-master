package com.parttime.enterprise.uicomman.chat.singlechat

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.parttime.enterprise.apiclients.ServerConstant.SOCKET_UPDATE_CHAT_LIST_UPDATE
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.helpers.chatsrequired.SocketHelper
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.chat.singlechat.chatlistmodels.ChatMessage
import com.parttime.enterprise.uicomman.chat.singlechat.singlechatadapter.ChatAdapter
import com.parttime.enterprise.uicomman.homes.HomeActivity
import com.parttime.enterprise.validations.ChatCalanderValidation
import com.parttime.enterprise.validations.ValidationMessageReturn
import com.parttime.enterprise.viewmodels.SingleChatViewModels
import kotlinx.android.synthetic.main.new_single_chat_screen.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SingleChatActivity : BaseActivity(), View.OnClickListener {

    internal var chatMessages: MutableList<ChatMessage.ChatData> = ArrayList<ChatMessage.ChatData>()
    internal lateinit var chatAdapter: ChatAdapter
    lateinit var layoutManagerAge: LinearLayoutManager
    lateinit var inputMsg_chat: EditText
    lateinit var viewModel: SingleChatViewModels
    var userID = 0
    var profileUserImg = " "
    var userName = " "

    override fun onClick(p0: View?) {
        when (p0) {
            imgClnder -> {
                showDialogSelectCalander()
            }
            accountSettingbck_add_user -> onBackPressed()
            send_Msgss -> {
                if (Utilities.isNetworkAvailable(this@SingleChatActivity)) {
                    if (!SocketHelper.getInstance(this@SingleChatActivity).isConnected) {
                        SocketHelper.getInstance(this@SingleChatActivity).startConnection()
                    }
                    onSend(inputMsg_chat.text.toString())
                } else {
                    showMessage(
                        this@SingleChatActivity,
                        singleChtRecycle,
                        getString(R.string.network_error)
                    )
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_single_chat_screen)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            accountSettingbck_add_user.rotation = 180F
            send_Msgss.rotation = 180F
        } else {
            accountSettingbck_add_user.rotation = 0F
            send_Msgss.rotation = 0F
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
            window
                .decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
        inputMsg_chat = findViewById<EditText>(R.id.inputMsg_chat1)
        if (!SocketHelper.getInstance(this@SingleChatActivity).isConnected) {
            SocketHelper.getInstance(this@SingleChatActivity).startConnection()
        }
        initViews()

        userID = intent.getIntExtra("USERID", 0)
        profileUserImg = intent.getStringExtra("IMG")
        userName = intent.getStringExtra("NAME")
        filterTitless.text = userName
        // local Receiver
        LocalBroadcastManager.getInstance(this@SingleChatActivity).registerReceiver(
            ChatListListener,
            IntentFilter(SOCKET_UPDATE_CHAT_LIST_UPDATE)
        )
        LocalBroadcastManager.getInstance(this@SingleChatActivity).registerReceiver(
            ReceiveMessageListener,
            IntentFilter(SocketHelper.RECEIVE_MESSAGE_ON)
        )
        LocalBroadcastManager.getInstance(this@SingleChatActivity).registerReceiver(
            ReceiveMessageListenerSend,
            IntentFilter(SocketHelper.RECEIVE_MESSAGE)
        )

    }

    private fun initViews() {
        imgClnder.setOnClickListener(this@SingleChatActivity)
        send_Msgss.setOnClickListener(this@SingleChatActivity)
        accountSettingbck_add_user.setOnClickListener(this@SingleChatActivity)
        chatAdapter = ChatAdapter(activity, chatMessages)
        singleChtRecycle.adapter = chatAdapter
        layoutManagerAge = LinearLayoutManager(this@SingleChatActivity)
        singleChtRecycle.layoutManager = layoutManagerAge

    }

    override fun onResume() {
        super.onResume()
        initiateChatScreen()

    }

    private fun initiateChatScreen() {
        if (Utilities.isNetworkAvailable(this@SingleChatActivity)) {
            showProgressBarNew()
            SocketHelper.getInstance(this@SingleChatActivity).getChatListEmit(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.enterpriseId.toString()), userID
            )
        } else {
            showMessage(
                this@SingleChatActivity,
                singleChtRecycle,
                getString(R.string.network_error)
            )
        }

    }

    /*On Message Send*/
    private fun onSend(mesg: String) {
        if (mesg.trim().length > 0) {
            if (SocketHelper.getInstance(this@SingleChatActivity).isConnected) {
                val sendMsgJsonObject = JSONObject()
                showProgressBarNew()
                try {
                    sendMsgJsonObject.accumulate("receiverId", userID.toString().trim())
                    sendMsgJsonObject.accumulate(
                        "senderId",
                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.enterpriseId.toString().trim())
                    )
                    sendMsgJsonObject.accumulate("message", mesg)
                    sendMsgJsonObject.accumulate("sender_type", "enterprise")
                    sendMsgJsonObject.accumulate("receiver_type", "user")
                } catch (e: JSONException) {
                    hideProgressBarNew()
                    e.printStackTrace()
                }

                // SocketManager.sendMsg("sendMessage", sendMsgJsonObject);
                SocketHelper.getInstance(this@SingleChatActivity).sendMsg(sendMsgJsonObject)
                inputMsg_chat.setText("")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        hideProgressBarNew()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgressBarNew()
        SocketHelper.getInstance(this@SingleChatActivity).stopConnection()
        LocalBroadcastManager.getInstance(this@SingleChatActivity)
            .unregisterReceiver(ChatListListener)
        LocalBroadcastManager.getInstance(this@SingleChatActivity)
            .unregisterReceiver(ReceiveMessageListener)
        LocalBroadcastManager.getInstance(this@SingleChatActivity)
            .unregisterReceiver(ReceiveMessageListenerSend)
    }

    // chat calander btn click
    fun showDialogSelectCalander(/*, pro: Object*/) {
        val dialog = Dialog(this@SingleChatActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var width = (resources.displayMetrics.widthPixels * 0.98).toInt()
        var height = (resources.displayMetrics.heightPixels * 0.90).toInt()
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.chat_calander_dialog)
        //  dialog.window.setLayout(width, height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window
                .decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
        val dateSelection = dialog.findViewById(R.id.txtDateCompleted) as TextView
        var cal = Calendar.getInstance()
        dateSelection.setOnClickListener {
            var da = DatePickerDialog(
                this@SingleChatActivity, getSelectedDate(dateSelection, cal),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            da.datePicker.minDate = cal.timeInMillis
            da.show()
        }
        // spinner clickable and validation
        var startHour = dialog.findViewById(R.id.spn_startHour) as Spinner
        var endHour = dialog.findViewById(R.id.spn_EndHour) as Spinner
        var startMin = dialog.findViewById(R.id.spn_startmin) as Spinner
        var endMin = dialog.findViewById(R.id.spn_Endmin) as Spinner
        var userInputMsg = dialog.findViewById(R.id.interview_acceptence_inputs) as EditText
        var btnSubmit = dialog.findViewById(R.id.interview_acceptence_btn) as Button
        btnSubmit.setOnClickListener {
            var result: ValidationMessageReturn = ChatCalanderValidation().isInputValida(
                context = this@SingleChatActivity,
                date1 = dateSelection.text.toString().trim(),
                startHour = startHour.selectedItem.toString().trim(),
                startTime = startMin.selectedItem.toString().trim(),
                endHour = endHour.selectedItem.toString().trim(),
                endTime = endMin.selectedItem.toString().trim(),
                userInputMsg = userInputMsg.text.toString().trim()
            )
            if (result.status == 1) {
                var startTimes = startHour.selectedItem.toString().trim().toInt()
                var endTimes = endHour.selectedItem.toString().trim().toInt()
                var endMinutes=endMin.selectedItem.toString().trim().toInt()
                var startMinutes=startMin.selectedItem.toString().trim().toInt()
                if ((endTimes > startTimes)||(endTimes==startTimes&&endMinutes>startMinutes)) {
                    //showMessage(this@SingleChatActivity, btnSubmit, "Ready to Upload")
                    if (Utilities.isNetworkAvailable(this@SingleChatActivity)) {
                        var sTime =
                            startHour.selectedItem.toString() + ":" + startMin.selectedItem.toString()
                        var eTime =
                            endHour.selectedItem.toString() + ":" + endMin.selectedItem.toString()
                        viewModel =
                            ViewModelProviders.of(activity).get(SingleChatViewModels::class.java)
                        viewModel.setDialogChatApi(
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                            userInputMsg.text.toString(),
                            sTime, eTime, dateSelection.text.toString(),
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.enterpriseId.toString()),
                            userID.toString()
                        )
                        chatDialogApiResponse()
                        dialog.dismiss()
                    } else {
                        showMessage(
                            this@SingleChatActivity,
                            btnSubmit,
                            getString(R.string.network_error)
                        )
                    }
                } else {
                    showMessage(
                        this@SingleChatActivity,
                        btnSubmit,
                        getString(R.string.start_time_end_time_validation)
                    )
                }

            } else {
                showMessage(this@SingleChatActivity, btnSubmit, result.errMessage)
            }

        }

        dialog.show()
    }


    override fun onBackPressed() {
        SocketHelper.getInstance(this@SingleChatActivity).stopConnection()
        LocalBroadcastManager.getInstance(this@SingleChatActivity)
            .unregisterReceiver(ChatListListener)
        LocalBroadcastManager.getInstance(this@SingleChatActivity)
            .unregisterReceiver(ReceiveMessageListener)
        LocalBroadcastManager.getInstance(this@SingleChatActivity)
            .unregisterReceiver(ReceiveMessageListenerSend)
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

    // get Date picker for dialog
    private fun getSelectedDate(
        textView: TextView,
        cal: Calendar
    ): DatePickerDialog.OnDateSetListener? {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textView.text = sdf.format(cal.time)
            }
        return dateSetListener
    }


    // broadcast reciver
    private val ChatListListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            hideProgressBarNew()
            val chatMessage =
                Gson().fromJson<ChatMessage>(intent.getStringExtra("data"), ChatMessage::class.java)
            chatMessages.clear()
            chatMessages.addAll(chatMessage.chat)
            chatAdapter.notifyDataSetChanged()
            singleChtRecycle.scrollToPosition(chatMessages.size - 1)
        }
    }

    private val ReceiveMessageListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            hideProgressBarNew()
            try {
                if (chatMessages.isNotEmpty()) {
                    chatMessages.clear()
                }
                val response = JSONObject(intent.getStringExtra("data"))
                var created_at = ""
                var message = ""
                var read_status = ""
                var receiver_id = 0
                var receiver_type = ""
                var sender_id = 0
                var sender_type = ""
                var messageType = 3
                var startTime = " "
                var endTime = " "
                var dates = " "
                val josonArray = response.optJSONArray("chat")
                for (i in 0 until josonArray.length()) {
                    val jobjResult = josonArray.optJSONObject(i)
                    if (jobjResult.has("created_at")) {
                        created_at = jobjResult.optString("created_at")
                    }
                    if (jobjResult.has("message")) {
                        message = jobjResult.optString("message")
                    }
                    if (jobjResult.has("read_status")) {
                        read_status = jobjResult.optString("read_status")
                    }
                    if (jobjResult.has("receiver_id")) {
                        receiver_id = jobjResult.optInt("receiver_id")
                    }
                    if (jobjResult.has("receiver_type")) {
                        receiver_type = jobjResult.optString("receiver_type")
                    }
                    if (jobjResult.has("sender_id")) {
                        sender_id = jobjResult.optInt("sender_id")
                    }
                    if (jobjResult.has("sender_type")) {
                        sender_type = jobjResult.optString("sender_type")
                    }
                    val chatData = ChatMessage.ChatData()
                    if (jobjResult.has("message_type")) {
                        var type = jobjResult.optInt("message_type")
                        chatData.message_type = type.toString()
                        if (chatData.message_type.toInt() == 1) {
                            if (jobjResult.has("end_time")) {
                                chatData.end_time = jobjResult.optString("end_time")
                            }
                            if (jobjResult.has("start_time")) {
                                chatData.start_time = jobjResult.optString("start_time")
                            }
                            if (jobjResult.has("date")) {
                                if (!jobjResult.optString("date").isNullOrEmpty()) {
                                    chatData.date =
                                        Utilities().getChatCalander(jobjResult.optString("date"))
                                }


                            }
                        }
                    }

                    chatData.created_at = created_at
                    if (chatData.message_type.toInt() == 1) {
                        chatData.message =
                            getString(R.string.chat_cal_send_msg) + " " + chatData.date + "\n" + message + "\n" + chatData.start_time + " to " + chatData.end_time

                    } else {
                        chatData.message = message
                    }
                    chatData.userImage = profileUserImg
                    chatData.read_status = read_status
                    chatData.receiver_id = receiver_id
                    chatData.receiver_type = receiver_type
                    chatData.sender_id = sender_id
                    chatData.sender_type = sender_type
                    chatMessages.add(chatData)
                }
                chatAdapter.notifyDataSetChanged()
                singleChtRecycle.scrollToPosition(chatMessages.size - 1)
            } catch (ex: Exception) {
                hideProgressBarNew()
                ex.printStackTrace()
            }

        }
    }


    private val ReceiveMessageListenerSend = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val response = JSONObject(intent.getStringExtra("data"))
                hideProgressBarNew()
                var created_at = ""
                var message = ""
                var read_status = ""
                var receiver_id = 0
                var receiver_type = ""
                var sender_id = 0
                var sender_type = ""
                var messageType = " "
                var startTime = " "
                var endTime = " "
                var dates = " "
                if (response.has("created_at")) {
                    var serverDate = response.optString("created_at")
                    var newDate = Utilities.formatDateTime(
                        Utilities.convertUTCtoLocalTime1(
                            serverDate
                        ), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    )
                    created_at = newDate
                }
                if (response.has("message")) {
                    message = response.optString("message")
                }
                if (response.has("read_status")) {
                    read_status = response.optString("read_status")
                }
                if (response.has("receiver_id")) {
                    receiver_id = response.optInt("receiver_id")
                }
                if (response.has("receiver_type")) {
                    receiver_type = response.optString("receiver_type")
                }
                if (response.has("sender_id")) {
                    sender_id = response.optInt("sender_id")
                }
                if (response.has("sender_type")) {
                    sender_type = response.optString("sender_type")
                }
                if (response.has("message_type")) {
                    if (response.optString("message_type").contentEquals("1")) {
                        messageType = response.optString("message_type")
                        if (response.has("end_time")) {
                            endTime = response.optString("end_time")
                        }
                        if (response.has("start_time")) {
                            startTime = response.optString("start_time")
                        }
                        if (response.has("date")) {
                            dates = response.optString("date")
                        }
                    }
                }
                val chatData = ChatMessage.ChatData()
                chatData.created_at = created_at
                if (messageType.contentEquals("1")) {
                    chatData.message =
                        getString(R.string.chat_cal_send_msg) + " " + dates + "\n" + message + "\n" + startTime + " to " + endTime

                } else {
                    chatData.message = message
                }
                chatData.created_at = created_at
                //chatData.message = message
                chatData.userImage = profileUserImg
                chatData.read_status = read_status
                chatData.receiver_id = receiver_id
                chatData.receiver_type = receiver_type
                chatData.sender_id = sender_id
                chatData.sender_type = sender_type
                // chatMessages.add(chatData)
                // chatAdapter.notifyDataSetChanged()
                //  singleChtRecycle.scrollToPosition(chatMessages.size - 1)
                initiateChatScreen()
            } catch (ex: Exception) {
                hideProgressBarNew()
                ex.printStackTrace()
            }

        }
    }


    /*Chat Dialog Response*/
    private fun chatDialogApiResponse() {
        hideProgressBarNew()
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer {
            activity.hideProgressBarNew()
            activity.showMessage(this@SingleChatActivity, singleChtRecycle, getString(it))
        })
        viewModel.singleChatViewmodel.observe(this, androidx.lifecycle.Observer {
            hideProgressBarNew()
            if (it.success) {
                if (it.code == 200) {
                    hideProgressBarNew()
                    // initiateChatScreen()
                    //showMessage(this@SingleChatActivity, singleChtRecycle, it.message.toString())
                    /*
                    * */
                    initiateChatScreen()
                } else {
                    hideProgressBarNew()
                    showMessage(
                        this@SingleChatActivity,
                        singleChtRecycle,
                        it.errorMessage.toString()
                    )
                }

            } else {
                hideProgressBarNew()
                showMessage(this@SingleChatActivity, singleChtRecycle, it.errorMessage.toString())
            }
        })

    }

}