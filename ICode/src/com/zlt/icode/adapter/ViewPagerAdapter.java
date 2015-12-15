package com.zlt.icode.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter<T extends View> extends PagerAdapter
{
	private List<T> view_list;

	public ViewPagerAdapter(List<T> view_list)
	{
		this.view_list = view_list;
	}

	@Override
	public int getCount() 
	{
		return view_list.size();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
		ViewPager pViewPager = ((ViewPager) container);
		pViewPager.removeView((T)view_list.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) 
	{
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) 
	{
		((ViewPager)container).addView(view_list.get(position));
		
		return (T)view_list.get(position);
	}
}
