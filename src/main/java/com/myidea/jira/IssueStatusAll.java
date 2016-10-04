package com.myidea.jira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class IssueStatusAll {
	
	public String getTotalIssuesAllIssuesStatus(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		String totalIssues = jsonElement.getAsJsonObject().get("total").getAsString();
		return totalIssues;
	}
	
	public String getLatIssueAllIssuesStatus(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonArray issueArray = jsonElement.getAsJsonObject().get("issues").getAsJsonArray();
		JsonObject fieldsObject = issueArray.get(0).getAsJsonObject().get("fields").getAsJsonObject();
		String newIssueKey = issueArray.get(0).getAsJsonObject().get("key").getAsString();
		String summery = fieldsObject.get("summary").getAsString();
		String reporterName = fieldsObject.get("reporter").getAsJsonObject().get("name").getAsString();
		String message = "New issue is created on Jira by " + reporterName + ". \"Issue ID\" : \"" + newIssueKey + "\", \"Issue Summery\" : \"" + summery + "\"";
		return message;
	}
	
	public String getJsonAllIssueStatus(IssueStatusAll jiraapi){		
		String authKey = Authentication.getAuthenticationKey();
		Response rp = null;
		try{
		rp = RestAssured.given().headers("Content-Type","application/json").headers("cookie",authKey).get("http://localhost:8080/rest/api/2/search?jql=project=CROS");
		if(rp.getStatusCode() != 200){
			authKey = Authentication.getAuthenticationKey();
		}
		}catch(Exception e){
			authKey = Authentication.getAuthenticationKey();
		}
		return rp.getBody().asString();
	}
	
	public static void main(String[] args) {
		IssueStatusAll jiraapi = new IssueStatusAll();
		String allIssueJson = jiraapi.getJsonAllIssueStatus(jiraapi);
		String totalIssues = jiraapi.getTotalIssuesAllIssuesStatus(allIssueJson);
		System.out.println("Total issues raised : " + totalIssues);
		String issueMessage = jiraapi.getLatIssueAllIssuesStatus(allIssueJson);
		System.out.println("New issue : " + issueMessage);
	}
}
