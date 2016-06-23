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
import com.hcpt.fastfood.object.TableObject;

public class TableAdapter extends BaseAdapter {

	private ArrayList<TableObject> mArrTable;
	private LayoutInflater mInflater;
	private Context context;

	public TableAdapter(Context activity, ArrayList<TableObject> mArrTable) {
		context = activity;
		this.mArrTable = mArrTable;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrTable.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mArrTable.get(position);
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
			convertView = mInflater.inflate(R.layout.row_list_table, null);
			holder.status_image = (ImageView) convertView
					.findViewById(R.id.status_image);
			holder.lblName = (TextView) convertView.findViewById(R.id.lblName);
			holder.lblSeat = (TextView) convertView.findViewById(R.id.lblSeat);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		TableObject tableObject = mArrTable.get(position);
		if (tableObject != null) {
			holder.lblName.setText(tableObject.getName());
			holder.lblSeat.setText(tableObject.getCurrent_seat() + "/"
					+ tableObject.getNum_of_seat());
			int Current_sea = Integer.valueOf(tableObject.getCurrent_seat());
			int Num_of_seat = Integer.valueOf(tableObject.getNum_of_seat());
			if (Current_sea == 0) {
				holder.status_image.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.ic_table_empty));
			} else {
				if (Current_sea >= Num_of_seat) {
					holder.status_image.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.ic_table_full));
				} else {
					holder.status_image.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.ic_table_half));
				}
			}
		}
		return convertView;
	}

	public class HolderView {
		private TextView lblName, lblSeat;
		private ImageView status_image;
	}

}
