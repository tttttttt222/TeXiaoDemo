package zitopay.com.dragsortlistviewimpl;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mDataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 100; i++) {
            mDataList.add(i+"");
        }

        DragSortListView dslView = (DragSortListView) findViewById(R.id.dsl);
        dslView.setAdapter(new MyAdapter(this,mDataList));
    }


    /*adapter*/
    public class MyAdapter extends DragListViewAdapter<String>{

        public MyAdapter(Context context, List<String> dragDatas) {
            super(context, dragDatas);
        }

        @Override
        public View getItemView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(MainActivity.this, R.layout.item_view,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(mDragDatas.get(position));
            viewHolder.desc.setText(mDragDatas.get(position)+"描述");

            return convertView;
        }

    }

    class ViewHolder{
        TextView name;
        TextView desc;
        ViewHolder(View convertView){
             name = (TextView) convertView.findViewById(R.id.tv_num);
             desc = (TextView) convertView.findViewById(R.id.tv_desc);
        }
    }


}
