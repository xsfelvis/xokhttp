package xsf.xokhttp;

import xsf.xokhttp.base.BaseActvity;

public class MVPActivity extends BaseActvity {


    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_mvp;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle("MVP");

    }
}
