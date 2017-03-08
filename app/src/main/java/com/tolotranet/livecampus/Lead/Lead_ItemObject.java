package com.tolotranet.livecampus.Lead;


public class Lead_ItemObject {
	private String Name;
	private String BottomText;
	private int Index;
	private int UserId;
	private int Score;

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
	public int getScore(){
		return this.Score;
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
	public void setScore(int index){
		this.Score = index;
	}

	public void setUserId(int userId){
		this.UserId = userId;
	}
}
