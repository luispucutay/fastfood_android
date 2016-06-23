package com.hcpt.fastfood.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.object.Item;
import com.hcpt.fastfood.object.ItemCart;
import com.hcpt.fastfood.object.Relish;

public class CartItemAdapter extends BaseAdapter {
	private ArrayList<ItemCart> arrItems;
	private LayoutInflater mInflater;
	private AQuery lstAq;
	private Context mContext;
	private CartItemAdapterListener listener;

	public interface CartItemAdapterListener {

		public void clickDisplaylistSubCookMethod(int itemIndex, ItemCart item);

		public void clickAddTopping(int itemIndex, ItemCart item);

		public void clickAddIntroduction(int itemIndex, ItemCart item);

		public void clickAddQuantities(int itemIndex, ItemCart item);

		public void clickDelete(int itemIndex);
	}

	public CartItemAdapter(Context activity, ArrayList<ItemCart> arrItems,
			CartItemAdapterListener listener) {
		this.arrItems = arrItems;
		this.mContext = activity;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = listener;
		lstAq = new AQuery(activity);
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
			convertView = mInflater.inflate(R.layout.row_list_card_item, null);
			holder.imgFood = (ImageView) convertView.findViewById(R.id.imgFood);
			holder.progess = (ProgressBar) convertView
					.findViewById(R.id.progess);
			holder.lblFoodName = (TextView) convertView
					.findViewById(R.id.lblFoodName);
			holder.lblCategoryName = (TextView) convertView
					.findViewById(R.id.lblCategory);
			holder.lblPrice = (TextView) convertView
					.findViewById(R.id.lblPrice);
			holder.lblSelectedCookMethod = (TextView) convertView
					.findViewById(R.id.lblSelectedCookMethod);
			holder.lblSubMethod = (TextView) convertView
					.findViewById(R.id.lblSubMethod);
			holder.lblToppingPrice = (TextView) convertView
					.findViewById(R.id.lblToppingPrice);
			holder.lblAddQuantity = (TextView) convertView
					.findViewById(R.id.lblAddQuantity);

			holder.btnAddTopping = (TextView) convertView
					.findViewById(R.id.btnAddTopping);
			holder.btnAddIntroduction = (TextView) convertView
					.findViewById(R.id.btnAddIntroduction);
			holder.btnDelete = (ImageView) convertView
					.findViewById(R.id.btnDelete);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		final ItemCart itemcart = arrItems.get(position);
		if (itemcart != null) {
			AQuery aq = lstAq.recycle(convertView);
			Item item = itemcart.getItem();
			holder.lblFoodName.setText(item.getName());
			holder.lblCategoryName.setText(item.getCategory().getName());

			// Remove by Cuongpm
			// show /hide selected cook method
			// if (itemcart.getSelectedCookMethod() != null) {
			// holder.lblSelectedCookMethod.setText(itemcart
			// .getSelectedCookMethod().getName());
			// if (itemcart.getSelectedSubCookMethod() != null) {
			// holder.lblSubMethod.setText(itemcart
			// .getSelectedSubCookMethod().getName());
			// holder.lblSubMethod.setVisibility(View.VISIBLE);
			// } else {
			// holder.lblSubMethod.setVisibility(View.GONE);
			//
			// }
			// } else {
			// holder.lblSelectedCookMethod.setText("");
			// holder.lblSubMethod.setVisibility(View.GONE);
			// }

			holder.lblSubMethod.setVisibility(View.GONE);

			// End

			holder.lblAddQuantity.setText(itemcart.getQuantities() + "");
			// show/hide select sub method

			// show/ hide topping price
			// holder.lblToppingPrice
			// .setText(" + $ " + itemcart.getPriceTopping());
			// if (itemcart.getArrRelish().size() > 0) {
			// holder.lblToppingPrice.setVisibility(View.VISIBLE);
			// } else {
			// holder.lblToppingPrice.setVisibility(View.INVISIBLE);
			// }

			String temp = "";
			for (Relish relishItem : itemcart.getArrRelish()) {
				if (!relishItem.getSelectedOption().getName()
						.equals(Constant.NONE)) {
					temp += relishItem.getName().trim() + ":"
							+ relishItem.getSelectedOption().getName().trim()
							+ ",";
				}
			}
			if (temp.length() > 1) {
				temp = temp.substring(0, temp.length() - 1);
				holder.lblToppingPrice.setText(temp);
				holder.lblToppingPrice.setSelected(true);
			} else {
				holder.lblToppingPrice.setText("");
			}
			holder.lblToppingPrice.setSelected(true);
			
			if (itemcart.getPriceTopping() > 0) {
				holder.lblPrice.setText("$ " + item.getPrice() + " (+$ "
						+ itemcart.getPriceTopping() + ")");
			}
			else
			{
				holder.lblPrice.setText("$ " + item.getPrice());
			}

			aq.id(holder.imgFood)
					.progress(holder.progess)
					.image(item.getThumb(), true, true, 0, R.drawable.no_image,
							new BitmapAjaxCallback() {
								@SuppressLint("NewApi")
								@Override
								public void callback(String url, ImageView iv,
										Bitmap bm, AjaxStatus status) {
									if (bm != null) {
										Drawable d = new BitmapDrawable(
												mContext.getResources(), bm);
										holder.imgFood.setBackgroundDrawable(d);
									} else {
										holder.imgFood
												.setBackgroundResource(R.drawable.no_image);
									}
								}
							});
		}

		holder.btnAddTopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.clickAddTopping(position, arrItems.get(position));
			}
		});
		holder.btnAddIntroduction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.clickAddIntroduction(position, arrItems.get(position));
			}
		});
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.clickDelete(position);
			}
		});
		holder.lblSubMethod.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.clickDisplaylistSubCookMethod(position,
						arrItems.get(position));
			}
		});
		holder.lblAddQuantity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.clickAddQuantities(position, arrItems.get(position));
			}
		});

		return convertView;
	}

	public class HolderView {
		private TextView lblCategoryName, lblFoodName, lblSelectedCookMethod,
				lblPrice, lblSubMethod, lblToppingPrice, lblAddQuantity,
				btnAddTopping, btnAddIntroduction;
		private ImageView imgFood, btnDelete;
		private ProgressBar progess;
	}
}
