package com.blackheart.brightscreenstatistics.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.blackheart.brightscreenstatistics.R;
import java.util.List;

public class BrightScreenDateAdapter extends RecyclerView.Adapter<BrightScreenDateAdapter.MyHolder> {
  private Context mContext;
  private List<String> mDateList;
  private List<String> mDurationList;

  private OnItemClickListener onItemClickListener;

  //
  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public BrightScreenDateAdapter(Context context, List<String> dateList, List<String> durationList) {
    this.mContext = context;
    this.mDateList = dateList;
    this.mDurationList = durationList;
  }

  @NonNull
  @Override
  public BrightScreenDateAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item, parent, false);
    MyHolder myHolder = new MyHolder(view);
    return myHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull BrightScreenDateAdapter.MyHolder holder,
      final int position) {

    String date = mDateList.get(position);
    String duration = mDurationList.get(position);
    holder.tvDate.setText(date);
    holder.tvRecordTime.setText(duration + "");
    holder.llDateItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Toast.makeText(context, durationList.get(position), Toast.LENGTH_SHORT).show();
        onItemClickListener.onItemClick(v, position);
      }
    });
  }



  @Override
  public int getItemCount() {
    return mDateList.size();
  }

  class MyHolder extends RecyclerView.ViewHolder {
    TextView tvDate;
    TextView tvRecordTime;
    View llDateItem;

    MyHolder(View itemView) {
      super(itemView);
      tvDate = itemView.findViewById(R.id.tv_date);
      tvRecordTime = itemView.findViewById(R.id.tv_record_time);
      llDateItem = itemView.findViewById(R.id.ll_date_item);
    }
  }

  public void update(List<String> dateList, List<String> durationList) {
    this.mDateList = dateList;
    this.mDurationList = durationList;
    notifyDataSetChanged();
  }

  public void remove(){
    
  }
}