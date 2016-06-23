package com.hcpt.fastfood.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.Constant;

public class StatusAdapter extends BaseAdapter {

	private ArrayList<String> mArrCategory;
	private LayoutInflater mInflater;
	private String selectedStatus;

	public StatusAdapter(Context activity, String role) {
		this.mArrCategory = new ArrayList<String>();
		switch (role) {
		case Constant.ROLE_ADMIN_USER:
			mArrCategory.add(Constant.STATUS_0);
			mArrCategory.add(Constant.STATUS_1);
			mArrCategory.add(Constant.STATUS_2);
			mArrCategory.add(Constant.STATUS_3);
			mArrCategory.add(Constant.STATUS_4);
			mArrCategory.add(Constant.STATUS_5);
			mArrCategory.add(Constant.STATUS_6);
			break;
		case Constant.ROLE_CHEF_USER:
			mArrCategory.add(Constant.STATUS_1);
			mArrCategory.add(Constant.STATUS_2);
			mArrCategory.add(Constant.STATUS_3);
			break;
		case Constant.ROLE_USER_DELIVERY:
			mArrCategory.add(Constant.STATUS_4);
			mArrCategory.add(Constant.STATUS_5);
			mArrCategory.add(Constant.STATUS_6);
			break;
		case Constant.ROLE_WAITER_USER:
			mArrCategory.add(Constant.STATUS_4);
			mArrCategory.add(Constant.STATUS_1);
			mArrCategory.add(Constant.STATUS_5);
			break;
		}
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrCategory.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mArrCategory.get(position);
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
			convertView = mInflater.inflate(R.layout.row_item_status, null);
			holder.imgSelected = (ImageView) convertView
					.findViewById(R.id.imgSelected);
			holder.lblStatus = (TextView) convertView
					.findViewById(R.id.lblStatus);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		String item = mArrCategory.get(position);
		if (item != null) {
			holder.lblStatus.setText(item);
			if (item.equals(getSelectedStatus())) {
				holder.imgSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgSelected.setVisibility(View.GONE);
			}

		}
		return convertView;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public class HolderView {
		private TextView lblStatus;
		private ImageView imgSelected;
	}

	public String getNumberStatusByStringName() {
		String result = "";
		switch (selectedStatus) {
		case Constant.STATUS_0:
			result = Constant.STATUS_CREATED;
			break;
		case Constant.STATUS_1:
			result = Constant.STATUS_REJECT;
			break;
		case Constant.STATUS_2:
			result = Constant.STATUS_IN_PROCESS;
			break;
		case Constant.STATUS_3:
			result = Constant.STATUS_READY;
			break;
		case Constant.STATUS_4:
			result = Constant.STATUS_DELIVERED;
			break;
		case Constant.STATUS_5:
			result = Constant.STATUS_FAIL;
			break;
		case Constant.STATUS_6:
			result = Constant.STATUS_ON_THE_WAY;
			break;
		}
		return result;
	}

	public void setSelectedStringByNumber(String status) {
		switch (status) {
		case Constant.STATUS_CREATED:
			selectedStatus = Constant.STATUS_0;
			break;
		case Constant.STATUS_REJECT:
			selectedStatus = Constant.STATUS_1;
			break;
		case Constant.STATUS_IN_PROCESS:
			selectedStatus = Constant.STATUS_2;
			break;
		case Constant.STATUS_READY:
			selectedStatus = Constant.STATUS_3;
			break;
		case Constant.STATUS_DELIVERED:
			selectedStatus = Constant.STATUS_4;
			break;
		case Constant.STATUS_FAIL:
			selectedStatus = Constant.STATUS_5;
			break;
		case Constant.STATUS_ON_THE_WAY:
			selectedStatus = Constant.STATUS_6;
			break;
		}
	}

}
