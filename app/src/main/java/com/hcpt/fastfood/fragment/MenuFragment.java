package com.hcpt.fastfood.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.fastfood.BaseFragment;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.adapter.CategoryAdapter;
import com.hcpt.fastfood.adapter.CookMethodAdapter;
import com.hcpt.fastfood.adapter.MenuItemAdapter;
import com.hcpt.fastfood.adapter.ToppingAdapter;
import com.hcpt.fastfood.adapter.MenuItemAdapter.MenuItemAdapterListener;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.Category;
import com.hcpt.fastfood.object.CookMethod;
import com.hcpt.fastfood.object.Item;
import com.hcpt.fastfood.object.ItemCart;
import com.hcpt.fastfood.object.Relish;
import com.hcpt.fastfood.utility.CustomCompare;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshListView;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

public class MenuFragment extends BaseFragment implements OnClickListener {

	// -----------------------------------------------------------------------------------------------------------------------------//
	// ------------------------- Bien cho view
	// -----------------------------------------------------------------------------------------------------------------------------//
	private TextView lblCategoryName;
	private ImageView imgCategoryName;
	private PullToRefreshListView lsvItems;
	private ArrayList<Item> arrItems;
	private ArrayList<Category> arrCategories;
	private CategoryAdapter categoryAdapter;
	private MenuItemAdapter itemAdapter;
	private Activity self;
	private Category currentCategory;
	private Dialog mDialog;
	private TextView btnCancel, btnSave;
	private ToppingAdapter mToppingAdapter;
	private EditText edtInsTruction;
	double totalPrice = 0;
	int totalItems = 0;
	private ArrayList<ItemCart> arrCartItems = new ArrayList<ItemCart>();
	private int currentPage = 1;

	// -----------------------------------------------------------------------------------------------------------------------------//
	// ------------------------- Bien cho dialog Add
	// -----------------------------------------------------------------------------------------------------------------------------//
	private TextView lblPrice;
	private TextView lblToppingPrice;
	private TextView lblAddQuantity;
	private ItemCart currentItemCart;
	private AQuery aq;

