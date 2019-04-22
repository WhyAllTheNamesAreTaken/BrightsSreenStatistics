package com.blackheart.brightscreenstatistics.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blackheart.brightscreenstatistics.AppDurationActivity;
import com.blackheart.brightscreenstatistics.R;
import com.blackheart.brightscreenstatistics.StatisticsActivity;
import com.blackheart.brightscreenstatistics.adapter.AppTimeDurationAdapter;
import com.blackheart.brightscreenstatistics.db.AppTime;
import com.blackheart.brightscreenstatistics.util.ConvertTimeUtil;
import com.blackheart.brightscreenstatistics.util.GetAppInfoUtil;
import com.blackheart.brightscreenstatistics.util.ValueComparatorUtil;
import com.hjq.bar.TitleBar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.litepal.LitePal;

public class ThreeDayFragment extends Fragment {
  RecyclerView recyclerApp;
  LinearLayoutManager linearLayoutManager;
  AppTimeDurationAdapter adapter;

  ArrayList<String> mAppNameList = new ArrayList<>();
  ArrayList<Long> mAppDurationTimeList = new ArrayList<>();
  ArrayList<Drawable> mAppIconList = new ArrayList<>();
  long totalTime = 0;
  public static final String TAG = "ThreeDayFragment";

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_3day_statistics, null);

    recyclerApp = view.findViewById(R.id.recycler_3day);

    linearLayoutManager = new LinearLayoutManager(this.getActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerApp.setLayoutManager(linearLayoutManager);

    TreeMap<String, Long> treeMap = countTime();
    for (TreeMap.Entry<String, Long> entry : treeMap.entrySet()) {

      mAppDurationTimeList.add(entry.getValue()/3);

      mAppIconList.add(
          GetAppInfoUtil.getApplicationIconByPackageName(this.getActivity(), entry.getKey()));

      mAppNameList.add(
          GetAppInfoUtil.getApplicationNameByPackageName(this.getActivity(), entry.getKey()));
      //Log.e(TAG,
      //    GetAppInfoUtil.getApplicationNameByPackageName(this.getActivity(), entry.getKey()));

      totalTime += entry.getValue();
    }

    adapter =
        new AppTimeDurationAdapter(this.getActivity(), mAppNameList,
            mAppDurationTimeList, mAppIconList, totalTime/3);
    recyclerApp.setAdapter(adapter);

    adapter.setOnItemClickListener(
        new AppTimeDurationAdapter.OnItemClickListener() {
          @Override
          public void onItemClick(View view, int position) {

          }
        });

    return view;
  }

  /**
   * 统计APP使用时间
   *
   * @return TreeMap<APP名称
               *       *
               *
               *       ,
               *       *
               *
               *       使
               *       用
               *                                                                                                                                       时
               *       间> 时 间降序排列
   */
  private TreeMap countTime() {

    HashMap<String, Long> appHM = new HashMap<>();

    List<AppTime> appTimes = LitePal.findAll(AppTime.class);

    if (!appTimes.isEmpty()) {
      Date resDate;
      long threeDayAgo;


      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DATE, -2);
      resDate = calendar.getTime();

      try {
        threeDayAgo =
            new SimpleDateFormat("yyyy-MM-dd").parse(ConvertTimeUtil.ms2YyyyMMdd(resDate.getTime()))
                .getTime();

        //提取3日内APP数据
        for (AppTime appTime1 : appTimes)
          if (appTime1.getAppStartTime() > threeDayAgo) {
            if (appHM.containsKey(appTime1.getAppName())&&appHM.get(appTime1.getAppName())!=null) {
              appHM.put(appTime1.getAppName(),
                  appHM.get(appTime1.getAppName()) + appTime1.getAppRunTimeDuration());
            } else {
              appHM.put(appTime1.getAppName(), appTime1.getAppRunTimeDuration());
            }
          }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    TreeMap<String, Long> appTM = new TreeMap<>(new ValueComparatorUtil(appHM));
    appTM.putAll(appHM);

    return appTM;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    Log.e(TAG, "onDestroyView");
    adapter.removeAll();
  }
}