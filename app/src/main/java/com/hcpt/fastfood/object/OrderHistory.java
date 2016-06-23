package com.hcpt.fastfood.object;

import java.util.ArrayList;

public class OrderHistory {
	private String id;
	private String orderName;
	private String orderTel;
	private String orderPrice;
	private String created;
	private String orderAddress;
	private String orderStatus;
	private String totalItems;
	private ArrayList<ItemHistory> listItem;

	public OrderHistory() {
		listItem = new ArrayList<ItemHistory>();
	}

	public String getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(String totalItems) {
		this.totalItems = totalItems;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderTel() {
		return orderTel;
	}

	public void setOrderTel(String orderTel) {
		this.orderTel = orderTel;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public ArrayList<ItemHistory> getListItem() {
		return listItem;
	}

	public void setListItem(ArrayList<ItemHistory> listItem) {
		this.listItem = listItem;
	}

}
