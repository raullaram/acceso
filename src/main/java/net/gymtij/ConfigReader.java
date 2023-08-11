package net.gymtij;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	private Properties properties;
	private static ConfigReader configReader;

    ConfigReader() {
		BufferedReader reader;
	    	String propertyFilePath = "./Configuration.properties";
	        try {
	            reader = new BufferedReader(new FileReader(propertyFilePath));
	            properties = new Properties();
	            try {
	                properties.load(reader);
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Configuration.properties no localizado " + propertyFilePath);
	        }		
	}

    public static ConfigReader getInstance( ) {
    	if(configReader == null) {
    		configReader = new ConfigReader();
    	}
        return configReader;
    }

    
    public String getServerIp() {
        String serverIp = properties.getProperty("serverIp");
        if(serverIp != null) return serverIp;
        else throw new RuntimeException("Dirección IP del servidor no indicada en Configuration.properties");
    }
}