package jrange.myordy.util;

import jrange.myordy.entity.Ordy;



public final class OrdyUtil {

	public static int getFinalTotalAmount(final Ordy ordy) {
		int result = 0;
		if (null != ordy) {
			if (null != ordy.getFixedAmount()) {
				result = (int)(ordy.getFixedAmount().floatValue() * 100);
			} else {
				result = getOrderRawAmount(ordy) - (getDiscountAmount(ordy) + getCashback(ordy));
			}
		}
		return result;
	}
	public static int getOrderRawAmount(final Ordy ordy) {
		int result = 0;
		if (null != ordy) {
			if (null != ordy.getAmount()) {
				result += ordy.getAmount().intValue();
			}
			if (null != ordy.getDeliveryCharge()) {
				result += ordy.getDeliveryCharge().intValue();
			}
		}
		return result;
	}
	public static int getDiscountAmount(final Ordy ordy) {
		int result = 0;
		if (null != ordy && null != ordy.getDiscountPercent()) {
			result = (int)((ordy.getDiscountPercent() / 100) * getOrderRawAmount(ordy));
		}
		return result;
	}
	public static int getCashback(final Ordy ordy) {
		int result = 0;
		if (null != ordy && null != ordy.getCashback()) {
			result = ordy.getCashback().intValue();
		}
		return result;
	}
/*
		getDiscountAmount : function (posUI, order) {
			var result = 0;
			if (order) {
				if (order.discountPercent) {
					var discountPercent = order.discountPercent; //posUI.cartUI.getDiscountPercent(posUI, order);
					result = ((discountPercent / 100) * posUI.cartUI.getOrderRawAmount(posUI, order));
				}
			}
			return result;
		},
		getOrderRawAmount : function (posUI, order) {
			var result = 0;
			if (order) {
				result = (order.amount + order.deliveryCharge);
			}
			return result;
		},


*/
//	public static String getInvoiceReference(final Ordy ordy) {
//		String result = null;
//		if (null != ordy) {
//			if (null != ordy.getOrdyNumber() && null != ordy.getOrderTime()) {
//				result = String.format("%d-%d", ordy.getOrderTime().getTime(), ordy.getOrdyNumber());
//			}
//		}
//		return result;
//	}

//	public static String getEwayTransactionRef(final Integer transactionID) {
//		String result = null;
//		if (null != transactionID) {
//			result = String.format("EWAY-%d", transactionID);
//		}
//		return result;
//	}

}
