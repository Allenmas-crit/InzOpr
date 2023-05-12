package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

import com.itextpdf.text.DocumentException;

import application.domain.Product;
import dataReader.RawMaterialReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pdfCreator.PDFCreator;
import utils.Utils;

public class SummaryController implements Initializable {

	private Product currentProduct = null;
	private double basePrice;
	private String currency = "PLN";

	@FXML
	private TableView<Product> table;
	public TableColumn<Product, String> overprintName;
	public TableColumn<Product, String> productName;
	public TableColumn<Product, String> numOfProducts;
	public TableColumn<Product, Double> clientPrice;
	@FXML
	private ChoiceBox<String> discountChoiceBox;
	@FXML
	private ChoiceBox<String> ProductChoiceBox;
	@FXML
	private Label overprintLabel;
	@FXML
	private TextField filepathField;
	@FXML
	private TextField fileNameField;
	@FXML
	private TextField numOfProductsField;
	@FXML
	private TextField multRawMaterialPriceField;
	@FXML
	private Button addToBasketButton;
	@FXML
	private Button generatePDFButton;
	@FXML
	private Button setFilepathButton;
	@FXML
	private ToggleButton PLN_EUR_Toggle;

	public void addNewProduct(ActionEvent event) {
		ProductBasket.addItem(currentProduct);
		switchSceneToMain(event);
	}

	private void switchSceneToMain(ActionEvent event) {
		Parent root = null;
		Stage stage;
		Scene scene;

		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setScene(scene);
		stage.show();
	}

	public void setNumOfProductsField(String text) {
		numOfProductsField.setText(text);
	}

	public void setDiscountChoiceBox(String[] discountValues, int selectedIndex) {
		discountChoiceBox.getItems().addAll(discountValues);
		discountChoiceBox.getSelectionModel().select(selectedIndex);
	}

	public void displayOverprintLabel(String overprint) {
		overprintLabel.setText(overprint);
	}

	public void setProductChoiceBox(String str) {
		String lines[] = str.split("\\r?\\n");
		ProductChoiceBox.getItems().addAll(lines);
		ProductChoiceBox.getSelectionModel().select("<Wybierz produkt>");
	}

	public void setFilepath(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		DirectoryChooser directoryChooser = new DirectoryChooser();

		directoryChooser
				.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
		File selectedFolder = directoryChooser.showDialog(stage);

		if (selectedFolder != null) {
			filepathField.setText(selectedFolder.getAbsolutePath());
			shouldPDFButtonBeDisabled();
		}
	}

