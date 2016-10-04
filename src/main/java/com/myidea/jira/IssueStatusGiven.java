package com.myidea.jira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class IssueStatusGiven {

	public String getJsonIssueStatus(IssueStatusGiven jiraapi, String issue){		
		String authKey = Authentication.getAuthenticationKey();
		Response rp = null;
		try{
		rp = RestAssured.given().headers("Content-Type","application/json").headers("cookie",authKey).get("http://localhost:8080/rest/api/2/issue/" + issue + "?expand=changelog");
		if(rp.getStatusCode() != 200){
			authKey = Authentication.getAuthenticationKey();
		}
		}catch(Exception e){
			authKey = Authentication.getAuthenticationKey();
		}
		return rp.getBody().asString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IssueStatusGiven obj = new IssueStatusGiven();
		String json = obj.getJsonIssueStatus(obj, "CROS-3");
		System.out.println(json);		
		String totalComments = obj.getTotalCommentsCount(json);
		System.out.println("Total comments : " + totalComments);
		String resturnMessage = obj.getNewComment(json, "CROS-3");
		System.out.println("New Comment : " + resturnMessage);
		System.out.println("Change count : " + obj.getChangeCount(json));
		String changeMessage = obj.getNewChanges(json, "CROS-3");
		System.out.println("New Changes : " + changeMessage);		
		System.out.println("Issue status : " + obj.getIssueStatus(json));
	}
	
	public String getIssueStatus(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonObject fieldsObject = jsonElement.getAsJsonObject().get("fields").getAsJsonObject();
		JsonElement statusCategory = fieldsObject.getAsJsonObject().get("status").getAsJsonObject().getAsJsonObject().get("statusCategory");
		String issueStatus = statusCategory.getAsJsonObject().get("key").getAsString();		
		return issueStatus;
	}
	
	
	
	
	
	public String getTotalCommentsCount(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonObject fieldsObject = jsonElement.getAsJsonObject().get("fields").getAsJsonObject();
		String totalComments = fieldsObject.getAsJsonObject().get("comment").getAsJsonObject().getAsJsonObject().get("total").getAsString();
		return totalComments;
	}
	
	public String getChangeCount(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		String totalComments = jsonElement.getAsJsonObject().get("changelog").getAsJsonObject().get("total").getAsString();
		return totalComments;
	}
	
	public String getNewComment(String json, String issue){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonObject fieldsObject = jsonElement.getAsJsonObject().get("fields").getAsJsonObject();
		JsonArray commentArray = fieldsObject.getAsJsonObject().get("comment").getAsJsonObject().getAsJsonObject().get("comments").getAsJsonArray();
		String comment = commentArray.get(0).getAsJsonObject().get("body").getAsString();
		String commentAuthor = commentArray.get(0).getAsJsonObject().get("updateAuthor").getAsJsonObject().get("name").getAsString();
		String message = commentAuthor  + " commented on issue " + issue + ". Comment : \"" + comment +"\"";
		return message;
	}
	
	public String getNewChanges(String json, String issue){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonElement fieldsObject = jsonElement.getAsJsonObject().get("changelog");
		JsonArray histories = fieldsObject.getAsJsonObject().get("histories").getAsJsonArray();		
		String changeAuther = histories.get(histories.size()-1).getAsJsonObject().get("author").getAsJsonObject().get("name").getAsString();		
		JsonArray itemArray = histories.get(histories.size()-1).getAsJsonObject().get("items").getAsJsonArray();
		String changes = "";
		for(JsonElement item : itemArray){
			String field = item.getAsJsonObject().get("field").getAsString();
			String updates = item.getAsJsonObject().get("toString").getAsString();
			changes = changes + "\"" + field + "\" : \"" + updates + "\",";
		}		
		String message = changeAuther + " has updated issue " + issue + ". New changes are  : " + changes;
		return message;	
	}
}
