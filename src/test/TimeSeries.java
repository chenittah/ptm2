package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class TimeSeries {

	public static class pair {
		public final String feature;
		public List<Float> column;

		public pair(String feature)
		{
			this.feature = feature;
			this.column = new ArrayList<>();
		}

	}

	private ArrayList<pair> Table;

	public ArrayList<pair> getTable() {
		return Table;
	}

	public TimeSeries(String csvFileName)  {
		this.Table = new ArrayList<pair>();
		String ReadLine;
		String[] line;
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(csvFileName));
			if ((ReadLine = csvReader.readLine()) != null) {
				line = ReadLine.split(",");
				for(String s:line)
					this.Table.add(new pair(s));
			}
			while ((ReadLine = csvReader.readLine()) != null) {
				line = ReadLine.split(",");
				for (int i = 0; i < this.Table.size(); i++) {
					try {
						Float num = Float.parseFloat(line[i]);
						this.Table.get(i).column.add(num);
					}
					catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			csvReader.close();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public pair getPair(int i){
		return this.Table.get(i);
	}
	public int getSizeList(){
		return this.getListColumns(0).size();
	}
	public List<Float> getListColumns(int i){
		return getPair(i).column;
	}
	public String getFeature(int i){
		return getPair(i).feature;
	}
	public Float getNum(int f,int v){
		return getPair(f).column.get(v);
	}
	public int getSizeTable(){
		return this.Table.size();
	}

	public float[] getArrayColumns(int f){
		float[] arrF = new float[getSizeList()];
		for(int i=0;i<getSizeList();i++){
			arrF[i]=getListColumns(f).get(i);
		}
		return arrF;
	}
	public static Point[] toArrayPoint(float[] f1,float[] f2){
		Point[] p = new Point[f1.length];
		for(int i=0;i< f1.length;i++){
			p[i] = new Point(f1[i],f2[i]);
		}
		return p;
	}
	public int featureIndex(String s){
		for(int i=0;i<getSizeTable();i++){
			if(getFeature(i).equals(s)){
				return i;
			}
		}
		return -1;
	}

	public List<Float> getLine(int time){
		List<Float> line = new ArrayList<>();
		for(int i=0;i<Table.size();i++){
			line.add(getNum(i,time));
		}
		return line;
	}

/*public ArrayList<String> getLineOfFeatures(int index){
		ArrayList<String> featuresArray = null;

		for (int i = 0; i < size(); i++) {
				featuresArray.add(getFeatureByIndex(index));
		}
		return featuresArray;
	}

	public ArrayList<Float> getLineOfColumns(int index){
		ArrayList<Float> columnsArray = null;

		for (int i = 0; i < size(); i++) {
				columnsArray.add(getColumnByIndex(index).get(i));
		}
		return columnsArray;
	}

	public ArrayList getLineOfTable(int index){
		if (index == 0)
			return getLineOfFeatures(index);
		else
			return getLineOfColumns(index);
	}*/

}

