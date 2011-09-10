package de.inovex.mobi.rsdemos;

import android.content.Context;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.view.MotionEvent;

public class RSGraphicsDemoView extends RSSurfaceView{

    // Renderscipt context
    private RenderScriptGL mRSGL;
	private RSGraphicsDemoScript mRenderScript;
	
	public RSGraphicsDemoView(Context context) {
		super(context);
		initRenderScript();
	}	
	
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initRenderScript();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Pass touch events from the system to the rendering script
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mRenderScript.onActionDown((int)ev.getX(), (int)ev.getY());
            return true;
        }
        return false;
    }

	private void initRenderScript() {
	    if (mRSGL == null) {
            // Initialize renderscript with desired surface characteristics.
            // In this case, just use the defaults
            RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
            mRSGL = createRenderScriptGL(sc);
            // Create an instance of the script that does the rendering
            mRenderScript = new RSGraphicsDemoScript();
            mRenderScript.init(mRSGL, getResources());
        }
	}

    @Override
    protected void onDetachedFromWindow() {
        // Handle the system event and clean up
        //mRender = null;
        if (mRSGL != null) {
            mRSGL = null;
            destroyRenderScriptGL();
        }
    }

}
