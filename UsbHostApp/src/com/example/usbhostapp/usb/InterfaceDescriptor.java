package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class InterfaceDescriptor extends UsbDescriptorBase {
	public InterfaceDescriptor(byte length, byte type, byte[] payload) {
		super(length, type);
		
		ByteBuffer b = ByteBuffer.wrap(payload);
		b.order(ByteOrder.LITTLE_ENDIAN);
		
		this.interfaceNumber = b.get();
		this.alternateSetting = b.get();
		this.numEndpoints = b.get() & 0xFF;
		this.interfaceClass = b.get();
		this.interfaceSubClass = b.get();
		this.interfaceProtocol = b.get();
		this.interfaceIndex = b.get() & 0xFF;
	}
	
	public byte interfaceNumber;
	public byte alternateSetting;
	public int numEndpoints;
	public byte interfaceClass;
	public byte interfaceSubClass;
	public byte interfaceProtocol;
	public int interfaceIndex;
}
