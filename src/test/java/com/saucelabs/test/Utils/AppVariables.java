package com.saucelabs.test.Utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

//import com.hewitt.custom.taleo.utilities.StringEncrypter.EncryptionException;

public class AppVariables {

	private static final String FILE_PATH = "config/Application.properties";

	private static Properties mProps = new Properties();

	/**
	 * Loads file into properties object
	 * 
	 * @throws java.lang.Exception
	 */
	public static void load() throws Exception {
		FileInputStream lFile = new FileInputStream(FILE_PATH);
		mProps.load(lFile);
		lFile.close();
	}

	public static void load(String s) throws Exception {
		FileInputStream lFile = new FileInputStream(s);
		mProps.load(lFile);
		lFile.close();
	}

	/**
	 * Loads file into properties object
	 * 
	 * @throws java.lang.Exception
	 */
	public static HashMap<String, String> loadMap(String fileName) {
		Properties dMapProps = new Properties();
		HashMap<String, String> lDMap = new HashMap<String, String>();
		try {
			FileInputStream lFile = new FileInputStream("config/" + fileName);
			dMapProps.load(lFile);
			lFile.close();

			// Get the key set
			Iterator lIter = dMapProps.keySet().iterator();
			// iterate to populate.
			while (lIter.hasNext()) {
				String lKey = (String) lIter.next();
				String lVal = (String) dMapProps.get(lKey);
				lDMap.put(lKey, lVal);
			}
		} catch (Exception e) {
			System.out.println("Exception while loading XML file " + fileName + "loadMap" + e);
			
		}
		return lDMap;
	}

	/**
	 * Checks for the Key
	 * 
	 * @param OraclePwd
	 * @return true or false based on its availability in Application.props.
	 */

	public static boolean getChkOraPwdKey(String oKey) {
		boolean getChkOraPwdKey = false;
		if (mProps.containsKey(oKey)) {
			getChkOraPwdKey = true;
			return getChkOraPwdKey;
		}
		return getChkOraPwdKey;

	}

	/**
	 * Checks for the Key
	 * 
	 * @param EncryptedPassword
	 * @return true or false based on based on its availability Application.props.
	 */

	public static boolean getChkEncPwdKey(String eKey) {
		boolean ChkEncPwdKey = false;
		if (mProps.containsKey(eKey)) {
			ChkEncPwdKey = true;
			return ChkEncPwdKey;
		}
		return ChkEncPwdKey;
	}

	/**
	 * Gets Value for the Keys
	 * 
	 * @param pKey
	 * @return
	 */
	public static String get(String pKey) {

		String value = null;
		try {
			if (mProps.isEmpty()) {
				load();
			}
			value = (String) mProps.get(pKey);
			/*if (pKey.equalsIgnoreCase("password") && !isNullOrBlank(value)) {
				StringEncrypter encrypter = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME);
				value = encrypter.decrypt(value);
			}*/
		} catch (Exception e) {
			System.out.println("Exception In AppVariables get Method for key " + pKey +  "get" + e);
		}
		return value;
	}

	/**
	 * Gets List for the Keys
	 * 
	 * @param pKey
	 * @return
	 */
	public static List<String> getList(String pKey) {

		String value = null;
		List<String> listValue = null;
		try {
			if (mProps.isEmpty()) {
				load();
			}
			value = (String) mProps.get(pKey);
			String[] s = value.split(",");
			listValue = new ArrayList<String>();
			for (int i = 0; i < s.length; i++) {
				listValue.add(s[i]);
			}
		} catch (Exception e) {
			System.out.println("Exception In AppVariables get Method for key " + pKey +  "get" +  e);
		}
		return listValue;
	}

	public static boolean isNullOrBlank(String pString) {
		return (pString == null || pString.trim().equals(""));
	}
}
