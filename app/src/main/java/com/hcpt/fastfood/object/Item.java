package com.hcpt.fastfood.object;

import com.hcpt.fastfood.config.GlobalValue;

public class Item extends BaseObject {

	// id: "1394421159",
	// name: "Coca-Cola",
	// desc: "We believe it&#39;s not just what you do but how you do it ",
	// thumb:
	// "http://fruitysolution.com/dwaine/site/image.html?id=1394421159&f=1397467940.jpg&t=products",
	// small_thumb:
	// "http://fruitysolution.com/dwaine/site/image.html?id=1394421159&f=S1397467940.jpg&t=products",
	// price: "2",
	// promotion: null,
	// promotion_desc: "",
	// urls_image: null,
	// urls_video: null,
	// menu: "1397446507"

	private String id, name, desc, thumb, menuId;
	private double price;
	private CookMethod selectedCookMethod;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Category getCategory() {
		for (Category category : GlobalValue.getInstance().arrCategories) {
			if (menuId.equals(category.getId())) {
				return category;
			}
		}
		return null;
	}

	public CookMethod getSelectedCookMethod() {

		if (selectedCookMethod != null)
			return selectedCookMethod;
		if (getCategory().getArrCookMethods().size() > 0) {
			selectedCookMethod = getCategory().getArrCookMethods().get(0);
		}
		return selectedCookMethod;
	}

	public void setSelectedCookMethod(CookMethod selectedCookMethod) {
		this.selectedCookMethod = selectedCookMethod;
	}

}
