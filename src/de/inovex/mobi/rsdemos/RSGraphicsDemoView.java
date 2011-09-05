package de.inovex.mobi.rsdemos;

import android.content.Context;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.view.MotionEvent;

public class RSGraphicsDemoView extends RSSurfaceView{

    // Renderscipt context
    private RenderScriptGL mRS;
	private RSGraphicsDemoScript mRender;
	
	public RSGraphicsDemoView(Context context) {
		super(context);
		initRenderScript();
	}

	private void initRenderScript() {
	    if (mRS == null) {
            // Initialize renderscript with desired surface characteristics.
            // In this case, just use the defaults
            RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
            mRS = createRenderScriptGL(sc);
            // Create an instance of the script that does the rendering
            mRender = new RSGraphicsDemoScript();
            mRender.init(mRS, getResources());
        }
	}
	
	
	
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initRenderScript();
    }

    @Override
    protected void onDetachedFromWindow() {
        // Handle the system event and clean up
        //mRender = null;
        if (mRS != null) {
            mRS = null;
            destroyRenderScriptGL();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Pass touch events from the system to the rendering script
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mRender.onActionDown((int)ev.getX(), (int)ev.getY());
            return true;
        }

        return false;
    }


}
