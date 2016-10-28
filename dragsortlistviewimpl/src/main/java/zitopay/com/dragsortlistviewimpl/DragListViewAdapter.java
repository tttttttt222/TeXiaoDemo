package zitopay.com.dragsortlistviewimpl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public abstract class DragListViewAdapter<T> extends BaseAdapter{

    public Context mContext;
    public List<T> mDragDatas;

    public DragListViewAdapter(Context context, List<T> dragDatas) {
        mContext = context;
        mDragDatas = dragDatas;
    }

    @Override
    public int getCount() {
        return mDragDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDragDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position,convertView,parent);
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    public void swapData(int from,int to){
        Collections.swap(mDragDatas,from,to);
        notifyDataSetChanged();
    }

    public void deleteData(int positoion){
        mDragDatas.remove(positoion);
        notifyDataSetChanged();
    }


}
