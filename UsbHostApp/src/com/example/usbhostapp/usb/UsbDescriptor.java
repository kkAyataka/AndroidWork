package com.example.usbhostapp.usb;

import java.nio.ByteBuffer;

public class UsbDescriptor {
	public static final byte TYPE_DEVICE = 1;
	public static final byte TYPE_CONFIGURAtION = 2;
	public static final byte TYPE_STRING = 3;
	public static final byte TYPE_INTERFACE = 4;
	public static final byte TYPE_ENDPOINT = 5;
	public static final byte RESERVED_1 = 6;
	public static final byte RESERVED_2 = 7;
	public static final byte INTERFACE_POWER = 8;
	public static final byte OTG = 9;
	public static final byte DEBUG = 10;
	public static final byte INTERFACE_ASSOCIATION = 11;
	public static final byte BOS = 15;
	public static final byte DEVICE_CAPABILITY = 16;
	public static final byte SUPERSPEED_USB_ENDPOINT_COMPANION = 48;
	public static final byte SUPERSPEEDPLUS_ISOCHRONOUS_ENDPOINT_COMPANION = 49;
	
	
	public UsbDescriptor(byte[] rawDescriptor) {
		ByteBuffer b = ByteBuffer.wrap(rawDescriptor);
		
		while (b.hasRemaining()) {
			byte length = b.get();
			byte type = b.get();
		}
	}
	
	//public List<UsbDescriptorBase> descriptors;
	public UsbDeviceDescriptor deviceDescriptor;
}
