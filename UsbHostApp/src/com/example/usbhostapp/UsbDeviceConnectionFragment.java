package com.example.usbhostapp;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.app.Fragment;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usbhostapp.usb.GlobalItem;
import com.example.usbhostapp.usb.LocalItem;
import com.example.usbhostapp.usb.Usage;
import com.example.usbhostapp.usb.UsbDescriptor;

public class UsbDeviceConnectionFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_usb_device_connection, container, false);
		ListView listView = (ListView) rootView.findViewById(R.id.usageListView);
		listView.setOnItemClickListener(onItemClickListener);
		return rootView;
	}
	
	@Override
	public void onStart() {
		Log.i(getTag(), "onSart");
		
		super.onStart();
		
		UsbDevice device = getArguments().getParcelable("device");
		mInterface = getArguments().getParcelable("interface");
		mEndpoint = getArguments().getParcelable("endpoint");
		
		mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
		
		mConnection = mUsbManager.openDevice(device);
		UsbDescriptor desc = new UsbDescriptor(mConnection.getRawDescriptors());
		
		boolean res = mConnection.claimInterface(mInterface, true);
		
		byte[] rawReportDescriptor = new byte[1611];
		int read = mConnection.controlTransfer(0x81, 0x06, 0x2200, 0, rawReportDescriptor, rawReportDescriptor.length, 3000);
		
		ArrayAdapter<Usage> adp = new ArrayAdapter<Usage>(getActivity(), android.R.layout.simple_list_item_1);
		try {
			ByteBuffer b = ByteBuffer.wrap(rawReportDescriptor);
			b.order(ByteOrder.LITTLE_ENDIAN);
			
			GlobalItem globalItem = new GlobalItem();
			LocalItem localItem = new LocalItem();
			while (b.hasRemaining()) {
				byte prefix = b.get();
				int size = 0;
				int type = 0;
				int tag = 0;
				
				if (prefix != 0xFE) {
					// short item
					size = ((prefix & 0x03) == 0x03) ? 4 : prefix & 0x03;
					type = (prefix >> 2) & 0x03;
					tag = (prefix >> 4) & 0x0F;
				}
				else {
					// long item
					size = b.get() & 0xFF;
					type = 0x3;
					tag = b.get() & 0xFF;
				}
				
				byte[] data = new byte[size];
				b.get(data);
					
				switch (type) {
				case 0: // main
					String format = "UPage:%04X, UID:%02X";
					switch (tag) {
					case 0x8: // Input
						Log.i("ReportDesc", String.format("Input   " + format, globalItem.usagePage, localItem.usage));
						break;
					case 0x9: // Output
						Log.i("ReportDesc", String.format("Output  " + format, globalItem.usagePage, localItem.usage));
						break;
					case 0xB: // Feature
						Usage usage = new Usage();
						usage.usagePage = globalItem.usagePage;
						usage.usageId = localItem.usage;
						usage.usageMin = localItem.usageMin;
						usage.usageMax = localItem.usageMax;
						usage.reportId = globalItem.reportId;
						usage.reportCount = globalItem.reportCount;
						usage.reportSize = globalItem.reportSize;
						adp.add(usage);
						Log.i("ReportDesc", String.format("Feature " + format, globalItem.usagePage, localItem.usage));
						break;
					case 0xA: // Collection
						Log.i("ReportDesc", String.format("Collection " + getInt(data, size)));
						break;
					case 0xC: // End Collection
						Log.i("ReportDesc", String.format("End Collection " + getInt(data, size)));
						break;
					}
					
					localItem = new LocalItem();
					break;
				case 1: // Global
					if (tag == 0x0) globalItem.usagePage = getInt(data, size);
					if (tag == 0x1) globalItem.logicalMin = getInt(data, size);
					if (tag == 0x2) globalItem.logicalMax = getInt(data, size);
					if (tag == 0x3) globalItem.physicalMin = getInt(data, size);
					if (tag == 0x4) globalItem.physicalMax = getInt(data, size);
					if (tag == 0x5) globalItem.unitExponent = getInt(data, size);
					if (tag == 0x6) globalItem.unit = getInt(data, size);
					if (tag == 0x7) globalItem.reportSize = getInt(data, size);
					if (tag == 0x8) globalItem.reportId = getInt(data, size);
					if (tag == 0x9) globalItem.reportCount = getInt(data, size);
					if (tag == 0xA) globalItem.push = getInt(data, size);
					if (tag == 0xB) globalItem.pop = getInt(data, size);
					break;
				case 2: // local
					if (tag == 0x0) localItem.usage = getInt(data, size);
					if (tag == 0x1) localItem.usageMin = getInt(data, size);
					if (tag == 0x2) localItem.usageMax = getInt(data, size);
					if (tag == 0x3) localItem.designatorIndex = getInt(data, size);
					if (tag == 0x4) localItem.designatorMin = getInt(data, size);
					if (tag == 0x5) localItem.designatorMax = getInt(data, size);
					if (tag == 0x7) localItem.stringIndex = getInt(data, size);
					if (tag == 0x8) localItem.stringMin = getInt(data, size);
					if (tag == 0x9) localItem.stringMax = getInt(data, size);
					if (tag == 0xA) localItem.delimiter = getInt(data, size);
					break;
				default:
					Log.e("ReportDesc", "Unknown type");
					break;
				}
			}
		}
		catch (BufferUnderflowException e) {
			Log.e("ReportDesc", e.toString());
		}
		
		ListView listView = (ListView) getView().findViewById(R.id.usageListView);
		listView.setAdapter(adp);
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Usage usage = (Usage) parent.getAdapter().getItem(position);
			
			if (usage != null) {
				byte[] buf = new byte[usage.reportCount * usage.reportSize / 8];
				// Get Report, Feature
				mConnection.controlTransfer(
						0xA1,
						0x01,
						0x0300 | usage.reportId,
						0,
						buf,
						buf.length,
						3000);
				ByteBuffer b = ByteBuffer.wrap(buf);
				//b.order(ByteOrder.LITTLE_ENDIAN);
				String text = "";
				for (int i = 0; i < buf.length; ++i) {
					text += Integer.toHexString(buf[i] & 0xFF);
					text += " ";
				}
				Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
			}
		}
	};
	
	public int getInt(byte[] data, int size) {
		ByteBuffer b = ByteBuffer.wrap(data);
		b.order(ByteOrder.LITTLE_ENDIAN);
		if (size == 1) return b.get() & 0xFF;
		if (size == 2) return b.getShort() & 0xFFFF;
		if (size == 4) return b.getInt();
		
		return 0;
	}
	
	@Override
	public void onPause() {
		Log.i(getTag(), "onPause");
		
		super.onPause();
		
		mConnection.releaseInterface(mInterface);
		mConnection.close();
	}
	
	private UsbManager mUsbManager;
	private UsbDeviceConnection mConnection;
	private UsbInterface mInterface;
	private UsbEndpoint mEndpoint;
}
