package com.example.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class persons implements Parcelable{
	int id;
	String name;
	String number;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "persons [id=" + id + ", name=" + name + ", number=" + number
				+ "]";
	}
	public persons(int id, String name, String number) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
	}
	public persons() {
		super();

	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		 dest.writeString(name);  
	     dest.writeString(number);
	}

	public static final Parcelable.Creator<persons> CREATOR = new Creator<persons>()  
		    {  
		        public persons createFromParcel(Parcel source)  
		        {  
		        	persons person = new persons(); 
		        	person.id = source.readInt();
		            person.name = source.readString();  
		            person.number = source.readString();  
		            return person;  
		        }  
		        public persons[] newArray(int size)  
		        {  
		            return new persons[size];  
		        }  
		    };  
	
}
