package com.inno.backdot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 
 * @author didikee
 * 
 * 不能滚动的Listview
 *
 */
public class HListView extends ListView{

	public HListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HListView(Context context) {
		super(context);
	}
	/**
	 * 设置不能滚动,撑大!
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
