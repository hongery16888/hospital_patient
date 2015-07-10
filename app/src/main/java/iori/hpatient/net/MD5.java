/**
 * Company ： Chengdu Tianfu Software Park Co., Ltd.</br>
 * Copyright ：  2009-2010,  Chengdu Tianfu Software Park Co., Ltd.</br>
 * @since：JDK1.6</br>
 * used android sdk level: 7</br>
 * @version：1.0</br>
 * @see：</br>
 */
package iori.hpatient.net;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * MD5数据加密类。
 * 
 * @author Panyx
 * @version : 1.00</br>
 * copyright 2009-2010, Chengdu Tianfu Software Park Co., Ltd.</br>
 * Create Time : 2012-9-19 下午6:54:30</br>
 * Description :MD5数据加密类。</br>
 * </br>History：</br>
 *   Editor **** Time **** Mantis No **** Operation **** Description ***</br>
 ***
 ***
 ***
 ***
 */
public class MD5 {
	private static char hexChar[] = {'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public MD5()
    {
    }

    /**
     * Description : 对指定字符串加密
     * <li>step1: 
     * @param  s 需要加密的字符串
     * @return String 加密处理后的字符串
     * @throws 
     */
    public static final String getMd5(String s)
    {
        try{
        char str[];
        byte strTemp[] = s.getBytes();
        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
        mdTemp.update(strTemp);
        byte md[] = mdTemp.digest();
        int j = md.length;
        str = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++)
        {
            byte byte0 = md[i];
				str[k++] = hexChar[byte0 >>> 4 & 0xf];
				str[k++] = hexChar[byte0 & 0xf];
        }

        return new String(str);
        }catch(Exception e){
        return null;
        }
    }



	public static String getFileMD5(InputStream is) {
		String str = "";
		try {
			str = getHash(is, "MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA1(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA256(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA-256");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA384(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA-384");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA512(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA-512");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	private static String getHash(String fileName, String hashType)
			throws Exception {
		InputStream fis = new FileInputStream(fileName);
		byte buffer[] = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		for (int numRead = 0; (numRead = fis.read(buffer)) > 0;) {
			md5.update(buffer, 0, numRead);
		}

		fis.close();
		return toHexString(md5.digest());
	}

	private static String getHash(InputStream is, String hashType)
			throws Exception {
		byte buffer[] = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		for (int numRead = 0; (numRead = is.read(buffer)) > 0;) {
			md5.update(buffer, 0, numRead);
		}

		is.close();
		return toHexString(md5.digest());
	}
	private static String toHexString(byte b[]) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0xf]);
		}

		return sb.toString();
	}
}
