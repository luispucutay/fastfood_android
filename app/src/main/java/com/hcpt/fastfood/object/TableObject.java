package com.hcpt.fastfood.object;

public class TableObject extends BaseObject {
	private String id;
	private String name;
	private String num_of_seat;
	private String current_seat;

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

	public String getNum_of_seat() {
		return num_of_seat;
	}

	public void setNum_of_seat(String num_of_seat) {
		this.num_of_seat = num_of_seat;
	}

	public String getCurrent_seat() {
		return current_seat;
	}

	public void setCurrent_seat(String current_seat) {
		this.current_seat = current_seat;
	}

}
