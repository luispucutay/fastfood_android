package com.hcpt.fastfood.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.CookMethod;
import com.hcpt.fastfood.object.Item;
import com.hcpt.fastfood.object.ItemHistory;

public class DetailOrderAdminAdapter extends BaseAdapter {

	private ArrayList<ItemHistory> arrItems;
	private LayoutInflater mInflater;
	private Context context;

	public interface MenuItemAdapterListener {
		public void clickAddButton(Item item);

		public void clickDisplaylistMethod(int itemIndex,
				ArrayList<CookMethod> arrCook);
	}

	public DetailOrderAdminAdapter(Context activity,
			ArrayList<ItemHistory> arrItems) {
		this.arrItems = arrItems;
		this.context = activity;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ArrayList<ItemHistory> getArrItems() {
		return arrItems;
	}

	public void setArrItems(ArrayList<ItemHistory> arrItems) {
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
			convertView = mInflater.inflate(R.layout.row_list_order_all_admin,
					null);

			holder.lblName = (TextView) convertView.findViewById(R.id.lblName);
			holder.lblTotal = (TextView) convertView
					.findViewById(R.id.lblTotal);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		final ItemHistory item = arrItems.get(position);
		if (item != null) {
			holder.lblName.setText(item.getDishName());
			holder.lblName.setSelected(true);
			holder.lblTotal.setText("$ " + item.getDishPrice() + " x "
					+ item.getSl() + " = $ " + item.getTotal());
		}

		return convertView;
	}

	public class HolderView {
		private TextView lblName, lblTotal;
	}

}
