package iori.hpatient.net.request;

import java.util.HashMap;

public class CheckVersionRequest extends BaseRequest {

	protected String versionCode;

	public CheckVersionRequest(String versionCode, int requestMark) {
		this.versionCode = versionCode;
		setRequestMark(requestMark);
	}
	
	@Override
	public int postUserId() {
		return UNABLE_POST;
	}

	@Override
	public boolean postFile() {
		return false;
	}

	@Override
	public String getRequestUrl() {
		return PATH_CHECK_VERSION;
	}

	@Override
	public String getRequestAction() {
		return CHECK_VERSION;
	}

	@Override
	public HashMap<String, String> getFileEncode() {
		// TODO Auto-generated method stub
		return null;
	}

}
