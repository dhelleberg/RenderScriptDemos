package de.inovex.mobi.rsdemos;


import android.content.Context;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * this code is based on the work of daoki2 
 * CarouselView.java
 * Copyright (c) 2011 daoki2
 * http://code.google.com/p/renderscript-examples/
 *
 */

public class RSCarouselDemoView extends RSSurfaceView implements GestureDetector.OnGestureListener {
	   private RenderScriptGL mRS;
	    private RSCarouselDemoScript mRender;

	    
	    public RSCarouselDemoView(Context context) {
	        super(context);
	        ensureRenderScript();
	    }
	    
	    
	    private void ensureRenderScript() {
	        if (mRS == null) {
	            RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
	            sc.setDepth(16, 24);
	            mRS = createRenderScriptGL(sc);
	            mRender = new RSCarouselDemoScript();
	            mRender.init(mRS, getResources());
	        }
	    }

	    @Override
	    protected void onAttachedToWindow() {
	        super.onAttachedToWindow();
	        ensureRenderScript();
	    }

	    @Override
	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        super.surfaceChanged(holder, format, w, h);
	        mRender.surfaceChanged();
	    }

	    @Override
	    protected void onDetachedFromWindow() {
	        mRender = null;
	        if (mRS != null) {
	            mRS = null;
	            destroyRenderScriptGL();
	        }
	    }

	    @Override
	    public boolean onTouchEvent(MotionEvent ev) {
	        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
	            mRender.onActionDown((int)ev.getX(), (int)ev.getY());
	            return true;
	        }

	        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
	            mRender.onActionMove((int)ev.getX(), (int)ev.getY());
	        	return true;
	        }
	        
	        if (ev.getAction() == MotionEvent.ACTION_UP) {
	            mRender.onActionUp((int)ev.getX(), (int)ev.getY());
	        	return true;
	        }
	        
	        return false;
	    }
	    
	    @Override
	    public boolean onDown(MotionEvent e) {
	        //addInfo("Down");
	        return false;
	    } 
	    
	    public boolean onFling(MotionEvent e0,MotionEvent e1,
	    		float velocityX,float velocityY) {
	    	//addInfo("Fling("+velocityX+","+velocityY+")");
	    	mRender.onFling(velocityX, velocityY);
	    	return false;
	    }

	    public void onLongPress(MotionEvent e) {
	        //addInfo("LongPress");
	    }

	    public boolean onScroll(MotionEvent e0,MotionEvent e1,
	    		float distanceX,float distanceY) {
	    	//addInfo("Scroll("+distanceX+","+distanceY+")");
	    	return false;
	    }

	    public void onShowPress(MotionEvent e) {
	    	//addInfo("ShowPress");
	    }

	    public boolean onSingleTapUp(MotionEvent e) {
	    	//addInfo("SigngleTapUp");
	    	return false;
	    }

}
