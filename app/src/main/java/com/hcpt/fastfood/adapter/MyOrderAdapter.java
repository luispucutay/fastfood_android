package com.hcpt.fastfood.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.OrderHistory;
import com.hcpt.fastfood.utility.DateUtil;
import com.hcpt.fastfood.utility.StringUtility;

public class MyOrderAdapter extends BaseAdapter {

	private ArrayList<OrderHistory> listOrderHistory;
	private LayoutInflater mInflater;
	public boolean isAssignScreen = false;
	private Context context;

	public MyOrderAdapter(Context activity,
			ArrayList<OrderHistory> listOrderHistory) {
		this.listOrderHistory = listOrderHistory;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = activity;
	}

	public ArrayList<OrderHistory> getListOrderHistory() {
		return listOrderHistory;
	}

	public void setListOrderHistory(ArrayList<OrderHistory> listOrderHistory) {
		this.listOrderHistory = listOrderHistory;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return mArrCategory.size();
		return listOrderHistory.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listOrderHistory.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			// convertView = mInflater.inflate(R.layout.row_list_my_order,
			// null);
			convertView = mInflater
					.inflate(R.layout.row_list_order_admin, null);
			holder.lblOrderId = (TextView) convertView
					.findViewById(R.id.lblOrderId);
			holder.lblDate = (TextView) convertView.findViewById(R.id.lblDate);
			holder.lblStatus = (TextView) convertView
					.findViewById(R.id.lblStatus);
			holder.lblTotal = (TextView) convertView
					.findViewById(R.id.lblTotal);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		OrderHistory orderHistory = listOrderHistory.get(position);
		if (orderHistory != null) {
			holder.lblOrderId.setText("# " + orderHistory.getId());
			if (orderHistory.getCreated() != null && orderHistory.getCreated().length() > 0) {
				holder.lblDate.setText(DateUtil.convertTimeStampToDate(
						orderHistory.getCreated(), "yyyy-MM-dd kk:mm"));
			}
			if (isAssignScreen) {
				holder.lblStatus.setText(context.getResources().getString(
						R.string.show));
			} else {
				holder.lblStatus.setText(StringUtility
						.getStatusByKey(orderHistory.getOrderStatus()));
			}

			holder.lblTotal.setText("$ " + orderHistory.getOrderPrice());
		}
		return convertView;
	}

	public class HolderView {
		private TextView lblOrderId, lblDate, lblStatus, lblTotal;
		private LinearLayout btnDetail;
	}

}
