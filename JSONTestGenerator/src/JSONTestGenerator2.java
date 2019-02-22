import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;

public class JSONTestGenerator2 {

	private static final String FILEPATH = "E:\\Área de Trabalho\\test\\template";

	private static HashMap<String, String> keys = new HashMap<>();
	private static JSONObject parentObj;

	public static void main(String[] args) {

		parentObj = new JSONObject(readFile());
		findKeys(parentObj, "");
		printLines();
	}

	private static void findKeys(JSONObject obj, String path) {

		Iterator<String> it = obj.keys();

		while (it.hasNext()) {

			String key = it.next().toString();
			Object childObj = obj.get(key);
			String type = childObj.getClass().getName();

			if (type.equals("org.json.JSONArray"))
				findKeys(obj.getJSONArray(key).getJSONObject(0), path + key + "[].");
			else if (type.equals("org.json.JSONObject"))
				findKeys(obj, path + key + ".");
			else
				keys.put(path + key, type);
		}
	}

	private static String readFile() {

		try {
			BufferedReader buffRead = new BufferedReader(new FileReader(FILEPATH + ".json"));

			String line = "";
			String content = "";
			while (true) {
				if (line != null) {
					content += line;

				} else
					break;
				line = buffRead.readLine();
			}
			buffRead.close();
			return content;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void printLines() {

		printCSV(Arrays.asList(keys.keySet()));

		ArrayList<Object> firstRow = new ArrayList<>();

		for (Entry<String, String> entry : keys.entrySet()) {
			if (entry.getValue().equals("java.lang.String"))
				firstRow.add("String");
			else if (entry.getValue().equals("java.lang.Integer"))
				firstRow.add(0);
			else if (entry.getValue().equals("java.lang.Boolean"))
				firstRow.add(false);
		}

		printCSV(firstRow);

		for (int i = 0; i < firstRow.size(); i++) {

			ArrayList<Object> temp = (ArrayList<Object>) firstRow.clone();
			temp.set(i, "null");
			printCSV(temp);
			if (!firstRow.get(i).getClass().getName().equals("java.lang.String")) {
				temp.set(i, "String");
				printCSV(temp);
			}
			if (!firstRow.get(i).getClass().getName().equals("java.lang.Integer")) {
				temp.set(i, "0");
				printCSV(temp);
			}
			if (!firstRow.get(i).getClass().getName().equals("java.lang.Boolean")) {
				temp.set(i, "false");
				printCSV(temp);
			}
		}
	}

	private static void printCSV(List<Object> array) {
		String line = array.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(", ", ";") + "\n";

		try {
			File file = new File(FILEPATH + ".csv");
			file.getParentFile().mkdirs();

			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(file, true));
			buffWrite.append(line);
			buffWrite.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
