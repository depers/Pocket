package cn.bravedawn.latte.delegates.bottom;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import cn.bravedawn.latte.R;
import cn.bravedawn.latte.delegates.LatteDelegate;

/**
 * Created by 冯晓 on 2017/9/23.
 */

public abstract class BottomItemDelegate extends LatteDelegate implements View.OnKeyListener {

    private long mExitTime = 0;
    private static final int EXIT_TIME = 2000;

    @Override
    public void onResume() {
        super.onResume();
        final View rootView = getView();
        if (rootView != null){
            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();
            rootView.setOnKeyListener(this);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == keyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME){
                Toast.makeText(getContext(), "双击退出"+getString(R.string.app_name),
                        Toast.LENGTH_LONG).show();
                mExitTime = System.currentTimeMillis();
            } else{
                _mActivity.finish();
                if (mExitTime != 0){
                    mExitTime = 0;
                }
            }
            // 该事件内部已处理，无需系统处理。
            return true;
        }
        return false;
    }
}
