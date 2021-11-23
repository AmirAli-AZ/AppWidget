package com.example.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;
import androidx.annotation.ColorInt;

import static com.example.appwidget.widgetConfig.PREFS;
import static com.example.appwidget.widgetConfig.KEY_TEXT;
import static com.example.appwidget.widgetConfig.COLOR_KEY;

public class appWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int id : appWidgetIds){
            // setting intent for main activity
            Intent intent = new Intent(context , MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 , intent , 0 );
            SharedPreferences pref = context.getSharedPreferences(PREFS , context.MODE_PRIVATE);
            //get values
            String text = pref.getString(KEY_TEXT + id , "Hi");
            @ColorInt int color = pref.getInt(COLOR_KEY + id , Color.WHITE);

            RemoteViews views = new RemoteViews(context.getPackageName() , R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.text , pendingIntent);
            // set Text
            views.setCharSequence(R.id.text , "setText" , text);
            // set background color
            views.setInt(R.id.linear1 , "setBackgroundColor" , color);
            // set text color
            views.setTextColor(R.id.text , isColorDark(color) ? Color.WHITE : Color.BLACK);

            // update app widget
            appWidgetManager.updateAppWidget(id , views);
        }
    }
    public boolean isColorDark(@ColorInt int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5) return false;
        else return true;
    }
}
