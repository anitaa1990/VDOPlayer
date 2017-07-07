package com.an.videoplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.an.videoplayer.R;

public class BaseActivity extends AppCompatActivity {

    protected TextView selectVideoBtn;
    protected TextView toolbarTitle;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        selectVideoBtn = (TextView) toolbar.findViewById(R.id.select_btn);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
    }
}
