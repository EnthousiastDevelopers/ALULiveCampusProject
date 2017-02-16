package com.tolotranet.livecampus;


public class Settings_ItemObject {
	private String Name;
	private String BottomText;
	private int Index;
	private int OptionId;
	private int ImgId;
	
	public String getName(){
		return this.Name;
	}
	
	public String getBottomText(){
		return this.BottomText;
	}
	
	public int getIndex(){
		return this.Index;
	}
	public int getOptionId(){
		return this.OptionId;
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

	public void setOptionId(int userId){
		this.OptionId = userId;
	}

	public void setImgId(int imgId){
		this.ImgId = imgId;
	}
}
