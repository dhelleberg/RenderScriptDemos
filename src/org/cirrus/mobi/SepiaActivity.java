package org.cirrus.mobi;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SepiaActivity extends Activity {

	Context mContext = this;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sepia_activity);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		final Bitmap input = BitmapFactory.decodeResource(getResources(), R.drawable.sunset, options);

		Button brs = (Button) findViewById(R.id.sepia_buttonRS);
		brs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RSSepiaTask().execute(input);
			}
		});

		Button bja = (Button) findViewById(R.id.sepia_buttonJava);
		bja.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new JavaSepiaTask().execute(input);

			}
		});


		Button bj2 = (Button) findViewById(R.id.sepia_buttonJavaThreads);
		bj2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//new JavaThreadedSepiaTask().execute(input);

			}

		});

	}

	private class RSSepiaTask extends AsyncTask<Bitmap, Void, Bitmap>
	{

		private RenderScript mRS;
		long duration;

		@Override
		protected Bitmap doInBackground(Bitmap... params) {
			
			long start = System.currentTimeMillis();
			
			mRS = RenderScript.create(mContext);

			final Bitmap mBitmapIn = params[0];

			Bitmap mBitmapOut = Bitmap.createBitmap(mBitmapIn.getWidth(), mBitmapIn.getHeight(),
					mBitmapIn.getConfig());

			Allocation mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn,
					Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			Allocation mOutAllocation = Allocation.createTyped(mRS, mInAllocation.getType());

			ScriptC_sepia mScript = new ScriptC_sepia(mRS, getResources(), R.raw.sepia);

			mScript.set_gIn(mInAllocation);
			mScript.set_gOut(mOutAllocation);
			mScript.set_gScript(mScript);
			mScript.invoke_sepiaFilter();
			mOutAllocation.copyTo(mBitmapOut);
			duration = System.currentTimeMillis()-start;

			return mBitmapOut;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			ImageView iv = (ImageView) findViewById(R.id.sepia_imageView1);
			iv.setImageBitmap(result);
			
			TextView tv = (TextView) findViewById(R.id.tv_rs);
			tv.setText(duration+" ms");

		}		
	}

	

	private class JavaSepiaTask extends AsyncTask<Bitmap, Void, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(Bitmap... params) {

			final float gSepiaDepth = 20/255.0f;
			final float gSepiaIntensity = 50/255.0f;


			final Bitmap bitmap_in = params[0];
			int count = bitmap_in.getWidth() * bitmap_in.getHeight();
			int[]pixels = new int[count];
			bitmap_in.getPixels(pixels, 0, bitmap_in.getWidth(), 0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());

			for (int i = 0; i < pixels.length; i++) {
				int pixel = pixels[i];
				float r = ((pixel >> 16) & 0xff) / 255.0f;
				float g = ((pixel >> 8) & 0xff) / 255.0f;
				float b = ((pixel >> 0) & 0xff) / 255.0f;
				float gray = ( r * 0.3f + g * 0.6f + b * 0.1f);

				r = gray + (gSepiaDepth*2);
				g = gray + gSepiaDepth;
				b = gray - gSepiaIntensity;

				if(r > 1.0) r = 1.0f;
				if(g > 1.0) g = 1.0f;
				if(b > 1.0) b = 1.0f;
				if(b < 0.0) b = 0.0f;


				pixels[i] = 0xff000000 | ( (int)(r*255.0f) << 16) | ((int)(g*255.0f)  << 8) | ((int)(b*255.0f) << 0);
			}
			bitmap_in.setPixels(pixels, 0, bitmap_in.getWidth(), 0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());

			return bitmap_in;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			ImageView iv = (ImageView) findViewById(R.id.sepia_imageView1);
			iv.setImageBitmap(result);
			/*
			TextView tv = (TextView) findViewById(R.id.tv_rs);
			tv.setText(duration+" ms");*/

		}		


	}
}
