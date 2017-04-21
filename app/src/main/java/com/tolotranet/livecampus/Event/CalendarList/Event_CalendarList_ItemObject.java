package com.tolotranet.livecampus.Event.CalendarList;


public class Event_CalendarList_ItemObject {
    private String Name;
    private String BottomText;
    private String MiniText;
    private String backGroundColor, ObjectID;
    private int Index;
    private int type;
    private Boolean State;

    public String getName() {
        return this.Name;
    }

    public String getBottomText() {
        return this.BottomText;
    }

    public String getMiniText() {
        return this.MiniText;
    }

    public int getIndex() {
        return this.Index;
    }

    public Boolean getState() {
        return this.State;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setMiniText(String miniTxt) {
        this.MiniText = miniTxt;
    }

    public void setBottomText(String bottomText) {
        this.BottomText = bottomText;
    }

    public void setIndex(int index) {
        this.Index = index;
    }

    public void setState(Boolean state) {
        this.State = state;
    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public String getBackGroundColor() {
        return backGroundColor;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
