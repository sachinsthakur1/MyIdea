package com.myidea.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Utility {
	static Properties prop = new Properties();
	ClassLoader classLoader = getClass().getClassLoader();
	File file = new File(classLoader.getResource("Config.properties").getFile());
	FileInputStream inputStream;
	public Properties loadProperty() throws IOException{
		//InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Config.properties");
		
		inputStream = new FileInputStream(file);
		prop.load(inputStream);	
		return prop;
	}
	
	public Properties updatePropertyFile(Properties prop2) throws IOException{
		inputStream.close();
		FileOutputStream out = new FileOutputStream(file);
        prop2.store(out, null);
        out.close();
        return loadProperty();
	}
}
