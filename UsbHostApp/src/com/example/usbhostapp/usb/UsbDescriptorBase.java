package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;

public class UsbDescriptorBase {
	public UsbDescriptorBase(byte length, byte type) {
		this.length = length;
		this.type = type;
	}
	
	public byte length;
	public byte type;
}
