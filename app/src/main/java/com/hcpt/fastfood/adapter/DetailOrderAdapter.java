package com.hcpt.fastfood.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.CookMethod;
import com.hcpt.fastfood.object.Item;
import com.hcpt.fastfood.object.ItemHistory;

public class DetailOrderAdapter extends BaseAdapter {

	private ArrayList<ItemHistory> arrItems;
	private LayoutInflater mInflater;
	private AQuery lstAq;
	private Context context;

	public interface MenuItemAdapterListener {
		public void clickAddButton(Item item);

		public void clickDisplaylistMethod(int itemIndex,
				ArrayList<CookMethod> arrCook);
	}

	public DetailOrderAdapter(Context activity, ArrayList<ItemHistory> arrItems) {
		this.arrItems = arrItems;
		this.context = activity;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lstAq = new AQuery(activity);
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
			convertView = mInflater.inflate(R.layout.row_list_order_item, null);
			holder.imgFood = (ImageView) convertView.findViewById(R.id.imgFood);
			holder.progess = (ProgressBar) convertView
					.findViewById(R.id.progess);
			holder.lblFoodName = (TextView) convertView
					.findViewById(R.id.lblFoodName);
			holder.lblCategoryName = (TextView) convertView
					.findViewById(R.id.lblCategory);
			holder.lblPrice = (TextView) convertView
					.findViewById(R.id.lblPrice);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		final ItemHistory item = arrItems.get(position);
		if (item != null) {

			AQuery aq = lstAq.recycle(convertView);
			holder.lblFoodName.setText(item.getDishName());
			holder.lblCategoryName.setText(item.getCategory());
			holder.lblPrice.setText("$ " + item.getDishPrice() + " x "
					+ item.getSl() + " = $ " + item.getTotal());

			aq.id(holder.imgFood)
					.progress(holder.progess)
					.image(item.getThumb(), true, true, 0, R.drawable.no_image,
							new BitmapAjaxCallback() {
								@SuppressLint("NewApi")
								@Override
								public void callback(String url, ImageView iv,
										Bitmap bm, AjaxStatus status) {
									if (bm != null) {
										Drawable d = new BitmapDrawable(context
												.getResources(), bm);
										holder.imgFood.setBackgroundDrawable(d);
									} else {
										holder.imgFood
												.setBackgroundResource(R.drawable.no_image);
									}
								}
							});
		}

		return convertView;
	}

	public class HolderView {
		private TextView lblCategoryName, lblFoodName, lblPrice;
		private ImageView imgFood;
		private ProgressBar progess;
	}

}
