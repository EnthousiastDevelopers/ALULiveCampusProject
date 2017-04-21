package com.tolotranet.livecampus.Maint;


public class Maint_ItemObject {
	private String Name;
	private String BottomText;
	private String State;
	private int Index;
	private int UserId;
	private int CommentsCount;

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
	public int getCommentsCount(){
		return this.CommentsCount;
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
	public void setCommentsCount(int index){
		this.CommentsCount = index;
	}

	public void setUserId(int userId){
		this.UserId = userId;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}
}
