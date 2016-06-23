package com.hcpt.fastfood.utility;

import com.hcpt.fastfood.object.ItemCart;

public class CustomCompare {
	public static boolean compareItemCard(ItemCart itemOne, ItemCart itemTwo) {
		if (!itemOne.getItem().getId().equals(itemTwo.getItem().getId())) {
			return false;
		}

		if (!itemOne.getInsTructions().equals(itemTwo.getInsTructions())) {
			return false;
		}

		if (itemOne.getArrRelish().size() != itemTwo.getArrRelish().size()) {
			return false;
		} else {
			for (int i = 0; i < itemOne.getArrRelish().size(); i++) {
				if (itemOne.getArrRelish().get(i).getSelectedOption().getName() != itemTwo
						.getArrRelish().get(i).getSelectedOption().getName()) {
					return false;
				}
			}
		}
		return true;
	}
}
