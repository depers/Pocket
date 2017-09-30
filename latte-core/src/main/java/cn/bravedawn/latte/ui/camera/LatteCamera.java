package cn.bravedawn.latte.ui.camera;

import android.net.Uri;

import cn.bravedawn.latte.delegates.PermissionCheckerDelegate;
import cn.bravedawn.latte.util.file.FileUtil;

/**
 * Created by 冯晓 on 2017/9/30.
 */

// 照相机调用类
public class LatteCamera {

    public static Uri createCropFile(){
        return Uri.parse(FileUtil.createFile("crop_image",
                FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }

    public static void start(PermissionCheckerDelegate delegate){
        new CameraHandler(delegate).beginCameraDialog();
    }
}
