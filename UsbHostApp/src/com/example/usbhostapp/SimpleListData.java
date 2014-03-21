package com.example.usbhostapp;

public class SimpleListData<T> {
	public SimpleListData(String label, T value) {
		this.label = label;
		this.value = value;
	}
	
	public String label;
	public T value;
	
	@Override
	public String toString() {
		return label;
	}
}
