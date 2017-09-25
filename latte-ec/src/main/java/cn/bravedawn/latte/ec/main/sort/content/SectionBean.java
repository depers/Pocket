package cn.bravedawn.latte.ec.main.sort.content;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class SectionBean extends SectionEntity<SectionContentItemEntity> {

    private boolean mIsMore = false;
    private int mId = -1;


    public SectionBean(SectionContentItemEntity sectionContentItemEntity) {
        super(sectionContentItemEntity);
    }

    public SectionBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public boolean isIsMore() {
        return mIsMore;
    }

    public void setIsMore(boolean isMore) {
        this.mIsMore = isMore;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}
