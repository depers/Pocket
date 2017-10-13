package cn.bravedawn.latte.ec.main.channel.addchannel;

import android.app.Instrumentation;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;

/**
 * Created by 冯晓 on 2017/10/12.
 */

public class AddChannelDelegate extends LatteDelegate {

    @BindView(R2.id.edit_channel_name)
    TextInputEditText mEditChannelName = null;

    @BindView(R2.id.edit_channel_desc)
    TextInputEditText mEditChannelDesc = null;

    @BindView(R2.id.finish_channel)
    AppCompatTextView mFinishText = null;

    @OnClick(R2.id.finish_channel)
    void onClickFinish(){
        if (checkForm()){
            getSupportDelegate().pop();
        }
    }

    private String CHANNEL_TITLE = null;
    private String CHANNEL_DESC = null;


    @Override
    public Object setLayout() {
        return R.layout.delegate_add_channel;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mEditChannelName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    CHANNEL_TITLE = s.toString();
                    if (CHANNEL_DESC != null && CHANNEL_DESC.length() != 0){
                        mFinishText.setTextColor(Color.WHITE);
                    }
                } else{
                    mFinishText.setTextColor(Resources.getSystem().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEditChannelDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    CHANNEL_DESC = s.toString();
                    if (CHANNEL_DESC != null && CHANNEL_TITLE.length() != 0){
                        mFinishText.setTextColor(Color.WHITE);
                    }
                } else{
                    mFinishText.setTextColor(Resources.getSystem().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean checkForm(){
        final String channelTitle = mEditChannelName.getText().toString();
        final String channelDesc = mEditChannelDesc.getText().toString();

        boolean isPass = true;
        if (channelTitle.isEmpty() || channelTitle.length() > 24){
            mEditChannelName.setError("栏目标题错误");
            isPass = false;
        } else{
            mEditChannelName.setError(null);
        }

        if (channelDesc.isEmpty()){
            mEditChannelDesc.setError("栏目描述不能为空");
            isPass = false;
        } else{
            mEditChannelDesc.setError(null);
        }
        return isPass;
    }

}
