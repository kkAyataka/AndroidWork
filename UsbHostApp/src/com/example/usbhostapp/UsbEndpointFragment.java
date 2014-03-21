package com.example.usbhostapp;

import android.app.Fragment;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class UsbEndpointFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_usb_endpoint, container, false);
		
		ListView listView = (ListView) rootView.findViewById(R.id.endpointListView);
		listView.setOnItemClickListener(onItemClickListener);
		
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		UsbDevice device = getArguments().getParcelable("device");
		UsbInterface usbif = getArguments().getParcelable("interface");
		
		ArrayAdapter<SimpleListData<Bundle>> adp = new ArrayAdapter<SimpleListData<Bundle>>(getActivity(), android.R.layout.simple_list_item_1);
		for (int i = 0; i < usbif.getEndpointCount(); ++i) {
			UsbEndpoint ep = usbif.getEndpoint(i);
			
			Bundle args = getArguments();
			args.putParcelable("endpoint", ep);
			
			String label = String.format("Type: %s, Direction: %s", getTypeStr(ep.getType()), getDirectionStr(ep.getDirection()));
			
			adp.add(new SimpleListData<Bundle>(label, args));
		}
		
		ListView view = (ListView) getView().findViewById(R.id.endpointListView);
		view.setAdapter(adp);
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SimpleListData<Bundle> item = (SimpleListData<Bundle>) parent.getAdapter().getItem(position);
		
			if (item != null) {
				
			}
			}
	};
	
	private String getTypeStr(int type) {
		IndexedString[] data = {
			new IndexedString(UsbConstants.USB_ENDPOINT_XFER_CONTROL, "Control"),
			new IndexedString(UsbConstants.USB_ENDPOINT_XFER_ISOC, "Isochronous"),
			new IndexedString(UsbConstants.USB_ENDPOINT_XFER_BULK, "Bulk"),
			new IndexedString(UsbConstants.USB_ENDPOINT_XFER_INT, "Interrupt"),
		};
		
		return IndexedString.toString(data,  type, "Unknown");
	}
	
	private String getDirectionStr(int direction) {
		IndexedString[] data = {
			new IndexedString(UsbConstants.USB_DIR_IN, "In"),
			new IndexedString(UsbConstants.USB_DIR_OUT, "Out"),
		};
		
		return IndexedString.toString(data, direction, "Unknown");
	}
}
