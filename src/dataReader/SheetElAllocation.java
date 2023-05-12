package dataReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* For file RABAT-PRZEDZIALOWY.ods */
public class SheetElAllocation {
	public static final int CATEGORYROW = 0;
	/*
	 * <Integer columnId, String name> Kolejność dodawania do kolekcji decyduje o
	 * tym, gdzie wg tej klasy jest dany arkusz (sheetId)
	 */
	private List<Map<Integer, String>> columns;
	private List<Integer> dataStartRows;

	public SheetElAllocation() {
		columns = new ArrayList<>();
		dataStartRows = new ArrayList<>();

		putDrukSmyczyNaszywane();
		putDrukSmyczyGSMOpaska();
		putDrukSmyczyDlugie();
		putDrukSmyczyKrotkie();
		putTampodruk();
		putUV();
		putYAG();
		putCO2();
		putSitodrukBluzy();
		putSitodrukBez();
		putSitodrukKoszulki();
		putSitodrukInne();
		putSitodrukCeramika();

		// ...
	}

	private void putSitodrukCeramika() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Szerokość\nKolorystyka", "Przedział", "Koszt", "Wariant", "Cena",
				"Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
	}

	private void putSitodrukInne() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Szerokość\nKolorystyka", "Przedział", "Koszt", "Wariant", "Cena",
				"Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
	}

	private void putSitodrukKoszulki() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Ilość\nKolorów", "Przedział", "Koszt", "Wariant", "Cena", "Cena",
				"Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
	}

	private void putSitodrukBez() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Opis", "Ilość\nKolorów", "Przedział", "Przedział", "Przedział",
				"Przedział", "Koszt", "Narzut", "Cena", "Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
	}

	private void putSitodrukBluzy() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Ilość\nKolorów", "Przedział", "Koszt", "Wariant", "Cena", "Cena",
				"Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(36);
	}

	private void putCO2() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Opis", "Ilość\nKolorów", "Przedział", "Przedział", "Przedział",
				"Przedział", "Koszt", "Narzut", "Cena", "Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(3);
	}

	private void putYAG() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Opis", "Ilość\nKolorów", "Przedział", "Przedział", "Przedział",
				"Przedział", "Koszt", "Narzut", "Cena", "Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(3);
	}

	private void putUV() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Opis", "Ilość\nKolorów", "Przedział", "Przedział", "Przedział",
				"Przedział", "Koszt", "Narzut", "Cena", "Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(3);
	}

	private void putTampodruk() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("Nadruk", "Nadruk", "Opis", "Ilość\nKolorów", "Przedział", "Przedział", "Przedział",
				"Przedział", "Koszt", "Narzut", "Cena", "Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(3);
	}

	private void putDrukSmyczyKrotkie() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("", "Nadruk", "Nadruk", "Szerokość\nKolorystyka", "Przedział", "Koszt", "Wariant",
				"Cena", "Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(4);
	}

	private void putDrukSmyczyDlugie() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("", "Nadruk", "Nadruk", "Szerokość\nKolorystyka", "Przedział", "Koszt", "Wariant",
				"Cena", "Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(4);
	}

	private void putDrukSmyczyGSMOpaska() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("", "Nadruk", "Szerokość\nKolorystyka", "Przedział", "Koszt", "Wariant", "Cena",
				"Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(4);
	}

	private void putDrukSmyczyNaszywane() {
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("", "Nadruk", "Szerokość\nKolorystyka", "Przedział", "Koszt", "Wariant", "Cena",
				"Cena", "Cena", "Cena", "Cena", "Rabat"));
		addListToCollection(list);
		dataStartRows.add(4);
	}

	private void addListToCollection(List<String> list) {
		Map<Integer, String> sheet;
		sheet = addToMap(list);
		columns.add(sheet);
	}

	private Map<Integer, String> addToMap(List<String> list) {
		Map<Integer, String> sheet = new HashMap<>();
		int i = 0;
		for (String str : list)
			sheet.put(i += 1, str);

		return sheet;
	}

	public String getColumnName(Integer sheetId, Integer columnId) {
		int numOfSet = 0;
		for (Map<Integer, String> map : columns) {
			if (numOfSet != sheetId) {
				numOfSet += 1;
				continue;
			}

			for (int i = 1; i < getMapSize(numOfSet) + 1; i++) {
				if (columnId == i) {
					return map.get(i + 1);
				}
			}
		}
		return null;
	}

	public int getFirstColumnNumberByName(Integer sheetId, String name) {
		Map<Integer, String> corrSheet = null;
		int numOfSet = 0;

		for (Map<Integer, String> sheet : columns) {
			if (numOfSet != sheetId) {
				numOfSet += 1;
				continue;
			}
			corrSheet = sheet;
			break;
		}
		int size = getMapSize(numOfSet);

		for (int i = 0; i < size; i++) {
			if (corrSheet.get(i) == name) {
				// bo liczymy od 0
				return i - 1;
			}
		}
		return -1;
	}

	/*
	 * To co tutaj jest mozna dac do nowej klasy bridge
	 ***********************************************/
	public int getPositionOfColorColumn(Integer sheetId) {
		int col;
		List<String> possibleNames = new ArrayList<>(
				Arrays.asList("Ilość\nKolorów", "Szerokość\nKolorystyka"));

		for (String str : possibleNames) {
			col = getFirstColumnNumberByName(sheetId, str);//
			if (col == -1)
				throw new NumberFormatException("Nie znaleziono nazwy kolumny " + str
						+ ", która powinna być zdefiniowana w " + SheetElAllocation.class.getName());
			return col;
		}
		return -1;
	}

	private List<Integer> getMapSize() {
		List<Integer> sizes = new ArrayList<>();
		for (Map<Integer, String> sheet : columns) {
			sizes.add(sheet.size());
		}
		// System.out.println(sizes);

		return sizes;
	}

	public Integer getMapSize(Integer id) throws RuntimeException {
		Integer mapSize;
		try {
			mapSize = getMapSize().get(id);
		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException("\nColumn is not defined in class " + SheetElAllocation.class.getName()
					+ ", \nso you cannot read it's size.", e);
		}
		return mapSize;
	}

	public Integer getSize() {
		return columns.size();
	}

	public List<Integer> getDataStartRows() {
		return dataStartRows;
	}

	public Integer getDataStartRow(Integer sheetId) {

		return dataStartRows.get(sheetId);
	}

	@Override
	public String toString() {
		return "SheetElAllocation [columns=\n" + columns + "]";
	}

}
