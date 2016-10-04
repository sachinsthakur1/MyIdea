package com.myidea.jira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class JiraApis {

	public String getAuthenticationKey(){
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
		String message = "New issue is created on Jira by " + reporterName + ". Issue ID : " + newIssueKey + ", Issue Summery : " + summery;
		return message;
	}
	
	public String getJsonAllIssueStatus(JiraApis jiraapi){		
		String authKey = jiraapi.getAuthenticationKey();
		Response rp = null;
		try{
		rp = RestAssured.given().headers("Content-Type","application/json").headers("cookie",authKey).get("http://localhost:8080/rest/api/2/search?jql=project=CROS");
		if(rp.getStatusCode() != 200){
			authKey = jiraapi.getAuthenticationKey();
		}
		}catch(Exception e){
			authKey = jiraapi.getAuthenticationKey();
		}
		return rp.getBody().asString();
	}
	
	public static void main(String[] args) {
		JiraApis jiraapi = new JiraApis();
		/*String allIssueJson = jiraapi.getJsonAllIssueStatus(jiraapi);
		String totalIssues = jiraapi.getTotalIssuesAllIssuesStatus(allIssueJson);
		System.out.println("Total issues raised : " + totalIssues);
		String issueMessage = jiraapi.getLatIssueAllIssuesStatus(allIssueJson);
		System.out.println(issueMessage);*/
	}
	
	public String getJsonIssueStatus(JiraApis jiraapi, String issue){
		String authKey = jiraapi.getAuthenticationKey();
		Response rp = null;
		try{
		rp = RestAssured.given().headers("Content-Type","application/json").headers("cookie",authKey).get("http://localhost:8080/rest/api/2/issue/" + issue + "?expand=changelog");
		if(rp.getStatusCode() != 200){
			authKey = jiraapi.getAuthenticationKey();
		}
		}catch(Exception e){
			authKey = jiraapi.getAuthenticationKey();
		}
		return rp.getBody().asString();
	}
}
