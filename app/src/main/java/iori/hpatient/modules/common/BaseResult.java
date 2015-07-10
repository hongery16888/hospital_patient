package iori.hpatient.modules.common;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseResult {

	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private int result;
	
	public ArrayList<HashMap<String, String>> getList() {
		return list;
	}
	public void setList(ArrayList<HashMap<String, String>> list) {
		this.list = list;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
}
