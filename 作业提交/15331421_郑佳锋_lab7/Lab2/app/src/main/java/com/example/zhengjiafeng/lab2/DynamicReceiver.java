package com.example.zhengjiafeng.lab2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by zhengjiafeng on 2017/10/28.
 */

class DynamicReceiver extends BroadcastReceiver{
      @Override
       public void onReceive(Context context, Intent intent) {
          Notification.Builder builder = new Notification.Builder(context);
          Bitmap bm = BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(intent.getStringExtra("picture")));
          Intent appIntent=null;
          appIntent = new Intent(context,MainActivity.class);
          appIntent.putExtra("open","open");
          appIntent.setAction(Intent.ACTION_MAIN);
          appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
          appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
          PendingIntent contentIntent =PendingIntent.getActivity(context, 0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
          builder.setContentTitle("马上下单")
                  .setContentText(intent.getStringExtra("name") +"已添加到购物车")
                  .setSmallIcon(Integer.parseInt(intent.getStringExtra("picture")))
                  .setLargeIcon(bm)
                  .setContentIntent(contentIntent)
                  .setAutoCancel(true);
          NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
          Notification notify = builder.build();
          manager.notify(0,notify);
             }
   }
