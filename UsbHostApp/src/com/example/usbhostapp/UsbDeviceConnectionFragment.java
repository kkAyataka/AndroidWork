package com.example.usbhostapp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.app.Fragment;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
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
		mInterface = getArguments().getParcelable("interface");
		mEndpoint = getArguments().getParcelable("endpoint");
		
		mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
		
		mConnection = mUsbManager.openDevice(device);
		UsbDescriptor desc = new UsbDescriptor(mConnection.getRawDescriptors());
		
		boolean res = mConnection.claimInterface(mInterface, true);
		
		byte[] rawReportDescriptor = new byte[148];
		int read = mConnection.controlTransfer(0x81, 0x06, 0x2200, 0, rawReportDescriptor, rawReportDescriptor.length, 3000);
		
		TextView view = (TextView) getView().findViewById(R.id.textView);
		view.setText(
				mConnection.toString() + " " +
				", " + read
				//desc.deviceDescriptor.productId + " "
				);
		
		ByteBuffer b = ByteBuffer.wrap(rawReportDescriptor);
		b.order(ByteOrder.LITTLE_ENDIAN);
		
	}
	
	@Override
	public void onPause() {
		Log.i(getTag(), "onPause");
		
		super.onPause();
		
		mConnection.releaseInterface(mInterface);
		mConnection.close();
	}
	
	private UsbManager mUsbManager;
	private UsbDeviceConnection mConnection;
	private UsbInterface mInterface;
	private UsbEndpoint mEndpoint;
}
