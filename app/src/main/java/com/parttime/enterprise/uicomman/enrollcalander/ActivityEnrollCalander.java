package com.parttime.enterprise.uicomman.enrollcalander;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parttime.enterprise.R;
import com.parttime.enterprise.calandarutils.model.EventModel;
import com.parttime.enterprise.helpers.Utilities;
import com.parttime.enterprise.helpers.chatsrequired.SocketHelper;
import com.parttime.enterprise.prefences.AppPrefence;
import com.parttime.enterprise.uicomman.BaseActivity;
import com.parttime.enterprise.uicomman.chat.singlechat.chatlistmodels.ChatMessage;
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.ActivityCalanderTaskList;
import com.parttime.enterprise.uicomman.enrollcalander.chatadapter.groupChatAdapter.GroupChatAdapter;
import com.parttime.enterprise.uicomman.enrollcalander.flexiblecalender.flexiblecalendar.FlexibleCalendarView;
import com.parttime.enterprise.uicomman.enrollcalander.flexiblecalender.flexiblecalendar.view.BaseCellView;
import com.parttime.enterprise.uicomman.enrollcalander.schedulecalander.ScheduleCalenderResponseModel;
import com.parttime.enterprise.uicomman.homes.HomeActivity;
import com.parttime.enterprise.uicomman.schedules.AddSchedulesActivity;
import com.parttime.enterprise.viewmodels.EnrollCalanderViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kotlin.jvm.internal.Intrinsics;

public class ActivityEnrollCalander extends BaseActivity {

    int freshDate = 0;
    private TextView monthTxtTitle;
    private ImageView imgAddSchedule;
    private List<EventModel> allEventsList = new ArrayList<>();
    //view
    private ScrollView parrentScroll;
    private RecyclerView groupRecycle;
    private ImageView profile_backedt;
    private EnrollCalanderViewModel viewModel;
    private int mWeekViewType = 7;
    private Spinner spndayWise;
    // group chat variable
    private int userId = 0;
    private ArrayList<ChatMessage.ChatData> groupChatMessages = new ArrayList<>();
    private GroupChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private EditText inputMsgEdit;
    private ImageView sendMsgChat;
    private RecyclerView recycleView;

    /*Calender View Variable*/
    private Map<Integer, List<CustomEvent>> eventMap;
    private FlexibleCalendarView calendarView;
    private TextView txtCurrent, txtPrevMonth, txtNextMonth;
    private int years;
    private int months;