	public void generatePDF(ActionEvent event) {
		PDFCreator pdf = new PDFCreator();
		String filepath = filepathField.getText();
		String title = fileNameField.getText();
		StringBuilder sb = new StringBuilder(filepath);

		if (!filepath.endsWith("\\"))
			sb.append("\\");

		pdf.setFilepath(sb.toString())
		.setTitle(title)
		.setProductPrice(getPriceOfAllProducts())
		.setItems(table.getItems())
		.setCurrency(currency);

		try {
			pdf.createPDF();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	/* TODO
	 * 
	 * kolory moga byc w niektorych arkuszach literami, badz calym stringiem, pozniej trzeba to uwzglednic
	 * (!) ceny-materialow.ods zawiera zmyślone dane, potrzeba pliku z danymi z bazy danych. 
	 * -> tylko dlatego mozna dodac wylacznie rzeczy z tampodruku.
	 */
	private double getRawMaterialPrice() {
		String desc = getProdName();
		if(!isProdNameValid() || !isNumOfProdValid()) return 0.0;

		String overprint2 = currentProduct.getOverprintCode();
		String colors = currentProduct.getNumOfColors();

		RawMaterialReader reader = new RawMaterialReader();

		reader.getRowsByDescFromFile(desc);
		reader.filterOutRowsBySecSeqOfOverprint(overprint2);
		if(Utils.isNotNegativeInteger(colors)) {
			reader.filterOutRowsByNumOfColors(colors + ".0");
		}
		else {
			throw new IllegalArgumentException("In certain sheets, values for color are no longer numbers. It's time to fix it." + colors);
		}

		double rawMaterialPrice = reader.getRawMaterialPrice();
		reader.clear();
		return rawMaterialPrice;
	}

	private Double getMultiplier() {
		return Double.valueOf(multRawMaterialPriceField.getText());
	}

	public void updateTableFirstTime(List<String> cellValues) {
		List<String> summaryList = new ArrayList<>();

		summaryList.add(cellValues.get(0));
		summaryList.add(cellValues.get(1));
		summaryList.add(cellValues.get(2));
		summaryList.add(cellValues.get(3));
		setBasePrice(Utils.parsePrice(cellValues.get(4)));
		summaryList.add("price");

		currentProduct = new Product(summaryList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		overprintName.setCellValueFactory(new PropertyValueFactory<>("overprintCode"));
		productName.setCellValueFactory(new PropertyValueFactory<>("name"));
		clientPrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
		numOfProducts.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		setDefaultFileNameField();

		ProductChoiceBox.setOnAction(event -> {
			processData();
			shouldPDFButtonBeDisabled();
		});
		numOfProductsField.setOnAction(event -> {
			processData();
			shouldPDFButtonBeDisabled();
		});

		multRawMaterialPriceField.setOnAction(event -> {
			shouldPDFButtonBeDisabled();
		});
		filepathField.setOnAction(event -> {
			shouldPDFButtonBeDisabled();
		});
		fileNameField.setOnAction(event -> {
			shouldPDFButtonBeDisabled();
		});
		
		PLN_EUR_Toggle.setOnAction(event -> {
			togglePLN_EUR_Pricing();
		});
	}

	private void togglePLN_EUR_Pricing() {
		if(PLN_EUR_Toggle.getText().equals("Wycena w EUR")) {
			PLN_EUR_Toggle.setText("Wycena w PLN");
			currency = "PLN";
		}
		else {
			PLN_EUR_Toggle.setText("Wycena w EUR");
			currency = "EUR";
		}
	}

	private void setDefaultFileNameField() {
		Long randNum = System.currentTimeMillis();
		fileNameField.setText("Wycena " + randNum.toString().substring(7));
	}

	private void processData() {
		Integer numOfProd = getNumOfProd();
		updateTable(getProdName(), numOfProd, getProductPrice());

		if (!isProdNameValid() || !isNumOfProdValid())
			return;
		addToBasketButton.setDisable(false);
	}

	private void shouldPDFButtonBeDisabled() {
		// for windows
		boolean isFilepathMatched = isFilepathMathed();
		boolean isFilenameMatched = isFilenameMatched();
		boolean isAddToBasketButtonDisabled = isAddToBasketButtonDisabled();
		boolean isMultiplierValid = isMultiplierValid(multRawMaterialPriceField);

		if (!isFilepathMatched || !isFilenameMatched || isAddToBasketButtonDisabled || !isMultiplierValid) {
			generatePDFButton.setDisable(true);
		} else {
			generatePDFButton.setDisable(false);
		}
	}

	private Boolean isMultiplierValid(TextField mult) {
		Double res;
		try {
			res = Double.parseDouble(mult.getText());
		} catch (NumberFormatException e) {
			return false;
		}

		if (res <= 0)
			return false;
		return true;
	}

	private boolean isAddToBasketButtonDisabled() {
		return addToBasketButton.isDisabled();
	}

	private boolean isFilenameMatched() {
		String filename = fileNameField.getText();
		if (filename.trim().length() != filename.length())
			return false;

		String regularExpression = "^[^*&%]+$";
		Pattern.compile(regularExpression);
		return Pattern.matches(regularExpression, filename);
	}

	private boolean isFilepathMathed() {
		String filepath = filepathField.getText();
		String regularExpression = "([a-zA-Z]:)" + "(\\\\" + "([\\p{L}\\p{N}_.-]+)" // first letter without space
				+ "([ \\p{L}\\p{N}_.-]+))*" + "+\\\\?";
		Pattern.compile(regularExpression);
		return Pattern.matches(regularExpression, filepath);
	}

	private Double getProductPrice() {
		return (getMultiplier() * getRawMaterialPrice() + getBasePrice()) * getNumOfProd() * (1 - getDiscount());
	}

	private Double getPriceOfAllProducts() {
		List<Product> items = ProductBasket.getItems();
		double sum = 0.0;
		Scanner sc;

		for (Product product : items) {
			sc = new Scanner(product.getBasePrice());
			sum += Double.valueOf(sc.nextDouble());
		}
		return sum + getProductPrice();
	}

	private double getDiscount() {
		String discount = discountChoiceBox.getSelectionModel().getSelectedItem();
		if(discount == null) return 0;
		Matcher matcher = Pattern.compile("\\d+").matcher(discount);
		matcher.find();
		int i = Integer.valueOf(matcher.group());
		return i / 100.0;
	}

	private void updateTable(String prodName, Integer numOfProd, Double totalPrice) {
		Product currItem = currentProduct;

		String quantity = (numOfProd == null) ? currItem.getQuantity() : numOfProd.toString();

		Product currProd = new Product(currItem.getOverprintCode(), prodName, currItem.getNumOfColors(), quantity,
				Utils.formatMoneyValues(totalPrice).concat(" zł"));
		ObservableList<Product> prodToShow = FXCollections.observableArrayList();
		List<List<String>> addedProducts = new ProductBasket().getSummaryList();

		for (List<String> p : addedProducts) {
			if (p.size() != 0) {
				prodToShow.add(new Product(p.get(0), p.get(1), "", p.get(2), p.get(3)));
			}
		}

		currentProduct = currProd;
		// set table
		Collections.reverse(prodToShow);
		prodToShow.add(0, currProd);
		table.setItems(FXCollections.observableArrayList(prodToShow));
	}

	private Integer getNumOfProd() {
		Integer quantity = getNumOfProducts(numOfProductsField);
		if (quantity != null) {
			return getNumOfProducts(numOfProductsField);
		} else {
			return 0;
		}
	}

	private String getProdName() {
		return ProductChoiceBox.getSelectionModel().getSelectedItem();
	}

	private boolean isNumOfProdValid() {
		return Utils.isNotNegativeInteger(numOfProductsField.getText());
	}

	private boolean isProdNameValid() {
		String prodName = ProductChoiceBox.getSelectionModel().getSelectedItem();
		if (prodName == "<Wybierz produkt>")
			return false;
		return true;
	}

	private double getBasePrice() {
		return basePrice;
	}

	private void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
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
