package cn.bravedawn.latte.net.callback.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.io.InputStream;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.net.callback.IRequest;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.util.file.FileUtil;
import okhttp3.ResponseBody;

/**
 * Created by 冯晓 on 2017/9/19.
 */

public class SaveFileTask extends AsyncTask<Object, Void, File>{

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    public SaveFileTask(IRequest REQUEST, ISuccess SUCCESS) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
    }

    @Override
    protected File doInBackground(Object... params) {

        String downloadDir = (String) params[0];
        String extension = (String) params[1];
        String name = (String) params[2];
        final ResponseBody body = (ResponseBody) params[3];
        final InputStream is = body.byteStream();

        if (downloadDir == null || downloadDir.equals("")){
            downloadDir = "down_laods";
        }
        if (extension == null || extension.equals("")){
            extension = "";
        }
        if (name == null){
            return FileUtil.writeToDisk(is, downloadDir, extension.toUpperCase(), extension);
        } else{
            return FileUtil.writeToDisk(is, downloadDir, name);
        }
    }

    /**
     * 异步执行完后，回到主线程
     * @param file
     */
    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }
        if (REQUEST != null){
            REQUEST.onRequestEnd();
        }
        autoInstallApk(file);
    }

    private void autoInstallApk(File file){
        if (FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            Latte.getApplication().startActivity(intent);
        }
    }
}
