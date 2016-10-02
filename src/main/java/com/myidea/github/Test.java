package com.myidea.github;

import java.io.Reader;

import groovy.json.internal.ReaderCharacterSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.internal.mapping.Jackson1Mapper;
import com.jayway.restassured.response.Response;

public class Test {

	public static void main(String[] args) {

		Response rp = RestAssured
				.given()
				.header("Authorization",
						"token a93e6d034de74d33515a80aecec0bc988006a41c")
				.header("Content-Type", "application/json")
				.get("https://api.github.com/repos/sachinsthakur1/Cuc-Test/commits?per_page=1");
		System.out.println(rp.getBody().asString());

		// TODO Auto-generated method stub
		/*
		 * Response rp = RestAssured .get(
		 * "https://api.github.com/repos/sachinsthakur1/Cuc-Test/commits?per_page=1"
		 * ); System.out.println(rp.getBody().asString()); Gson gson = new
		 * Gson(); JsonElement jsonElement =
		 * gson.fromJson(rp.getBody().asString(), JsonElement.class); JsonArray
		 * ar = jsonElement.getAsJsonArray(); System.out.println(ar.size());
		 * System.out.println(ar.get(0).getAsJsonObject().get("sha"));
		 */
	}

}
