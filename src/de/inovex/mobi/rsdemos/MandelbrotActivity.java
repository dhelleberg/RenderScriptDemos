package de.inovex.mobi.rsdemos;

import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Calculates a Mandelbrot algorithm based bitmap using renderscript, java single threaded and java multithreaded
 * For demo purposes only
 * @author dhelleberg
 *
 */

public class MandelbrotActivity extends Activity {
	private RenderScript mRS;
	private ScriptC_mandelbrot mScript;
	private Context mContext = this;

	static int BAILOUT = 16;
	static int MAX_ITERATIONS = 1300;

	static final int SIZE_QUAD = 600;
	static final int SIZE_QUAD_HALF = SIZE_QUAD/2;
	
	//don't do this in production
	static int[] out = new int[SIZE_QUAD*SIZE_QUAD];


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mandelbrot_activity);
		Button brs = (Button) findViewById(R.id.buttonRS);
		brs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RSMandelBrotTask().execute(SIZE_QUAD*SIZE_QUAD);
			}
		});

		Button bja = (Button) findViewById(R.id.buttonJava);
		bja.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new JavaMandelBrotTask().execute(SIZE_QUAD * SIZE_QUAD);
			}
		});


		Button bj2 = (Button) findViewById(R.id.buttonJavaThreads);
		bj2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new JavaThreadedMandelBrotTask().execute(SIZE_QUAD*SIZE_QUAD);
			}
		});
	}



	private int iterate(double x, double y)
	{
		double cr = y-0.5f;
		double ci = x;
		double zi = 0.0f;
		double zr = 0.0f;
		int i = 0;
		while (true) {
			i++;
			double temp = zr * zi;
			double zr2 = zr * zr;
			double zi2 = zi * zi;
			zr = zr2 - zi2 + cr;
			zi = temp + temp + ci;
			if (zi2 + zr2 > BAILOUT)
			{
				return i;
			}
			if (i > MAX_ITERATIONS)
			{
				return 0;
			}
		}
	}

	private class JavaMandelBrotTask extends AsyncTask<Integer, Void, Bitmap>
	{
		long duration = 0;

		@Override
		protected Bitmap doInBackground(Integer... params) {
			final int count = params[0];
			final Bitmap b = Bitmap.createBitmap(SIZE_QUAD, SIZE_QUAD, Bitmap.Config.ARGB_8888);			

			long start = System.currentTimeMillis();
			for(int c = 0; c < count; c++)
			{
				double myy = ((int)(-SIZE_QUAD_HALF+(c / SIZE_QUAD))) / (double)SIZE_QUAD_HALF;
				double myx = (-SIZE_QUAD_HALF+(c % SIZE_QUAD)) / (double)SIZE_QUAD_HALF;
				int val = iterate(myx , myy);
				int by = (c / SIZE_QUAD);
				int bx = (c % SIZE_QUAD);
				b.setPixel(bx, by, Color.rgb((int)(val*1.8),(int) (val*1.0), (int)(val*1.5)));							

			}
			long end = System.currentTimeMillis();
			duration = end-start;
			Log.v("!!","Java Elapsed " + duration);

			return b;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			ImageView iv = (ImageView) findViewById(R.id.imageView1);
			iv.setImageBitmap(result);

			TextView tv = (TextView) findViewById(R.id.tv_java);
			tv.setText(duration+" ms");
		}

	}

	private class JavaThreadedMandelBrotTask extends AsyncTask<Integer, Void, Bitmap>
	{
		long time = 0;

		@Override
		protected Bitmap doInBackground(Integer... params) {
			final int count = params[0];
			Thread t1;
			Thread t2;

			final CountDownLatch doneSignal = new CountDownLatch(2);

			
			final int[]res = new int[params[0]];
			final Bitmap b = Bitmap.createBitmap(SIZE_QUAD, SIZE_QUAD, Bitmap.Config.ARGB_8888);

			t1 = new Thread(new Runnable() {

				@Override
				public void run() {
					for(int c = 0; c < (count/2)+25000; c++)
					{
						double myy = ((int)(-SIZE_QUAD_HALF+(c / SIZE_QUAD))) / (double)SIZE_QUAD_HALF;
						double myx = (-SIZE_QUAD_HALF+(c % SIZE_QUAD)) / (double)SIZE_QUAD_HALF;
						int val = iterate(myx , myy);
						res[c] = val;
					}
					Log.v("T1", "done");
					doneSignal.countDown();
				}
			});

			t2 = new Thread(new Runnable() {

				@Override
				public void run() {
					for(int c = (count/2)+25000; c < count; c++)
					{
						double myy = ((int)(-SIZE_QUAD_HALF+(c / SIZE_QUAD))) / (double)SIZE_QUAD_HALF;
						double myx = (-SIZE_QUAD_HALF+(c % SIZE_QUAD)) / (double)SIZE_QUAD_HALF;
						int val = iterate(myx , myy);
						res[c] = val;
					}
					Log.v("T2", "done");
					doneSignal.countDown();
				}
			});
			
			long start = System.currentTimeMillis();
			
			t1.start();
			t2.start();
			try {
				doneSignal.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < res.length; i++) {
				int val = res[i];
				int by = (i / SIZE_QUAD);
				int bx = (i % SIZE_QUAD);
				b.setPixel(bx, by, Color.rgb((int)(val*1.8),(int) (val*1.0), (int)(val*1.5)));							
			}
			long end = System.currentTimeMillis();

			long diff = end-start;

			Log.v("!!","Java Elapsed " + diff);
			time = diff;
			return b;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			ImageView iv = (ImageView) findViewById(R.id.imageView1);
			iv.setImageBitmap(result);

			TextView tv = (TextView) findViewById(R.id.tv_javathreads);
			tv.setText(time+" ms");
		}
	}


	private class RSMandelBrotTask extends AsyncTask<Integer, Void, Bitmap>
	{

		long duration = 0;
		@Override
		protected Bitmap doInBackground(Integer... params) {

			mRS = RenderScript.create(mContext);
			mScript = new ScriptC_mandelbrot(mRS, getResources(), R.raw.mandelbrot);

			final int size = params[0];

			Allocation alloc_in = Allocation.createSized(mRS, Element.BOOLEAN(mRS), size);

			Bitmap b = Bitmap.createBitmap(SIZE_QUAD,SIZE_QUAD,Bitmap.Config.ARGB_8888);
			Allocation alloc_out = Allocation.createFromBitmap(mRS, b);

			mScript.set_gIn(alloc_in);
			mScript.set_gOut(alloc_out);
			mScript.set_gScript(mScript);

			long start = System.currentTimeMillis();
			mScript.invoke_mandelbrot();

			alloc_out.copyTo(b);

			long end = System.currentTimeMillis();
			duration = end-start;
			Log.v("Jupp:", duration+" ms");
			return b;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			ImageView iv = (ImageView) findViewById(R.id.imageView1);
			iv.setImageBitmap(result);

			TextView tv = (TextView) findViewById(R.id.tv_rs);
			tv.setText(duration+" ms");

		}

	}
}