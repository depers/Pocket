package cn.bravedawn.latte.net.rx;

import android.content.Context;
import android.database.Observable;

import java.io.File;
import java.util.WeakHashMap;

import cn.bravedawn.latte.net.HttpMethod;
import cn.bravedawn.latte.net.RestCreator;
import cn.bravedawn.latte.ui.loader.LatteLoader;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by 冯晓 on 2017/9/15.
 */

public class RxRestClient {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParam();
    private final RequestBody BODY;
    private final LoaderStyle LOADERSTYLE;
    private final File FILE;
    private final Context CONTENT;

    public RxRestClient(String url,
                        WeakHashMap<String, Object> params,
                        RequestBody body,
                        LoaderStyle loaderStyle,
                        File file,
                        Context context) {
        this.URL = url;
        this.BODY = body;
        this.LOADERSTYLE = loaderStyle;
        this.FILE = file;
        this.CONTENT = context;
    }

    public static RxRestClientBuilder builder(){
        return new RxRestClientBuilder();
    }

    private Observable<String> request(HttpMethod method){
        final RxRestService service = RestCreator.getRxRestService();
        Observable<String> observable = null;

        if (LOADERSTYLE != null){
            LatteLoader.showLoading(CONTENT, LOADERSTYLE);
        }

        switch (method){
            case GET:
                observable = service.get(URL, PARAMS);
                break;
            case POST:
                observable = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                observable = service.postRaw(URL, BODY);
                break;
            case PUT:
                observable = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                observable = service.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody
                        .create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body = MultipartBody
                        .Part.createFormData("file", FILE.getName(), requestBody);
                observable = service.upload(URL, body);
                break;
            default:
                break;
        }

        return observable;
    }


    // 调用接口
    public final Observable<String> get(){
        return request(HttpMethod.GET);
    }
    public final Observable<String> post(){
        if (BODY == null){
            return request(HttpMethod.POST);
        } else{
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("param must be null!");
            }
            return request(HttpMethod.POST_RAW);
        }
    }
    public final Observable<String> put(){
        if (BODY == null){
            return request(HttpMethod.PUT);
        } else{
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("param must be null!");
            }
            return request(HttpMethod.PUT_RAW);
        }


    }
    public final Observable<String> delete(){
        return request(HttpMethod.DELETE);
    }

    public final Observable<ResponseBody> download(){
        return RestCreator.getRxRestService().download(URL, PARAMS);
    }


}
