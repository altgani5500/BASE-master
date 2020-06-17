package com.parttime.enterprise.pushnotificaton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.parttime.enterprise.apiclients.ServerConstant;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
            // Create intent with action
            Intent localIntent = new Intent(ServerConstant.NETWORK_CHECK);
            if (isOnline(context)) {
                localIntent.putExtra("data", "online");
                Log.e("Prakhar", "Online Connect Intenet ");
            } else {
                localIntent.putExtra("data", "offline");
                Log.e("Prakhar", "Conectivity Failure !!! ");
            }

            localBroadcastManager.sendBroadcast(localIntent);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}