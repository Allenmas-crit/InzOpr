package dataReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ReaderControllerBridge.ODSMainBridge;

class SheetElAllocationTest {

	SheetElAllocation sheet = new SheetElAllocation();
	ODSMainBridge bridge = new ODSMainBridge();

	@Test
	void namesAreExatlyTheSame() throws IOException {
		String constName, cellName;

		// System.out.println(sheet);

		for (int j = 0; j < sheet.getSize(); j++) {
			System.out.println("\nj: " + j);

			for (int i = 1; i < sheet.getMapSize(j); i++) {
				constName = sheet.getColumnName(j, i);
				cellName = bridge.getCell(j, 0, i);
				//System.out.println("test i: " + i + " constName: " + constName);
				// System.out.println(i + ") cell: " + bridge.getCell(j, 0, i));

				if (cellName == "")
					cellName = null;
				assertEquals(constName, cellName);
			}
		}
	}

	@Test
	void getColumnByName1() {
		Integer sheetId = 4;
		assertEquals(3, sheet.getFirstColumnNumberByName(sheetId, "Ilość\nKolorów"));
	}

	@Test
	void getName1() {
		Integer sheetId = 4;
		String str = "Ilość\nKolorów";
		assertEquals(sheet.getColumnName(sheetId, sheet.getFirstColumnNumberByName(sheetId, str)), str);
	}

	@Test
	void getNameThatDoesntExist() {
		Integer sheetId = 4;
		String str = "ABBA";
		assertEquals(-1, sheet.getFirstColumnNumberByName(sheetId, str));
	}

	//@Test
	void addToSet() {
		Set<Map<Integer, String>> s = new HashSet<>();
		Map<Integer, String> m = new HashMap<>();
		Map<Integer, String> m2 = new HashMap<>();

		m.put(0, "q");
		m.put(1, "w");
		m.put(2, "e");
		m.put(3, "r");

		m2.put(0, "q");
		m2.put(1, "w");
		m2.put(2, "t");
		m2.put(3, "y");

		s.add(m2);
		s.add(m);

		for (Map<Integer, String> map : s) {
			for (int i = 0; i < 4; i++)
				System.out.println("map: " + map.get(i));
		}

		// Displaying the Set
		System.out.println("Set: " + s);
	}
}
