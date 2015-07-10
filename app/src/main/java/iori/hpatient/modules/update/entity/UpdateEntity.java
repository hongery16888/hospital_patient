package iori.hpatient.modules.update.entity;

import java.util.HashMap;

public class UpdateEntity {
	
	private int statusCode;

	public void setResult(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getResult() {
		return statusCode;
	}

	public void setMap(HashMap<String, String> map) {
		
	}
}
