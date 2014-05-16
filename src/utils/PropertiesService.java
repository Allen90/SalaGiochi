package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesService {
	
	private static final String CONFIG_FILE_NAME = "config.properties";
	static private PropertiesService _instance = null;
	private InputStream file = null;
	private Properties properties = null;
	
	protected PropertiesService() {
		try {
			File f = new File(CONFIG_FILE_NAME);
			
			if (!f.exists()) {
				f.createNewFile();
			}
			
			file = new FileInputStream(f);
			properties = new Properties();
			
			properties.load(file);
		} catch (Exception e) {
			e.printStackTrace();
			
			if (file != null) {
				try {
					file.close();
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			}
		}
	}
	
	static public PropertiesService instance() {
		if(_instance == null) {
			_instance = new PropertiesService();
		}
		
		return _instance;
	}
	
	public String getProperties(String key) {
		if (key != null && key != "") {
			return properties.getProperty(key);
		} else {
			return null;
		}
	}
	
	public String getProperties(String key, String defaultValue) {
		if (key != null && key != "") {
			return properties.getProperty(key, defaultValue);
		} else {
			return null;
		}
	}
}
