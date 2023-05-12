package dataReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RawMaterialReaderTest {

	static RawMaterialReader r = new RawMaterialReader();

	@BeforeEach
	void clearStaticList() {
		r.clear();
	}

	@Test
	void getRows1() {
		//should contain values with 1 and 2 colors, so it's equals 2.
		r.getRowsByDescFromFile("TZOLOWE-0002 - Ołówek Quick");
		assertEquals(2, r.getList().size());
	}

	@Test
	void getRows2() {
		r.getRowsByDescFromFile("TZLINIJ-0001 - Linijka plastikowa 20cm");
		assertEquals(2, r.getList().size());
	}

	@Test
	void getDescWithoutRepeat() {
		r.getRowsByDescFromFile("TZOLOWE-0002 - Ołówek Quick");
		r.clear();
		r.getRowsByDescFromFile("TZOLOWE-0002 - Ołówek Quick");
		assertEquals(2, r.getList().size());
	}

	@Test
	void filterByColors1() {
		Double color = 1.0;

		r.getRowsByDescFromFile("TZOLOWE-0002 - Ołówek Quick");
		r.filterOutRowsByNumOfColors(color.toString());
		assertEquals(1, r.getList().size());

		Double colorVal = Double.valueOf(r.getList().get(0).get(3));
		assertEquals(color, colorVal);
	}

	@Test
	void filterByColors2() {
		Double color = 2.0;

		r.getRowsByDescFromFile("TZOLOWE-0002 - Ołówek Quick");
		r.filterOutRowsByNumOfColors(color.toString());
		assertEquals(1, r.getList().size());

		Double colorVal = Double.valueOf(r.getList().get(0).get(3));
		assertEquals(color, colorVal);
	}

	@Test
	void filterByOverprint1() {
		r.getRowsByDescFromFile("TZOLOWE-0002 - Ołówek Quick");
		r.filterOutRowsBySecSeqOfOverprint("NTAMP-0001-100-1 ");
		assertEquals(1, r.getList().size());
	}

	@Test
	void filterByOverprint2() {
		r.getRowsByDescFromFile("Rzepy");
		assertEquals(2, r.getList().size());

		r.filterOutRowsBySecSeqOfOverprint("RZEPY – 2");
		assertEquals(1, r.getList().size());
	}

	@Test
	void getPrice1() {
		r.getRowsByDescFromFile("TZOLOWE-0002 - Ołówek Quick");
		r.filterOutRowsBySecSeqOfOverprint("NTAMP-0001-100-1 ");

		assertEquals(0.1, r.getRawMaterialPrice());
	}

	@Test
	void getPrice2() {
		r.getRowsByDescFromFile("TZLINIJ-0001 - Linijka plastikowa 20cm");
		r.filterOutRowsBySecSeqOfOverprint("NTAMP-0002-100-1 ");

		assertEquals(13.7, r.getRawMaterialPrice());
	}

	@Test
	void getPrice3() {
		r.getRowsByDescFromFile("Rzepy");
		r.filterOutRowsBySecSeqOfOverprint("RZEPY – 2");

		assertEquals(19.7, r.getRawMaterialPrice());
	}
}
