package com.blackheart.brightscreenstatistics.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
  private FragmentPagerAdapter mFragmentPagerAdapter;
  private List<Fragment> mList;

  public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
    super(fm);
    this.mList = list;
  }

  @Override public Fragment getItem(int i) {
    return mList.get(i);//显示第几个页面
  }

  @Override public int getCount() {
    return mList.size();//有几个页面
  }
}
