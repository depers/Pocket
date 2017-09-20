package cn.bravedawn.latte.net;

import android.content.Context;

import java.io.File;
import java.util.WeakHashMap;

import cn.bravedawn.latte.net.callback.IError;
import cn.bravedawn.latte.net.callback.IFailure;
import cn.bravedawn.latte.net.callback.IRequest;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 冯晓 on 2017/9/15.
 */

public class RestClientBuilder {

    private String mUrl = null;
    private static final WeakHashMap<String, Object> mParams = RestCreator.getParam();
    private IRequest mRequest = null;
    private ISuccess mSuccess = null;
    private IFailure mFailure = null;
    private IError mError = null;
    private RequestBody mBody = null;
    private LoaderStyle mLoaderStyle = null;
    private File mFile = null;
    private Context mContent = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;


    RestClientBuilder(){
    }

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params){
        this.mParams.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value){
        this.mParams.put(key, value);
        return this;
    }

    public final RestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest request){
        this.mRequest = request;
        return this;
    }

    public final RestClientBuilder success(ISuccess success){
        this.mSuccess = success;
        return this;
    }

    public final RestClientBuilder failure(IFailure failure){
        this.mFailure = failure;
        return this;
    }

    public final RestClientBuilder error(IError error){
        this.mError = error;
        return this;
    }

    public final RestClientBuilder loader(Context context, LoaderStyle loaderStyle){
        this.mContent = context;
        this.mLoaderStyle = loaderStyle;
        return this;
    }

    public final RestClientBuilder file(File file){
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String filePath){
        this.mFile = new File(filePath);
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mContent = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotateIndicator;
        return this;
    }

    public final RestClientBuilder dir(String dir){
        this.mDownloadDir = dir;
        return this;
    }

    public final RestClientBuilder extension(String extension){
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder name(String name){
        this.mName = name;
        return this;
    }

    public final RestClient build(){
        return new RestClient(mUrl, mParams, mRequest, mSuccess, mFailure, mError, mBody, mLoaderStyle,
                mFile, mContent, mDownloadDir, mExtension, mName);
    }

}
