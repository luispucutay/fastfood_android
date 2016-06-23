package com.hcpt.fastfood.object;

import java.util.ArrayList;

public class Relish extends BaseObject {

	private String id, name;
	private double price;
	private ArrayList<RelishOption> arrOptions;
	private int selectedIndex = 0;
	private String selectedToppingOption = "";
	private boolean isChecked;

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ArrayList<RelishOption> getArrOptions() {
		return arrOptions;
	}

	public void setArrOptions(ArrayList<RelishOption> arrOptions) {
		this.arrOptions = arrOptions;
	}

	public ArrayList<String> getRelishOptionName() {
		ArrayList<String> arr = new ArrayList<String>();
		if (arrOptions != null)
			for (RelishOption option : arrOptions) {
				arr.add(option.getName());
			}
		return arr;
	}

	public RelishOption getSelectedOption() {
		return arrOptions.get(selectedIndex);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public String getSelectedToppingOption() {
		return selectedToppingOption;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		if (arrOptions.size() > 0) {
			this.selectedToppingOption = arrOptions.get(selectedIndex).getId();
		}
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
