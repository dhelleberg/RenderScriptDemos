package de.inovex.mobi.rsdemos;

/**
 * This code is based on the work of daoki2 
 * CarouselActivity.java
 * Copyright (c) 2011 daoki2
 * http://code.google.com/p/renderscript-examples/
 * 
 */

import android.app.Activity;
import android.os.Bundle;

public class RSCarouselDemoActivity extends Activity {

	RSCarouselDemoView mCView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCView = new RSCarouselDemoView(this);
		setContentView(mCView);
	}

    /**
     * Missing onDestroy, cleanups, etc. Don't use this in production!
     */


}
