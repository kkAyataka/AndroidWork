package com.example.usbhostapp;

import android.app.Fragment;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.usbhostapp.usb.UsbDescriptor;

public class UsbDeviceConnectionFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_usb_device_connection, container, false);
		return rootView;
	}
	
	@Override
	public void onStart() {
		Log.i(getTag(), "onSart");
		
		super.onStart();
		
		UsbDevice device = getArguments().getParcelable("device");
		//UsbInterface usbif = getArguments().getParcelable("interface");
		//UsbEndpoint ep = getArguments().getParcelable("endpoint");
		
		mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
		
		mConnection = mUsbManager.openDevice(device);
		UsbDescriptor desc = new UsbDescriptor(mConnection.getRawDescriptors());
		
		TextView view = (TextView) getView().findViewById(R.id.textView);
		view.setText(
				mConnection.toString() + " " +
				desc.deviceDescriptor.vendorId + " " +
				desc.deviceDescriptor.productId + " "
				);
	}
	
	@Override
	public void onPause() {
		Log.i(getTag(), "onPause");
		
		super.onPause();
		
		mConnection.close();
	}
	
	private UsbManager mUsbManager;
	private UsbDeviceConnection mConnection;
}
