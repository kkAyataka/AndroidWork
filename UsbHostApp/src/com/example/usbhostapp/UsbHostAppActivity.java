package com.example.usbhostapp;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Build;

public class UsbHostAppActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_host_app);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.usb_host_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	
        	mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        	mPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        	IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        	getActivity().registerReceiver(mUsbReceiver, filter);
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_usb_host_app, container, false);
            ListView list = (ListView) rootView.findViewById(R.id.listView);
            list.setOnItemClickListener(onItemClickListenr);
            return rootView;
        }
        
        @Override
        public void onStart() {
        	super.onStart();
        	
        	UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        	
        	HashMap<String, UsbDevice> devices = manager.getDeviceList();
        	
        	Iterator<UsbDevice> ite = devices.values().iterator();
        	//ArrayAdapter<String> listAdp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        	ArrayAdapter<UsbListData> listAdp = new ArrayAdapter<UsbListData>(getActivity(), android.R.layout.simple_list_item_1);
        	while (ite.hasNext()) {
        		UsbDevice device = ite.next();
        		
        		final int vid = device.getVendorId();
        		final int pid = device.getProductId();
        		final String name = device.getDeviceName();
        		String str = String.format("VID: %04X, PID: %04X (%s)", vid, pid, name);
        		
        		listAdp.add(new UsbListData(str, device));
        	}
        	
        	ListView list = (ListView) getView().findViewById(R.id.listView);
        	list.setAdapter(listAdp);
        }
        
        private final OnItemClickListener onItemClickListenr = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UsbListData item = (UsbListData) parent.getAdapter().getItem(position);
				
				mUsbManager.requestPermission(item.device, mPermissionIntent);
			}
        };
        
        private static final String ACTION_USB_PERMISSION = "com.example.USB_PERMISSION";
        private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        	
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action == ACTION_USB_PERMISSION) {
					UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {
							Log.i("UsbHostApp", "permitted " + device);
						}
					}
					else {
						Log.d("UsbHostApp", "permission denied for device " + device);
					}
				}
			}
        };
        
        class UsbListData {
        	UsbListData(String label, UsbDevice device) {
        		this.label = label;
        		this.device = device;
        	}
        	
        	public String label;
        	public UsbDevice device;
        	
        	@Override
        	public String toString() {
        		return label;
        	}
        };
        
        private UsbManager mUsbManager;
        private PendingIntent mPermissionIntent;
    }
}
