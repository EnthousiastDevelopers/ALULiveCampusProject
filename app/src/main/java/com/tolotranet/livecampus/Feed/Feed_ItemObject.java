package com.tolotranet.livecampus.Feed;


public class Feed_ItemObject {
	private String Name;
	private String BottomText;
	private String RightText;
	private int Index;
	private int UserId;
	private int ImgID;
	private int votes;
	private int comments;

	public String getName(){
		return this.Name;
	}
	
	public String getBottomText(){
		return this.BottomText;
	}
	
	public int getIndex(){
		return this.Index;
	}
	public int getImgID(){
		return this.ImgID;
	}
	public int getUserId(){
		return this.UserId;
	}
	
	public void setName(String name){
		this.Name = name;
	}
	public void setImgID(int name){
		this.ImgID = name;
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

	public void setVotes(int votes) {
		this.votes = votes;
	}
	public int getVotes() {
		return   this.votes;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getComments() {
		return comments;
	}

	public String getRightText() {
		return RightText;
	}

	public void setRightText(String rightText) {
		RightText = rightText;
	}
}
