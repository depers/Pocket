package cn.bravedawn.latte.util.timer;

import java.util.TimerTask;

/**
 * Created by 冯晓 on 2017/9/20.
 */

public class BaseTimerTask extends TimerTask {

    private ITimeListener mITimeListener = null;

    public BaseTimerTask(ITimeListener iTimeListener) {
        this.mITimeListener = iTimeListener;
    }

    @Override
    public void run() {
        if (mITimeListener != null){
            mITimeListener.onTimer();
        }
    }
}
