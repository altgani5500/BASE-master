package com.parttime.enterprise.helpers.myglides;

import android.content.Context;

import com.parttime.enterprise.apiclients.SSLTrust;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;



import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            OkHttpClient okHttpClient =  SSLTrust.getUnsafeOkHttpClient().build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*public Picasso getPicasoObject(Context context){

        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        picassoBuilder.downloader(
                new OkHttp3Downloader(
                        UnsafeOkHttpClient.getUnsafeOkHttpClient()
                )
        );
        Picasso picasso = picassoBuilder.build();
        return picasso;
    }*/
}
