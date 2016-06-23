package com.hcpt.fastfood.modelmanager;

public interface ModelManagerListener {
	public void onError();

	public void onSuccess(String json);
}
