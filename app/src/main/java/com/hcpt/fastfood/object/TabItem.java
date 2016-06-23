package com.hcpt.fastfood.object;

public class TabItem extends BaseObject{
	private String title;
	private int icon;
	private int icon_unselected;

	public TabItem(String title, int selectedIcon,int icon_unselected) {
		super();
		this.title = title;
		this.icon = selectedIcon;
		this.icon_unselected=icon_unselected;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon_unselected;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	public int getSelected() {
		return icon;
	}

}
