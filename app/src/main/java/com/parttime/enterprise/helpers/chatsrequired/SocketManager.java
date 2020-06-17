package com.parttime.enterprise.helpers.chatsrequired;

import android.os.Handler;
import android.os.Looper;
import com.parttime.enterprise.apiclients.ServerConstant;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by Techugo on 9/12/2017.
 */

public class SocketManager {
    public static Socket socket;
    static String socketUrl = ServerConstant.SOCKET_BASE_URL + ":8686";
    static String socketId;

    /*
    Method to Initialize and Add Listener on Socket
     */
    public static void initialize(final SocketListener socketListener) {
        try {
            socket = IO.socket(socketUrl);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            socketListener.onConnected();
                            socketId = socket.id();
                        }
                    });
                }

            }).on("userConnectStatus", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (args != null && args.length > 0) {

                            }
                        }
                    });

                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    if (args != null && args.length > 0) {
                        /*JSONArray jsonArray = (JSONArray) args[0];
                        String msg = (String) args[0];
                        if (msg != null && msg.length() > 0) {
                            socketListener.onDisConnected();
                        }*/
                    }
                }

            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    Method to connect Socket
     */
    public static void connect() {
        if (!socket.connected())
            socket.connect();
    }

    /*
    Method to send message via Socket on a Key
     */
    public static void sendMsg(String key, final Object... args) {
        if (socket.connected()) {
            socket.emit(key, args);
        }
    }

    /*
    Add Listener to Socket
     */
    public static void addListener(String key, final SocketMessageListener socketMessageListener) {

        socket.on(key, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (args != null && args.length > 0) {
                            socketMessageListener.onMessage(args);
                        }
                    }
                });

            }

        });
    }

    /*
    Disconnect Socket
     */
    public static void disConnect() {
        if (socket.connected())
            socket.disconnect();
    }

    public static Socket getInstance() {
        return socket;
    }

    public static boolean isConnected() {
        return socket.connected();
    }

    /*
    Interface to Handle Connect and Disconnect events of Socket
     */
    public interface SocketListener {
        void onConnected();

        void onDisConnected();

    }

    /*
   Interface to Handle Message event of Socket
    */
    public interface SocketMessageListener {
        void onMessage(Object... args);
    }

}