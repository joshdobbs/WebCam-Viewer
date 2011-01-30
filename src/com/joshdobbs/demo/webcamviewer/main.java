package com.joshdobbs.demo.webcamviewer;
// a simple WebCam Viewer 
// Josh Dobbs joshdobbs@gmail.com

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class main extends Activity {
	private ImageView webCamViewer;
	private Button refreshButton;
	private static String IMAGE_URL = "http://itwebcammh.fullerton.edu/axis-cgi/jpg/image.cgi?resolution=480x320";
	private static Bitmap BROKEN_IMAGE;// needs to be static so we can access
										// from inner class
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//initialize error image to be displayed 
		BROKEN_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.dlerror);
		
		initializeControls();
		
		getNewImage();
	}

	private void initializeControls(){
		//reference the views in the main layout
		refreshButton = (Button) findViewById(R.id.refreshButton);
		webCamViewer = (ImageView) findViewById(R.id.webCamImage);

		webCamViewer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getNewImage();
			}
		});

		refreshButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getNewImage();
			}
		});

	}
	
	private void getNewImage(){
		//get the first image
		new DownloadImagesTask().execute(webCamViewer);
	}
	
	public static class DownloadImagesTask extends
			AsyncTask<ImageView, Void, Bitmap> {

		ImageView imageView = null;

		@Override
		protected Bitmap doInBackground(ImageView... imageViews) {
			this.imageView = imageViews[0];
			Bitmap img = null;

			img = download_Image(IMAGE_URL);

			//loading bitmaps from the net can be a bit buggy sometimes.
			//so we try to download it again..
			if (img == null) {
				img = download_Image(IMAGE_URL);

			}
			
			
			//if the image still isnt loaded we set img to our broken image 
			if (img == null) {
				img = BROKEN_IMAGE;
			}

			return img;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			try {
				imageView.setImageBitmap(result);
			} catch (Exception ex) {
				imageView.setImageBitmap(result);
			}
		}

		private Bitmap download_Image(String url) {
			try {
				//declare a new url
				URL imageURL = new URL(url);
				
				//declare a connection
				URLConnection conn;
				
				//open the connection
				conn = imageURL.openConnection();
				
				//connect
				conn.connect();
				
				//declare and instantiate the input stream
				final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

				final Bitmap bitmap = BitmapFactory.decodeStream(bis);

				return bitmap;

			} catch (Exception ex) {
				Log.e("download_Image", ex.getMessage());
			}
			return null;
		}


	}

}
