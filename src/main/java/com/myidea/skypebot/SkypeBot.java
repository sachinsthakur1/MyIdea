package com.myidea.skypebot;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;
import com.samczsun.skype4j.exceptions.ParseException;

public class SkypeBot {

	public static Skype loginToSkype(String skypeUid, String skypePwd){
		Skype skype = null;
		try {
			// write your code here
	        skype = new SkypeBuilder("sachin.sadanand.thakur", "Ness#123").withAllResources().build();
	        try {
	            skype.login();
	        } catch (InvalidCredentialsException e) {
	            e.printStackTrace();
	        } catch (ConnectionException e) {
	            e.printStackTrace();
	        } catch (NotParticipatingException e) {
	            e.printStackTrace();
	        }
		} catch (Exception e3) {
		    e3.printStackTrace();
		}
		return skype;
	}
	
	public static Chat getChat(Skype skype, String chatID) throws ConnectionException, ChatNotFoundException{
		Chat myChat = skype.loadChat(chatID);
		return myChat;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// write your code here
	        Skype skype = new SkypeBuilder("sachin.sadanand.thakur", "Ness#123").withAllResources().build();
	        try {
	            skype.login();
	        } catch (InvalidCredentialsException e) {
	            e.printStackTrace();
	        } catch (ConnectionException e) {
	            e.printStackTrace();
	        } catch (NotParticipatingException e) {
	            e.printStackTrace();
	        }
			Chat myChat = skype.loadChat("19:d0a34b49fcbd4f53995b0cf23cfcb943@thread.skype");
			if(myChat == null){
				System.out.println("chat is null");
			}
			myChat.sendMessage("Hello Rohan");
		} catch (Exception e3) {
		    e3.printStackTrace();
		}

	}

}
