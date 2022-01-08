package com.example.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.*;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import com.coderz.f1.customdialogs.colordialog.ColorDialog;

public class WidgetConfig extends AppCompatActivity {
    private TextView colorValue;
    private TextView color;
    private LinearLayout linear1;
    private EditText editText;
    public static String PREFS = "PREFS";
    public static String KEY_TEXT = "TEXT";
    public static String COLOR_KEY = "COLOR";
    private int id = AppWidgetManager.INVALID_APPWIDGET_ID;
    private @ColorInt int appWidgetColor = Color.WHITE;
    private String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config);

        color = findViewById(R.id.color);
        colorValue = findViewById(R.id.colorValue);
        linear1 = findViewById(R.id.linear1);
        editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        LinearLayout linear2 = findViewById(R.id.linear2);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if(extras != null){
            id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , id);
        setResult(RESULT_CANCELED , result);
        if(id == AppWidgetManager.INVALID_APPWIDGET_ID) finish();

        colorValue.setText(intTohex(appWidgetColor));

        GradientDrawable ui = new GradientDrawable();
        ui.setColor(Color.WHITE);
        ui.setCornerRadius(getDensity() * 16);
        linear2.setElevation(getDensity() * 4);
        linear2.setBackground(ui);

        setColorButton(Color.WHITE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = editText.getText().toString();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetConfig.this);
                Intent intent = new Intent(WidgetConfig.this , MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(WidgetConfig.this , 0 , intent , 0);

                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName() , R.layout.widget_layout);

                views.setOnClickPendingIntent(R.id.text , pendingIntent);
                views.setCharSequence(R.id.text , "setText" , text);
                views.setInt(R.id.linear1 , "setBackgroundColor" , appWidgetColor);
                views.setTextColor(R.id.text , isColorDark(appWidgetColor) ? Color.WHITE : Color.BLACK);

                appWidgetManager.updateAppWidget(id , views);

                SharedPreferences pref = getSharedPreferences(PREFS , MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(KEY_TEXT + id , text);
                editor.putInt(COLOR_KEY + id , appWidgetColor);
                editor.apply();

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , id);
                setResult(RESULT_OK);
                finish();
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Color piker by TheRandomCrafter83
                ColorDialog cd = new ColorDialog(WidgetConfig.this, new ColorDialog.DialogResponseListener() {
                    @Override
                    public void onOkClicked(int c) {
                        appWidgetColor = c;
                        colorValue.setText(intTohex(c));
                        setColorButton(c);
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
                cd.setTitle("Choose a color");
                cd.setTabIndex(ColorDialog.TabIndex.PALETTE);
                cd.setInitialColor(Color.RED);
                cd.setMargins(8);
                cd.setBackgroundColor(Color.GRAY);
                cd.setTextColor(Color.YELLOW);
                cd.showDialog();
            }
        });

    }

    private boolean isColorDark(@ColorInt int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5) return false;
        else return true;
    }
    private void setColorButton(@ColorInt int c){
        GradientDrawable ui2 = new GradientDrawable();
        ui2.setColor(c);
        ui2.setCornerRadius(getDensity() * 5);
        ui2.setStroke((int)(getDensity() * 2), Color.BLACK);
        color.setBackground(ui2);
    }
    private float getDensity(){
        return getApplicationContext().getResources().getDisplayMetrics().density;
    }
    private String intTohex(@ColorInt int color){
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
