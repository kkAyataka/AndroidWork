package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

public class HidClassDescriptor extends UsbDescriptorBase {
	public HidClassDescriptor(byte length, byte type, byte[] payload) {
		super(length, type);
		
		ByteBuffer b = ByteBuffer.wrap(payload);
		b.order(ByteOrder.LITTLE_ENDIAN);
		
		this.bcdHid = b.getShort() & 0xFFFF;
		this.countryCode = b.get();
		this.numDescriptors = b.get();
		this.descriptorType = b.get();
		this.descriptorLength = b.getShort() & 0xFFFF;
		
 		Log.i("Descriptor",
				"Report Descriptor type: " + Integer.toString(descriptorType, 16) + " size: " + descriptorLength);
	}
	
	public int bcdHid;
	public byte countryCode;
	public byte numDescriptors;
	public byte descriptorType;
	public int descriptorLength;
	public byte optionalDescriptorType;
	public int optionalDescriptorLength;
}
