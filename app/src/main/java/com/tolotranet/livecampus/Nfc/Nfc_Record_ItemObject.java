package com.tolotranet.livecampus.Nfc;


public class Nfc_Record_ItemObject {
	private String Name;
	private String BottomText;
	private String BottomText_2;
	private int Index;
	private int UserId;
	private int ImgId;
	
	public String getName(){
		return this.Name;
	}

	public String getBottomText(){
		return this.BottomText;
	}
	public String getBottomText_2(){
		return this.BottomText_2;
	}

	public int getIndex(){
		return this.Index;
	}
	public int getUserId(){
		return this.UserId;
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
	public void setBottomText_2(String bottomText_2){
		this.BottomText_2 = bottomText_2;
	}

	public void setIndex(int index){
		this.Index = index;
	}

	public void setUserId(int userId){
		this.UserId = userId;
	}

	public void setImgId(int imgId){
		this.ImgId = imgId;
	}
}
