package cn.bravedawn.latte.util.callback;

import java.util.WeakHashMap;

/**
 * Created by 冯晓 on 2017/9/30.
 */

public class CallBackManager {

    private static final WeakHashMap<Object, IGlobalCallback> CALLBACKS = new WeakHashMap<>();

    private static class Holder{
        private static final CallBackManager INSTANCE = new CallBackManager();
    }

    public static CallBackManager getInstance(){
        return Holder.INSTANCE;
    }

    public CallBackManager addCallBack(Object tag, IGlobalCallback callback){
        CALLBACKS.put(tag, callback);
        return this;
    }

    public IGlobalCallback getCallBack(Object tag){
        return CALLBACKS.get(tag);
    }
}
