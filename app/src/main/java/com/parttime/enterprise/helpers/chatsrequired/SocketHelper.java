package com.parttime.enterprise.helpers.chatsrequired;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.parttime.enterprise.apiclients.ServerConstant;
import com.parttime.enterprise.R;
import com.parttime.enterprise.helpers.Utilities;
import com.parttime.enterprise.prefences.AppPrefence;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.OkHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static io.socket.client.Socket.*;


/**
 * Created by Prakhar
 */
public class SocketHelper {

    private static final String NOTIFICATION_TAG = "NewMessage";
    public static SocketHelper socketHelper;
    public static String GET_CHAT_LIST_ON = "getChatList";
    public static String RECEIVE_MESSAGE_ON = "chatList";
    public static String SEND_MESSAGE = "sendMessage";
    public static String SAVE_SOCKET = "save_socket";
    public static String REDIRECT_SCREEN = "screen_redirect";
    public static String RECEIVE_MESSAGE = "oneMessage";

    // group chat variables
    public static String GROUP_SEND_MESSAGE = "sendGroupMessage";
    public static String GROUP_CHAT_MESSAGES_EMIT="getGroupMessage";



    /*Socket event method from sever*/
    private final String TAG = "SOCKET";
    /*for trust Software*/
    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }};
    public Context context;
    JSONObject data = new JSONObject();
    private Handler mHandler;
    private int icon, iconTransparent;
    private Bitmap largeIcon;
    private int notificationID = (int) System.currentTimeMillis();
    private Socket mSocket;
    private AppPrefence appPrefence;
    private String title = "";
    private Ringtone r;
    //  for ring tone off
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utilities.print("====Broadcast");
            offRingTone(); // Do what you want here
            context.unregisterReceiver(this);
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Utilities.print("socket===onDisconnect=====");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10);
        }
    };
    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Utilities.print("socket===onError=====" + args[0]);
                        Utilities.print("socket===onError===1==" + args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10);

        }
    };
    private Emitter.Listener getChatListOn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Utilities.print("socket===getChatListOn=====" + args[0]);
                        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                        // Create intent with action
                        Intent localIntent = new Intent(ServerConstant.SOCKET_UPDATE_CHAT_LIST_UPDATE);
                        localIntent.putExtra("data", args[0].toString());
                        localBroadcastManager.sendBroadcast(localIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10);

        }
    };
    private Emitter.Listener getReceiveOn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            mHandler.postDelayed(() -> {
                try {
                    Utilities.print("socket===getReceiveOn=====" + args[0]);
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                    // Create intent with action
                    Intent localIntent = new Intent(RECEIVE_MESSAGE_ON);
                    localIntent.putExtra("data", args[0].toString());
                    localBroadcastManager.sendBroadcast(localIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 10);

        }
    };






    private Emitter.Listener getReceiveSendON = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            mHandler.postDelayed(() -> {
                try {
                    Utilities.print("socket===getReceiveOnSend=====" + args[0]);
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                    // Create intent with action
                    Intent localIntent = new Intent(RECEIVE_MESSAGE);
                    localIntent.putExtra("data", args[0].toString());
                    localBroadcastManager.sendBroadcast(localIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 10);

        }
    };
    private long elapsedTime = 0;
    private Emitter.Listener joinCallListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 100);

        }
    };
    private Emitter.Listener onEvenConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Utilities.print("socket===onEvenConnect=====");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10);
        }
    };
    private Emitter.Listener onEvenConnecting = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
//                        CommonUtill.print("socket onEvenConnecting====="+args[0]);
                        Utilities.print("socket===onEvenConnecting===1==" + args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10);
        }
    };

    /**
     * Constructor
     *
     * @param context
     */
    private SocketHelper(Context context) {
        {
            Log.e(TAG, " INITIATE");
            this.context = context;
            mHandler = new Handler(this.context.getMainLooper());
            appPrefence = AppPrefence.INSTANCE;
            appPrefence.initAppPreferences(context);
            try {
                HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                SSLContext mySSLContext = SSLContext.getInstance("TLS");
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }

                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType)  {
                    }
                }};
                mySSLContext.init(null, trustAllCerts, new java.security.SecureRandom());
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .hostnameVerifier(myHostnameVerifier)

                        .sslSocketFactory(mySSLContext.getSocketFactory(), new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType)  {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        })
                        .build();


                // HttpsURLConnection.setDefaultHostnameVerifier(myHostnameVerifier);
                IO.Options options = new IO.Options();
                options.webSocketFactory = okHttpClient;
                //options.secure = true;
                options.transports = new String[]{WebSocket.NAME};
                //options.reconnection = true;
                //options.forceNew = true;
