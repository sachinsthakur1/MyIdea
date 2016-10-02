package com.myidea.skypebot;

import com.skype.Chat;
import com.skype.ContactList;
import com.skype.Group;
import com.skype.Skype;

public class SkypeBot {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ContactList contactList = Skype.getContactList();
			System.out.println(contactList.getAllFriends().length);
			System.out.println(contactList.getAllSystemGroups().length);
			System.out.println(contactList.getAllGroups().length);
			//System.out.println(chat.length);
			for(Group group : contactList.getAllSystemGroups())
			{
				System.out.println(group.getDisplayName());
				
			}
			//System.out.println(group[0].getDisplayName());
			/*System.out.println(Skype.getContactList().getAllGroups());
			Chat[] chat = Skype.getAllChats();
			chat[0].*/
			
			/*Group[] group = Skype.getContactList().getAllGroups();
			group.*/
			/*for (Group group : Skype.getContactList().getAllGroups()) {
				System.out.println(group.getDisplayName());
		        if ((group.getDisplayName()).equals("Nameofthegroup")) { //Whatever the group name is
		            String id = group.getId();
		            Skype.chat(id).send(ep.getDisplayName() + " joins !");
		            ep.sendMessage("Die ID: "+ id);
		        }
		    }*/
			//System.out.println(Skype.getContactList().getAllGroups().);
		    /*for (Group group : Skype.getContactList().getAllGroups()) {
		        if ((group.getDisplayName()).equals("Nameofthegroup")) { //Whatever the group name is
		            String id = group.getId();
		            Skype.chat(id).send(ep.getDisplayName() + " joins !");
		            ep.sendMessage("Die ID: "+ id);
		        }
		    }*/
		} catch (Exception e3) {
		    e3.printStackTrace();
		}

	}

}
