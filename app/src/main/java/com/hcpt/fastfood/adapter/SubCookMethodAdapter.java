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
import com.hcpt.fastfood.object.CookMethod;
import com.hcpt.fastfood.object.ItemCart;

public class SubCookMethodAdapter extends BaseAdapter {

	private ArrayList<CookMethod> arrCookMethods;
	private LayoutInflater mInflater;
	private CookMethod currentMethod;

	public SubCookMethodAdapter(Context activity,
			ItemCart item) {
		this.arrCookMethods = item.getSelectedCookMethod().getArrSubMethods();
		this.currentMethod = item.getSelectedSubCookMethod();
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrCookMethods.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrCookMethods.get(position);
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
			convertView = mInflater.inflate(R.layout.row_list_text, null);
			holder.imgSelected = (ImageView) convertView
					.findViewById(R.id.imgSelectedIcon);
			holder.lblCookMethodName = (TextView) convertView
					.findViewById(R.id.lblText);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		CookMethod cookMethod = arrCookMethods.get(position);
		if (cookMethod != null) {
			holder.lblCookMethodName.setText(cookMethod.getName());
			if (cookMethod.equals(currentMethod)) {
				holder.imgSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgSelected.setVisibility(View.GONE);
			}

		}
		return convertView;
	}

	public class HolderView {
		private TextView lblCookMethodName;
		private ImageView imgSelected;
	}

}
