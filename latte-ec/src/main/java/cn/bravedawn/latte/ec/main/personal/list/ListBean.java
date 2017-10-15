package cn.bravedawn.latte.ec.main.personal.list;


import android.widget.CompoundButton;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.bravedawn.latte.delegates.LatteDelegate;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public class ListBean implements MultiItemEntity {

    private int mItemType = 0;
    private String mImageUrl = null;
    private String mText = null;
    private String mValue = null;
    private int mId = 0;
    private int mResImage = 0;
    private LatteDelegate mDelegate = null;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = null;


    public ListBean(int mItemType, String mImageUrl, String mText, String mValue, int mId, int mResImage,
                    LatteDelegate mDelegate,
                    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mItemType = mItemType;
        this.mImageUrl = mImageUrl;
        this.mText = mText;
        this.mValue = mValue;
        this.mId = mId;
        this.mResImage = mResImage;
        this.mDelegate = mDelegate;
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getText() {
        if (mText == null) {
            return "";
        }
        return mText;
    }

    public String getValue() {
        if (mValue == null) {
            return "";
        }
        return mValue;
    }

    public int getId() {
        return mId;
    }

    public LatteDelegate getDelegate() {
        return mDelegate;
    }

    public CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public int getResImage() {
        return mResImage;
    }

    public void setResImage(int mResImage) {
        this.mResImage = mResImage;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public static final class Builder {
        private int itemType = 0;
        private String imageUrl = null;
        private String text = null;
        private String value = null;
        private int id = 0;
        private int resImage = 0;
        private LatteDelegate delegate = null;
        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = null;

        public Builder setItemType(int itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDelegate(LatteDelegate delegate) {
            this.delegate = delegate;
            return this;
        }

        public Builder setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            this.onCheckedChangeListener = onCheckedChangeListener;
            return this;
        }

        public Builder setResImage(int resImage) {
            this.resImage = resImage;
            return this;
        }

        public ListBean build() {
            return new ListBean(itemType, imageUrl, text, value, id, resImage, delegate, onCheckedChangeListener);
        }
    }


}
