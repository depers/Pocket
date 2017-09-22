package cn.bravedawn.fastec.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.IError;
import cn.bravedawn.latte.net.callback.IFailure;
import cn.bravedawn.latte.net.callback.ISuccess;

/**
 * Created by 冯晓 on 2017/9/15.
 */

public class ExampleDelegate extends LatteDelegate{
    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //testRestClient();
    }


    private void testRestClient(){
        RestClient.builder()
                .url("http://127.0.0.1/index")
                .loader(getContext())
                //.params("", "")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("hahahaha", response);
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build()
                .get();

    }

}
