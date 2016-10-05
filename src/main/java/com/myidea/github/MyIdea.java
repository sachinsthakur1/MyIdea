package com.myidea.github;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.myidea.jira.Authentication;
import com.myidea.jira.IssueStatusAll;
import com.myidea.jira.IssueStatusGiven;
import com.myidea.skypebot.SkypeBot;
import com.myidea.utils.Utility;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;

public class MyIdea {
	public static String commitID = null;
	
	public static void main(String[] args) throws InterruptedException, IOException, ConnectionException, ChatNotFoundException {
		Properties prop = new Properties();
		Utility ul = new Utility();
		prop = ul.loadProperty();
		String gitUrl = prop.getProperty("GitURL");
		String gitLoginId = prop.getProperty("GitHubLoginID");
		String gitHubAuthKey = prop.getProperty("GitHubAuthKey");
		String[] gitHubBranchName = prop.getProperty("GitHubBranchName").split("\\,");
			
		String jiraURL = prop.getProperty("JiraURL");
		String jiraUserName = prop.getProperty("JiraUserName");
		String jiraPassword = prop.getProperty("JiraPassword");
		String[] jiraProjects = prop.getProperty("JiraProject").split("\\,");
		String[] jiraIssues = prop.getProperty("JiraIssue").split("\\,");
			
		Skype skype = SkypeBot.loginToSkype("sachin.sadanand.thakur", "Ness#123");
		Chat chat = SkypeBot.getChat(skype, "19:d0a34b49fcbd4f53995b0cf23cfcb943@thread.skype");
		// TODO Auto-generated method stub
		for(String branch : gitHubBranchName){
		PollingThreadForGit infiniteLoopForGit = new PollingThreadForGit(1000);
		Thread loopingTh = new Thread(infiniteLoopForGit);
		infiniteLoopForGit.initialize(gitUrl, gitLoginId, gitHubAuthKey, branch.trim(), chat);
		loopingTh.start();
		}
		
		Authentication.initialize(jiraURL, jiraUserName, jiraPassword);
		Authentication.generateAuthenticationKey();
		// TODO Auto-generated method stub
		for(String jiraProject : jiraProjects){
		PollingThreadForJiraAllIssues infiniteLoopForJiraAllIssues = new PollingThreadForJiraAllIssues(1000);
		Thread loopingTh = new Thread(infiniteLoopForJiraAllIssues);
		infiniteLoopForJiraAllIssues.initialize(jiraURL, jiraProject.trim(), chat);
		loopingTh.start();
		}
		
		for(String jiraIssue : jiraIssues){
			PollingThreadForJiraGivenIssue infiniteLoopForJiraAllIssues = new PollingThreadForJiraGivenIssue(1000);
			Thread loopingTh = new Thread(infiniteLoopForJiraAllIssues);
			infiniteLoopForJiraAllIssues.initialize(jiraURL, jiraIssue.trim(), chat);
			loopingTh.start();
		}
	}
}

