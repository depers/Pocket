package cn.bravedawn.latte.net.rx;

import android.content.Context;

import java.io.File;
import java.util.WeakHashMap;

import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.RestCreator;
import cn.bravedawn.latte.net.callback.IError;
import cn.bravedawn.latte.net.callback.IFailure;
import cn.bravedawn.latte.net.callback.IRequest;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 冯晓 on 2017/9/15.
 */

public class RxRestClientBuilder {

    private String mUrl = null;
    private static final WeakHashMap<String, Object> mParams = RestCreator.getParam();
    private RequestBody mBody = null;
    private LoaderStyle mLoaderStyle = null;
    private File mFile = null;
    private Context mContent = null;


    RxRestClientBuilder(){
    }

    public final RxRestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RxRestClientBuilder params(WeakHashMap<String, Object> params){
        this.mParams.putAll(params);
        return this;
    }

    public final RxRestClientBuilder params(String key, Object value){
        this.mParams.put(key, value);
        return this;
    }

    public final RxRestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RxRestClientBuilder loader(Context context, LoaderStyle loaderStyle){
        this.mContent = context;
        this.mLoaderStyle = loaderStyle;
        return this;
    }

    public final RxRestClientBuilder file(File file){
        this.mFile = file;
        return this;
    }

    public final RxRestClientBuilder file(String filePath){
        this.mFile = new File(filePath);
        return this;
    }

    public final RxRestClientBuilder loader(Context context){
        this.mContent = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotateIndicator;
        return this;
    }

    public final RxRestClient build(){
        return new RxRestClient(mUrl, mParams, mBody, mLoaderStyle, mFile, mContent);
    }

}
