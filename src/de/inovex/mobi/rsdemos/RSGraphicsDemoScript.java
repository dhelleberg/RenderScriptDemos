package de.inovex.mobi.rsdemos;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Mesh;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramFragmentFixedFunction;
import android.renderscript.ProgramStore;
import android.renderscript.RenderScriptGL;

public class RSGraphicsDemoScript {

	private Resources mRes;
	private RenderScriptGL mRS;
	private ScriptC_rsgraphicsdemo mScript;
	private ProgramFragmentFixedFunction mProgramFragment;



	// This provides us with the renderscript context and resources that
	// allow us to create the script that does rendering
	public void init(RenderScriptGL rs, Resources res) {
		mRS = rs;
		mRes = res;

		mScript = new ScriptC_rsgraphicsdemo(mRS, mRes, R.raw.rsgraphicsdemo);

		Bitmap logo = BitmapFactory.decodeResource(	res, R.drawable.inovex_logo);
		float logo_with = logo.getWidth();
		float logo_height = logo.getHeight();
		
		Allocation mLogoAlloc = Allocation.createFromBitmap(rs, logo);
		
		Mesh.TriangleMeshBuilder tmb = new Mesh.TriangleMeshBuilder(rs, 2, Mesh.TriangleMeshBuilder.TEXTURE_0);
		tmb.setTexture(0f, 0f);
		tmb.addVertex(0f, 0f);
		tmb.setTexture(1, 0);
		tmb.addVertex(logo_with, 0f);
		tmb.setTexture(1, 1);
		tmb.addVertex(logo_with, logo_height);
		tmb.setTexture(0, 1);
		tmb.addVertex(0, logo_height);
		tmb.addTriangle(0, 3, 1);
		tmb.addTriangle(1, 3, 2);
		Mesh logoMesh = tmb.create(true);
		mScript.set_gLogoMesh(logoMesh);
		
		ProgramFragmentFixedFunction.Builder textureBuilder = new ProgramFragmentFixedFunction.Builder(rs);
		textureBuilder.setTexture(ProgramFragmentFixedFunction.Builder.EnvMode.DECAL, 
								  ProgramFragmentFixedFunction.Builder.Format.RGBA, 0);
		mProgramFragment = textureBuilder.create();
		mProgramFragment.bindTexture(mLogoAlloc, 0);
		mScript.set_gProgFragmentTexture(mProgramFragment);
		
		//rs.bindProgramStore(ProgramStore.BLEND_ALPHA_DEPTH_NONE(rs));
		
		mRS.bindRootScript(mScript);
		
		
	}

	public void onActionDown(int x, int y) {
		mScript.set_gTouchX(x);
		mScript.set_gTouchY(y);
	}


}
