package com.example.tolotranet.livecampus;

public class Event_ItemObject {
	private String Name;
	private String BottomText;
	private String MiniText;
	private int Index;
	private int UserId;
	
	public String getName(){
		return this.Name;
	}
	
	public String getBottomText(){
		return this.BottomText;
	}
	public String getMiniText(){
		return this.MiniText;
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
	public void setMiniText(String miniTxt){
		this.MiniText = miniTxt;
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
}
