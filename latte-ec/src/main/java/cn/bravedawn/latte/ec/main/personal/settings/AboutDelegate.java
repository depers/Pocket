package cn.bravedawn.latte.ec.main.personal.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;

/**
 * Created by 冯晓 on 2017/10/1.
 */

public class AboutDelegate extends LatteDelegate{

    @BindView(R2.id.tv_info)
    AppCompatTextView mTextView = null;

    @BindView(R2.id.about_toolBar_title)
    AppCompatTextView mToolTitle = null;

    @BindView(R2.id.about_toolbar)
    Toolbar mToolbar = null;

    @OnClick(R2.id.icon_about_back)
    void onClickAbout(){
        getSupportDelegate().pop();
    }

    private String URL = null;

    public static AboutDelegate create(String url){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        AboutDelegate delegate = new AboutDelegate();
        delegate.setArguments(bundle);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        URL = bundle.getString("url");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_about;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mToolbar.setContentInsetsAbsolute(0, 0);
        if(URL.equals(AboutUrl.ABOUT)){
            mToolTitle.setText("关于");
        } else{
            mToolTitle.setText("开源协议");
        }
        RestClient.builder()
                .loader(getContext(), LoaderStyle.LineScaleIndicator)
                .url(URL)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final String info = JSON.parseObject(response).getString("data");
                        mTextView.setText(info);
                    }
                })
                .build()
                .get();
    }
}
