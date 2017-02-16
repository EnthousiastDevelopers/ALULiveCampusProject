package com.tolotranet.livecampus;


public class Sis_DetailListItem {
	public String DetailName = "";
	private String DetailValue = "";
	private int columnValue= 0;

	public Sis_DetailListItem(String Name, String Value) {
		// TODO Auto-generated constructor stub
		this.DetailName = Name;
		this.DetailValue = Value;
	}
	
	public Sis_DetailListItem(){
		
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

	public void setColumnValue(int columnValue) {
		this.columnValue = columnValue;
	}
	public int getColumnValue() {
		return this.columnValue;
	}
}
