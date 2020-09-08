package com.pingidentity.apac.pf.bulkconfigtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {

	private final static String DEFAULT_IN_CONFIG = "in/pa-config.json";
	private final static String DEFAULT_IN_BULKCONFIG = "in/pa-bulk-config.json";
	private final static String DEFAULT_IN_ENVPROPERTIES = "in/pa-env.properties";
	private final static String DEFAULT_IN_OUTCONFIG = "in/out-pa-bulk-config.json";
	
	private final JSONObject inConfigJSON;
	private final JSONArray inConfigExposeParametersArray;
	private final JSONArray inConfigRemoveConfig;
	private final JSONArray inConfigChangeValue;
	private final JSONObject inBulkJSON;
	
	private final Properties returnProperties = new Properties();
	
	private final String envFileName, outJSON;
	
	public static void main(String [] args) throws ParseException, IOException, RemoveNodeException
	{
		App app = null;
		if(args.length > 3)
			app = new App(args[0], args[1], args[2], args[3]);
		else
			app = new App();
		
		app.writeEnvFile();
		app.writeBulkConfigFile();
		
		
	}

	public App(String inConfigFile, String inBulkFile, String inEnvPropertiesFile, String outJSON) throws ParseException, IOException, RemoveNodeException
	{
		this.inConfigJSON = convertFileToJSON(inConfigFile);
		this.inConfigExposeParametersArray = this.inConfigJSON.containsKey("expose-parameters")? (JSONArray) this.inConfigJSON.get("expose-parameters"): null;
		this.inConfigRemoveConfig = this.inConfigJSON.containsKey("remove-config")? (JSONArray) this.inConfigJSON.get("remove-config"): null;
		this.inConfigChangeValue = this.inConfigJSON.containsKey("change-value")? (JSONArray) this.inConfigJSON.get("change-value"): null;
		this.inBulkJSON = getReplacedJSONObject(inBulkFile, this.inConfigJSON);
		this.envFileName = inEnvPropertiesFile;
		this.outJSON = outJSON;
		
		loadProperties();
		
		processBulkJSON();
	}

	public App() throws ParseException, IOException, RemoveNodeException
	{
		this.inConfigJSON = convertFileToJSON(DEFAULT_IN_CONFIG);
		this.inConfigExposeParametersArray = this.inConfigJSON.containsKey("expose-parameters")? (JSONArray) this.inConfigJSON.get("expose-parameters"): null;
		this.inConfigRemoveConfig = this.inConfigJSON.containsKey("remove-config")? (JSONArray) this.inConfigJSON.get("remove-config"): null;
		this.inConfigChangeValue = this.inConfigJSON.containsKey("change-value")? (JSONArray) this.inConfigJSON.get("change-value"): null;
		this.inBulkJSON = getReplacedJSONObject(DEFAULT_IN_BULKCONFIG, this.inConfigJSON);
		this.envFileName = DEFAULT_IN_ENVPROPERTIES;
		this.outJSON = DEFAULT_IN_OUTCONFIG;

		loadProperties();
		
		processBulkJSON();
	}
	
	private void processBulkJSON() throws RemoveNodeException {
		
		if(this.inConfigExposeParametersArray != null)
			processBulkJSONNode("", this.inBulkJSON);	
	}
	
	@SuppressWarnings("unchecked")
	private void processBulkJSONNode(String path, JSONObject jsonObject) throws RemoveNodeException {
		
		processRemoveConfig(jsonObject);
		processChangeValue(jsonObject);
		
		if(jsonObject.containsKey("resourceType"))
			path = String.valueOf(jsonObject.get("resourceType")).replace("/", "_").substring(1);
		
		if(jsonObject.containsKey("id"))
			path = path + "_" + getEscapedValue(String.valueOf(jsonObject.get("id")));
		
		System.out.println("Path: " + path);
		
		processExposeConfig(path, jsonObject);
		
		for(Object key : jsonObject.keySet())
		{
			if(jsonObject.get(key) instanceof JSONObject)
			{
				JSONObject currentJSON = (JSONObject) jsonObject.get(key);

				String newPath = path + "_" + key;
				
				try
				{
					processBulkJSONNode(newPath, currentJSON);
				}catch(RemoveNodeException e)
				{
					jsonObject.remove(key);
				}
			}
			else if(jsonObject.get(key) instanceof JSONArray)
			{
				String newPath = path + "_" + key;
				
				JSONArray jsonArray = (JSONArray) jsonObject.get(key);
				
				JSONArray newJSONArray = new JSONArray();
				
				for(Object currentObject : jsonArray)
				{
					if(currentObject instanceof JSONObject)
					{
						try
						{
							processBulkJSONNode(newPath, (JSONObject) currentObject); 
							newJSONArray.add(currentObject);
							
						}catch(RemoveNodeException e)
						{
						}
					}
					else
						newJSONArray.add(currentObject);
				}

				jsonObject.put(key, newJSONArray);
			}
		}
		
		
	}
	
	private void processRemoveConfig(JSONObject jsonObject) throws RemoveNodeException
	{
		if(this.inConfigRemoveConfig != null)
		{
			for(Object configObject : this.inConfigRemoveConfig)
			{
				JSONObject configJSON = (JSONObject) configObject;

				String key = String.valueOf(configJSON.get("key"));
				String value = String.valueOf(configJSON.get("value"));
				
				if(jsonObject.containsKey(key))
				{
					String jsonValue = String.valueOf(jsonObject.get(key));
					
					if(jsonValue.equals(value) || jsonValue.matches(value))
					{
						System.out.println("Ignoring " + key + ":" + value);
						throw new RemoveNodeException();
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processChangeValue(JSONObject jsonObject)
	{		
		if(this.inConfigChangeValue != null)
		{
			for(Object configObject : this.inConfigChangeValue)
			{
				JSONObject configJSON = (JSONObject) configObject;

				String key = String.valueOf(configJSON.get("parameter-name"));
				
				JSONObject matchingIdentifier = (JSONObject) configJSON.get("matching-identifier");
				String matchingName = String.valueOf(matchingIdentifier.get("id-name"));
				String matchingValue = String.valueOf(matchingIdentifier.get("id-value"));
				
				if(jsonObject.containsKey(matchingName) && jsonObject.get(matchingName).equals(matchingValue))
				{
					Object newValue = configJSON.get("new-value");
					jsonObject.put(key, newValue);
				}
				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processExposeConfig(String path, JSONObject jsonObject)
	{
		
		if(this.inConfigExposeParametersArray != null)
		{
			for(Object configObject : this.inConfigExposeParametersArray)
			{
				JSONObject configJSON = (JSONObject) configObject;
				
				String parameterName = String.valueOf(configJSON.get("parameter-name"));
				
				if(jsonObject.containsKey(parameterName))
				{
					String replaceName = parameterName;
					String replaceValue = "";
					
					if(configJSON.containsKey("replace-name"))
					{
						replaceName = String.valueOf(configJSON.get("replace-name"));
						jsonObject.remove(parameterName);
					}
					else
						replaceValue = String.valueOf(jsonObject.get(parameterName));
					
					String currentIdentifier = null;
					if(configJSON.containsKey("unique-identifiers"))
					{
						JSONArray uidArray = (JSONArray) configJSON.get("unique-identifiers");
						for(Object uidObj : uidArray)
						{
							if(jsonObject.containsKey(uidObj))
								currentIdentifier = String.valueOf(jsonObject.get(uidObj));
						}
					}
	
					String propertyName = path + "_" + replaceName;
					if(currentIdentifier != null)
						propertyName = path + "_" + getEscapedValue(currentIdentifier) + "_" + getEscapedValue(replaceName);
					
					jsonObject.put(replaceName, "${" + propertyName + "}");
					
					if(!returnProperties.containsKey(propertyName))
						returnProperties.put(propertyName, replaceValue);
				}
			}
		}
	}
	
	private String getEscapedValue(String in)
	{
		return in.replace(" ", "_").replace("-", "_").replace("|", "_").replace(":", "");
	}
	
	private JSONObject getReplacedJSONObject(String fileName, JSONObject configuration) throws FileNotFoundException, ParseException
	{
		String jsonString = convertFileToString(fileName);
		
		JSONArray searchReplaceConfigs = configuration.containsKey("search-replace")? (JSONArray) configuration.get("search-replace") : new JSONArray();
		
		for(Object searchReplaceConfig : searchReplaceConfigs)
		{
			JSONObject searchReplaceConfigJSON = (JSONObject) searchReplaceConfig;
			
			String search = String.valueOf(searchReplaceConfigJSON.get("search"));
			String replace = String.valueOf(searchReplaceConfigJSON.get("replace"));
			String applyEnvFileStr = String.valueOf(searchReplaceConfigJSON.get("apply-env-file"));
			Boolean isApplyEnvFile = Boolean.parseBoolean(applyEnvFileStr);
			
			
			jsonString = jsonString.replace(search, replace);
			
			if(isApplyEnvFile)
			{
				String propertyName = replace.replace("$", "").replace("{", "").replace("}", "");
							
				if(!returnProperties.containsKey(propertyName))
				{
					returnProperties.put(propertyName, search);
				}
			}
		}
		
		JSONParser parser = new JSONParser();
		
		JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
		
		return jsonObject;
	}

	private JSONObject convertFileToJSON(String fileName) throws FileNotFoundException, ParseException {
		String jsonString = convertFileToString(fileName);
		
		JSONParser parser = new JSONParser();
		
		JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
		
		return jsonObject;
	}

	private String convertFileToString(String fileName) throws FileNotFoundException, ParseException {
		Scanner scanner = new Scanner(new File(fileName));
		
		String jsonString = scanner.useDelimiter("\\Z").next();
		
		scanner.close();
		
		return jsonString;
	}
	
	private void loadProperties() throws IOException {
		try {
			returnProperties.load(new FileInputStream(this.envFileName));
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void writeEnvFile() throws IOException {
		PrintWriter pw = new PrintWriter( this.envFileName );
		
		for(Object key : this.returnProperties.keySet())
		{
			String value = this.returnProperties.getProperty(String.valueOf(key), "");
			if(value.equals("null"))
				value = "";
			
			pw.println(String.valueOf(key) + "=" + value);
		}
		
		pw.close();
		
	}
	
	public void writeBulkConfigFile() throws IOException {
		        
        org.json.JSONObject confJSON = new org.json.JSONObject(this.inBulkJSON.toJSONString()); 
        FileOutputStream frBulkConfig = new FileOutputStream(this.outJSON);
        frBulkConfig.write(confJSON.toString(4).getBytes());
        frBulkConfig.close();
		
	}
}
