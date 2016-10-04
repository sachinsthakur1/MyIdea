package com.myidea.jira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class IssueStatusAll {
	
	public static String getTotalIssuesAllIssuesStatus(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		String totalIssues = jsonElement.getAsJsonObject().get("total").getAsString();
		return totalIssues;
	}
	
	public static String getLatIssueAllIssuesStatus(String json, String jiraProject){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonArray issueArray = jsonElement.getAsJsonObject().get("issues").getAsJsonArray();
		JsonObject fieldsObject = issueArray.get(0).getAsJsonObject().get("fields").getAsJsonObject();
		String newIssueKey = issueArray.get(0).getAsJsonObject().get("key").getAsString();
		String summery = fieldsObject.get("summary").getAsString();
		String reporterName = fieldsObject.get("reporter").getAsJsonObject().get("name").getAsString();
		String message = "New issue is created on Jira by \"" + reporterName + "\" under project " + jiraProject + ". \"Issue ID\" : \"" + newIssueKey + "\", \"Issue Summery\" : \"" + summery + "\"";
		return message;
	}
	
	public static String getJsonAllIssueStatus(String jiraUrl, String jiraProject){		
		String authKey = Authentication.getAuthKey();
		Response rp = null;
		try{
		rp = RestAssured.given().headers("Content-Type","application/json").headers("cookie",authKey).get(jiraUrl + "/rest/api/2/search?jql=project=" + jiraProject);
		if(rp.getStatusCode() != 200){
			authKey = Authentication.getAuthKey();
		}
		}catch(Exception e){
			authKey = Authentication.getAuthKey();
		}
		return rp.getBody().asString();
	}
	
	public static void main(String[] args) {
		Authentication.initialize("http://localhost:8080", "sachinsthakur", "Aindia#123");
		Authentication.generateAuthenticationKey();
		String allIssueJson = IssueStatusAll.getJsonAllIssueStatus("http://localhost:8080", "CROS");
		String totalIssues = IssueStatusAll.getTotalIssuesAllIssuesStatus(allIssueJson);
		System.out.println("Total issues raised : " + totalIssues);
		String issueMessage = IssueStatusAll.getLatIssueAllIssuesStatus(allIssueJson, "PROJ");
		System.out.println("New issue : " + issueMessage);
	}
}
