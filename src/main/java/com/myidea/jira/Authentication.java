package com.myidea.jira;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class Authentication {
	public static String getAuthenticationKey(){
		Gson gson = new Gson();
		Response rp2 = RestAssured.given().header("Content-Type","application/json").body("{\"username\": \"sachinsthakur\", \"password\": \"Aindia#123\"}").when().post("http://localhost:8080/rest/auth/1/session");
		System.out.println(rp2.getBody().asString());
		JsonElement jsonElement = gson.fromJson(rp2.getBody().asString(), JsonElement.class);
		JsonObject jsonObject = jsonElement.getAsJsonObject().get("session").getAsJsonObject();
		String name = jsonObject.get("name").getAsString();
		String value = jsonObject.get("value").getAsString();
		String authentication = name + "=" + value; 
		return authentication;
	}
}
