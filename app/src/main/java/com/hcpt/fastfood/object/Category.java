package com.hcpt.fastfood.object;

import java.util.ArrayList;

public class Category extends BaseObject{
	private static final String TAG = Category.class.getName();
	private String id, name;
	private boolean isPanini = false;
	private ArrayList<Relish> arrRelishs;
	private ArrayList<CookMethod> arrCookMethods;
	private boolean isSelected = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPanini() {
		return isPanini;
	}

	public void setPanini(boolean isPanini) {
		this.isPanini = isPanini;
	}

	public ArrayList<Relish> getArrRelishs() {
		return arrRelishs;
	}

	public void setArrRelishs(ArrayList<Relish> arrRelishs) {
		this.arrRelishs = arrRelishs;
	}

	public ArrayList<CookMethod> getArrCookMethods() {
		return arrCookMethods;
	}

	public void setArrCookMethods(ArrayList<CookMethod> arrCookMethods) {
		this.arrCookMethods = arrCookMethods;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}	
}
