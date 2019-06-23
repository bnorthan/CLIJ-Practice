
package com.tnia.imagej;

import java.io.IOException;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import ij.IJ;
import ij.ImagePlus;

public class HelloCLIJ {

	public static <T extends RealType<T> & NativeType<T>> void main(
		final String[] args) throws IOException 
	{
		
		ImageJ ij=new ImageJ();
		ij.launch(args);
		
		ImagePlus imp= IJ.openImage("../images/bridge.tif");

		Dataset dataset= (Dataset) ij.io().open("../images/bridge.tif");
		// init
		CLIJ clij = CLIJ.getInstance();

		// send image to GPU memory and reserve memory for output
		ClearCLBuffer input = clij.push(dataset);
		ClearCLBuffer output = clij.create(input);

		// Gaussian blur
		float sigma = 3;
		clij.op().blur(input, output, sigma, sigma);

		// get result back from GPU
		ImagePlus result = clij.pull(output);
		result.show();

		// cleanup memory
		input.close();
		output.close();

	}
}
