package com.myidea.github;

import java.io.Reader;

import groovy.json.internal.ReaderCharacterSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.internal.mapping.Jackson1Mapper;
import com.jayway.restassured.response.Response;

public class GitHubApi {
	static Gson gson = new Gson();	
	public static String getJsonGitCommit(String apiUrl, String gitHubLoginID, String authToken, String branchName){
		Response rp = RestAssured.given().header("Authorization","Bearer " + authToken)
				.header("Content-Type","application/json")
				.get(apiUrl + "/repos/" + gitHubLoginID + "/" + branchName + "/commits?per_page=1");
		return rp.getBody().asString();
	}
	
	public static String getCommitID(String json){
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		JsonArray ar = jsonElement.getAsJsonArray();
		String commit = ar.get(0).getAsJsonObject().get("sha").getAsString();
		return commit;
	}
	
	public static String getJsonSingleCommitStatus(String apiUrl, String gitHubLoginID, String authToken, String branchName, String commitID){
		Response rp = RestAssured.given().header("Authorization","Bearer " + authToken)
				.header("Content-Type","application/json")
				.get(apiUrl + "/repos/" + gitHubLoginID + "/" + branchName + "/commits/" + commitID);
		return rp.getBody().asString(); 
	}
	
	public static String getCommitMessage(String json, String branch){
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		String commiter = jsonElement.getAsJsonObject().get("commit").getAsJsonObject().get("committer").getAsJsonObject().get("name").getAsString();
		JsonArray filesArray = jsonElement.getAsJsonObject().get("files").getAsJsonArray();
		String updatedFiles = "";
		for(JsonElement file : filesArray){
			String fileName = file.getAsJsonObject().get("filename").getAsString();
			String status = file.getAsJsonObject().get("status").getAsString();
			updatedFiles = updatedFiles + "File \"" + fileName +"\" has been \"" + status + "\", ";
		}
		String message = commiter  + " has commited new changes to branch \"" + branch + "\". CHANGES : "  + updatedFiles;
		return message;
	}
	
	public static void main(String[] args) {
		String json = getJsonGitCommit("https://api.github.com", "sachinsthakur1", "f0348a5479ab8fd46fa72319aa7961ca26851d42", "Cuc-Test");
		System.out.println("commit json : " + json);
		String commit = getCommitID(json);
		System.out.println("Commit : " + commit);
		String singleCommitJson = getJsonSingleCommitStatus("https://api.github.com", "sachinsthakur1", "f0348a5479ab8fd46fa72319aa7961ca26851d42", "Cuc-Test", commit);
		System.out.println("Single commit json : " + singleCommitJson);
		String messageToPrint = getCommitMessage(singleCommitJson, "Cuc-Test" );
		System.out.println("Message to print : " + messageToPrint);
		
	}
}
