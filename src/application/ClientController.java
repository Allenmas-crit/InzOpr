package application;

import javafx.scene.control.TextField;
import utils.Utils;

class ClientController {
	protected Integer getNumOfProducts(TextField numOfProductsField) {
		String str = numOfProductsField.getText();
		Integer number = (Utils.isPositiveInteger(str)) ? Integer.parseInt(str) : null;

		if (number != null) {
			numOfProductsField.setPromptText("Ile zamówiono sztuk?");
		} else {
			numOfProductsField.setPromptText("Liczba produktów powinna być > 0");
			numOfProductsField.setText("");
		}
		return number;
	}
}