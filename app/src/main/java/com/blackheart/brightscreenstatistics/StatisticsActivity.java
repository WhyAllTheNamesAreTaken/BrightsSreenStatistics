package com.blackheart.brightscreenstatistics;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.blackheart.brightscreenstatistics.adapter.TabFragmentPagerAdapter;
import com.blackheart.brightscreenstatistics.fragment.SevenDayFragment;
import com.blackheart.brightscreenstatistics.fragment.ThreeDayFragment;
import com.blackheart.brightscreenstatistics.fragment.ThirtyDayFragment;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
  private TitleBar mTitleBar;


  private TextView tvThreeDay;
  private TextView tvSevenDay;
  private TextView tvTotalDay;

  private ViewPager myViewPager;
  private List<Fragment> list;
  private TabFragmentPagerAdapter tabFragmentPagerAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_statistics);
    InitView();

    //title bar点击事件
    mTitleBar = (TitleBar) findViewById(R.id.tb_statistics_tool_bar);
    mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {

      @Override
      public void onLeftClick(View v) {
        finish();
      }

      @Override
      public void onTitleClick(View v) {

      }

      @Override
      public void onRightClick(View v) {

      }
    });

    // 设置菜单栏的点击事件
    tvThreeDay.setOnClickListener(this);
    tvSevenDay.setOnClickListener(this);
    tvTotalDay.setOnClickListener(this);
    myViewPager.addOnPageChangeListener(new MyPagerChangeListener());

    //把Fragment添加到List集合里面
    list = new ArrayList<>();
    list.add(new ThreeDayFragment());
    list.add(new SevenDayFragment());
    list.add(new ThirtyDayFragment());
    tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
    myViewPager.setAdapter(tabFragmentPagerAdapter);
    myViewPager.setCurrentItem(0);  //初始化显示第一个页面
    myViewPager.setOffscreenPageLimit(3);
    tvThreeDay.setBackgroundColor(ContextCompat.getColor(this,R.color.selectButtonColor));//被选中
  }

  private void InitView() {
    tvThreeDay = findViewById(R.id.tv_3day_average);
    tvSevenDay = findViewById(R.id.tv_7day_average);
    tvTotalDay = findViewById(R.id.tv_total_day);
    myViewPager = findViewById(R.id.myViewPager);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_3day_average:
        myViewPager.setCurrentItem(0);
        tvThreeDay.setBackgroundColor(ContextCompat.getColor(this,R.color.selectButtonColor));
        tvSevenDay.setBackgroundColor(ContextCompat.getColor(this,R.color.noSelectButtonColor));
        tvTotalDay.setBackgroundColor(ContextCompat.getColor(this,R.color.noSelectButtonColor));
        break;
      case R.id.tv_7day_average:
        myViewPager.setCurrentItem(1);
        tvThreeDay.setBackgroundColor(ContextCompat.getColor(this,R.color.noSelectButtonColor));
        tvSevenDay.setBackgroundColor(ContextCompat.getColor(this,R.color.selectButtonColor));
        tvTotalDay.setBackgroundColor(ContextCompat.getColor(this,R.color.noSelectButtonColor));
        break;
      case R.id.tv_total_day:
        myViewPager.setCurrentItem(2);
        tvThreeDay.setBackgroundColor(ContextCompat.getColor(this,R.color.noSelectButtonColor));
        tvSevenDay.setBackgroundColor(ContextCompat.getColor(this,R.color.noSelectButtonColor));
        tvTotalDay.setBackgroundColor(ContextCompat.getColor(this,R.color.selectButtonColor));
        break;
    }
  }

  public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrollStateChanged(int arg0) {
      switch (arg0) {
        case 0:

          break;
      }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
      switch (arg0) {
        case 0:
          tvThreeDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.selectButtonColor));
          tvSevenDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.noSelectButtonColor));
          tvTotalDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.noSelectButtonColor));
          break;
        case 1:
          tvThreeDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.noSelectButtonColor));
          tvSevenDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.selectButtonColor));
          tvTotalDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.noSelectButtonColor));
          break;
        case 2:
          tvThreeDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.noSelectButtonColor));
          tvSevenDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.noSelectButtonColor));
          tvTotalDay.setBackgroundColor(ContextCompat.getColor(StatisticsActivity.this,R.color.selectButtonColor));
          break;
      }
    }
  }
}
