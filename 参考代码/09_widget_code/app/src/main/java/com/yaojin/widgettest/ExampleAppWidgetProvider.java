package com.yaojin.widgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.example_app_widget_provider);//实例化RemoteView,其对应相应的Widget布局
        Intent i=new Intent("com.yaojin.widgettest.CLICK");
        PendingIntent pi=PendingIntent.getBroadcast(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pi=PendingIntent.getActivity(context,0,i,0);
        updateViews.setOnClickPendingIntent(R.id.appwidget_button,pi);//给RemoteView上的Button设置按钮事件
        ComponentName me=new ComponentName(context,ExampleAppWidgetProvider.class);
        appWidgetManager.updateAppWidget(me,updateViews);
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
    public void onReceive(Context context,Intent intent)
    {
        super.onReceive(context,intent);
        if(intent.getAction().equals("com.yaojin.widgettest.CLICK"))
        {
            Toast.makeText(context,"You hit me!!",Toast.LENGTH_LONG).show();
        }
    }
//    CharSequence widgetText = context.getString(R.string.appwidget_text);
//    // Construct the RemoteViews object
//    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_app_widget_provider);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//    // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
}

