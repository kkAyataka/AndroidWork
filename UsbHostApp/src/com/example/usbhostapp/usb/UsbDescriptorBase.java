package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;

public class UsbDescriptorBase {
	public UsbDescriptorBase(ByteBuffer rawDescriptor) {
		this.length = rawDescriptor.get();
		this.type = rawDescriptor.get();
	}
	
	public byte length;
	public byte type;
}
