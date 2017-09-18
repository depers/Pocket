package cn.bravedawn.latte.net;

import android.content.Context;

import java.io.File;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Multipart;

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
    private final RequestBody BODY;
    private final LoaderStyle LOADERSTYLE;
    private final File FILE;
    private final Context CONTENT;

    public RestClient(String url,
                      WeakHashMap<String, Object> params,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      LoaderStyle loaderStyle,
                      File file,
                      Context context) {
        this.URL = url;
        this.PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.LOADERSTYLE = loaderStyle;
        this.FILE = file;
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
            case POST_RAW:
                call = service.postRaw(URL, BODY);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody
                        .create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body = MultipartBody
                        .Part.createFormData("file", FILE.getName(), requestBody);
                call = RestCreator.getRestService().upload(URL, body);
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
        if (BODY == null){
            request(HttpMethod.POST);
        } else{
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("param must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }
    public final void put(){
        if (BODY == null){
            request(HttpMethod.PUT);
        } else{
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("param must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }


    }
    public final void delete(){
        request(HttpMethod.DELETE);
    }

}
