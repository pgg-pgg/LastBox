package com.example.pgg.qboxdemo.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pgg on 2018/5/13.
 */

public class LEDMagicActivity extends BaseCommonActivity {

    Timer timer = null;
    private int countLog = 0;
    String[] mStrings;
    private HTextView mContentView;
    private static final boolean AUTO_HIDE = true;
    private boolean isStart = false;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String string = mStrings[countLog % mStrings.length];
                if (TextUtils.isEmpty(string)){
                    string = getString(R.string.app_name);
                }
                mContentView.animateText(string);

            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    public View mView;

    private void delayedHide(int autoHideDelayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, autoHideDelayMillis);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_ledmagic);
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String ledContent = extras.getString(Constant.LED_CONTENT);
        int ledBgcolor = extras.getInt(Constant.LED_BG_COLOR);
        int ledFontcolor = extras.getInt(Constant.LED_FONT_COLOR);
        String magicStyle = extras.getString(Constant.LED_MAGIC_STYLE);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView =  findViewById(R.id.fullscreen_content);

        if (!TextUtils.isEmpty(ledContent)) {
            mStrings = ledContent.split("#");
        }

        if (ledFontcolor != 0) {
            mContentView.setTextColor(ledFontcolor);
        }
        if (ledBgcolor != 0) {
            mContentView.setBackgroundColor(ledBgcolor);
        }
        String[] stringArray = getResources().getStringArray(R.array.arrays_led_magicstyle);
        Map<String,HTextViewType> viewTypeMap = new HashMap<String,HTextViewType>();
        viewTypeMap.put("scale",HTextViewType.SCALE);
        viewTypeMap.put("evaporate",HTextViewType.EVAPORATE);
        viewTypeMap.put("fall",HTextViewType.FALL);
        viewTypeMap.put("pixelate",HTextViewType.PIXELATE);
        viewTypeMap.put("anvil",HTextViewType.ANVIL);
        viewTypeMap.put("sparkle",HTextViewType.SPARKLE);
        viewTypeMap.put("line",HTextViewType.LINE);
        viewTypeMap.put("typer",HTextViewType.TYPER);
        viewTypeMap.put("rainbow",HTextViewType.RAINBOW);

        if (!TextUtils.isEmpty(magicStyle)) {
            mContentView.setAnimateType(viewTypeMap.get(magicStyle));
        }

        mContentView.animateText(TextUtils.isEmpty(mStrings[0])?getString(R.string.app_name):mStrings[0]);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        mView = findViewById(R.id.dummy_button);
        mView.setOnTouchListener(mDelayHideTouchListener);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = !isStart;
                if (isStart) {
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new MyTask(), 1, 3000);
                }else {
                    if (timer != null) {
                        timer.cancel();
                    }
                }
            }
        });

        ((AppCompatSeekBar) findViewById(R.id.seekbar_ledmagic))
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mContentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1*progress+50);
                        mContentView.reset(mContentView.getText());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    public void initPresenter() {

    }

    private class MyTask extends TimerTask{
        @Override
        public void run() {
            countLog++;
            Message message=new Message();
            message.what=1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
