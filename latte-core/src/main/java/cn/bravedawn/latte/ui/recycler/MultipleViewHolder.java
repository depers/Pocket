package cn.bravedawn.latte.ui.recycler;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class MultipleViewHolder extends BaseViewHolder{


    private MultipleViewHolder(View view) {
        super(view);
    }

    public static MultipleViewHolder create(View view){
        return new MultipleViewHolder(view);
    }
}
