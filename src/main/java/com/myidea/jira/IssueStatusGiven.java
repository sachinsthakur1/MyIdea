package com.myidea.jira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class IssueStatusGiven {

	public static String getJsonIssueStatus(String jiraUrl, String issue){		
		String authKey = Authentication.getAuthKey();
		Response rp = null;
		try{
		rp = RestAssured.given().headers("Content-Type","application/json").headers("cookie",authKey).get(jiraUrl + "/rest/api/2/issue/" + issue + "?expand=changelog");
		if(rp.getStatusCode() != 200){
			authKey = Authentication.getAuthKey();
		}
		}catch(Exception e){
			authKey = Authentication.getAuthKey();
		}
		return rp.getBody().asString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IssueStatusGiven obj = new IssueStatusGiven();
		String json = getJsonIssueStatus("", "CROS-3");
		System.out.println(json);		
		String totalComments = getTotalCommentsCount(json);
		System.out.println("Total comments : " + totalComments);
		String resturnMessage = getNewComment(json, "CROS-3");
		System.out.println("New Comment : " + resturnMessage);
		System.out.println("Change count : " + getChangeCount(json));
		String changeMessage = getNewChanges(json, "CROS-3");
		System.out.println("New Changes : " + changeMessage);		
		System.out.println("Issue status : " + getIssueStatus(json));
	}
	
	public static String getIssueStatus(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonObject fieldsObject = jsonElement.getAsJsonObject().get("fields").getAsJsonObject();
		JsonElement statusCategory = fieldsObject.getAsJsonObject().get("status").getAsJsonObject().getAsJsonObject().get("statusCategory");
		String issueStatus = statusCategory.getAsJsonObject().get("key").getAsString();		
		return issueStatus;
	}
	
	
	
	
	
	public static String getTotalCommentsCount(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonObject fieldsObject = jsonElement.getAsJsonObject().get("fields").getAsJsonObject();
		String totalComments = fieldsObject.getAsJsonObject().get("comment").getAsJsonObject().getAsJsonObject().get("total").getAsString();
		return totalComments;
	}
	
	public static String getChangeCount(String json){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		String totalComments = jsonElement.getAsJsonObject().get("changelog").getAsJsonObject().get("total").getAsString();
		return totalComments;
	}
	
	public static String getNewComment(String json, String issue){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonObject fieldsObject = jsonElement.getAsJsonObject().get("fields").getAsJsonObject();
		JsonArray commentArray = fieldsObject.getAsJsonObject().get("comment").getAsJsonObject().getAsJsonObject().get("comments").getAsJsonArray();
		String comment = commentArray.get(0).getAsJsonObject().get("body").getAsString();
		String commentAuthor = commentArray.get(0).getAsJsonObject().get("updateAuthor").getAsJsonObject().get("name").getAsString();
		String message = commentAuthor  + " commented on issue " + issue + ". Comment : \"" + comment +"\"";
		return message;
	}
	
	public static String getNewChanges(String json, String issue){
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
