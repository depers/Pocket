package cn.bravedawn.latte.ec.main.channel.add;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
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
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.storage.LattePreference;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

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

    @BindView(R2.id.add_channel_title)
    AppCompatTextView mDelegateTile = null;

    @OnClick(R2.id.finish_channel)
    void onClickFinish(){
        if (checkForm()){
            if (mChannelId != null && mChannelId != 0){
                RestClient.builder()
                        .url("channel/"+mChannelId)
                        .loader(getContext(), LoaderStyle.LineScaleIndicator)
                        .params("title", mEditChannelName.getText().toString())
                        .params("desc", mEditChannelDesc.getText().toString())
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                //LatteLogger.d("channel_modify", response);
                                getSupportDelegate().pop();
                            }
                        })
                        .build()
                        .put();
            } else{
                RestClient.builder()
                        .url("channel")
                        .loader(getContext(), LoaderStyle.LineScaleIndicator)
                        .params("title", mEditChannelName.getText().toString())
                        .params("desc", mEditChannelDesc.getText().toString())
                        .params("userId", LattePreference.getCustomAppProfile("userId"))
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                //LatteLogger.d("channel_add", response);
                                getSupportDelegate().pop();
                            }
                        })
                        .build()
                        .post();
            }

        }
    }

    private String CHANNEL_TITLE = null;
    private String CHANNEL_DESC = null;
    private Integer mChannelId = null;

    public static AddChannelDelegate create(Integer id){
        Bundle bundle = new Bundle();
        bundle.putInt("channelId", id);
        AddChannelDelegate delegate = new AddChannelDelegate();
        delegate.setArguments(bundle);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null){
            mChannelId = args.getInt("channelId");
            LatteLogger.d("Channel_id", mChannelId);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_add_channel;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        if (mChannelId != null && mChannelId != 0){
            RestClient.builder()
                    .url("channel/info/"+mChannelId)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            LatteLogger.d("channel/info", response);
                            JSONObject channel = JSON.parseObject(response);
                            JSONObject data = channel.getJSONObject("data");
                            mEditChannelName.setText(data.getString("channel"));
                            mEditChannelDesc.setText(data.getString("descText"));
                            mDelegateTile.setText("修改栏目");
                        }
                    })
                    .build()
                    .get();
        }
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

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

}
