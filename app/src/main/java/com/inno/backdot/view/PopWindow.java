package com.inno.backdot.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inno.backdot.R;
import com.inno.backdot.activity.MainActivity;

/**
 * Created by didik on 2016/5/26.
 */
public class PopWindow {


    private View item;
    private int prePosition;
    private View mRootView;
    private TextView mTextView;
    private Activity mActivity;
    private HListView mHlistview;
    private onItemCheckedLister listener;
    private PopupWindow pop;

    public PopWindow(Activity activity, TextView textView, View rootView, int prePosition, View item) {
        this.mActivity=activity;
        this.mTextView=textView;
        this.mRootView=rootView;
        this.prePosition=prePosition;
        this.item=item;
        init();
    }

    private void init() {
        item.setBackgroundColor(mActivity.getResources().getColor(R.color.text_color_blue));
        View inflate = View.inflate(mActivity, R.layout.pop_main, null);
        mHlistview = ((HListView) inflate.findViewById(R.id.pop_listview));
        mHlistview.setAdapter(popAdapter);
        mHlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (listener!=null){
                    listener.checked(position);
                }
                mTextView.setText("( "+MainActivity.mList[position]+" )");
                pop.dismiss();
            }
        });
        pop = new PopupWindow(inflate, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        pop.showAtLocation(mRootView, Gravity.CENTER,0,0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int phoneWin = displayMetrics.widthPixels;
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                item.setBackgroundColor(Color.WHITE);
            }
        });
        pop.showAsDropDown(mRootView,phoneWin-pop.getWidth(),0, Gravity.END);
    }

    private BaseAdapter popAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return  MainActivity.mList==null?0:MainActivity.mList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHold holder;
            if (convertView==null){
                holder=new ViewHold();
                convertView = View.inflate(mActivity, R.layout.item_pop_main, null);
                holder.tv_text= (TextView) convertView.findViewById(R.id.pop_tv_text);
                convertView.setTag(holder);
            }
            holder= (ViewHold) convertView.getTag();
            holder.tv_text.setText(MainActivity.mList[position]);
            if (position==prePosition){
                holder.tv_text.setTextColor(mActivity.getResources().getColor(R.color.text_color_blue));
            }else {
                holder.tv_text.setTextColor(mActivity.getResources().getColor(R.color.text_color));
            }
            return convertView;
        }
    };

    private class ViewHold{
        public TextView tv_text;
    }
    /**回调**/
    public interface onItemCheckedLister{
        void checked(int position);
    }
    public void setonItemCheckedLister(onItemCheckedLister listener){
        this.listener=listener;
    }


}
