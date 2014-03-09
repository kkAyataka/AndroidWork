package com.example.cameraapp;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public class CameraView extends SurfaceView implements Callback {

	private Camera mCamera;
	
	public CameraView(Context context) {
		super(context);
		
		setOnClickListener(onClickListener);
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(holder);
		}
		catch (IOException e) {
			Log.e(VIEW_LOG_TAG, e.getMessage());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters p = mCamera.getParameters();
		List<Size> sizes = p.getSupportedPreviewSizes();
		p.setPreviewSize(sizes.get(0).width, sizes.get(0).height);
	    
		mCamera.setParameters(p);
		mCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mCamera != null) {
				mCamera.autoFocus(autoFocusCallback);
			}
		}
	};
	
	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				camera.setOneShotPreviewCallback(previewCallback);
			}
		}
	};
	
	private PreviewCallback previewCallback = new PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			// TODO
		}
	};
}
