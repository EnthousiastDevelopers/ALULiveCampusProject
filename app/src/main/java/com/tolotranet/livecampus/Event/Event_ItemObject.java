package com.tolotranet.livecampus.Event;


public class Event_ItemObject {
	private String Name;
	private String BottomText;
	private String MiniText;
	private String timeStamp, ObjectID, BgColor;
	private int Index;
	private int UserId;
	private int type;
	private int  childCount;

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

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getObjectID() {
		return ObjectID;
	}

	public void setObjectID(String objectID) {
		ObjectID = objectID;
	}

	public String getBgColor() {
		return BgColor;
	}

	public void setBgColor(String bgColor) {
		BgColor = bgColor;
	}

    public int getType() {
        return type;
    }

	public void setType(int type) {
		this.type = type;
	}


	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public int getChildCount() {
		return childCount;
	}
}
