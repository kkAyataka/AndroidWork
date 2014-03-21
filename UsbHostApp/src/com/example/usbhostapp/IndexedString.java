package com.example.usbhostapp;

public class IndexedString {
	public static String toString(IndexedString[] data, int index) {
		return toString(data, index, "");
	}
	
	public static String toString(IndexedString[] data, int index, String def) {
		for (IndexedString d : data) {
			if (d.getIndex() == index) {
				return d.getValue();
			}
		}
		
		return def;
	}
	
	IndexedString(int index, String value) {
		this.mIndex = index;
		this.mValue = value;
	}
	
	public int getIndex() {
		return mIndex;
	}
	private int mIndex;
	
	public String getValue() {
		return mValue;
	}
	public String mValue;
}
