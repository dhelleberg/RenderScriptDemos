package org.cirrus.mobi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooserActivity extends Activity {
	
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooser_activity);
		
		Button brs = (Button) findViewById(R.id.chooser_button1);
		brs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MandelbrotActivity.class));
			}
		});

		Button br = (Button) findViewById(R.id.chooser_button2);
		br.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SepiaActivity.class));
			}
		});

	}

}
