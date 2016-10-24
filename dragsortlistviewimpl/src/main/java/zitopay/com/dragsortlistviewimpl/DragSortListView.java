package zitopay.com.dragsortlistviewimpl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DragSortListView extends ListView {

    public DragSortListView(Context context) {
        this(context,null);
    }

    public DragSortListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragSortListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int pointToPosition(int x, int y) {
        return super.pointToPosition(x, y);
    }
}
