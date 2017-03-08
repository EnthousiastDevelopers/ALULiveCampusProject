package com.tolotranet.livecampus.Mu;


public class Mu_DetailListItem {
	public String DetailName = "";
	private String DetailValue = "";
	
	public Mu_DetailListItem(String Name, String Value) {
		// TODO Auto-generated constructor stub
		this.DetailName = Name;
		this.DetailValue = Value;
	}
	
	public Mu_DetailListItem(){
		
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
