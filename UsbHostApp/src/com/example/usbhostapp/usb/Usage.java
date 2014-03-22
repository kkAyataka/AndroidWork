package com.example.usbhostapp.usb;

public class Usage {
	public int usagePage;
	public int usageId;
	public int usageMin;
	public int usageMax;
	public int reportId;
	public int reportSize;
	public int reportCount;
	
	@Override
	public String toString() {
		return String.format("UPage:%04X,  UID:%02X, rid:%02X, rSize:%d, rCount:%d, min:%d, max:%d",
				usagePage,
				usageId,
				reportId,
				reportSize,
				reportCount,
				usageMin,
				usageMax);
	}
}