//                options.callFactory = okHttpClient;
//                options.webSocketFactory = okHttpClient;
//                options.query = "authtoken=" + AppSharedPrefrence.getString(this, AppSharedPrefrence
//                        .AUTH_TOKEN)
//                        + "&Lang=" + (AppSharedPrefrence.
//                        getString(this, AppSharedPrefrence.LANGUAGE).
//                        equalsIgnoreCase(AppConstant.ENGLISH)
//                        ? AppConstant.ENGLISH : AppConstant.ARABIC);


                options.reconnection = true;
                options.reconnectionDelay = 1000;
                options.timeout = 10000;
                options.query = "s_id=" + Build.SERIAL;
                mSocket = IO.socket(ServerConstant.SOCKET_BASE_URL + ":8686", options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to get the instance of socket class
     *
     * @param context
     * @return
     */
    public static SocketHelper getInstance(Context context) {
        if (socketHelper == null) {
            socketHelper = new SocketHelper(context);
        }
        Utilities.print("===socket==socketHelper=" + socketHelper);
        return socketHelper;
    }

    private void createIcons(Context mActivity) {
        title = mActivity.getString(R.string.app_name);
        icon = R.mipmap.ic_launcher;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon = R.mipmap.ic_launcher;
        } else {
            icon = R.mipmap.ic_launcher;
        }

        iconTransparent = R.mipmap.ic_launcher;
        largeIcon = BitmapFactory.decodeResource(mActivity.getResources(), iconTransparent);
    }

    /**
     * Method to get the boolean whether the socket is connected or not
     *
     * @return
     */
    public boolean isConnected() {
        return mSocket.connected();
    }

    /**
     * Method to start socket connection
     */
    public void startConnection() {
        Utilities.print("===socket=startConnection=");
//        CommonUtill.print("===socket=isConnected="+isConnected());

        //all on Methods
        //mSocket.on(receiveMessage + login_prference.getString(GlobalStaticData.USER_ID, ""), onNewMessage);
        // here we event create
        mSocket.on(EVENT_CONNECTING, onEvenConnecting);
        mSocket.on(EVENT_CONNECT, onEvenConnect);
        mSocket.on(EVENT_DISCONNECT, onDisconnect);
        mSocket.on(EVENT_CONNECT_ERROR, onError);
        mSocket.on(GET_CHAT_LIST_ON, getChatListOn);
        mSocket.on(RECEIVE_MESSAGE_ON, getReceiveOn);
        mSocket.on(RECEIVE_MESSAGE, getReceiveSendON);

        mSocket.connect();
        Join();
    }

    /**
     * Method to stop the socket connection
     */
    public void stopConnection() {
//        mSocket.off(receiveMessage + login_prference.getString(GlobalStaticData.USER_ID, ""), onNewMessage);
        mSocket.disconnect();
    }

    /**
     * Joining the socket by user
     * This is for save socket or Join With Socket
     */
    private void Join() {
        //Add user
        JSONObject addUserjsonObject = new JSONObject();
        try {
            addUserjsonObject.accumulate("userId", appPrefence.getString(AppPrefence.SharedPreferncesKeys.enterpriseId.toString(), ""));
            addUserjsonObject.accumulate("type", "enterprise");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(SAVE_SOCKET, addUserjsonObject, new Ack() {
            @Override
            public void call(Object... args) {
//                final JSONObject data = (JSONObject) args[0];
                Utilities.print("===socket=inside=join==data=");
            }
        });
        Utilities.print("===socket=isConnected=" + isConnected());


    }

    public void getChatListEmit(String s_id, int r_id) {
        //Add user
        JSONObject getHistory = new JSONObject();
        try {
            int a = Integer.parseInt(s_id);
            getHistory.accumulate("senderId", a);
            getHistory.accumulate("receiverId", r_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(GET_CHAT_LIST_ON, getHistory, (Ack) args -> {
//                final JSONObject data = (JSONObject) args[0];
            Utilities.print("===socket=inside=join==data=");
        });
    }

    public void getGROUPChatListEmit(String s_id) {
        //Add user
        JSONObject getHistory = new JSONObject();
        try {
            int a = Integer.parseInt(s_id);
            getHistory.accumulate("enterpriseId", a);
           // getHistory.accumulate("receiverId", r_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(GROUP_CHAT_MESSAGES_EMIT, getHistory, (Ack) args -> {
//                final JSONObject data = (JSONObject) args[0];
            Utilities.print("===socket=inside=join==data=");
        });
    }
    // for multiple call..................

    public void sendMsg(final Object... args) {
        mSocket.emit(SEND_MESSAGE, args);
    }

    /*Group Chat message*/
    public void groupSendMsg(final Object... args) {
        mSocket.emit(GROUP_SEND_MESSAGE, args);
    }


    ///esrfghjkml,

    public void sendNotification() {
        //Get an instance of NotificationManager//
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText("Another call is coming..");

        // for notification sound.......
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(notificationSound);

        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//

//        NotificationManager.notify().

        mNotificationManager.notify(001, mBuilder.build());
    }

    private void createNotification(Intent notificationIntent, Context mActivity, JSONObject remoteMessage) {

        createIcons(context);
        notificationIntent.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        Intent intentdelNot = new Intent("delNotification");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentdelNot, 0);
        context.registerReceiver(receiver, new IntentFilter("delNotification"));
       /* Notification n = new Notification.Builder(context).
                setContentText(text).
                setDeleteIntent(pendintIntent).
                build();
        NotificationManager.notify(0, n);
*/
//        ghjkl;
        PendingIntent intent = PendingIntent.getActivity(mActivity, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mActivity, "" + notificationID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(iconTransparent);
        } else {
            notificationBuilder.setSmallIcon(icon);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setSmallIcon(iconTransparent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(iconTransparent);
            notificationBuilder.setLargeIcon(largeIcon);
        } else {
            notificationBuilder.setSmallIcon(icon);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder.setContentTitle(title);
        }
        Utilities.print("MakeNotification==" + remoteMessage);

        notificationBuilder.setContentText("Please take call.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent)
                .setDeleteIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Please take call."))

                .setAutoCancel(true);

        // play ringtone on call.............
        playRingTone();
//        NotificationCompat.InboxStyle mInboxStyle = new NotificationCompat.InboxStyle();
//        mInboxStyle.addLine(remoteMessage.getData().get("message"))
//                .setSummaryText("Booking")
//                .setSummaryText(title)
//                .setBigContentTitle(title);
//        notificationBuilder.setStyle(mInboxStyle);

//            notify(mActivity, notificationBuilder.build());

    }

    private void notify(final Context context, final Notification notification) {

        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, notificationID, notification);

        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }


    }

    public void offRingTone() {
        if (r.isPlaying())
            r.stop();
    }

    public void playRingTone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(context, notification);
        r.play();

    }
}



