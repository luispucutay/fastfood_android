package com.hcpt.fastfood.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.ItemCart;
import com.hcpt.fastfood.object.Relish;

public class ToppingAdapter extends BaseAdapter {
	public static final String TAG = "ToppingAdapter";
	private Context mContext;
	private ItemCart mItemCart;
	private ArrayList<Relish> arrRelish;

	public ToppingAdapter(Context context, ItemCart itemCart) {
		super();
		this.mContext = context;
		this.mItemCart = itemCart;
		initArrRelist(itemCart.getArrRelish());

	}

	private void initArrRelist(List<Relish> list) {
		arrRelish = new ArrayList<Relish>();
		for (Relish relish : list) {
			try {
				arrRelish.add((Relish) relish.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				arrRelish.add(relish);
			}
		}
	}

	public ToppingAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrRelish.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrRelish.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public ItemCart getmItemCart() {
		return mItemCart;
	}

	public void setmItemCart(ItemCart mItemCart) {
		this.mItemCart = mItemCart;
		initArrRelist(mItemCart.getArrRelish());
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_list_topping, null);
			holder = new ViewHolder();
			holder.mRelish = (TextView) convertView.findViewById(R.id.tvRelish);
			holder.mCheckBox = (CheckBox) convertView
					.findViewById(R.id.cbRelish);
			holder.mtvTotal = (TextView) convertView.findViewById(R.id.tvTotal);
			holder.spnType = (Spinner) convertView.findViewById(R.id.spnType);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Relish relist = arrRelish.get(position);
		if (relist != null) {
			// Set adapter for Spinner
			ArrayList<String> temp = new ArrayList<String>();
			temp.addAll(relist.getRelishOptionName());

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					mContext, android.R.layout.simple_spinner_item, temp);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			holder.spnType.setAdapter(dataAdapter);
			holder.mRelish.setText(mItemCart.getArrRelish().get(position)
					.getName());

			holder.mCheckBox.setChecked(mItemCart.getArrRelish().get(position)
					.isChecked());
			holder.spnType.setSelection(mItemCart.getArrRelish().get(position)
					.getSelectedIndex());
			holder.mtvTotal.setText("+ $ "
					+ mItemCart.getArrRelish().get(position).getPrice());
			if (mItemCart.getArrRelish().get(position).getSelectedIndex() != 0) {
				holder.mtvTotal.setVisibility(View.VISIBLE);
			} else {
				holder.mtvTotal.setVisibility(View.INVISIBLE);
			}

			final Spinner finalSpnType = holder.spnType;
			finalSpnType.setClickable(true);
			holder.spnType
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							relist.setSelectedIndex(arg2);
							arrRelish.set(position, relist);
							mItemCart.setArrRelish(arrRelish);
							if (arg2 != 0) {
								holder.mtvTotal.setVisibility(View.VISIBLE);
							} else {
								holder.mtvTotal.setVisibility(View.INVISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
		}
		return convertView;
	}

	static class ViewHolder {
		public Spinner spnType;
		public TextView mRelish;
		public CheckBox mCheckBox;
		public TextView mtvTotal;
	}

}
