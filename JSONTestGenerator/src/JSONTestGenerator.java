import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONTestGenerator {
	
	private static final String FILEPATH = "E:\\Área de Trabalho\\test\\template.json";
	private static final String TESTPATH = "E:\\Área de Trabalho\\test\\generated jsons\\";

	private static String tempString, strTest = "STRING";
	private static int tempInt, intTest = 0, count = 0;
	private static boolean tempBool, boolTest = false;
	private static JSONArray tempJArray;
	private static JSONObject tempJObj, parentObj;

	public static void main(String[] args) {

		parentObj = new JSONObject(readFile(FILEPATH));
		findKeys(parentObj);

	}

	private static void findKeys(JSONObject obj) {

		for (String key : obj.keySet()) {

			Object childObj = obj.get(key);

			switch (childObj.getClass().getName()) {

			case "java.lang.String":
				tempString = obj.getString(key);
				writeString(TESTPATH, key, obj, intTest);
				writeString(TESTPATH, key, obj, boolTest);
				obj.put(key, tempString);

				break;

			case "java.lang.Integer":
				tempInt = obj.getInt(key);

				writeString(TESTPATH, key, obj, strTest);
				writeString(TESTPATH, key, obj, boolTest);
				obj.put(key, tempInt);

				break;

			case "java.lang.Boolean":
				tempBool = obj.getBoolean(key);

				writeString(TESTPATH, key, obj, strTest);
				writeString(TESTPATH, key, obj, intTest);
				obj.put(key, tempBool);

				break;

			case "org.json.JSONArray":
				tempJArray = obj.getJSONArray(key);
				findKeys(obj.getJSONArray(key).getJSONObject(0));
				break;

			case "org.json.JSONObject":
				tempJObj = obj.getJSONObject(key);
				findKeys(obj);
				break;
			}
		}
	}

	private static String readFile(String filePath) {

		try {
			BufferedReader buffRead = new BufferedReader(new FileReader(filePath));

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

	public static void writeString(String filePath, String key, JSONObject obj, Object content) {
		
		obj.put(key, content);
		
		try {
			File file = new File(filePath + count + ".json");
			file.getParentFile().mkdirs();

			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(file));
			buffWrite.append(parentObj.toString(4));
			buffWrite.close();
			
			count++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
