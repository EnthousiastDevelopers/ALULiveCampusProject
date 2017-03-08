package com.tolotranet.livecampus.Com;


public class Com_DetailListItem {
	public String DetailName = "";
	private String DetailValue = "";
	
	public Com_DetailListItem(String Name, String Value) {
		// TODO Auto-generated constructor stub
		this.DetailName = Name;
		this.DetailValue = Value;
	}
	
	public Com_DetailListItem(){
		
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
