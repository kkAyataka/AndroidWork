package com.example.usbhostapp.usb;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;

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
			try {
				final byte length = b.get();
				final byte type = b.get();
				
				byte[] payload = new byte[length - 2];
				b.get(payload);
				
				UsbDescriptorBase desc;
				switch (type) {
				case 0x01: // Device
					desc = new UsbDeviceDescriptor(length, type, payload);
					break;
				case 0x02: // Configuration
					desc = new ConfigurationDescriptor(length, type, payload);
					break;
				case 0x04: // Interface
					desc = new InterfaceDescriptor(length, type, payload);
					break;
				case 0x05: // Endpoint
					desc = new EndpointDescriptor(length, type, payload);
					break;
				case 0x21: // HID
					desc = new HidClassDescriptor(length, type, payload);
					break;
				default:
					desc = new UsbDescriptorBase(length, type);
					Log.i("Descriptor", "Descriptor type: " + type + " len: " + length);
					break;
				}
				descriptors.add(desc);
			}
			catch (BufferUnderflowException e) {
				Log.e("UsbDescriptor", e.getMessage());
			}
		}
	}
	
	public List<UsbDescriptorBase> descriptors = new ArrayList<UsbDescriptorBase>();
	public UsbDeviceDescriptor deviceDescriptor;
}
