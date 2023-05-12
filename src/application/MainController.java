package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import ReaderControllerBridge.ODSMainBridge;
import application.domain.Product;
import dataReader.SheetElAllocation;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.Utils;

public class MainController implements Initializable {

	private String[] overprintNames = {};
	private String[] sheetNames = {};
	final private String[] discountValues = { "0%", "20%", "25%", "30%", "35%" };

	private List<String> cellValues;

	@FXML
	private ChoiceBox<String> overprintChoiceBox;
	@FXML
	private ChoiceBox<String> discountChoiceBox;
	@FXML
	private ChoiceBox<String> colorChoiceBox;
	@FXML
	private ChoiceBox<String> sheetChoiceBox;
	@FXML
	private TableView<Product> table;
	public TableColumn<Product, String> overprintName;
	public TableColumn<Product, String> productName;
	public TableColumn<Product, String> numOfColors, numOfProducts;
	public TableColumn<Product, Double> basePrice;
	@FXML
	private Label overprintLabel;
	@FXML
	private TextField numOfProductsField;
	@FXML
	private Button addProduct;

	private ODSMainBridge bridge;
	final SheetElAllocation se = new SheetElAllocation();

	public void switchSceneToSummary(ActionEvent event) {
		Parent root = null;
		Stage stage;
		Scene scene;
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Summary.fxml"));
			root = loader.load();
			addDataToSummary(loader);

		} catch (IOException e) {
			e.printStackTrace();
		}
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setScene(scene);
		stage.show();
	}

	private void addDataToSummary(FXMLLoader loader) {
		SummaryController sc = loader.getController();
		sc.displayOverprintLabel(overprintLabel.getText());
		sc.updateTableFirstTime(cellValues);
		sc.setProductChoiceBox(cellValues.get(1));
		sc.setDiscountChoiceBox(discountValues, discountChoiceBox.getSelectionModel().getSelectedIndex());
		sc.setNumOfProductsField(numOfProductsField.getText());
	}

	public void showTable(ActionEvent event) throws IOException {
		int sheetId = sheetChoiceBox.getSelectionModel().getSelectedIndex();

		List<Integer> columns = new ArrayList<>();
		columns.add(se.getFirstColumnNumberByName(sheetId, "Nadruk") + 1);
		columns.add(se.getFirstColumnNumberByName(sheetId, "Opis"));
		columns.add(se.getPositionOfColorColumn(sheetId));
		columns.add(se.getFirstColumnNumberByName(sheetId, "Przedział"));
		columns.add(se.getFirstColumnNumberByName(sheetId, "Cena") + getDiscount());

		cellValues = bridge.getCustomCellValues(sheetId, getRowOveprint(), columns);

		enableSwitchToSummaryButton();
		showTable();
	}
	
	private Integer getDiscount() {
		return discountChoiceBox.getSelectionModel().getSelectedIndex();
	}

	private int getRowOveprint() throws IOException {
		int sheetId = sheetChoiceBox.getSelectionModel().getSelectedIndex();
		String selectedOverprint = overprintChoiceBox.getSelectionModel().getSelectedItem();
		int colColor = se.getPositionOfColorColumn(sheetId);
		int startingRowOp = se.getDataStartRow(sheetId);

		int rowOp = bridge.findOverprintRowByName(sheetId, startingRowOp, 0, selectedOverprint);

		Integer numOfProd = getNumOfProducts(numOfProductsField);
		String colorVal = colorChoiceBox.getSelectionModel().getSelectedItem();
		int rowShift = getShiftByTheNumOfProd(rowOp, numOfProd);

		Integer rowCol = bridge.getRowWithCorrColorNum(sheetId, rowOp, colColor, colorVal);
		return rowShift + rowCol;
	}

	private int getShiftByTheNumOfProd(Integer rowOverprint, Integer numOfProd) throws IOException {
		int sheetId = sheetChoiceBox.getSelectionModel().getSelectedIndex();
		int shift = 0;
		Integer prodQuantity = numOfProd;

		if (prodQuantity != null) {
			String str;
			List<String> maxQuantityRanges = bridge.getColRange(sheetId, rowOverprint, 7);
			for (Iterator<String> iter = maxQuantityRanges.iterator(); iter.hasNext();) {
				str = iter.next();
				shift = maxQuantityRanges.indexOf(str);
				if (Double.valueOf(str) >= prodQuantity)
					break;
			}
		}
		return shift;
	}
	
	private void enableSwitchToSummaryButton() {
		addProduct.setDisable(false);
	}

	private void showTable() {
		if (Utils.isNumeric(cellValues.get(2)))
			cellValues.set(2, cellValues.get(2).substring(0, cellValues.get(2).indexOf('.')));

		ObservableList<Product> observableList = FXCollections.observableArrayList(
				new Product(cellValues.get(0), cellValues.get(1), cellValues.get(2), cellValues.get(3), cellValues.get(4)));
		table.setItems(observableList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		overprintName.setCellValueFactory(new PropertyValueFactory<>("overprintCode"));
		productName.setCellValueFactory(new PropertyValueFactory<>("name"));
		numOfColors.setCellValueFactory(new PropertyValueFactory<>("numOfColors"));
		basePrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
		numOfProducts.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		int sheetId = 4;
		bridge = new ODSMainBridge();

		try {
			sheetNames = bridge.getSheets();
			overprintNames = bridge.getOverprint(sheetId, 3, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sheetChoiceBox.getItems().addAll(sheetNames);
		sheetChoiceBox.getSelectionModel().select(sheetId);
		initializeControls(sheetId);
		
		sheetChoiceBox.setOnAction(event -> {
			setSheet(event);
			initializeControls(sheetChoiceBox.getSelectionModel().getSelectedIndex());
		});

		updateOverprintLabel();
		updateColorValues();
		
		overprintChoiceBox.setOnAction(event -> {
			if (overprintChoiceBox.getSelectionModel().getSelectedItem() == null) {
				updateOverprint();
			}
			updateOverprintLabel();
			updateColorValues();
		});
		
		numOfProductsField.setOnAction(event -> {
			try {
				showTable(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void setSheet(ActionEvent event) {
		int sheetId = sheetChoiceBox.getSelectionModel().getSelectedIndex();
		sheetChoiceBox.getSelectionModel().select(sheetId);
	}

	private void initializeControls(int sheetId) {
		updateSheetChoiceBox(sheetId);

		try {
			overprintNames = bridge.getOverprint(sheetId, se.getDataStartRow(sheetId), 0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		updateOverprint();
		updateDiscountChoiceBox();
	}

	private void updateOverprint() {
		overprintChoiceBox.getItems().clear();

		ObservableList<String> items = overprintChoiceBox.getItems();
		List<String> asList = Arrays.asList(overprintNames);
		if(!items.equals(asList)) {
			items.clear();
			items.addAll(overprintNames);
			overprintChoiceBox.getSelectionModel().selectFirst();
		}
	}

	private void updateOverprintLabel() {
		int sheetId = sheetChoiceBox.getSelectionModel().getSelectedIndex();
		String chosenOverprint = overprintChoiceBox.getSelectionModel().getSelectedItem();
		
		try {
			Integer dataStartRow = se.getDataStartRow(sheetId);
			Integer colOp = 0;
			String overprintCode = getOverprintCode(dataStartRow, colOp, chosenOverprint);
			overprintLabel.setText(overprintCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getOverprintCode(Integer rowCol, Integer colOp, String chosenOverprint) throws IOException {
		int sheetId = sheetChoiceBox.getSelectionModel().getSelectedIndex();
		return bridge.getOverprintCode(sheetId, rowCol, colOp, chosenOverprint);
	}
	
	private void updateColorValues() {
		int sheetId = sheetChoiceBox.getSelectionModel().getSelectedIndex();
		String chosenOverprint = overprintChoiceBox.getSelectionModel().getSelectedItem();
		Integer dataStartRow = se.getDataStartRow(sheetId);
		Integer colOp = 0;
		int colorCol = se.getPositionOfColorColumn(sheetId);
		
		try {
			List<String> colorValues = bridge.getColorValues(sheetId, dataStartRow, colOp, colorCol, chosenOverprint);
			updateColorChoiceBox(colorValues);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateColorChoiceBox(List<String> list) throws IOException {
		colorChoiceBox.getItems().clear();
		colorChoiceBox.getItems().addAll(list);
		colorChoiceBox.getSelectionModel().selectFirst();
	}
	
	private void updateDiscountChoiceBox() {
		discountChoiceBox.getItems().clear();
		discountChoiceBox.getItems().addAll(discountValues);
		discountChoiceBox.getSelectionModel().selectFirst();
	}

	private void updateSheetChoiceBox(int sheetId) {
		sheetChoiceBox.getItems().clear();
		sheetChoiceBox.getItems().addAll(sheetNames);
		sheetChoiceBox.getSelectionModel().select(sheetId);
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