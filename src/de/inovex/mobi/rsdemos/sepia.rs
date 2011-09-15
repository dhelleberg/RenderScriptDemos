
#pragma version(1)
#pragma rs java_package_name(de.inovex.mobi.rsdemos)

// set from the java SDK level
rs_allocation gIn;
rs_allocation gOut;

rs_script gScript;

//magic sepia parameters 
const float gSepiaDepth = 20/255.0f;
const float gSepiaIntensity = 50/255.0f;

//magic grayscale factor
const static float3 gMonoMult = {0.299f, 0.587f, 0.114f};


void sepiaFilter() {
    rsForEach(gScript, gIn, gOut, 0);		// for each element of the input allocation,
    										// call root() method on gScript
}


void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {

    float4 f4 = rsUnpackColor8888(*v_in);	// extract RGBA values, see rs_core.rsh

    float3 mono = dot(f4.rgb, gMonoMult);	// dot product of the vector, see rs_cl.rsh
    
    mono.x = mono.x + (gSepiaDepth*2);		// sepia calculations
    mono.y = mono.y + gSepiaDepth;
    mono.z = mono.z - gSepiaIntensity;
    
    if(mono.x > 1.0) mono.x = 1.0f;			// clipping check
    if(mono.y > 1.0) mono.y = 1.0f;
    if(mono.z > 1.0) mono.z = 1.0f;
    if(mono.z < 0.0) mono.z = 0.0f;
    
    *v_out = rsPackColorTo8888(mono);		// pack color back to uchar4
	
}




