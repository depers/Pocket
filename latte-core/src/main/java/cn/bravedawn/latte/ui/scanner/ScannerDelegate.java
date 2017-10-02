package cn.bravedawn.latte.ui.scanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ui.camera.RequestCodes;
import cn.bravedawn.latte.util.callback.CallBackManager;
import cn.bravedawn.latte.util.callback.CallBackType;
import cn.bravedawn.latte.util.callback.IGlobalCallback;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by 冯晓 on 2017/10/2.
 */

public class ScannerDelegate extends LatteDelegate implements ZBarScannerView.ResultHandler{

    private ScanView mScanView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mScanView == null){
            mScanView = new ScanView(getContext());
        }
        mScanView.setAutoFocus(true);
        mScanView.setResultHandler(this);
    }

    @Override
    public Object setLayout() {
        return mScanView;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mScanView != null){
            mScanView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScanView != null){
            mScanView.stopCameraPreview();
            mScanView.stopCamera();
        }
    }

    @Override
    public void handleResult(Result result) {
        final IGlobalCallback<String> callback = CallBackManager
                .getInstance()
                .getCallBack(CallBackType.ON_SCAN);
        if (callback != null){
            callback.executeCallBack(result.getContents());
        }
        getSupportDelegate().pop();
    }
}
