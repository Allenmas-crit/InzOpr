package application.domain;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class Product{

	private SimpleStringProperty overprintCode;
	private SimpleStringProperty name; //opis produktow, konkretne nazwy
	private SimpleStringProperty numOfColors;
	private SimpleStringProperty quantity;
	private SimpleStringProperty basePrice;

	public Product(String overprintCode, String name, String colors, String quantity, String basePrice) {
		super();
		this.overprintCode = new SimpleStringProperty(overprintCode);
		this.name = new SimpleStringProperty(name);
		this.numOfColors = new SimpleStringProperty(colors);
		this.quantity = new SimpleStringProperty(quantity);
		this.basePrice = new SimpleStringProperty(basePrice);
	}

	public Product(List<String> list) {
		super();
		this.overprintCode = new SimpleStringProperty(list.get(0));
		this.name = new SimpleStringProperty(list.get(1));
		this.numOfColors = new SimpleStringProperty(list.get(2));//
		this.quantity = new SimpleStringProperty(list.get(3));
		this.basePrice = new SimpleStringProperty(list.get(4));
	}

	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		if(name == "" || (name == null)) throw new IllegalArgumentException("Name is not set");
		this.name = new SimpleStringProperty(name);
	}
	public String getNumOfColors() {
		return numOfColors.get();
	}
	public void setNumOfColors(String numOfColors) {
		this.numOfColors = new SimpleStringProperty(numOfColors);
	}
	public String getQuantity() {
		return quantity.get();
	}

	public void setQuantity(String quantity) {
		this.quantity = new SimpleStringProperty(quantity);
	}

	public String getBasePrice() {
		return basePrice.get();
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = new SimpleStringProperty(basePrice);
	}

	public String getOverprintCode() {
		return overprintCode.get();
	}

	public void setOverprintCode(String overprintCode) {
		this.overprintCode = new SimpleStringProperty(overprintCode);
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", numOfColors=" + numOfColors + ", quantity=" + quantity
				+ ", basePrice=" + basePrice + ", overprintCode=" + overprintCode + "]\n";
	}
}