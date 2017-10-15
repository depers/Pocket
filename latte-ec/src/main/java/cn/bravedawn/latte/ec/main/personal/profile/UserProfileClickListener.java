package cn.bravedawn.latte.ec.main.personal.profile;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.main.personal.list.ListBean;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.date.DateDialogUtil;
import cn.bravedawn.latte.util.callback.CallBackManager;
import cn.bravedawn.latte.util.callback.CallBackType;
import cn.bravedawn.latte.util.callback.IGlobalCallback;
import cn.bravedawn.latte.util.log.LatteLogger;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public class UserProfileClickListener extends SimpleClickListener{

    private final LatteDelegate DELEGATE;

    private String[] mGenders = new String[]{"男", "女", "保密"};

    public UserProfileClickListener(LatteDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, final View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        final int id = bean.getId();
        switch (id){
            case 1:
                // 设置回调
                CallBackManager.getInstance()
                        .addCallBack(CallBackType.ON_CROP, new IGlobalCallback<Uri>() {
                            @Override
                            public void executeCallBack(Uri args) {
                                LatteLogger.d("ON_CROP", args);
                                final ImageView avator = (ImageView) view.findViewById(R.id.img_arrow_avatar);
                                Glide.with(DELEGATE)
                                        .load(args)
                                        .into(avator);

                                RestClient.builder()
                                        .url(UploadConfig.UPLOAD_IMG)
                                        .file(args.getPath())
                                        .loader(DELEGATE.getContext())
                                        .success(new ISuccess() {
                                            @Override
                                            public void onSuccess(String response) {
                                                LatteLogger.d("ON_CROP_UPLOAD", response);
                                                String path = JSON.parseObject(response).getString("path");
                                                // 通知服务器更新信息
                                                RestClient.builder()
                                                        .url("user_profile")
                                                        .params("avatar", path)
                                                        .success(new ISuccess() {
                                                            @Override
                                                            public void onSuccess(String response) {
                                                                //获取更新后的用户信息，然后更新本地数据库
                                                                //没有本地数据的APP，每次打开APP都请求API，获取信息
                                                            }
                                                        })
                                                        .build()
                                                        .post();
                                            }
                                        })
                                        .build()
                                        .post();
                            }
                        });
                DELEGATE.startCameraWithCheck();
                break;
            case 2:

                new MaterialDialog.Builder(DELEGATE.getContext())
                        .title("修改用户名")
                        .titleColorRes(R.color.app_main)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .widgetColorRes(R.color.app_main)
                        .inputRangeRes(2, 20, R.color.colorGrey)
                        .input("请输入您的用户名", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                Toast.makeText(DELEGATE.getContext(), input.toString(), Toast.LENGTH_SHORT).show();
                                final TextView textView = (TextView) view.findViewById(R.id.tv_arrow_value);
                                textView.setText(input);
                                // Do something
                            }
                        }).show();

                break;


            case 3:
                getGenderDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final TextView textView = (TextView) view.findViewById(R.id.tv_arrow_value);
                        textView.setText(mGenders[which]);
                        dialog.cancel();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    private void getGenderDialog(DialogInterface.OnClickListener listener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        builder.setSingleChoiceItems(mGenders, 0, listener);
        builder.show();
    }

}
