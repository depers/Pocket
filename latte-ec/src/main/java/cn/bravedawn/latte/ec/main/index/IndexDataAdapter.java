package cn.bravedawn.latte.ec.main.index;

import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;

import java.util.List;

import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.ui.recycler.MultipleViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 冯晓 on 2017/10/8.
 */

public class IndexDataAdapter extends BaseItemDraggableAdapter<MultipleItemEntity, MultipleViewHolder>{


    public IndexDataAdapter(List<MultipleItemEntity> data) {
        super(R.layout.item_record, data);
    }


    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity item) {
        final String title = item.getField(MultipleFields.TITLE);
        final String colorAvatar = item.getField(MultipleFields.COLOR_AVATAR);
        final String resource = item.getField(MultipleFields.TEXT);
        holder.setText(R.id.textViewRecordName, title);
        final CircleImageView circleImageView = holder.getView(R.id.circleImageView);
        circleImageView.setColorFilter(Color.parseColor(colorAvatar));
        holder.setText(R.id.textViewAvatar, title.substring(0, 1));
        holder.setText(R.id.textViewResource, resource);
    }
}
