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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.CookMethod;
import com.hcpt.fastfood.object.Item;
import com.hcpt.fastfood.object.ItemCart;

public class MenuItemAdapter extends BaseAdapter {

	private ArrayList<Item> arrItems;
	public ArrayList<ItemCart> arrCart = new ArrayList<ItemCart>();
	private LayoutInflater mInflater;
	private AQuery lstAq;
	private Context context;
	private MenuItemAdapterListener listener;

	public interface MenuItemAdapterListener {

		public void clickAddButton(int itemIndex, Item item);

		public void clickDisplaylistMethod(int itemIndex,
				ArrayList<CookMethod> arrCook);

		// Add new by cuongpm
		public void clickAddTopping(int itemIndex, ItemCart item);

		public void clickAddIntroduction(int itemIndex, ItemCart item);

		public void clickAddQuantities(int itemIndex, ItemCart item);

		public void clickDelete(int itemIndex);
	}

	public MenuItemAdapter(Context activity, ArrayList<Item> arrItems,
			MenuItemAdapterListener listener) {
		this.arrItems = arrItems;
		this.context = activity;
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
			// convertView = mInflater.inflate(R.layout.layout_item_history,
			// null);
			// holder.lblResNameHistory = (TextView)
			// convertView.findViewById(R.id.lblResName_History);
			// convertView = mInflater.inflate(R.layout.row_list_item, null);
			convertView = mInflater.inflate(R.layout.row_list_item_vs_order,
					null);
			holder.layInCart = (LinearLayout) convertView
					.findViewById(R.id.layInCart);
			holder.btnDelete = (ImageView) convertView
					.findViewById(R.id.btnDelete);
			holder.btnAddTopping = (TextView) convertView
					.findViewById(R.id.btnAddTopping);
			holder.lblToppingPrice = (TextView) convertView
					.findViewById(R.id.lblToppingPrice);
			holder.btnAddIntroduction = (TextView) convertView
					.findViewById(R.id.btnAddIntroduction);
			holder.lblAddQuantity = (TextView) convertView
					.findViewById(R.id.lblAddQuantity);

			holder.imgFood = (ImageView) convertView.findViewById(R.id.imgFood);
			holder.progess = (ProgressBar) convertView
					.findViewById(R.id.progess);
			holder.lblFoodName = (TextView) convertView
					.findViewById(R.id.lblFoodName);
			holder.lblCategoryName = (TextView) convertView
					.findViewById(R.id.lblCategory);
			holder.lblCookMethod = (TextView) convertView
					.findViewById(R.id.lblCookMethod);
			holder.lblPrice = (TextView) convertView
					.findViewById(R.id.lblPrice);
			holder.btnAddItem = (TextView) convertView
					.findViewById(R.id.btnAddItem);

			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		final Item item = arrItems.get(position);
		if (item != null) {
			boolean inCart = false;
			ItemCart itemcart = new ItemCart();
			for (ItemCart btem : arrCart) {
				if (btem.getItem().getId().equals(item.getId())) {
					itemcart = btem;
					inCart = true;
					break;
				}
			}
			holder.btnAddItem.setVisibility(View.VISIBLE);
			holder.layInCart.setVisibility(View.GONE);
			holder.lblPrice.setText("$ " + item.getPrice());
			holder.btnDelete.setVisibility(View.GONE);
			holder.lblCookMethod.setVisibility(View.GONE);
			// Add new
			// if (inCart) {
			// // holder.btnDelete.setVisibility(View.VISIBLE);
			// // holder.btnAddItem.setVisibility(View.GONE);
			// // holder.layInCart.setVisibility(View.VISIBLE);
			// holder.lblPrice.setText("$ " + item.getPrice() + " (+$ "
			// + itemcart.getPriceTopping() + ")");
			// String temp = "";
			// for (Relish relishItem : itemcart.getArrRelish()) {
			// if (!relishItem.getSelectedOption().getName()
			// .equals(Constant.NONE)) {
			// temp += relishItem.getName().trim()
			// + ":"
			// + relishItem.getSelectedOption().getName()
			// .trim() + ",";
			// }
			// }
			// if (temp.length() > 1) {
			// temp = temp.substring(0, temp.length() - 1);
			// holder.lblToppingPrice.setText(temp);
			// holder.lblToppingPrice.setSelected(true);
			// } else {
			// holder.lblToppingPrice.setText("");
			// }
			// holder.lblAddQuantity.setText(itemcart.getQuantities() + "");
			// } else {
			// holder.lblPrice.setText("$ " + item.getPrice());
			// // holder.btnDelete.setVisibility(View.GONE);
			// // holder.btnAddItem.setVisibility(View.VISIBLE);
			// // holder.layInCart.setVisibility(View.GONE);
			// }

			// Start Remove by Cuongpm
			// if (item.getSelectedCookMethod() != null) {
			// holder.lblCookMethod.setText(item.getSelectedCookMethod()
			// .getName());
			// holder.lblCookMethod.setVisibility(View.VISIBLE);
			// } else {
			// holder.lblCookMethod.setVisibility(View.GONE);
			// }
			// End Remove by Cuongpm

			AQuery aq = lstAq.recycle(convertView);
			holder.lblFoodName.setText(item.getName());
			holder.lblCategoryName.setText(item.getCategory().getName());
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

			holder.btnAddTopping.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tempPosition = 0;
					for (ItemCart btem : arrCart) {
						if (btem.getItem().getId().equals(item.getId())) {
							break;
						} else {
							tempPosition++;
						}
					}
					listener.clickAddTopping(tempPosition,
							arrCart.get(tempPosition));
				}
			});
			holder.btnAddIntroduction.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tempPosition = 0;
					for (ItemCart btem : arrCart) {
						if (btem.getItem().getId().equals(item.getId())) {

							break;
						} else {
							tempPosition++;
						}
					}
					listener.clickAddIntroduction(tempPosition,
							arrCart.get(tempPosition));
				}
			});
			holder.btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tempPosition = 0;
					for (ItemCart btem : arrCart) {
						if (btem.getItem().getId().equals(item.getId())) {
							break;
						} else {
							tempPosition++;
						}
					}
					listener.clickDelete(tempPosition);
				}
			});
			holder.lblAddQuantity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tempPosition = 0;
					for (ItemCart btem : arrCart) {
						if (btem.getItem().getId().equals(item.getId())) {

							break;
						} else {
							tempPosition++;
						}
					}
					listener.clickAddQuantities(tempPosition,
							arrCart.get(tempPosition));
				}
			});
		}

		holder.btnAddItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tempPosition = 0;
				for (ItemCart btem : arrCart) {
					if (btem.getItem().getId().equals(item.getId())) {

						break;
					} else {
						tempPosition++;
					}
				}
				listener.clickAddButton(tempPosition, item);
			}
		});

		holder.lblCookMethod.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.clickDisplaylistMethod(position, item.getCategory()
						.getArrCookMethods());
			}
		});

		return convertView;
	}

	public class HolderView {
		private TextView lblCategoryName, lblFoodName, lblCookMethod, lblPrice,
				btnAddTopping, lblToppingPrice, btnAddIntroduction,
				lblAddQuantity, btnAddItem;
		private ImageView imgFood, btnDelete;
		private ProgressBar progess;
		private LinearLayout layInCart;
	}

}