class PollingThreadForGit implements Runnable {
	private long sleep;
	public String gitCommitCount = null;
	Gson gson = new Gson();
	public String gitUrlG = null;
	public String gitLoginIdG = null;
	public String gitHubAuthKeyG = null;
	public String branchG = null;
	public Chat chatg = null;
	public void initialize(String gitUrl, String gitLoginId, String gitHubAuthKey, String branch, Chat chat){		
		gitUrlG = gitUrl;
		gitLoginIdG = gitLoginId;
		gitHubAuthKeyG = gitHubAuthKey;
		branchG = branch;		
		String json = GitHubApi.getJsonGitCommit(gitUrlG, gitLoginIdG, gitHubAuthKeyG, branchG);
		gitCommitCount = GitHubApi.getCommitID(json);
		chatg = chat;
	}
	PollingThreadForGit(long sleep) {
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
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void poll() throws InterruptedException, IOException, ConnectionException {
		while (true) {	
			String json = GitHubApi.getJsonGitCommit(gitUrlG, gitLoginIdG, gitHubAuthKeyG, branchG);
			String newGitCommitCount = GitHubApi.getCommitID(json);
			if(!newGitCommitCount.equals(gitCommitCount)){
				String singleCommitJson = GitHubApi.getJsonSingleCommitStatus(gitUrlG, gitLoginIdG, gitHubAuthKeyG, branchG, newGitCommitCount);
				String messageToPrint = GitHubApi.getCommitMessage(singleCommitJson, branchG);
				System.out.println(messageToPrint);
				chatg.sendMessage(messageToPrint);
				gitCommitCount = newGitCommitCount;
			}
			Thread.sleep(sleep);
		}
	}
}

class PollingThreadForJiraAllIssues implements Runnable {
	private long sleep;
	public String gitCommitCount = null;
	Gson gson = new Gson();
	public String jiraUrlG = null;
	public String gitLoginIdG = null;
	public String gitPasswordG = null;
	public String jiraProjectG = null;
	public String jiraTotalIssuesG = null;
	public Chat chatg = null;
	public void initialize(String jiraUrl, String jiraProject, Chat chat){		
		jiraUrlG = jiraUrl;		
		jiraProjectG = jiraProject;
		String json = IssueStatusAll.getJsonAllIssueStatus(jiraUrlG, jiraProjectG);
		System.out.println(json);
		jiraTotalIssuesG = IssueStatusAll.getTotalIssuesAllIssuesStatus(json);
		chatg = chat;
	}
	PollingThreadForJiraAllIssues(long sleep) {
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
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void poll() throws InterruptedException, IOException, ConnectionException {
		while (true) {	
			String json = IssueStatusAll.getJsonAllIssueStatus(jiraUrlG, jiraProjectG);
			String jiraTotalIssues = IssueStatusAll.getTotalIssuesAllIssuesStatus(json);
			if(!jiraTotalIssuesG.equals(jiraTotalIssues)){
				String issueMessage = IssueStatusAll.getLatIssueAllIssuesStatus(json, jiraProjectG);
				System.out.println(issueMessage);
				chatg.sendMessage(issueMessage);
				jiraTotalIssuesG = jiraTotalIssues;
			}
			Thread.sleep(sleep);
		}
	}
}

class PollingThreadForJiraGivenIssue implements Runnable {
	private long sleep;
	public String gitCommitCount = null;
	Gson gson = new Gson();
	public String jiraUrlG = null;
	public String jiraIssueG = null;
	public String changeCountG = null;
	public String totalCommentsCountG = null;
	public String issueStatusG = null;
	public Chat chatg = null;
	public void initialize(String jiraUrl, String jiraIssue, Chat chat){		
		jiraUrlG = jiraUrl;		
		jiraIssueG = jiraIssue;
		String json = IssueStatusGiven.getJsonIssueStatus(jiraUrlG, jiraIssueG);
		changeCountG = IssueStatusGiven.getChangeCount(json);
		totalCommentsCountG = IssueStatusGiven.getTotalCommentsCount(json);
		issueStatusG = IssueStatusGiven.getIssueStatus(json);
		chatg = chat;
	}
	PollingThreadForJiraGivenIssue(long sleep) {
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
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void poll() throws InterruptedException, IOException, ConnectionException {
		while (true) {	
			String json = IssueStatusGiven.getJsonIssueStatus(jiraUrlG, jiraIssueG);
			String changeCount = IssueStatusGiven.getChangeCount(json);
			String totalCommentsCount = IssueStatusGiven.getTotalCommentsCount(json);
			String issueStatus = IssueStatusGiven.getIssueStatus(json);
			if(!changeCountG.equals(changeCount)){
				String changeMessage = IssueStatusGiven.getNewChanges(json, jiraIssueG);
				System.out.println("New Changes : " + changeMessage);
				chatg.sendMessage(changeMessage);
				changeCountG = changeCount;
			}
			if (!totalCommentsCountG.equals(totalCommentsCount)) {
				String resturnMessage = IssueStatusGiven.getNewComment(json, jiraIssueG);
				System.out.println("New Comment : " + resturnMessage);
				chatg.sendMessage(resturnMessage);
				totalCommentsCountG = totalCommentsCount;
			}
			if (!issueStatusG.equals(issueStatus)) {
				System.out.println(jiraIssueG + " issue status has been updated from \"" + issueStatusG + "\" to \"" + issueStatus + "\"");
				chatg.sendMessage(jiraIssueG + " issue status has been updated from \"" + issueStatusG + "\" to \"" + issueStatus + "\"");
				issueStatusG = issueStatus;
			}
			Thread.sleep(sleep);
		}
	}
}
