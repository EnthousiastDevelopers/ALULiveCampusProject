package com.tolotranet.livecampus.Com;


public class Com_ItemObject {
	private String Name;
	private String BottomText;
	private String Parent;
	private String Object;
	private String Author;
	private int Comments;
	private int Index;
	private int UserId;
	private int ImgID;
	private int votes;

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

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getObject() {
		return Object;
	}

	public void setObject(String object) {
		Object = object;
	}

	public String getParent() {
		return Parent;
	}

	public void setParent(String parent) {
		Parent = parent;
	}

	public int getComments() {
		return Comments;
	}

	public void setComments(int comments) {
		Comments = comments;
	}
}
