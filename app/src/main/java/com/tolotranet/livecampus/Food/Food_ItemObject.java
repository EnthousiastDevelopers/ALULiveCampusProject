package com.tolotranet.livecampus.Food;


public class Food_ItemObject {
	private String Name;
	private String BottomText1;
	private String BottomText2;
	private String BottomText3;
	private String BottomText4;
	private String BottomText5;
	private String RightText;
	private int Index;
	private int UserId;
	private int ImgID;
	private int votes;
	private int comments;

	public Food_ItemObject() {
	}

	public String getName(){
		return this.Name;
	}
	
	public String getBottomText1(){
		return this.BottomText1;
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

	public void setBottomText1(String bottomText){
		this.BottomText1 = bottomText;
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

	public void setBottomText2(String bottomText2) {
		this.BottomText2 = bottomText2;
	}

	public void setBottomText3(String bottomText3) {
		this.BottomText3 = bottomText3;
	}

	public void setBottomText4(String bottomText4) {
		this.BottomText4 = bottomText4;
	}

	public void setBottomText5(String bottomText5) {
		this.BottomText5 = bottomText5;
	}

	public String getBottomText2() {
		return BottomText2;
	}

	public String getBottomText3() {
		return BottomText3;
	}

	public String getBottomText4() {
		return BottomText4;
	}

	public String getBottomText5() {
		return BottomText5;
	}
}
