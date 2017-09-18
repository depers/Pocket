package cn.bravedawn.latte.net;

import android.content.Context;

import java.util.Map;
import java.util.WeakHashMap;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.net.callback.IError;
import cn.bravedawn.latte.net.callback.IFailure;
import cn.bravedawn.latte.net.callback.IRequest;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.net.callback.RequestCallbacks;
import cn.bravedawn.latte.ui.LatteLoader;
import cn.bravedawn.latte.ui.LoaderStyle;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by 冯晓 on 2017/9/15.
 */

public class RestClient {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParam();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final ResponseBody BODY;
    private final LoaderStyle LOADERSTYLE;
    private final Context CONTENT;

    public RestClient(String url,
                      WeakHashMap<String, Object> params,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      ResponseBody body,
                      LoaderStyle loaderStyle,
                      Context context) {
        this.URL = url;
        this.PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.LOADERSTYLE = loaderStyle;
        this.CONTENT = context;
    }

    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }

    private void request(HttpMethod method){
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if (REQUEST != null){
            REQUEST.onRequestStart();
        }

        if (LOADERSTYLE != null){
            LatteLoader.showLoading(CONTENT, LOADERSTYLE);
        }

        switch (method){
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            default:
                break;
        }

        if (call != null){
            // 在主线程执行
            //call.execute();
            // 另起线程执行
            call.enqueue(getRequestCallBack());
        }
    }


    private Callback<String> getRequestCallBack(){
        return new RequestCallbacks(REQUEST, SUCCESS, FAILURE, ERROR, LOADERSTYLE);
    }


    // 调用接口
    public final void get(){
        request(HttpMethod.GET);
    }
    public final void post(){
        request(HttpMethod.POST);
    }
    public final void put(){
        request(HttpMethod.PUT);
    }
    public final void delete(){
        request(HttpMethod.DELETE);
    }

}
