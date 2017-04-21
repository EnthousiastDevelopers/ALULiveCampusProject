package com.tolotranet.livecampus.App;


public class Apps_ItemObject {
	private String Name;
	private String BottomText;
	private int Index;
	private int id;
	private int ImgId;
	private int menuId;
	private int score;

	public String getName(){
		return this.Name;
	}
	
	public String getBottomText(){
		return this.BottomText;
	}
	
	public int getIndex(){
		return this.Index;
	}
	public int getId(){
		return this.id;
	}
	public int getImgId(){
		return this.ImgId;
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

	public void setId(int id){
		this.id = id;
	}

	public void setImgId(int imgId){
		this.ImgId = imgId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}
}
