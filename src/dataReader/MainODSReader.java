package dataReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;

import utils.Utils;

public class MainODSReader{

	private final static SpreadSheet SPREAD = init();

	private static SpreadSheet init() {
		SpreadSheet s = null;
		try {
			s = new SpreadSheet(new File("src/resources/RABAT-PRZEDZIALOWY.ods"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	static public String getCell(Integer selectedSheet, Integer row, Integer col) throws IOException {
		Range range = (SPREAD.getSheets().get(selectedSheet).getDataRange());
		Object value = range.getCell(row, col).getValue();
		return value == null ? "" : value.toString();
	}

	public static List<String> getSelectedCellsInRow(Integer selectedSheet, Integer row, List<Integer> cells)
			throws IOException {
		List<Sheet> sheets = SPREAD.getSheets();
		Range dataRange = sheets.get(selectedSheet).getDataRange();

		List<String> content = new ArrayList<>();

		cells.forEach(i -> {
			if(i == -1) {
				content.add("");
				return;
			}
			Range cell = dataRange.getCell(row, i);
			if (cell.getValue() == null) {
				content.add("");
			} else {
				content.add(cell.getValue().toString());
			}
		});
		return content;
	}

	// return list after empty cell
	public static List<String> getCellsInColumn(Integer selectedSheet, Integer rowOfStartingCell, Integer col)
			throws IOException {
		List<Sheet> sheets = SPREAD.getSheets();
		Range range = sheets.get(selectedSheet).getDataRange();

		List<String> content = new ArrayList<>();

		Object value = range.getCell(rowOfStartingCell, col).getValue();
		for (int i = 1;; i++) {
			if (value == null)
				return content;
			content.add(value.toString());
			value = range.getCell(rowOfStartingCell + i, col).getValue();
		}
	}

	// return list after 2 consecutive empty cells
	public static List<String> getFirstCellsInColumn(Integer selectedSheet, Integer rowOfStartingCell, Integer col)
			throws IOException {
		List<Sheet> sheets = SPREAD.getSheets();
		Range range = sheets.get(selectedSheet).getDataRange();

		List<String> content = new ArrayList<>();

		Object value;
		Object nextVal;
		for (int i = 1;; i++) {
			value = range.getCell(rowOfStartingCell + i, col).getValue();
			nextVal = range.getCell(rowOfStartingCell + i + 1, col).getValue();
			if (value == null) {
				if (nextVal == null)
					return content;
				continue;
			}
			if (content.contains(value.toString())) {
				value = range.getCell(rowOfStartingCell + i, col).getValue();
				continue;
			}
			content.add(value.toString());
		}
	}

	// used to display two overprint segments on overprintLabel
	public static String findCellInCol(Integer selectedSheet, Integer row, Integer col, String myOverprint)
			throws IOException {
		Range range = (SPREAD.getSheets().get(selectedSheet).getDataRange());
		Object value, nextVal;
		String firstOverprint;

		for (int i = 1;; i++) {
			value = range.getCell(row + i, col).getValue();

			if (value == null) {
				nextVal = range.getCell(row + i + 1, col).getValue();
				if (nextVal == null)
					return "Found nothing";
				continue;
			}

			firstOverprint = value.toString();
			if (firstOverprint.equals(myOverprint)) {
				String secOverprint = getCell(selectedSheet, row + i, col + 1);

				if (firstOverprint.equals(secOverprint)) {
					return firstOverprint;
				}
				return firstOverprint.concat(" - ").concat(secOverprint);
			}
		}
	}

	public static Integer getRowNumber(Integer selectedSheet, Integer startingRow, Integer col, String opName) {
		Range range = (SPREAD.getSheets().get(selectedSheet).getDataRange());
		Object value, nextVal;
		String res;

		for (int i = 0;; i++) {
			value = range.getCell(startingRow + i, col).getValue();

			if (value == null) {
				nextVal = range.getCell(startingRow + i + 1, col).getValue();
				if (nextVal == null)
					return -1;
				continue;
			}

			res = value.toString();
			if (res.equals(opName)) {
				return i + startingRow;
			}
		}
	}

	public static List<String> getColorValuesByOverprintCode(Integer selectedSheet, Integer startingRow,
			Integer colOverprint, Integer colColor, String overprint) throws IOException {
		List<Sheet> sheets = SPREAD.getSheets();
		Range range = sheets.get(selectedSheet).getDataRange();

		List<String> content = new ArrayList<>();
		Object value, nextVal = null;
		String prevStr = "-1";

		Integer row = getRowNumber(selectedSheet, startingRow, colOverprint, overprint);

		if (row == -1)
			throw new IOException("Row where we start searching for colors " + row);

		// get color
		for (int i = 1;; i++) {
			value = range.getCell(row + i, colColor).getValue();
			if (value == null) {
				nextVal = range.getCell(row + i + 1, colColor).getValue();
				if (nextVal == null || Double.valueOf(nextVal.toString()) <= Double.valueOf(prevStr))
					return content;
				continue;
			}
			if ((value.toString()).equals(prevStr))
				continue;

			if (Utils.isNumeric(value.toString())) {
				content.add(value.toString());
				prevStr = value.toString();
			}
		}
	}

	public static List<String> getSheets() {
		List<Sheet> sheets = SPREAD.getSheets();
		List<String> content = new ArrayList<>();

		for (Sheet sheet : sheets) {
			content.add(sheet.getName());
		}
		return content;
	}

	public static Integer getLastColumn(Integer selectedSheet) {
		List<Sheet> sheets = SPREAD.getSheets();
		Range dataRange = sheets.get(selectedSheet).getDataRange();
		return dataRange.getLastColumn();
	}
}
