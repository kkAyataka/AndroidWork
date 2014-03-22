package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;

public class UsbDeviceDescriptor extends UsbDescriptorBase {
	public UsbDeviceDescriptor(ByteBuffer rawDescriptor) {
		super(rawDescriptor);
		
		this.bcdUsb = rawDescriptor.get();
		this.deviceClass = rawDescriptor.get();
		this.deviceSubClass= rawDescriptor.get();
		this.deviceProtocol = rawDescriptor.get();
		this.maxPacketSize = rawDescriptor.get();
		this.vendorId = rawDescriptor.getShort();
		this.productId = rawDescriptor.getShort();
		this.bcdDevice = rawDescriptor.getShort();
		this.manufacturer = rawDescriptor.get();
		this.product = rawDescriptor.get();
		this.serialNumber = rawDescriptor.get();
		this.numConfigurations = rawDescriptor.get();
	}
	
	public short bcdUsb;
	public byte deviceClass;
	public byte deviceSubClass;
	public byte deviceProtocol;
	public byte maxPacketSize;
	public short vendorId;
	public short productId;
	public short bcdDevice;
	public byte manufacturer;
	public byte product;
	public byte serialNumber;
	public byte numConfigurations;
}
