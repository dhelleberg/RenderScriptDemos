/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#pragma version(1)
#pragma rs java_package_name(de.inovex.mobi.rsdemos)

#define BAILOUT 16
#define MAX_ITERATIONS 1300

#define SIZE 600
#define HALF_SIZE 300

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

uint32_t counter = 0;
 
 
static int calculateDistance(double x, double y)
{
	int c = 0;
	
	double cr = y - 0.5;
	double ci = x;
	double zi = 0.0;
	double zr = 0.0;
	int i = 0;

	while(1) {
		c++;
		i ++;
		double temp = zr * zi;
		double zr2 = zr * zr;
		double zi2 = zi * zi;
		zr = zr2 - zi2 + cr;
		zi = temp + temp + ci;
		if (zi2 + zr2 > BAILOUT)
		{	
			//rsDebug("return for",x,y,c,i);
			return i;
		}
		if (i > MAX_ITERATIONS)
		{
			//rsDebug("return for",x,y,c,0);
			return 0;
		}
	}
	
}

const static float3 gColorMult = {1.8f, 1.0f, 1.5f};


void root(const void *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {
	counter++;

	int64_t mod = x % 600L;
	
	double mandel_y =  ((int) (-HALF_SIZE + (x / SIZE)) ) / (float)HALF_SIZE;
	double mandel_x = (-HALF_SIZE + mod) / (float)HALF_SIZE;
	int val = calculateDistance(mandel_x, mandel_y);
	float valf = val/255.0;
	float3 val3 = {valf, valf, valf};  
	float3 col = val3 * gColorMult;
	*v_out = rsPackColorTo8888(col);
	
}

void mandelbrot() {
    rsForEach(gScript, gIn, gOut, 0);
    rsDebug("counter",counter);
}



