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
import com.hcpt.fastfood.object.DashboardRowItemObject;

public class DashboarItemAdapter extends BaseAdapter {

	private ArrayList<DashboardRowItemObject> arrItems;
	private LayoutInflater mInflater;
	private Context context;

	public DashboarItemAdapter(Context activity,
			ArrayList<DashboardRowItemObject> arrItems) {
		this.arrItems = arrItems;
		this.context = activity;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ArrayList<DashboardRowItemObject> getArrItems() {
		return arrItems;
	}

	public void setArrItems(ArrayList<DashboardRowItemObject> arrItems) {
		this.arrItems = arrItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.row_dashboard_item, null);
			holder.layMain = (LinearLayout) convertView
					.findViewById(R.id.layMain);
			holder.titleLeft = (TextView) convertView
					.findViewById(R.id.title_left);
			holder.titleNew = (TextView) convertView
					.findViewById(R.id.title_new);
			holder.titlePending = (TextView) convertView
					.findViewById(R.id.title_pending);
			holder.titleVoid = (TextView) convertView
					.findViewById(R.id.title_void);
			holder.titleDelivered = (TextView) convertView
					.findViewById(R.id.title_delivered);

			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}

		final DashboardRowItemObject item = arrItems.get(position);
		if (item != null) {
			if (position % 2 == 0) {
				holder.layMain.setBackgroundColor(context.getResources()
						.getColor(R.color.tbl_row_odd));
			} else {
				holder.layMain.setBackgroundColor(context.getResources()
						.getColor(R.color.tbl_row_odd));
			}
			holder.titleLeft.setText(item.getTitle_left());
			holder.titleNew.setText(item.getTitle_new());
			holder.titlePending.setText(item.getTitle_pending());
			holder.titleVoid.setText(item.getTitle_void());
			holder.titleDelivered.setText(item.getTitle_delivered());
		}
		return convertView;
	}

	public class HolderView {
		private LinearLayout layMain;
		private TextView titleLeft;
		private TextView titleNew;
		private TextView titlePending;
		private TextView titleVoid;
		private TextView titleDelivered;
	}

}
