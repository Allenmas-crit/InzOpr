package utils;

public class Utils {

	public static Double euroPlnExchangeRate = 4.69;

	public static boolean isNotNegativeInteger(String str) {
		return str != null && str.matches("[0-9]+");
	}

	public static boolean isPositiveInteger(String str) {
		return isNotNegativeInteger(str) && !str.equals("0");
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static Double PLNToEUR(Double pricePLN) {
		return pricePLN/euroPlnExchangeRate;
	}

	public static Double EURToPLN(Double priceEUR) {
		return priceEUR/(1/euroPlnExchangeRate);
	}

	public static String formatMoneyValues(Double price) {
		Double priceRoundedUp = roundUp(price);
		return String.format("%.2f", priceRoundedUp);
	}

	public static double roundUp(Double price) {
		return Math.ceil(price * 100)/100;
	}

	public static double parsePrice(String price) {
		return Double.valueOf(price.replaceAll(",",".").replaceAll("[^\\d.]+", ""));
	}
}
//wyslac projekt
//zrob exe z projektu