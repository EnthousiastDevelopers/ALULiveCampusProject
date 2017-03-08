package com.tolotranet.livecampus.Food;


public class Food_DetailListItem {
	public String DetailName = "";
	private String DetailValue = "";
	
	public Food_DetailListItem(String Name, String Value) {
		// TODO Auto-generated constructor stub
		this.DetailName = Name;
		this.DetailValue = Value;
	}
	
	public Food_DetailListItem(){
		
	}
	
	public void setDetailName(String DetailName){
		this.DetailName = DetailName;
	}
	
	public void setDetailValue(String DetailValue){
		this.DetailValue = DetailValue;
	}
	
	public String getDetailName(){
		return this.DetailName;
	}
	
	public String getDetailValue(){
		return this.DetailValue;
	}
}
