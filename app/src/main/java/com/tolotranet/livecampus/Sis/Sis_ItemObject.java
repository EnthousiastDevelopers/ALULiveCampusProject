package com.tolotranet.livecampus.Sis;


public class Sis_ItemObject {
	private String Name;
	private String Email;
	private String BottomText;
	private int Index;
	private int UserId;
	
	public String getName(){
		return this.Name;
	}
	
	public String getBottomText(){
		return this.BottomText;
	}
	
	public int getIndex(){
		return this.Index;
	}
	public int getUserId(){
		return this.UserId;
	}
	
	public void setName(String name){
		this.Name = name;
	}
	
	public void setBottomText(String bottomText){
		this.BottomText = bottomText;
	}
	
	public void setIndex(int index){
		this.Index = index;
	}

	public void setUserId(int userId){
		this.UserId = userId;
	}

    public String getEmail() {
		return Email;
    }

	public void setEmail(String email) {
		Email = email;
	}
}
