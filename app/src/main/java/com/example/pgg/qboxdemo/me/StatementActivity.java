package com.example.pgg.qboxdemo.me;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;

import butterknife.BindView;

public class StatementActivity extends BaseCommonActivity {

    @BindView(R.id.toolbar_statement)
    Toolbar mToolbarStatement;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_statement);

    }

    @Override
    public void initView() {
        initToolbar();
    }

    @Override
    public void initPresenter() {

    }

    private void initToolbar() {
        setSupportActionBar(mToolbarStatement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
