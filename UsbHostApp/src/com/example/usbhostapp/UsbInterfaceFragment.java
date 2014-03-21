package com.example.usbhostapp;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class UsbInterfaceFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater infrator, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = infrator.inflate(R.layout.fragment_usb_interface, container, false);
		
		ListView listView = (ListView) rootView.findViewById(R.id.interfaceListView);
		listView.setOnItemClickListener(onItemClickListener);
		
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		UsbDevice device = (UsbDevice) getArguments().getParcelable("com.example.USB_DEVICE");
		
		ArrayAdapter<SimpleListData<Bundle>> adp = new ArrayAdapter<SimpleListData<Bundle>>(getActivity(), android.R.layout.simple_list_item_1);
		for (int i = 0; i < device.getInterfaceCount(); ++i) {
			UsbInterface usbif = device.getInterface(i);
			
			Bundle args = new Bundle();
			args.putParcelable("device", device);
			args.putParcelable("interface", usbif);
			
			String label = String.format("ID:%2d, Class: %2d", usbif.getId(), usbif.getInterfaceClass());
			
			adp.add(new SimpleListData<Bundle>(label, args));
		}
		
		ListView listView = (ListView) getView().findViewById(R.id.interfaceListView);
		listView.setAdapter(adp);
	}
	
	private final OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SimpleListData<Bundle> item = (SimpleListData<Bundle>) parent.getAdapter().getItem(position);
			
			if (item != null) {
				UsbEndpointFragment fragment = new UsbEndpointFragment();
				fragment.setArguments(item.value);
				
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.container, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		}
	};
}
