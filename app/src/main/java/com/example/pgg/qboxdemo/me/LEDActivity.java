package com.example.pgg.qboxdemo.me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.LEDRecommendColor;
import com.example.pgg.qboxdemo.model.entities.LEDRecommendColorManager;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pgg on 2018/5/3.
 */

public class LEDActivity extends BaseCommonActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.toolbar_led)
    Toolbar mToolbarLed;
    @BindView(R.id.content_led)
    TextInputEditText mContentLed;
    @BindView(R.id.fontcolor_btn_led)
    AppCompatButton mFontcolorBtnLed;
    @BindView(R.id.bgcolor_btn_led)
    AppCompatButton mBgcolorBtnLed;
    @BindView(R.id.recommendcolor_btn_led)
    AppCompatButton mRecommendcolorBtnLed;
    @BindView(R.id.preview_led)
    AppCompatTextView mPreviewLed;
    @BindView(R.id.single_radiobtn_led)
    AppCompatRadioButton mSingleRadiobtnLed;
    @BindView(R.id.single_toss_radiobtn_led)
    AppCompatRadioButton mSingleTossBtnLed;
    @BindView(R.id.rollspeed_seekbar_led)
    AppCompatSeekBar mRollspeedSeekbarLed;
    @BindView(R.id.adaptive_radiobtn_led)
    AppCompatRadioButton mAdaptiveRadiobtnLed;
    @BindView(R.id.magic_radiobtn_led)
    AppCompatRadioButton mMagicRadiobtnLed;
    @BindView(R.id.showstyle_radiogroup_led)
    RadioGroup mShowstyleRadiogroupLed;
    @BindView(R.id.start_btn_led)
    AppCompatButton mStartBtnLed;
    @BindView(R.id.reverseColor_led)
    ImageView mReverseColorLed;
    @BindView(R.id.spinner_magicstyle_led)
    AppCompatSpinner mCompatSpinner;
    @BindView(R.id.tv_lines_led)
    TextView mLinesTextView;
    @BindView(R.id.lines_seekbar_led)
    AppCompatSeekBar mlinesSeekbar;

    public int mBgColor;
    public int mFontColor;
    public int mProgress;
    public int mShowStyle;
    public int mMagicStyle;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mMagicStyle=i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        mShowStyle=i;
        if (i==R.id.single_radiobtn_led||i==R.id.single_toss_radiobtn_led){
            if (!mRollspeedSeekbarLed.isEnabled()){
                mRollspeedSeekbarLed.setEnabled(true);
            }
        }else {
            if (mRollspeedSeekbarLed.isEnabled()){
                mRollspeedSeekbarLed.setEnabled(false);
            }
        }
        if (i!=R.id.magic_radiobtn_led){
            if (mCompatSpinner.isEnabled()){
                mCompatSpinner.setEnabled(false);
            }
        }else {
            if (!mCompatSpinner.isEnabled()){
                mCompatSpinner.setEnabled(true);
            }
        }

        if (i == R.id.adaptive_radiobtn_led) {
            if (!mlinesSeekbar.isEnabled()) {
                mlinesSeekbar.setEnabled(true);
            }
        }else {
            if (mlinesSeekbar.isEnabled()) {
                mlinesSeekbar.setEnabled(false);
            }
        }
    }

    /**
     * 对滚动速度进度条的监听
     * @param seekBar
     * @param i
     * @param b
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mProgress=i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_led);
    }

    @Override
    public void initView() {
        initToolbar();
        initViewEvent();
    }

    private void initViewEvent() {
        mShowstyleRadiogroupLed.check(R.id.single_radiobtn_led);
        mShowStyle = R.id.single_radiobtn_led;
        if (!mAdaptiveRadiobtnLed.isChecked()) {
            mlinesSeekbar.setEnabled(false);
        }
        mCompatSpinner.setSelection(0);
        mMagicStyle = 0;
        mRollspeedSeekbarLed.setOnSeekBarChangeListener(this);
        mShowstyleRadiogroupLed.setOnCheckedChangeListener(this);
        mCompatSpinner.setOnItemSelectedListener(this);
        mlinesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mLinesTextView.setText(String.valueOf(i + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initToolbar() {
        mToolbarLed.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbarLed.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.fontcolor_btn_led, R.id.bgcolor_btn_led, R.id.start_btn_led, R.id.recommendcolor_btn_led, R.id.reverseColor_led})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fontcolor_btn_led:
                //字体颜色按钮
                showColorPicker(true);
                break;
            case R.id.bgcolor_btn_led:
                //背景颜色
                showColorPicker(false);
                break;
            case R.id.recommendcolor_btn_led:
                //推荐颜色
                showRecommendColorDialog();
                break;
            case R.id.reverseColor_led:
                //重置颜色
                if (mBgColor != 0 && mFontColor != 0) {
                    int bgColor = mBgColor;
                    changePreviewBgColor(mFontColor);
                    changePreviewFontColor(bgColor);
                }
                break;
            case R.id.start_btn_led:
                //开始按钮
                startShowLed();
                break;
        }
    }

    private void startShowLed() {
        Editable mContentLedText = mContentLed.getText();
        if (TextUtils.isEmpty(mContentLedText)) {
            mContentLed.setError("请填写要显示的内容");
            return;
        }
        if (mFontColor == 0) {
            mFontColor = mPreviewLed.getCurrentTextColor();
        }
        if (mBgColor == 0) {
            mBgColor = 0xffffffff;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constant.LED_CONTENT, mContentLedText.toString());
        bundle.putInt(Constant.LED_BG_COLOR, mBgColor);
        bundle.putInt(Constant.LED_FONT_COLOR, mFontColor);

        switch (mShowStyle) {
            case R.id.single_radiobtn_led:
                bundle.putInt(Constant.LED_ROLL_SPEED, mProgress);
                bundle.putBoolean(Constant.LED_SINGLE_ISH, true);
                startSingleLED(bundle);
                break;
            case R.id.single_toss_radiobtn_led:
                bundle.putInt(Constant.LED_ROLL_SPEED, mProgress);
                bundle.putBoolean(Constant.LED_SINGLE_ISH, false);
                startSingleLED(bundle);
                break;
            case R.id.adaptive_radiobtn_led:
                bundle.putString(Constant.LED_LINES, mLinesTextView.getText().toString());
                startAdaptiveLED(bundle);
                break;
            case R.id.magic_radiobtn_led:
                bundle.putString(Constant.LED_MAGIC_STYLE, getResources().getStringArray(R.array.arrays_led_magicstyle)[mMagicStyle]);
                startMagicLED(bundle);
                break;
            default:
                break;
        }
    }

    private void startMagicLED(Bundle bundle) {
        if(!mContentLed.getText().toString().contains("#")){
            mContentLed.setError("至少输入两句话，用'#'分隔");
            return;
        }
        startActivity(new Intent(this,LEDMagicActivity.class).putExtras(bundle));
    }

    private void startAdaptiveLED(Bundle bundle) {
        startActivity(new Intent(this,LEDAutoActivity.class).putExtras(bundle));
    }

    private void startSingleLED(Bundle bundle) {
        startActivity(new Intent(this,LEDSingleActivity.class).putExtras(bundle));
    }

    /**
     * 改变字体颜色
     * @param bgColor
     */
    private void changePreviewFontColor(int bgColor) {
        mPreviewLed.setTextColor(bgColor);
        mFontColor = bgColor;
    }

    /**
     * 改变背景色
     * @param mFontColor
     */
    private void changePreviewBgColor(int mFontColor) {
        mPreviewLed.setBackgroundColor(mFontColor);
        mBgColor = mFontColor;
    }

    /**
     * 显示推荐颜色
     */
    private void showRecommendColorDialog() {
        final List<LEDRecommendColor> ledRecommendColors= LEDRecommendColorManager.getInstance().getLEDRecommendColors();
        new AlertDialog.Builder(this)
                .setTitle("选择颜色组合")
                .setItems(LEDRecommendColorManager.colorName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        LEDRecommendColor ledRecommendColor = ledRecommendColors.get(i);
                        changePreviewBgColor(ledRecommendColor.getBackgroundColor());
                        changePreviewFontColor(ledRecommendColor.getFontColor());
                    }
                }).show();
    }

    /**
     * 显示颜色选择器
     * @param isFontColor
     */
    private void showColorPicker(final boolean isFontColor) {
        ColorPickerDialogBuilder.with(this)
                .setTitle("选择颜色")
                .initialColor(0xffff0000)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("确定", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                        if (isFontColor){
                            changePreviewFontColor(i);
                        }else {
                            changePreviewBgColor(i);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).build().show();
    }


}