	// -----------------------------------------------------------------------------------------------------------------------------//
	// ------------------------- Override
	// -----------------------------------------------------------------------------------------------------------------------------//

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		self = getActivity();
		aq = new AQuery(self);
		initUI(view);
		initControl();
		initData();
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v == lblCategoryName) {
			showDialogCategory();
		}
		if (v == imgCategoryName) {
			showDialogCategory();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------//
	// ------------------------- Control
	// -----------------------------------------------------------------------------------------------------------------------------//

	private void initControl() {
		lblCategoryName.setOnClickListener(this);
		imgCategoryName.setOnClickListener(this);
	}

	private void initUI(View view) {
		lblCategoryName = (TextView) view.findViewById(R.id.lblCategoryName);
		imgCategoryName = (ImageView) view.findViewById(R.id.img_selectCategory);
		lsvItems = (PullToRefreshListView) view.findViewById(R.id.lsvItems);

		lsvItems.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				arrItems.clear();
				getData(currentCategory.getId());
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getData(currentCategory.getId());
			}
		});
	}

	public void initData() {
		updateContent();
		setDataCategories();
	}

	private void getData(String menu_id) {
		ModelManager.getFoodByCategory("", menu_id, currentPage + "",
				getActivity(), true, new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						lsvItems.onRefreshComplete();
						if (!ParserUtility.parseListItem(json).isEmpty()) {
							arrItems.addAll(ParserUtility.parseListItem(json));
							if (GlobalValue.getInstance().arrCartItems != null
									& !arrCartItems.equals(GlobalValue
											.getInstance().arrCartItems)) {
								arrCartItems.clear();
								arrCartItems.addAll(GlobalValue.getInstance().arrCartItems);
							}
							itemAdapter.arrCart = arrCartItems;
							itemAdapter.notifyDataSetChanged();
							currentPage++;
						} else {
							showToastMessage(getResources().getString(
									R.string.have_no_more_data));
						}
					}

					@Override
					public void onError() {
						lsvItems.onRefreshComplete();
						showToastMessage(getResources().getString(
								R.string.have_some_error));
					}
				});
	}

	private void setDataCategories() {
		arrCategories = GlobalValue.getInstance().arrCategories;
		categoryAdapter = new CategoryAdapter(self, arrCategories);
		refreshListCategory(0);
		loadItembyCategory();
	}

	private void refreshListCategory(int position) {
		for (int i = 0; i < arrCategories.size(); i++) {
			if (i == position) {
				arrCategories.get(i).setSelected(true);
				currentCategory = arrCategories.get(i);
			} else {
				arrCategories.get(i).setSelected(false);
			}
		}
		categoryAdapter.notifyDataSetChanged();
	}

	public void loadItembyCategory() {
		currentPage = 1;
		lblCategoryName.setText(currentCategory.getName());
		if (arrItems == null) {
			arrItems = new ArrayList<Item>();
		} else {
			arrItems.clear();
		}
		getData(currentCategory.getId());
	}

	private void initItemlist() {
		arrItems = new ArrayList<Item>();
		itemAdapter = new MenuItemAdapter(self, arrItems,
				new MenuItemAdapterListener() {

					@Override
					public void clickDisplaylistMethod(int itemIndex,
							ArrayList<CookMethod> arrCook) {
						showDialogCookMethod(itemIndex, arrCook);
					}

					@Override
					public void clickAddButton(int itemIndex, Item item) {
						if (GlobalValue.getInstance().currentUser.getRole() != null) {
							if (GlobalValue.getInstance().currentUser.getRole()
									.equals("1")
									|| GlobalValue.getInstance().currentUser
											.getRole().equals("2")
									|| GlobalValue.getInstance().currentUser
											.getRole().equals("3")) {
								showToastMessage(getResources().getString(
										R.string.you_cannot_make_an_order));
							} else {
								addItem(item);
							}
						} else {
							addItem(item);
						}
					}

					@Override
					public void clickDelete(int itemIndex) {
						GlobalValue.getInstance().arrCartItems
								.remove(itemIndex);
						arrCartItems = GlobalValue.getInstance().arrCartItems;
						itemAdapter.arrCart = arrCartItems;
						itemAdapter.notifyDataSetChanged();
					}

					@Override
					public void clickAddTopping(int itemIndex, ItemCart itemCart) {
						if (itemCart.getArrRelish().size() > 0) {
							// showToppingsDialog(itemIndex, itemCart);
						} else {
							Toast.makeText(self,
									self.getString(R.string.error_no_topping),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void clickAddQuantities(int itemIndex, ItemCart item) {
						// showListQuantities(itemIndex);
					}

					@Override
					public void clickAddIntroduction(int itemIndex,
							ItemCart itemCart) {
						// showInstructionDialog(itemIndex, itemCart);

					}
				});
		if (GlobalValue.getInstance().arrCartItems != null
				& !arrCartItems.equals(GlobalValue.getInstance().arrCartItems)) {
			arrCartItems.clear();
			arrCartItems.addAll(GlobalValue.getInstance().arrCartItems);
			itemAdapter.arrCart = arrCartItems;
		}
		lsvItems.setAdapter(itemAdapter);

	}

	public void updateContent() {
		if (GlobalValue.getInstance().arrCartItems != null
				& !arrCartItems.equals(GlobalValue.getInstance().arrCartItems)) {
			arrCartItems.clear();
			arrCartItems.addAll(GlobalValue.getInstance().arrCartItems);
		}
		initItemlist();
	}

	private void showDialogCategory() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_select_category);
		dialog.setCancelable(false);
		ListView lsvCategory = (ListView) dialog
				.findViewById(R.id.lsvCategories);
		TextView lblClose = (TextView) dialog.findViewById(R.id.lblCancel);

		// set value list category :

		categoryAdapter.notifyDataSetChanged();
		lsvCategory.setAdapter(categoryAdapter);

		lblClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadItembyCategory();
				dialog.dismiss();
			}
		});

		lsvCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				refreshListCategory(position);
			}
		});

		dialog.show();
	}

	// -----------------------------------------------------------------------------------------------------------------------------//
	// ------------------------- Control for Add cart
	// -----------------------------------------------------------------------------------------------------------------------------//
	private void updateDialog(ItemCart mItemCart) {
		lblPrice.setText("$ " + mItemCart.getItem().getPrice() + " (+$ "
				+ mItemCart.getPriceTopping() + ")");
		String temp = "";
		for (Relish relishItem : mItemCart.getArrRelish()) {
			if (!relishItem.getSelectedOption().getName().equals(Constant.NONE)) {
				temp += relishItem.getName().trim() + ":"
						+ relishItem.getSelectedOption().getName().trim() + ",";
			}
		}
		if (temp.length() > 1) {
			temp = temp.substring(0, temp.length() - 1);
			lblToppingPrice.setText(temp);
			lblToppingPrice.setSelected(true);
		} else {
			lblToppingPrice.setText("");
		}
		lblAddQuantity.setText(mItemCart.getQuantities() + "");
	}

	private void showDialogAdd() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_add_item);
		dialog.setCancelable(false);

		// Get view
		TextView lblClose = (TextView) dialog.findViewById(R.id.lblCancel);
		TextView lblSave = (TextView) dialog.findViewById(R.id.lblSave);
		TextView lblFoodName = (TextView) dialog.findViewById(R.id.lblFoodName);
		TextView lblCategory = (TextView) dialog.findViewById(R.id.lblCategory);
		lblPrice = (TextView) dialog.findViewById(R.id.lblPrice);
		lblToppingPrice = (TextView) dialog.findViewById(R.id.lblToppingPrice);
		TextView btnAddIntroduction = (TextView) dialog
				.findViewById(R.id.btnAddIntroduction);
		TextView btnAddTopping = (TextView) dialog
				.findViewById(R.id.btnAddTopping);
		lblAddQuantity = (TextView) dialog.findViewById(R.id.lblAddQuantity);
		final ImageView imgFood = (ImageView) dialog.findViewById(R.id.imgFood);
		ProgressBar progess = (ProgressBar) dialog.findViewById(R.id.progess);

		// Set value
		lblFoodName.setText(currentItemCart.getItem().getName());
		lblCategory.setText(currentItemCart.getItem().getCategory().getName());
		lblFoodName.setText(currentItemCart.getItem().getName());

		// Update value
		updateDialog(currentItemCart);

		aq.id(imgFood)
				.progress(progess)
				.image(currentItemCart.getItem().getThumb(), true, true, 0,
						R.drawable.no_image, new BitmapAjaxCallback() {
							@SuppressLint("NewApi")
							@Override
							public void callback(String url, ImageView iv,
									Bitmap bm, AjaxStatus status) {
								if (bm != null) {
									Drawable d = new BitmapDrawable(
											getResources(), bm);
									imgFood.setBackgroundDrawable(d);
								} else {
									imgFood.setBackgroundResource(R.drawable.no_image);
								}
							}
						});

		btnAddTopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentItemCart.getArrRelish().size() > 0) {
					showToppingsDialog(currentItemCart);
				} else {
					Toast.makeText(self,
							self.getString(R.string.error_no_topping),
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		lblAddQuantity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showListQuantities(currentItemCart);
			}
		});

		btnAddIntroduction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showInstructionDialog(currentItemCart);
			}
		});

		lblClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		lblSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GlobalValue.getInstance().arrCartItems.size() > 0) {
					boolean checkAdd = true;
					for (int i = 0; i < GlobalValue.getInstance().arrCartItems
							.size(); i++) {
						if (CustomCompare.compareItemCard(
								GlobalValue.getInstance().arrCartItems.get(i),
								currentItemCart)) {
							GlobalValue.getInstance().arrCartItems
									.get(i)
									.setQuantities(
											currentItemCart.getQuantities()
													+ GlobalValue.getInstance().arrCartItems
															.get(i)
															.getQuantities());
							checkAdd = false;
							break;
						}
					}
					if (checkAdd) {
						GlobalValue.getInstance().arrCartItems
								.add(currentItemCart);
					}
				} else {
					GlobalValue.getInstance().arrCartItems.add(currentItemCart);
				}
				dialog.dismiss();
				Toast.makeText(
						self,
						getResources().getString(
								R.string.add_to_your_cart_successfully),
						Toast.LENGTH_SHORT).show();
				arrCartItems = GlobalValue.getInstance().arrCartItems;
				itemAdapter.arrCart = arrCartItems;
				itemAdapter.notifyDataSetChanged();
			}
		});

		dialog.show();
	}

	private void addItem(Item item) {
		if (GlobalValue.getInstance().arrCartItems == null)
			GlobalValue.getInstance().arrCartItems = new ArrayList<ItemCart>();
		currentItemCart = new ItemCart(item);
		showDialogAdd();
	}

	private void showToppingsDialog(final ItemCart mItemCart) {
		// Create view
		mDialog = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_toppings);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = 19 * (displaymetrics.widthPixels / 20);
		lp.height = 3 * (displaymetrics.heightPixels / 5);
		mDialog.getWindow().setAttributes(lp);
		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
		mDialog.setCancelable(false);

		// Get view
		ListView lvToppings = (ListView) mDialog.findViewById(R.id.lvToppings);
		btnCancel = (TextView) mDialog.findViewById(R.id.btnCancel);
		btnSave = (TextView) mDialog.findViewById(R.id.btnSave);

		// Set data
		if (mItemCart != null) {
			// reset topping adapter
			if (mToppingAdapter != null) {
				mToppingAdapter = null;
			}
			// creat adapter again
			mToppingAdapter = new ToppingAdapter(self, mItemCart);
			lvToppings.setAdapter(mToppingAdapter);
		}

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mToppingAdapter.getmItemCart();
				updateDialog(mItemCart);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	private void showListQuantities(final ItemCart mItemCart) {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_select_cook_method);
		dialog.setCancelable(false);

		TextView lblDialogTitle = (TextView) dialog
				.findViewById(R.id.lblDialogTitle);
		lblDialogTitle.setText(getResources().getString(R.string.add_item_quantity));
		ListView lsvCook = (ListView) dialog.findViewById(R.id.lsvCookMethod);
		Integer[] numberlist = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(self,
				android.R.layout.simple_list_item_single_choice, numberlist);
		lsvCook.setAdapter(adapter);
		lsvCook.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mItemCart.setQuantities((Integer) parent
						.getItemAtPosition(position));
				updateDialog(mItemCart);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showInstructionDialog(final ItemCart mItemCart) {
		// Create view
		mDialog = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_instruction);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = 4 * (displaymetrics.widthPixels / 5);
		mDialog.getWindow().setAttributes(lp);
		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
		mDialog.setCancelable(false);

		// Get view
		edtInsTruction = (EditText) mDialog.findViewById(R.id.edtInsTructions);
		if (null != mItemCart.getInsTructions()
				&& mItemCart.getInsTructions().length() > 0) {
			edtInsTruction.setText(mItemCart.getInsTructions());
		}
		btnCancel = (TextView) mDialog.findViewById(R.id.btnCancel);
		btnSave = (TextView) mDialog.findViewById(R.id.btnSave);
		if (mToppingAdapter != null) {
			mToppingAdapter = null;
		}
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edtInsTruction.getText().toString().length() > 0) {
					mItemCart.setInsTructions(edtInsTruction.getText()
							.toString());
					updateDialog(mItemCart);
				}
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	private void showDialogCookMethod(final int itemIndex,
			final ArrayList<CookMethod> arrCook) {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_select_cook_method);
		dialog.setCancelable(false);
		ListView lsvCook = (ListView) dialog.findViewById(R.id.lsvCookMethod);
		CookMethodAdapter adapter = new CookMethodAdapter(self, arrCook,
				arrItems.get(itemIndex));
		lsvCook.setAdapter(adapter);
		lsvCook.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CookMethod cookmethod = (CookMethod) parent
						.getItemAtPosition(position);
				arrItems.get(itemIndex).setSelectedCookMethod(cookmethod);
				itemAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
