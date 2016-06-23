package com.hcpt.fastfood.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;

import com.hcpt.fastfood.config.GlobalValue;

public class ItemCart extends BaseObject implements Serializable {
	private static final String TAG = ItemCart.class.getName();
	private static final long serialVersionUID = -7060210544600464481L;
	private Item item;

	// using for cart
	private CookMethod selectedCookMethod, selectedSubCookMethod;
	private List<Relish> arrRelish;
	private int mChosenRelishOptionIndex;
	private String insTructions;

	public String getInsTructions() {
		return insTructions != null ? insTructions : "";
	}

	public void setInsTructions(String insTructions) {
		this.insTructions = insTructions;
	}

	public int getmChosenRelishOptionIndex() {
		return mChosenRelishOptionIndex;
	}

	public void setmChosenRelishOptionIndex(int mChosenRelishOptionIndex) {
		this.mChosenRelishOptionIndex = mChosenRelishOptionIndex;
	}

	private int quantities = 1;

	public ItemCart() {

	}

	public ItemCart(Item item) {
		try {
			this.item = (Item) item.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			this.item = item;
		}
		try {
			if (item.getSelectedCookMethod() != null) {
				this.selectedCookMethod = (CookMethod) item
						.getSelectedCookMethod().clone();
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			this.selectedCookMethod = item.getSelectedCookMethod();
		}
		setSelectedSubCookMethod();
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;

	}

	@SuppressLint("NewApi")
	public List<Relish> getArrRelish() {
		if (arrRelish == null) {
			arrRelish = new ArrayList<Relish>();
			if (item.getMenuId() != null && !item.getMenuId().isEmpty()) {
				List<Category> categories = GlobalValue.getInstance()
						.getArrCategories();
				if (categories != null && categories.size() > 0) {
					for (Category category : categories) {
						if (item.getMenuId().equalsIgnoreCase(category.getId())) {
							arrRelish.addAll(category.getArrRelishs());
						}
					}
				}
			}
		}
		return arrRelish;
	}

	public void setArrRelish(List<Relish> arrSelectedRelish) {
		this.arrRelish = arrSelectedRelish;
	}

	public CookMethod getSelectedCookMethod() {
		return selectedCookMethod;
	}

	public void setSelectedCookMethod(CookMethod selectedCookMethod) {
		this.selectedCookMethod = selectedCookMethod;
	}

	public int getQuantities() {
		return quantities;
	}

	public void setQuantities(int quantities) {
		this.quantities = quantities;
	}

	public double getPriceTopping() {

		double total = 0;
		for (Relish relish : getArrRelish()) {
			if (relish.getSelectedIndex() != 0)
				total += relish.getPrice();
		}
		return total;
	}

	public double getTotalPrice() {
		return (item.getPrice() + getPriceTopping()) * quantities;
	}

	public CookMethod getSelectedSubCookMethod() {
		return selectedSubCookMethod;
	}

	public void setSelectedSubCookMethod(CookMethod selectedSubCookMethod) {
		this.selectedSubCookMethod = selectedSubCookMethod;
	}

	private void setSelectedSubCookMethod() {
		if (selectedCookMethod != null) {
			if (selectedCookMethod.getArrSubMethods() != null
					&& !selectedCookMethod.getArrSubMethods().isEmpty()) {
				try {
					selectedSubCookMethod = (CookMethod) selectedCookMethod
							.getArrSubMethods().get(0).clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					selectedSubCookMethod = (CookMethod) selectedCookMethod
							.getArrSubMethods().get(0);
				}
			}
		}
	}
}
