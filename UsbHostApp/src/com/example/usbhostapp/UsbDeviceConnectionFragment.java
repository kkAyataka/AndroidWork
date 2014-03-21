package com.example.usbhostapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UsbDeviceConnectionFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_usb_device_connection, container, false);
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		
		
	}
}
