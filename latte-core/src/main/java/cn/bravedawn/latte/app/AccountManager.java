package cn.bravedawn.latte.app;

import cn.bravedawn.latte.util.storage.LattePreference;

/**
 * Created by 冯晓 on 2017/9/22.
 */

public class AccountManager {

    private enum SignTag{
        SIGN_TAG
    }

    // 保存用户的登录状态，登录后调用
    public static void setSignState(boolean state){
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }


    private static boolean isSignIn(){
        return LattePreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker){
        if (isSignIn()){
            checker.onSignIn();
        } else{
            checker.NotSignIn();
        }
    }

}
