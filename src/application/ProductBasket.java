package application;

import java.util.ArrayList;
import java.util.List;

import application.domain.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductBasket {
	private final static ObservableList<Product> LIST = init();

	private static ObservableList<Product> init() {
		return FXCollections.observableArrayList();
	}

	public static List<Product> getItems() {
		return LIST;
	}

	public List<List<String>> getSummaryList() {
		List<List<String>> summaryList = new ArrayList<>();
		List<String> list = new ArrayList<>();

		for (Product p : getItems()) {
			list.add(p.getOverprintCode());
			list.add(p.getName());
			list.add(p.getQuantity());
			list.add(p.getBasePrice());

			summaryList.add(list);
			list = new ArrayList<>();
		}
		return summaryList;
	}

	public static Product getItem(int index) {
		return LIST.get(index);
	}

	public static void addItems(ObservableList<Product> items) {
		LIST.addAll(items);
	}
	public static void addItem(Product item) {
		LIST.add(item);
	}
}