    /*Group chat Brod cast Receiver*/
    private BroadcastReceiver ReceiveMessageListenerSend = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideProgressBarNew();
            try {
                JSONObject jobjResult = new JSONObject(intent.getStringExtra("data"));
                String message = " ";
                String created_at = " ";
                String sender_type = " ";
                String receiver_type = " ";

                //JSONObject jobjResult = response.getJSONObject("chat");

                if (jobjResult.has("message")) {
                    message = jobjResult.getString("message");
                }
                if (jobjResult.has("created_at")) {
                    created_at = jobjResult.getString("created_at");

                }
                if (jobjResult.has("sender_type")) {
                    sender_type = jobjResult.getString("sender_type");
                }

                if (jobjResult.has("receiver_type")) {
                    receiver_type = jobjResult.getString("receiver_type");
                }
                /*if (jobjResult.has("id")) {
                    id = jobjResult.getInt("id");
                }*/
                ChatMessage.ChatData chatData = new ChatMessage.ChatData();
                chatData.setCreated_at(created_at);
                chatData.setSender_type(sender_type);
                chatData.setReceiver_type(receiver_type);
                chatData.setMessage(message);

             /*   groupChatMessages.add(chatData);
                chatAdapter.notifyDataSetChanged();
                recycleView.scrollToPosition(groupChatMessages.size() - 1);*/
                if (Utilities.isNetworkAvailable(ActivityEnrollCalander.this)) {
                    //showProgressBarNew();
                    SocketHelper.getInstance(ActivityEnrollCalander.this).getGROUPChatListEmit(
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.enterpriseId.toString())
                    );
                } else {
                    showMessage(
                            ActivityEnrollCalander.this,
                            recycleView,
                            getString(R.string.network_error)
                    );
                }
            } catch (Exception ex) {
                hideProgressBarNew();
                ex.printStackTrace();
            }

        }
    };
    /*Group Chat List/Messages */
    private BroadcastReceiver ReceiveMessagesListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideProgressBarNew();
            try {
                if (!groupChatMessages.isEmpty() && groupChatMessages != null) {
                    groupChatMessages.clear();
                }
                JSONObject response = new JSONObject(intent.getStringExtra("data"));
                JSONArray resultArray = response.getJSONArray("chat");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jobjResult = resultArray.getJSONObject(i);
                    ChatMessage.ChatData chatData = new ChatMessage.ChatData();
                    if (jobjResult.has("created_at")) {
                        chatData.setCreated_at(jobjResult.getString("created_at"));
                    }

                    if (jobjResult.has("message")) {
                        chatData.setMessage(jobjResult.getString("message"));
                    }
                    if (jobjResult.has("name")) {
                        chatData.setNameReceiver(jobjResult.getString("name"));
                    }
                    if (jobjResult.has("receiver_type")) {
                        chatData.setReceiver_type(jobjResult.getString("receiver_type"));

                    }
                    if (jobjResult.has("sender_type")) {
                        chatData.setSender_type(jobjResult.getString("sender_type"));
                    }

                    if (jobjResult.has("userPic")) {
                        chatData.setImgReceiver(jobjResult.getString("userPic"));
                    }
                    groupChatMessages.add(chatData);
                }

                chatAdapter.notifyDataSetChanged();
                recycleView.scrollToPosition(groupChatMessages.size() - 1);
            } catch (Exception ex) {
                hideProgressBarNew();
                ex.printStackTrace();
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001)
            if (data != null) {
                appEventApiCall(years, months);
            }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_enroll);
        profile_backedt = findViewById(R.id.profile_backedt);
        sendMsgChat = findViewById(R.id.send_Msgss);
        imgAddSchedule = findViewById(R.id.calander_menu);
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
        ).equals("ar")
        ) {
            profile_backedt.setRotation(180F);
            sendMsgChat.setRotation(180F);
            imgAddSchedule.setRotation(180F);
        } else {
            profile_backedt.setRotation(0F);
            sendMsgChat.setRotation(0F);
            imgAddSchedule.setRotation(0F);
        }
        monthTxtTitle = findViewById(R.id.jobAddTitle_txt);
        groupRecycle = findViewById(R.id.groupRecycle);
        Calendar curCal2 = Calendar.getInstance();
        years = curCal2.get(Calendar.YEAR);
        months = curCal2.get(Calendar.MONTH) + 1;
        // call Event
        eventMap = new HashMap<>();
        appEventApiCall(years, months);
        initViewCalandar();

        imgAddSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityEnrollCalander.this, AddSchedulesActivity.class);
            startActivityForResult(intent, 1001);
        });


        profile_backedt.setOnClickListener(v -> {
            onBackPressed();
        });

        // group chat initilise
        if (!SocketHelper.getInstance(ActivityEnrollCalander.this).isConnected()) {
            SocketHelper.getInstance(ActivityEnrollCalander.this).startConnection();
        }
        // chat view initilise
        chatViewInit();

        /*Chat Received Messge Of for message*/
        LocalBroadcastManager.getInstance(ActivityEnrollCalander.this).registerReceiver(
                ReceiveMessageListenerSend,
                new IntentFilter(SocketHelper.RECEIVE_MESSAGE)
        );

        /*Messages Receiver chat List*/
        LocalBroadcastManager.getInstance(ActivityEnrollCalander.this).registerReceiver(
                ReceiveMessagesListener,
                new IntentFilter(SocketHelper.RECEIVE_MESSAGE_ON)
        );


        // for Calandar scroll
        calendarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.calendar_view) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()) {
                       /*//* case MotionEvent.ACTION_DOWN:
                            v.requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_CANCEL:*/
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                        default:
                            break;
                    }

                }
                return false;
            }
        });

        // for Calandar RecycleVew
        groupRecycle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.groupRecycle) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()) {
                       /* case MotionEvent.ACTION_DOWN:
                            v.requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_CANCEL:*/
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                        default:
                            break;
                    }

                }
                return false;
            }
        });

    }

    /*Group chat initilise*/
    private void chatViewInit() {
        inputMsgEdit = findViewById(R.id.inputMsg_chat);
        recycleView = findViewById(R.id.groupRecycle);
        //chat adapter init
        chatAdapter = new GroupChatAdapter(ActivityEnrollCalander.this, groupChatMessages);
        recycleView.setAdapter(chatAdapter);
        layoutManager = new LinearLayoutManager(ActivityEnrollCalander.this, LinearLayoutManager.VERTICAL, false);
        recycleView.setLayoutManager(layoutManager);

        // chat input
        sendMsgChat.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(ActivityEnrollCalander.this)) {
                if (!SocketHelper.getInstance(ActivityEnrollCalander.this).isConnected()) {
                    SocketHelper.getInstance(ActivityEnrollCalander.this).startConnection();
                }
                onSend(inputMsgEdit.getText().toString());
            } else {
                showMessage(
                        ActivityEnrollCalander.this,
                        recycleView,
                        getString(R.string.network_error)
                );
            }

        });


    }

    public void showDialog(String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.calander_view_dialog);

        TextView text = dialog.findViewById(R.id.txtMoreMonthNameCal);
        text.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_calDialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.isNetworkAvailable(ActivityEnrollCalander.this)) {
            //showProgressBarNew();
            SocketHelper.getInstance(ActivityEnrollCalander.this).getGROUPChatListEmit(
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.enterpriseId.toString())
            );
        } else {
            showMessage(
                    ActivityEnrollCalander.this,
                    recycleView,
                    getString(R.string.network_error)
            );
        }
    }

    /*Chat message Send */
    public void onSend(String mesg) {
        if (mesg.trim().length() > 0) {
            if (SocketHelper.getInstance(ActivityEnrollCalander.this).isConnected()) {
                showProgressBarNew();
                JSONObject sendMsgJsonObject = new JSONObject();
                try {
                    sendMsgJsonObject.accumulate("userId", userId);
                    sendMsgJsonObject.accumulate("enterpriseId", Integer.parseInt(appPrefence.getString(AppPrefence.SharedPreferncesKeys.enterpriseId.toString())));
                    sendMsgJsonObject.accumulate("message", mesg);
                    sendMsgJsonObject.accumulate("sender_type", "enterprise");
                    sendMsgJsonObject.accumulate("receiver_type", "user");
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideProgressBarNew();
                }
//                SocketManager.sendMsg("sendMessage", sendMsgJsonObject);
                SocketHelper.getInstance(ActivityEnrollCalander.this).groupSendMsg(sendMsgJsonObject);
                inputMsgEdit.setText("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressBarNew();
        SocketHelper.getInstance(ActivityEnrollCalander.this).stopConnection();
        /*Group message Receiver un register*/
        LocalBroadcastManager.getInstance(ActivityEnrollCalander.this)
                .unregisterReceiver(ReceiveMessageListenerSend);

        LocalBroadcastManager.getInstance(ActivityEnrollCalander.this)
                .unregisterReceiver(ReceiveMessagesListener);
    }

    @Override
    public void onBackPressed() {
        SocketHelper.getInstance(ActivityEnrollCalander.this).stopConnection();
        /*Group message Receiver un register*/
        LocalBroadcastManager.getInstance(ActivityEnrollCalander.this)
                .unregisterReceiver(ReceiveMessageListenerSend);

        LocalBroadcastManager.getInstance(ActivityEnrollCalander.this)
                .unregisterReceiver(ReceiveMessagesListener);

        if (getIntent().hasExtra("FLAGS_BACK")) {
            if (getIntent().getIntExtra("FLAGS_BACK", 0) == 1) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        } else
            finish();
    }

    private void initViewCalandar() {
        // get Current Calender Date
        Calendar curCal = Calendar.getInstance();
        Calendar curCal2 = Calendar.getInstance();
        Calendar curCal3 = Calendar.getInstance();
        years = curCal.get(Calendar.YEAR);
        months = curCal.get(Calendar.MONTH) + 1;
        String cMonth = curCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        curCal2.add(Calendar.MONTH, -1);
        curCal3.add(Calendar.MONTH, +1);
        String pMonth = curCal2.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String nMonth = curCal3.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        txtCurrent = findViewById(R.id.txtCurrentMonth);
        txtPrevMonth = findViewById(R.id.txtCurrentPrev);
        txtNextMonth = findViewById(R.id.txtCurrentNext);
        txtCurrent.setText(cMonth);
        txtNextMonth.setText(nMonth);
        txtPrevMonth.setText(pMonth);


        calendarView = findViewById(R.id.calendar_view);
        calendarView.setMonthViewHorizontalSpacing(10);
        calendarView.setMonthViewVerticalSpacing(10);
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, @FlexibleCalendarView.Direction int direction) {
                int currentMonth = month + 1;
                int prevMonth = currentMonth - 1;
                int extnMonth = currentMonth + 1;
                if (currentMonth == 1) {
                    txtPrevMonth.setText(theMonth(12));
                } else {
                    txtPrevMonth.setText(theMonth(prevMonth));
                }
                if (currentMonth == 12) {
                    txtNextMonth.setText(theMonth(1));
                } else {
                    txtNextMonth.setText(theMonth(extnMonth));
                }
                txtCurrent.setText(theMonth(currentMonth));
                months = currentMonth;
                years = year;

                appEventApiCall(year, months);
                // showToast(String.valueOf(currentMonth)+" "+theMonth(currentMonth));
            }
        });

        calendarView.setOnDateClickListener(new FlexibleCalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                String cMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
               // Toast.makeText(ActivityEnrollCalander.this, cal.getTime().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(ActivityEnrollCalander.this, ActivityCalanderTaskList.class);
                in.putExtra("DATES", day + " " + cMonth + " , " + year);
                int mm = month + 1;
                in.putExtra("DATE", year + "-" + mm + "-" + day);
                startActivity(in);
            }
        });

        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(ActivityEnrollCalander.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar3_date_cell_view, null);
                    cellView.setTextColor(getResources().getColor(R.color.black));
                    cellView.setTextSize(14F);
                    cellView.setPadding(0, 2, 0, 3);

                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(ActivityEnrollCalander.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar3_week_cell_view, null);
                    cellView.setTextColor(getResources().getColor(R.color.white));
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<CustomEvent> getEventsForTheDay(int year, int month, int day) {
                return getEvents(year, month, day);
            }
        });

    }

    public List<CustomEvent> getEvents(int year, int month, int day) {
        return eventMap.get(day);
    }


    public String theMonth(int month) {
        String[] months = getResources().getStringArray(R.array.years);
        return months[month - 1];
    }


    // calander Event Set
    private void setEventDataFromApi() {
        hideProgressBarNew();
        viewModel.getValidationErr().observe(this, new Observer() {
            public void onChanged(Object var1) {
                this.onChanged(((Number) var1).intValue());
            }

            public final void onChanged(int msg) {
                hideProgressBarNew();
                showMessage(ActivityEnrollCalander.this, parrentScroll, getString(msg));
            }
        });

        viewModel.getEnrollCalanderViewModel().observe(this, new Observer() {
            public void onChanged(Object var1) {
                this.onChanged((ScheduleCalenderResponseModel) var1);
            }

            public final void onChanged(@NotNull ScheduleCalenderResponseModel response) {
                Intrinsics.checkParameterIsNotNull(response, "response");
                hideProgressBarNew();
                if (response.getSuccess()) {
                    if (response.getCode() == 200) {

                        //List<CustomEvent> colorLst1 = new ArrayList<>();
                       /* colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                               eventMap.put(22, colorLst1);
                        */
                        if (eventMap.size() > 0) {
                            eventMap.clear();
                        }

                        for (int i = 0; i < response.getMessage().size(); i++) {
                            List<CustomEvent> colorLst1 = new ArrayList<>();
                            for (int j = 0; j < response.getMessage().get(i).getCount(); j++) {
                                colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                                String[] spDate = response.getMessage().get(i).getDate().split("-");
                                eventMap.put(Integer.valueOf(spDate[2]), colorLst1);
                            }
                        }

                        calendarView.refresh();

                    } else {
                        showMessage(ActivityEnrollCalander.this, parrentScroll, String.valueOf(response.getErrorMessage()));
                    }
                } else {
                    showMessage(ActivityEnrollCalander.this, parrentScroll, String.valueOf(response.getErrorMessage()));
                }

            }
        });

    }


    private void appEventApiCall(int year, int months) {
        if (eventMap.size() > 0) {
            eventMap.clear();
        }
        if (Utilities.isNetworkAvailable(ActivityEnrollCalander.this)) {
            viewModel = ViewModelProviders.of(this).get(EnrollCalanderViewModel.class);
            showProgressBarNew();
            viewModel.getSchedulesCalanders(appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                    Utilities.isNetworkAvailable(ActivityEnrollCalander.this), String.valueOf(months), String.valueOf(year));
            setEventDataFromApi();
        } else {
            showMessage(ActivityEnrollCalander.this, parrentScroll, getResources().getString(R.string.network_error));
        }
    }

}
