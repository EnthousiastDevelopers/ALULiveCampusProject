package com.tolotranet.livecampus.Sis.Interact;


public class Interaction_SendObject {
    private String Name;
    private String BottomText;
    private int Index;
    private int id;
    private int ImgId;
    private int menuId;
    private int score;
    private String sender, recipient, category, privacy;


    public String getName() {
        return this.Name;
    }

    public String getBottomText() {
        return this.BottomText;
    }

    public int getIndex() {
        return this.Index;
    }

    public int getId() {
        return this.id;
    }

    public int getImgId() {
        return this.ImgId;
    }


    public void setName(String name) {
        this.Name = name;
    }

    public void setBottomText(String bottomText) {
        this.BottomText = bottomText;
    }

    public void setIndex(int index) {
        this.Index = index;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImgId(int imgId) {
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

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getCategory() {
        return category;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
