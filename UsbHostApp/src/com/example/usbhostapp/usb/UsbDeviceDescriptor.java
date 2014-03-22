package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

public class UsbDeviceDescriptor extends UsbDescriptorBase {
	public UsbDeviceDescriptor(byte length, byte type, byte[] payload) {
		super(length, type);
		
		ByteBuffer b = ByteBuffer.wrap(payload);
		b.order(ByteOrder.LITTLE_ENDIAN);
		
		this.bcdUsb = b.getShort();
		this.deviceClass = b.get();
		this.deviceSubClass= b.get();
		this.deviceProtocol = b.get();
		this.maxPacketSize = b.get();
		this.vendorId = b.getShort();
		this.productId = b.getShort();
		this.bcdDevice = b.getShort();
		this.manufacturer = b.get();
		this.product = b.get();
		this.serialNumber = b.get();
		this.numConfigurations = b.get();
		
		Log.i("Descriptor", "MaxPacketSize: " + maxPacketSize + " " + Math.pow(2, maxPacketSize));
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
