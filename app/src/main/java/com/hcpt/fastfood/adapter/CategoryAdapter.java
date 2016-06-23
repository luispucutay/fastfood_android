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
import com.hcpt.fastfood.object.Category;

public class CategoryAdapter extends BaseAdapter {

	private ArrayList<Category> mArrCategory;
	private LayoutInflater mInflater;

	public CategoryAdapter(Context activity, ArrayList<Category> arrCategory) {
		this.mArrCategory = arrCategory;
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
			convertView = mInflater.inflate(R.layout.row_list_category, null);
			holder.imgSelected = (ImageView) convertView
					.findViewById(R.id.imgSelectedIcon);
			holder.lblCategoryName = (TextView) convertView
					.findViewById(R.id.lblCategoryName);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Category Category = mArrCategory.get(position);
		if (Category != null) {
			holder.lblCategoryName.setText(Category.getName().trim());
			if (Category.isSelected()) {
				holder.imgSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgSelected.setVisibility(View.GONE);
			}

		}
		return convertView;
	}

	public class HolderView {
		private TextView lblCategoryName;
		private ImageView imgSelected;
	}

}
