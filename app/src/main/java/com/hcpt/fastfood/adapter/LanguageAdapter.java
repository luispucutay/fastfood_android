package com.hcpt.fastfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.Category;
import com.hcpt.fastfood.object.Language;

import java.util.ArrayList;

public class LanguageAdapter extends BaseAdapter {

	private ArrayList<Language> mArrLanguage;
	private LayoutInflater mInflater;

	public LanguageAdapter(Context activity, ArrayList<Language> arr) {
		this.mArrLanguage = arr;
		this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mArrLanguage.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrLanguage.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.row_list_category, null);
			holder.imgSelected = (ImageView) convertView.findViewById(R.id.imgSelectedIcon);
			holder.lblCategoryName = (TextView) convertView.findViewById(R.id.lblCategoryName);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Language language = mArrLanguage.get(position);
		if (language != null) {
			holder.lblCategoryName.setText(language.getName().trim());
			if (language.isSelected()) {
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
