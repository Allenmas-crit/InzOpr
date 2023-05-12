package dataReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.SpreadSheet;

import utils.Utils;

public class RawMaterialReader {

	public final static int SELECTEDSHEET = 0;

	public enum COLUMNS {
        OVERPRINT1(0),
        OVERPRINT2(1),
        DESCRIPTION(2),
        NUMOFCOLORS(3),
        BASEPRICE(4);

		private int numVal;

		COLUMNS(int numVal) {
	        this.numVal = numVal;
	    }

	    public int getNumVal() {
	        return numVal;
	    }
    }

	private static class RawMaterials {
		private static RawMaterials single_instance = null;
		private static List<List<String>> mat;

		private RawMaterials() {
			if(mat == null) mat = new ArrayList<>();
		}

		public List<List<String>> getList() {
			if(mat == null || mat.size() == 0) throw new IllegalStateException("List in " + getClass() + " is empty!");
			return mat;
		}

		public static RawMaterials getInstance()
	    {
	        if (single_instance == null)
	            single_instance = new RawMaterials();

	        return single_instance;
	    }

		public void clearList() {
			mat.clear();
		}

		public void addAll(List<String> list) {
			if(list.size() != 5) throw new IllegalArgumentException("List in " + getClass() + " received wrong number of elements.");

			if(mat.size() != 0 && mat.get(0).size() == 0) mat.remove(0);
			mat.add(list);
		}
	}

	private final static SpreadSheet SPREAD = init();

	private static SpreadSheet init() {
		SpreadSheet s = null;
		try {
			s = new SpreadSheet(new File("src/resources/ceny-materialow.ods"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	private String getCell(Integer row, Integer col) throws IOException {
		Range range = (SPREAD.getSheets().get(SELECTEDSHEET).getDataRange());
		Object value = range.getCell(row, col).getValue();
		return value == null ? "" : value.toString();
	}

	/******************/

	public void getRowsByDescFromFile(String desc){
		Range range = (SPREAD.getSheets().get(SELECTEDSHEET).getDataRange());
		int rowOfStartingCell = 1;
		int col = COLUMNS.DESCRIPTION.getNumVal();
		Object value;

		int id = -1, addedValues = 0;
		while(range.getLastRow() - 1 != id) {
			id += 1;
			value = range.getCell(rowOfStartingCell + id, col).getValue();

			if ((value == null) || !value.equals(desc)) continue;

			addRowtoList(range, rowOfStartingCell, id);
			addedValues += 1;
		}

		if(addedValues == 0) throw new IllegalArgumentException("There's nothing matching your description.");
	}

	private void addRowtoList(Range range, int rowOfStartingCell, int id) {
		List<String> list = new ArrayList<>();

		for(int i = 0; i < 5; i++){
			try {
				list.add(getCell(rowOfStartingCell + id, i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RawMaterials.getInstance().addAll(list);
	}

	public void filterOutRowsByNumOfColors(String colors) {
		List<List<String>> list = getList();
		List<String> res = new ArrayList<>();

		for (List<String> subList : list) {
			if(subList.get(COLUMNS.NUMOFCOLORS.getNumVal()).equals(colors)) res.addAll(subList);
		}

		clear();
		list.add(res);
	}

	public List<List<String>> getList() {
		RawMaterials instance = RawMaterials.getInstance();
		return instance.getList();
	}

	public void filterOutRowsBySecSeqOfOverprint(String overprint) {
		List<List<String>> list = getList();
		List<String> res = new ArrayList<>();

		for (List<String> subList : list) {
			if(subList.get(COLUMNS.OVERPRINT2.getNumVal()).trim().equals(overprint.trim())) res.addAll(subList);
		}

		clear();
		list.add(res);
	}
	public double getRawMaterialPrice(){
		List<List<String>> list = getList();

		if(list.get(0).size() == 0) throw new IndexOutOfBoundsException("List is empty. Check if your parameters are correct.");
		if(list.size() != 1) throw new IllegalStateException("List should be filtered to one entry, but\n" + list.toString() + "\nwas received.");

		return Utils.parsePrice(list.get(0).get(COLUMNS.BASEPRICE.getNumVal()));
	}

	public void clear() {
		RawMaterials.getInstance().clearList();
	}
}