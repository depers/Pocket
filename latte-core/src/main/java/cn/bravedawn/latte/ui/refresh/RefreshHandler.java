package cn.bravedawn.latte.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class RefreshHandler implements SwipeRefreshLayout.OnRefreshListener{

    private final SwipeRefreshLayout REFRESH_LAYOUT;

    public RefreshHandler(SwipeRefreshLayout refreshLayout) {
        this.REFRESH_LAYOUT = refreshLayout;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    private void refresh(){
        REFRESH_LAYOUT.setRefreshing(true);
        Latte.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // todo 进行一些网络请求

                REFRESH_LAYOUT.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    public void firstPage(String url){
        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //Toast.makeText(Latte.getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                })
                .build()
                .get();
    }


}
