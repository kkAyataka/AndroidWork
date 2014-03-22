package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConfigurationDescriptor extends UsbDescriptorBase {
	public ConfigurationDescriptor(byte length, byte type, byte[] payload) {
		super(length, type);
		
		ByteBuffer b = ByteBuffer.wrap(payload);
		b.order(ByteOrder.LITTLE_ENDIAN);
		
		this.totalLength = b.getShort() & 0xFFFF;
		this.numInterfaces = b.get();
		this.configurationValue = b.get();
		this.configuration = b.get();
		this.attributes = b.get();
	}
	
	public int totalLength;
	public byte numInterfaces;
	public byte configurationValue;
	public byte configuration;
	public byte attributes;
	public byte maxPower;	
}
