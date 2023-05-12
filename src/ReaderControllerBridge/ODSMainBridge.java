package ReaderControllerBridge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import application.MainController;
import dataReader.MainODSReader;
import utils.Utils;

public class ODSMainBridge {

	MainController mc;

	public ODSMainBridge() {
		mc = new MainController();
	}

	public List<String> getColRange(Integer selectedSheet, Integer startingRow, Integer col) throws IOException {
		List<String> colList = MainODSReader.getCellsInColumn(selectedSheet, startingRow, col);
		return colList;
	}

	public Integer getRowWithCorrColorNum(Integer selectedSheet, Integer row, Integer col, String selectedColorValue)
			throws IOException {
		Double cell = Double.valueOf(MainODSReader.getCell(selectedSheet, row, col));
		if (Utils.isNumeric(selectedColorValue))
			selectedColorValue = (Double.valueOf(selectedColorValue)).toString();

		while (selectedColorValue.compareTo(cell.toString()) != 0) {
			row += 1;
			String cellStr = MainODSReader.getCell(selectedSheet, row, col);
			cell = cellStr == "" ? 0.0 : Double.valueOf(cellStr);
		}
		return row;
	}

	public Integer findOverprintRowByName(Integer selectedSheet, Integer startingRow, Integer col, String opName)
			throws IOException {
		return MainODSReader.getRowNumber(selectedSheet, startingRow, col, opName);
	}

	public String[] getOverprint(Integer selectedSheet, Integer startingRow, Integer col) throws IOException {
		List<String> opFromColumn = new LinkedList<>(MainODSReader.getFirstCellsInColumn(selectedSheet, startingRow, col));
		List<String> res = filterRepeatingElements(selectedSheet, startingRow, opFromColumn);
		return res.toArray(new String[0]);
	}

	private List<String> filterRepeatingElements(Integer selectedSheet, Integer startingRow, List<String> overprints)
			throws IOException {
		List<String> opFromColumn = new LinkedList<>(overprints);
		List<Integer> rowsToCheck = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
		List<String> selectedRowCells, listWithoutDuplicates;
		List<String> toRemove = new ArrayList<>();
		int howMuchWasEmpty = 0;

		int rowNum = startingRow;
		while (howMuchWasEmpty < 5) {
			selectedRowCells = MainODSReader.getSelectedCellsInRow(selectedSheet, rowNum, rowsToCheck);
			listWithoutDuplicates = new ArrayList<>(new HashSet<>(selectedRowCells));
			rowNum = rowNum + 1;

			// now, get bad overprints from column and ignore rows without data.
			if (listWithoutDuplicates.size() == 1) {
				if (listWithoutDuplicates.get(0).isBlank()) {
					howMuchWasEmpty += 1;
				} else {
					toRemove.add(listWithoutDuplicates.get(0));
					howMuchWasEmpty = 0;
				}
			}
		}
		opFromColumn.removeAll(toRemove);
		return opFromColumn;
	}

	public String getOverprintCode(Integer selectedSheet, Integer row, Integer col, String chosenOverprint)
			throws IOException {
		return MainODSReader.findCellInCol(selectedSheet, row, col, chosenOverprint);
	}

	public List<String> getColorValues(Integer selectedSheet, Integer startingRow, Integer colOp, Integer colColor,
			String overprint) throws IOException {
		//System.out.println("selectedSheet: " + selectedSheet + " startingRow: " + startingRow + " colOp: " + colOp
		//		+ " colColor: " + colColor + " overprint: " + overprint);

		List<String> cv = MainODSReader.getColorValuesByOverprintCode(selectedSheet, startingRow, colOp, colColor,
				overprint);
		List<String> colorList = new ArrayList<>();
		double d = 0.0;

		for (String s : cv) {
			d = Double.parseDouble(s);
			colorList.add(Integer.valueOf((int) d).toString());
		}
		return colorList;
	}

	public List<String> getCustomCellValues(Integer selectedSheet, int row, List<Integer> columns) throws IOException {
		return MainODSReader.getSelectedCellsInRow(selectedSheet, row, columns);
	}

	public String[] getSheets() {
		return MainODSReader.getSheets().toArray(new String[0]);
	}

	public String getCell(Integer selectedSheet, Integer row, Integer col) throws IOException {
		return MainODSReader.getCell(selectedSheet, row, col);
	}
}
