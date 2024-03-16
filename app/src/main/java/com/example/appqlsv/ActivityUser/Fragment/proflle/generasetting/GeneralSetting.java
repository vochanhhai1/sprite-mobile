package com.example.appqlsv.ActivityUser.Fragment.proflle.generasetting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.appqlsv.R;
import com.example.appqlsv.util.LanguagePrefs;
import com.example.appqlsv.util.LocaleManager;
import com.example.appqlsv.util.NightModePrefs;
import com.google.android.material.card.MaterialCardView;

public class GeneralSetting extends AppCompatActivity {
    private FrameLayout buttonBack;
    private MaterialCardView languageBtn, darkMode, orderNotify, messNotify;
    private SwitchCompat switchBtn, switchMess, switchOrder;
    private NightModePrefs nightModePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_setting);
        nightModePrefs = new NightModePrefs(getApplicationContext());
        boolean systemNightMode = nightModePrefs.isSystemNightModeEnabled(this);

        nightModePrefs.setNightModeEnabled(systemNightMode);
        intView();
        eventButton();
        setSwitchButton();
    }

    private void eventButton() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNightMode();
            }
        });
        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildLanguageDialog();
            }
        });
    }

    private void intView() {
        buttonBack = findViewById(R.id.buttonBack);
        languageBtn = findViewById(R.id.languageBtn);
        darkMode = findViewById(R.id.darkMode);
        orderNotify = findViewById(R.id.orderNotify);
        messNotify = findViewById(R.id.messNotify);
        switchBtn = findViewById(R.id.switchBtn);
        switchMess = findViewById(R.id.switchMess);
        switchOrder = findViewById(R.id.switchOrder);
    }

    private void setSwitchButton() {
        switchBtn.setChecked(nightModePrefs.isNightModeEnabled());
    }

    private void changeNightMode() {
        nightModePrefs.isSystemNightModeEnabled(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Warning));
        builder.setMessage(getResources().getString(R.string.Restartapp));
        builder.setPositiveButton(getResources().getString(R.string.YES), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isNightMode = switchBtn.isChecked();
                if (isNightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                nightModePrefs.setNightModeEnabled(isNightMode);


                restartApp();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.NO), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setSwitchButton();
            }
        });

        AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            }
        });

        alert.show();
    }
    private void buildLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Chooseyourlanguage));

        String[] status = {"English", "Tiếng Việt"};
        builder.setItems(status, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setLanguage("en");
                        break;
                    case 1:
                        setLanguage("vi");
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setLanguage(String languageCode) {
        LanguagePrefs.setLang(getApplicationContext(), languageCode);
        LocaleManager.setLocale(getResources(),languageCode);
        restartApp();
    }
    private void restartApp() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
