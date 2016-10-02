package com.myidea.github;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.myidea.utils.Utility;

public class GitHubConnect {
	static long sleep = 1100;
	
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		PollingThread infiniteLoop = new PollingThread(500);
		Thread loopingTh = new Thread(infiniteLoop);
		loopingTh.start();
		Thread.sleep(2 * sleep);
		//loopingTh.interrupt();
		/*Response rp = RestAssured
				.get("https://api.github.com/repos/sachinsthakur1/Cuc-Test/commits?per_page=1");
		System.out.println(rp.getBody().asString());*/
	}
}

class PollingThread implements Runnable {
	private long sleep;
	static Properties prop = new Properties();
	Gson gson = new Gson();
	
	PollingThread(long sleep) {
		this.sleep = sleep;
	}

	public void run() {
		System.out.println("Starting to Poll");
		try {
			poll();
		} catch (InterruptedException e) {
			System.out.println("Stopping to Poll");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void poll() throws InterruptedException, IOException {
		int count = 0;
		Utility ul = new Utility();
		prop = ul.loadProperty();
		String newCommit = "";
		String oldCommit = "";
		Response rp = null;
		while (true) {			
			try{
			rp = RestAssured.given().header("Authorization","Bearer a93e6d034de74d33515a80aecec0bc988006a41c")
					.header("Content-Type","application/json")
					.get("https://api.github.com/repos/sachinsthakur1/Cuc-Test/commits?per_page=1");
			System.out.println(rp.getBody().asString());
			if(rp.getStatusCode() == 200){
			JsonElement jsonElement = gson.fromJson(rp.getBody().asString(), JsonElement.class);
			JsonArray ar = jsonElement.getAsJsonArray();
			newCommit = ar.get(0).getAsJsonObject().get("sha").getAsString();
			oldCommit = prop.get("LastCommit").toString();
			}else{
				throw new InterruptedException("Something went wrong : Failed to get json data");
				
			}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			System.out.println("newCommit" + newCommit);
			System.out.println("oldCommit" + oldCommit);
			
			System.out.println("iteration :" + (count++)+ " " + rp.getBody().asString());
			
			if(!oldCommit.equals(newCommit)){
				System.out.println("Commit has been changed");
				prop.setProperty("LastCommit", newCommit);
				prop = ul.updatePropertyFile(prop);
			}
			
			Thread.sleep(sleep);
		}
	}
}
