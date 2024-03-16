package com.example.appqlsv.ActivityUser.Fragment.Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.example.appqlsv.Models.Filter;
import com.example.appqlsv.R;
import com.google.android.material.slider.RangeSlider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

public class FilterScreen extends AppCompatActivity {
    private FrameLayout backBtn;
    private AppCompatButton confirmButton;
    private RadioButton radio0,radio1,radio2,radio21,radio22;
    private CardView radio3;
    private RadioGroup radioGroup,radioGroup2;
    private  Filter filter;
    private boolean isArrowDown = true;
    private RangeSlider priceRange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);

        anhxaid();


        Intent intent = getIntent();
        String json = intent.getStringExtra("filterdata");
        Gson gson = new Gson();
        Type listType = new TypeToken<Filter>(){}.getType();
        filter = gson.fromJson(json, listType);

        int type = filter.getType();
        switch (type) {
            case 0:
                updateType(R.id.radio0);
                break;
            case 1:
                updateType(R.id.radio1);//đô ăn
                break;
            case 2:
                updateType(R.id.radio2);//đô uống
                break;
        }

        Log.d("CHEKCK SORT", filter.getSort());
        String sort = filter.getSort();

        if (sort.equals("price") || sort.equals("-price")) {
            updateSort(R.id.radio21);
        } else if (sort.equals("rating") || sort.equals("-rating")) {
            updateSort(R.id.radio22);
        }

        boolean wayUp = filter.isWayUp();
        Animation rotation;
        if (wayUp) {
            rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_arrow_up);
        } else {
            rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_arrow_down);
        }

        radio3.startAnimation(rotation);
        rotation.setFillAfter(true);

        if (wayUp) {
            isArrowDown = false;
        } else {
            isArrowDown = true;
        }
        initSlider();
        eventbutton();
    }

    private void eventbutton() {
        confirmButton.setOnClickListener(view -> goBack());
        backBtn.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("filterdata", String.valueOf(filter));
            setResult(Activity.RESULT_CANCELED, resultIntent);
            finish();

        });
        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> updateType(checkedId));
        radioGroup2.setOnCheckedChangeListener((radioGroup, checkedId) -> updateSort(checkedId));
        radio3.setOnClickListener(view -> {
            if (isArrowDown) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_arrow_up);
                view.startAnimation(rotation);
                rotation.setFillAfter(true);
                isArrowDown = false;
                filter.setWayUp(true);
            } else {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_arrow_down);
                view.startAnimation(rotation);
                rotation.setFillAfter(true);
                isArrowDown = true;
                filter.setWayUp(false);
            }
        });

    }
    private void initSlider() {
        priceRange.setValueFrom(0f);
        priceRange.setValueTo(500000f);
        priceRange.getValues().clear();
        priceRange.getValues().addAll(filter.getRange());
        priceRange.setLabelFormatter(value -> {
            DecimalFormat dec = new DecimalFormat("#,###");
            return dec.format(value) + "đ";
        });
        priceRange.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> range = slider.getValues();
            filter.getRange().clear();
            filter.getRange().addAll(range);
        });
        priceRange.setStepSize(1000f);
    }

    private void updateSort(int i) {
        radio21.setBackgroundResource(R.drawable.radio_button_uncheck);
        radio22.setBackgroundResource(R.drawable.radio_button_uncheck);

        RadioButton bt = findViewById(i);
        bt.setBackgroundResource(R.drawable.radio_button_check);

        if (bt.getId() == radio22.getId()) {
            filter.setSort("rating");
        } else if (bt.getId() == radio21.getId()) {
            filter.setSort("price");
        }
    }


    private void updateType(int i) {
        radio0.setBackgroundResource(R.drawable.radio_button_uncheck);
        radio1.setBackgroundResource(R.drawable.radio_button_uncheck);
        radio2.setBackgroundResource(R.drawable.radio_button_uncheck);

        RadioButton bt = findViewById(i);
        bt.setBackgroundResource(R.drawable.radio_button_check);

        if (bt.getId() == radio0.getId()) {
            filter.setType(0);
        } else {
            if (bt.getId() == radio1.getId()) {
                filter.setType(1);
            } else {
                filter.setType(2);
            }
        }
    }

    private void anhxaid() {
        backBtn= findViewById(R.id.backBtn);
        confirmButton= findViewById(R.id.confirmButton);
        radio0= findViewById(R.id.radio0);
        radio1= findViewById(R.id.radio1);
        radio2= findViewById(R.id.radio2);
        radio21= findViewById(R.id.radio21);
        radio22= findViewById(R.id.radio22);
        radio3= findViewById(R.id.radio3);
        radioGroup= findViewById(R.id.radioGroup);
        radioGroup2= findViewById(R.id.radioGroup2);
        priceRange= findViewById(R.id.priceRange);
    }
    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra("filterdata", new Gson().toJson(filter));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }



}