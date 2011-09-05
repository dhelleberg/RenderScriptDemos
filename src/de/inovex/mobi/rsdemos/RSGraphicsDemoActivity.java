package de.inovex.mobi.rsdemos;

import android.app.Activity;
import android.os.Bundle;

public class RSGraphicsDemoActivity extends Activity {
    private RSGraphicsDemoView mView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mView = new RSGraphicsDemoView(this);
        setContentView(mView);
    }
    
    
    
}