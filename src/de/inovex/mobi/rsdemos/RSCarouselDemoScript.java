package de.inovex.mobi.rsdemos;

/**
 * This code is based on the work of daoki2 
 * CarouselRS.java
 * Copyright (c) 2011 daoki2
 * http://code.google.com/p/renderscript-examples/
 */


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Matrix4f;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramFragmentFixedFunction;
import android.renderscript.ProgramRaster;
import android.renderscript.ProgramStore;
import android.renderscript.ProgramStore.BlendDstFunc;
import android.renderscript.ProgramStore.BlendSrcFunc;
import android.renderscript.ProgramVertex;
import android.renderscript.ProgramVertexFixedFunction;
import android.renderscript.RenderScript;
import android.renderscript.RenderScriptGL;
import android.renderscript.Sampler;


public class RSCarouselDemoScript {	

	int mWidth;
	int mHeight;

	public void init(RenderScriptGL rs, Resources res) {
		mRS = rs;
		mWidth = mRS.getWidth();
		mHeight = mRS.getHeight();
		mRes = res;
		mOptionsARGB.inScaled = false;
		mOptionsARGB.inPreferredConfig = Bitmap.Config.ARGB_8888;
		initRS();
	}

	public void surfaceChanged() {
		mWidth = mRS.getWidth();
		mHeight = mRS.getHeight();
		Matrix4f proj = new Matrix4f();
		proj.loadOrthoWindow(mWidth, mHeight);
		mPVA.setProjection(proj);
	}

	private Resources mRes;
	private RenderScriptGL mRS;
	private Sampler mLinearClamp;
	private ProgramStore mProgStoreBlendNone;
	private ProgramFragment mProgFragmentTexture;
	private ProgramVertex mProgVertex;
	private ProgramVertexFixedFunction.Constants mPVA;
	private ProgramRaster mCullNone;
	private ScriptC_carousel mScript;

	private final BitmapFactory.Options mOptionsARGB = new BitmapFactory.Options();

	int px, py;

	public void onActionDown(int x, int y) {
		px = x;
		py = y;
		mScript.set_gPx(x);
		mScript.set_gPy(y);
	}

	public void onActionMove(int x, int y) {
		mScript.set_gVx((float)(px - x)/5);
		mScript.set_gVy((float)(py - y)/5);
		px = x;
		py = y;
	}

	public void onActionUp(int x, int y) {
		//mScript.set_gVx(0);
		//mScript.set_gVy(0);
		//px = 0;
		//py = 0;
	}

	public void onFling(float vx, float vy) {
		mScript.set_gVx(vx);
		mScript.set_gVy(vy);
	}

	ProgramStore BLEND_ADD_DEPTH_NONE(RenderScript rs) {
		ProgramStore.Builder builder = new ProgramStore.Builder(rs);
		builder.setDepthFunc(ProgramStore.DepthFunc.ALWAYS);
		builder.setBlendFunc(BlendSrcFunc.ONE, BlendDstFunc.ONE);
		builder.setDitherEnabled(false);
		builder.setDepthMaskEnabled(false);
		return builder.create();
	}

	private void initProgramStore() {
		// Use stock the stock program store object
		mProgStoreBlendNone = ProgramStore.BLEND_NONE_DEPTH_NONE(mRS);
		mScript.set_gProgStoreBlendNone(mProgStoreBlendNone);
	}

	private void initProgramFragment() {
		ProgramFragmentFixedFunction.Builder texBuilder = new ProgramFragmentFixedFunction.Builder(mRS);
		texBuilder.setTexture(ProgramFragmentFixedFunction.Builder.EnvMode.REPLACE,
				ProgramFragmentFixedFunction.Builder.Format.RGBA, 0);
		mProgFragmentTexture = texBuilder.create();
		mProgFragmentTexture.bindSampler(mLinearClamp, 0);

		mScript.set_gProgFragmentTexture(mProgFragmentTexture);
	}

	private void initProgramVertex() {
		ProgramVertexFixedFunction.Builder pvb = new ProgramVertexFixedFunction.Builder(mRS);
		mProgVertex = pvb.create();

		mPVA = new ProgramVertexFixedFunction.Constants(mRS);
		((ProgramVertexFixedFunction)mProgVertex).bindConstants(mPVA);
		Matrix4f proj = new Matrix4f();
		proj.loadOrthoWindow(mWidth, mHeight);
		mPVA.setProjection(proj);

		mScript.set_gProgVertex(mProgVertex);
	}

	private void loadImages() {

		mScript.set_gTex_00(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c1_ff_wl,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_01(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c2_ff_gh,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_02(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c3_sublime,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_03(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c4_dm_dr,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_04(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c5_pf_dsm,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_05(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c6_rhcp_c,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_06(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c7_rhcp_iwy,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_07(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c8_rhcp_sa,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_08(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c9_patrice_one,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_09(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c10_muse_tr,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_10(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c11_muse_sb,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));
	
		mScript.set_gTex_11(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c12_incubus_sc,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));
		
		mScript.set_gTex_12(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c13_korn_ftl,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));
		
		mScript.set_gTex_13(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c14_pt_pt,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));

		mScript.set_gTex_14(Allocation.createFromBitmapResource(mRS, mRes, R.drawable.c15_ra_eg,
				Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
				Allocation.USAGE_GRAPHICS_TEXTURE));


		ScriptField_Bitmaps bitmap = new ScriptField_Bitmaps(mRS, 15);
		mScript.bind_bitmap(bitmap);
	}

	private void initSamplers() {
		mLinearClamp = Sampler.CLAMP_LINEAR(mRS);
		mScript.set_gLinearClamp(mLinearClamp);
	}

	private void initProgramRaster() {
		mCullNone = ProgramRaster.CULL_NONE(mRS);
		mScript.set_gCullNone(mCullNone);
	}

	private void initRS() {
		mScript = new ScriptC_carousel(mRS, mRes, R.raw.carousel);

		initSamplers();
		initProgramStore();
		initProgramFragment();
		initProgramVertex();
		loadImages();
		initProgramRaster();

		mRS.bindRootScript(mScript);
	}
}



