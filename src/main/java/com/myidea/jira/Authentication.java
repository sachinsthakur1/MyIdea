package com.myidea.jira;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class Authentication {

	private static String jiraUrl;
	private static String userName;
	private static String password;
	private static String authKey;
	
	public static void initialize(String jiraurl, String uname, String pwd){
		userName = uname;
		password = pwd;
		jiraUrl = jiraurl;
	}
	
	public static void generateAuthenticationKey(){
		Gson gson = new Gson();
		Response rp2 = RestAssured.given().header("Content-Type","application/json").body("{\"username\": \"" + userName + "\", \"password\": \"" + password + "\"}").when().post(jiraUrl + "/rest/auth/1/session");
		System.out.println(rp2.getBody().asString());
		JsonElement jsonElement = gson.fromJson(rp2.getBody().asString(), JsonElement.class);
		JsonObject jsonObject = jsonElement.getAsJsonObject().get("session").getAsJsonObject();
		String name = jsonObject.get("name").getAsString();
		String value = jsonObject.get("value").getAsString();
		String authentication = name + "=" + value; 
		authKey = authentication;
	}
	
	public static String getAuthKey(){
		return authKey;
	}
}
