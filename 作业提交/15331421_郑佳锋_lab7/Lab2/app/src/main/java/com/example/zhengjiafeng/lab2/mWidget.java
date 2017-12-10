package com.example.zhengjiafeng.lab2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.ObjectInputStream;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class mWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);  // Identifies the particular widget...
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
        views.setOnClickPendingIntent(R.id.all_widget, pendIntent);
        //appWidgetManager.updateAppWidget(appId, views);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
   @Override
    public  void onReceive(Context context , Intent intent){
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();
       if(intent.getAction().equals("mywidget"))
       {
           RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
           views.setTextViewText(R.id.appwidget_text, intent.getStringExtra("name") + "仅售"+intent.getStringExtra("price") + "!");
           views.setImageViewResource(R.id.appwidget_image,Integer.parseInt(intent.getStringExtra("picture")));
           Intent newIntent = new Intent(context, NewActivity.class);
           newIntent.putExtra("passname",intent.getStringExtra("name"));
           newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, new ComponentName(context, mWidget.class));  // Identifies the particular widget...
           newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           PendingIntent pendIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
           //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
           views.setOnClickPendingIntent(R.id.all_widget, pendIntent);
           appWidgetManager.updateAppWidget(new ComponentName(context, mWidget.class), views);
       }
       if(intent.getAction().equals("widgetShop")){
           RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
           views.setTextViewText(R.id.appwidget_text, intent.getStringExtra("name") + "已添加到购物车");
           views.setImageViewResource(R.id.appwidget_image,Integer.parseInt(intent.getStringExtra("picture")));
           Intent newIntent = new Intent(context, MainActivity.class);
           newIntent.putExtra("widget","widget");
           newIntent.putExtra("passname",intent.getStringExtra("name"));
           newIntent.setAction(Intent.ACTION_MAIN);
           newIntent.addCategory(Intent.CATEGORY_LAUNCHER);
           newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
           newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, new ComponentName(context, mWidget.class));  // Identifies the particular widget...
           PendingIntent pendIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
           //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
           views.setOnClickPendingIntent(R.id.all_widget, pendIntent);
           appWidgetManager.updateAppWidget(new ComponentName(context, mWidget.class), views);
       }
    }
}

