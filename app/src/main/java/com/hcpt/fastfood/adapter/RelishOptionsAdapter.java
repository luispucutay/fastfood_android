package com.hcpt.fastfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hcpt.fastfood.R;

public class RelishOptionsAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private String[] objects;

	public RelishOptionsAdapter(Context ctx, int txtViewResourceId,
			String[] objects) {
		super(ctx, txtViewResourceId, objects);
		this.objects = objects;
		mContext = ctx;
	}

	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
		return getCustomView(position, cnvtView, prnt);
	}

	@Override
	public View getView(int pos, View cnvtView, ViewGroup prnt) {
		return getCustomView(pos, cnvtView, prnt);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View mySpinner = inflater.inflate(R.layout.item_spinner, parent, false);
		TextView main_text = (TextView) mySpinner
				.findViewById(R.id.txtItemSpinner);
		if (objects != null) {
			main_text.setText(objects[position]);
		}
		return mySpinner;
	}
}
