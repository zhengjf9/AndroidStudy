package com.example.swee1.pptdemo;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by swee1 on 2017/10/29.
 */

public class MyService extends Service {
    private IBinder mBinder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // 必须实现的接口
        return mBinder;
    }

    public class MyBinder extends Binder
    {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 101:
                    Log.d("debug","It's a log from service...");
                    // downLoadPic();
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
