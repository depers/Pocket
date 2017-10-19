package cn.bravedawn.latte.ec.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.bravedawn.latte.app.AccountManager;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.ec.database.DatabaseManager;
import cn.bravedawn.latte.ec.database.UserProfile;
import cn.bravedawn.latte.util.storage.LattePreference;

/**
 * Created by 冯晓 on 2017/9/22.
 */

public class SignHandler {

    // todo 这里的代码需要再加工抽象
    public static void onSignUp(String response, ISignListener signListener){
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long userId = profileJson.getLong("id");
        final String name = profileJson.getString("name");
        final String avatar = profileJson.getString("avatar");
        final String gender = profileJson.getString("gender");
        final String address = profileJson.getString("address");
        LattePreference.addCustomAppProfile("userId", String.valueOf(userId));

        final UserProfile profile = new UserProfile(userId, name, avatar, gender, address);
        DatabaseManager.getInstance().getDao().insert(profile);

        // 已经注册并登录登录成功
        AccountManager.setSignState(true);
        signListener.onSignUpSuccess();
    }

    // todo 这里的代码需要再加工抽象
    public static void onSignIn(String response, ISignListener signListener){
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long userId = profileJson.getLong("id");
        final String name = profileJson.getString("name");
        final String avatar = profileJson.getString("avatar");
        final String gender = profileJson.getString("gender");
        final String address = profileJson.getString("address");
        LattePreference.addCustomAppProfile("userId", String.valueOf(userId));

        final UserProfile profile = new UserProfile(userId, name, avatar, gender, address);
        DatabaseManager.getInstance().getDao().insert(profile);

        // 已经注册并登录登录成功
        AccountManager.setSignState(true);
        signListener.onSignInSuccess();
    }
}
