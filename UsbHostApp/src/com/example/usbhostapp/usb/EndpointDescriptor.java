package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

public class EndpointDescriptor extends UsbDescriptorBase {
	public EndpointDescriptor(byte length, byte type, byte[] payload) {
		super(length, type);
		
		ByteBuffer b = ByteBuffer.wrap(payload);
		b.order(ByteOrder.LITTLE_ENDIAN);
		
		this.endpointAddress = b.get();
		this.attributes = b.get();
		this.maxPacketSize = b.getShort() & 0xFFFF;
		this.interval = b.get() & 0xFF;
		
		Log.i("Descriptor", "Endpoint maxPacketSize: " + maxPacketSize);
	}
	
	public byte endpointAddress;
	public byte attributes;
	public int maxPacketSize;
	public int interval;
}
