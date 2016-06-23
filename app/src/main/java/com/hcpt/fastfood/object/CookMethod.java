package com.hcpt.fastfood.object;

import java.util.ArrayList;

public class CookMethod extends BaseObject {

	private String id, name;
	private boolean isSelected;
	private ArrayList<CookMethod> arrSubMethods;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

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

	public ArrayList<CookMethod> getArrSubMethods() {
		return arrSubMethods;
	}

	public void setArrSubMethods(ArrayList<CookMethod> arrSubMethods) {
		ArrayList<CookMethod> arr = new ArrayList<CookMethod>();
		for (CookMethod cookMethod : arrSubMethods) {
			try {
				arr.add((CookMethod) cookMethod.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				arr.add(cookMethod);
			}
		}
		this.arrSubMethods = arr;
	}

}
