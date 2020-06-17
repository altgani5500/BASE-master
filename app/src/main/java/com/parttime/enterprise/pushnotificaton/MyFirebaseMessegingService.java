package com.parttime.enterprise.pushnotificaton;

/**
 * Created by Tech Ugo on 8/29/2016.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.parttime.com.uicomman.auth.LoginActivity;
import com.parttime.enterprise.R;
import com.parttime.enterprise.prefences.AppPrefence;
import com.parttime.enterprise.uicomman.chat.singlechat.SingleChatActivity;
import com.parttime.enterprise.uicomman.enrollcalander.ActivityEnrollCalander;
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.EnrollWorkerTaskDetails;
import com.parttime.enterprise.uicomman.jobdetails.JobDetailsActivity;
import com.parttime.enterprise.uicomman.notifications.NotificationListActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessegingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public AppPrefence appPrefence;
    Intent intent;
    String stdImage, fullName, address, distance, subject, price, rating, stdClass, stgAge, stdId, bookingId,
            duration, bookingTime, currentTime;
    int expiryTime;
    // int bageCount=1;
    int NOTIFICATION_ID = 1;
    private String title;
    private String type, msg, body;
    private String profilePic;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        appPrefence = AppPrefence.INSTANCE;
        appPrefence.initAppPreferences(MyFirebaseMessegingService.this);
      /*  String tempApiToken = appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(), " ");
        if (tempApiToken != null && tempApiToken.trim().length() > 2) {*/
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //{body=4f6hht, data={"date":"2019-09-18T13:33:40+03:00","senderId":"94","receiverId":"4","sender_type":"user","message":"4f6hht","receiver_type":"enterprise"}, type=5, title=New Message, message=4f6hht
            //  getPushNotificationData();
            JSONObject jObj = new JSONObject(remoteMessage.getData());
            try {
                msg = jObj.optString("body");
                type = jObj.optString("type");
                title = jObj.optString("title");
                body = jObj.optString("body");
                if (jObj.has("profilePic")) {
                    profilePic = jObj.optString("profilePic");
                } else {
                    profilePic = jObj.optString("NA");
                }
                if (jObj.has("result")) {
                    String stdDataObj = jObj.getString("result");
                    JSONObject jobjStdData = new JSONObject(stdDataObj);
                    stdImage = jobjStdData.optString("image");
                    fullName = jobjStdData.optString("full_name");
                    address = jobjStdData.optString("address");
                    distance = jobjStdData.optString("distance");
                    subject = jobjStdData.optString("subject");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String tempApiToken = appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(), " ");
            if (tempApiToken != null && tempApiToken.trim().length() > 2) {
                if (type != null && !type.trim().isEmpty()) {
                    if (Integer.valueOf(type) == 5) {
                        // for chat screen
                        intent = new Intent(this, SingleChatActivity.class);
                        intent.putExtra("USERID", jObj.optInt("senderId"));
                        intent.putExtra("IMG", jObj.optString("profilePic"));
                        intent.putExtra("NAME", jObj.optString("name"));
                        intent.putExtra("FLAGS_BACK", 1);
                    } else if (Integer.valueOf(type) == 1) {
                        //  for job Details Screen
                        intent = new Intent(this, JobDetailsActivity.class);
                        intent.putExtra("JOBID", jObj.optString("jobId"));
                        intent.putExtra("FLAGS_BACK", 1);
                    } else if (/*Integer.valueOf(type) == 5 ||*/ Integer.valueOf(type) == 6 || Integer.valueOf(type) == 7) {
                        //  for task Details Screen
                        intent = new Intent(this, EnrollWorkerTaskDetails.class);
                        intent.putExtra("JOBID", jObj.optString("jobId"));
                        intent.putExtra("FLAGS_BACK", 1);
                    } else if (Integer.valueOf(type) == 14) {
                        //  for Group chat
                        intent = new Intent(this, ActivityEnrollCalander.class);
                        intent.putExtra("FLAGS_BACK", 1);
                        /*intent.putExtra("JOBID", jObj.optString("jobId"));
                       */
                    } else {
                        intent = new Intent(this, NotificationListActivity.class);
                    }
                } else {
                    intent = new Intent(this, NotificationListActivity.class);
                }

            } else {
                intent = new Intent(this, LoginActivity.class);
            }
        }

        showNotification(msg, title);
        // }
    }

    // [END receive_message]
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, "Refreshed token: " + s);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        //  Implement this method to send token to your app server.
        appPrefence = AppPrefence.INSTANCE;
        appPrefence.initAppPreferences(MyFirebaseMessegingService.this);
        //SharedPrefrencesManager.getInstance(MyFirebaseMessegingService.this).setString(AppConstant.KEY_DEVICE_TOKEN, token);
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.deviceToken.toString(), token);
    }


    // show notification
    public void showNotification(String title, String alert) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription(getResources().getString(R.string.app_name));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setColor(getResources().getColor(R.color.aqua))
                .setContentIntent(pi)
                .setAutoCancel(true)
                //.setNumber(bageCount)
                .setContentText(alert);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.notify_trans);
            builder.setColor(getResources().getColor(R.color.colorPrimary));
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}