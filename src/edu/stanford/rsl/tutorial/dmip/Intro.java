package edu.stanford.rsl.tutorial.dmip;

import java.awt.Image;
import java.util.Vector;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;
import edu.stanford.rsl.conrad.data.numeric.NumericGrid;
import edu.stanford.rsl.conrad.numerics.DecompositionSVD;
import edu.stanford.rsl.conrad.numerics.SimpleMatrix;
import edu.stanford.rsl.conrad.numerics.SimpleMatrix.MatrixNormType;
import edu.stanford.rsl.conrad.numerics.SimpleVector.VectorNormType;
import edu.stanford.rsl.conrad.utils.ImageUtil;
import edu.stanford.rsl.conrad.utils.VisualizationUtil;
import edu.stanford.rsl.conrad.numerics.SimpleOperators;
import edu.stanford.rsl.conrad.numerics.SimpleVector;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.Convolver;
import ij.process.FloatProcessor;


/**
 * Introduction to the CONRAD Framework
 * Exercise 0 of Diagnostic Medical Image Processing (DMIP)
 * @author Frank Schebesch, Marco Boegel
 *
 */

public class Intro {
	
	
	public static void gridIntro(){
				
		//Define the image size
		int imageSizeX = 256;
		int imageSizeY = 384;
	
		//Define an image
		//Hint: Import the package edu.stanford.rsl.conrad.data.numeric.Grid2D
		Grid2D grid = new Grid2D(imageSizeX, imageSizeY);
	
		//Draw a circle
		int radius = 55;
		//Set all pixels within the circle to 100
		int insideVal = 100;
	
		for(int x = 0; x < imageSizeX; x++)
		{
			for(int y = 0; y < imageSizeY; y++)
			{
				if(Math.pow(x-(imageSizeX/2.f), 2) + Math.pow(y-(imageSizeY/2.f), 2) 
					< Math.pow(radius, 2)) {
					grid.setAtIndex(x, y, insideVal);
				}
				else {
					grid.setAtIndex(x, y, 0);
				}
			}
		}
		
		//Show ImageJ GUI
		ImageJ ij = new ImageJ();
		//Display image
		grid.show("Circle with radius: " + radius);
		
		//Copy an image into a new container
		NumericGrid copy = grid.clone();
		copy.show("Copy of circle");
		
		//Load an image from file
		String filename = "D:/Dropbox/Tobias/Eclipse Workspace/CONRAD/src/edu/stanford/rsl/tutorial/dmip/mr12.dcm";
		Grid2D mrImage = ImageUtil.wrapImagePlus(IJ.openImage(filename)).getSubGrid(0);
		mrImage.show();
		
		//prepare convolution by creating the relevant objects
		Convolver conv = new Convolver();
		FloatProcessor ip = ImageUtil.wrapGrid2D(mrImage);
		
		//define the kernel. Try simple averaging 3x3 filter
		int kw = 3;
		int kh = 3;
		float[] kernel = new float[kw*kh];
		for(int i = 0; i < kernel.length; i++)
		{	
			kernel[i] = 1.f / (kw*kh);
		}
		
		// test for error and directly show convolved image
		if(!conv.convolve(ip, kernel, kw, kh))
		{
			System.err.println("Convolution failed");
			return;
		}
		else {
			ImageUtil.wrapFloatProcessor(ip).show("Convolved image");
		}
			
		
		//write an image to disk, check the supported output formats
		String outFilename ="D:/Dropbox/Tobias/Eclipse Workspace/CONRAD/src/edu/stanford/rsl/tutorial/dmip/mr12out.tif";
		IJ.save(ImageUtil.wrapGrid(mrImage, null), outFilename);
	}
	
	
	public static void signalIntro()
	{
		//How can I plot a sine function sin(x) 
		//which has its zeroes at multiples of 3?
		double stepSize = 0.02;
		int plotLength = 800;
		
		double[] y = new double[plotLength];
		
		for(int i = 0; i < y.length; i++)
		{
			double val = Math.sin(Math.PI * stepSize * i/3.0);
			y[i] = val;
			
		}
		
		VisualizationUtil.createPlot(y).show();
		
		// now plot it with the specified x values
		double[] x = new double [plotLength];
		for(int i = 0; i < x.length; i++) {
			x[i] = (double) i * stepSize;
		}
		
		VisualizationUtil.createPlot(x, y, "sin(x)", "x", "y").show();		
		
	}
	
	public static void basicIntro()
	{
		//Display text
		System.out.println("Creating a vector: v1 = [3.0; 2.0; 1.0]");
		
		//create column vector
		SimpleVector v1 = new SimpleVector(3.f, 2.f, 1.f);
		System.out.println("v1 = " + v1.toString());
		
		//create a randomly initialized vector
		SimpleVector vRand = new SimpleVector(3);
		vRand.randomize(0.f, 1.f);
		System.out.println("vRand = " + vRand.toString());
		
		//create matrix M 3x3  1 2 3; 4 5 6; 7 8 9
		double[][] m = new double[3][3];
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++) {
				m[i][j] = 1 + i * 3 + j;
			}
		}
		SimpleMatrix M = new SimpleMatrix(m);
		
		// Alternativ:
		/*
		SimpleMatrix M = new SimpleMatrix(3,3);
		M.setColValue(0, new SimpleVector(1.f, 4.f, 7.f));
		M.setColValue(1, new SimpleVector(2.f, 5.f, 8.f));
		M.setColValue(2, new SimpleVector(3.f, 6.f, 9.f));
		*/
		
		System.out.println("M = " + M.toString());
		
		//determinant of M
		System.out.println("Determinant of matrix m: " + M.determinant() );
		
		//transpose M
		SimpleMatrix Mtrans = M.transposed();
		//copy matrix
		SimpleMatrix Mcopy = new SimpleMatrix(M);
		//transpose M inplace
		Mcopy.transpose();
		
		//get size
		int numRows = M.getRows();
		int numCols = M.getCols();
		
		//access elements of M
		System.out.println("M: ");
		for(int i = 0 ; i < numRows; i++)
		{
			for(int j = 0; j < numCols; j++) {
				System.out.print(M.getElement(i, j) + " ");
			}
			System.out.println();
		}
		
		//Create 3x3 Matrix of 1's
		SimpleMatrix Mones = new SimpleMatrix(3,3);
		Mones.ones();
		//Create a 3x3 Matrix of 0's
		SimpleMatrix Mzeros = new SimpleMatrix(3,3);
		Mzeros.zeros();
		//Create a 3x3 Identity matrix
		SimpleMatrix Midentity = new SimpleMatrix(3,3);
		Midentity.identity();
		
		//Matrix multiplication
		SimpleMatrix ResMat = SimpleOperators.multiplyMatrixProd(Mtrans, M);
		System.out.println("M^T * M = " + ResMat.toString());
		

		//Matrix vector multiplication
		SimpleVector resVec = SimpleOperators.multiply(M, v1);
		System.out.println("M * v1 = " + resVec.toString());
		
		
		//Extract the last column vector from matrix M
		SimpleVector colVector = M.getCol(2);
		//Extract the 1x2 subvector from the last column of matrix M
		SimpleVector subVector = M.getSubCol(0, 2, 2);
		System.out.println("[m(0)(2); m(1)(2)] = " + subVector);
		
		//Matrix elementwise multiplication
		SimpleMatrix MsquaredElem = SimpleOperators.multiplyElementWise(M, M);
		System.out.println("M squared Elements: " + MsquaredElem.toString());
		
		//round vectors
		SimpleVector vRandCopy = new SimpleVector(vRand);
		System.out.println("vRand         = " + vRandCopy.toString());
		
		vRandCopy.floor();
		System.out.println("vRand.floor() = " + vRandCopy.toString());
		
		vRand.ceil();
		System.out.println("vRand.ceil()  = " + vRand.toString());
		
		//min, max, mean
		double minV1 = v1.min();
		double maxV1 = v1.max();
		System.out.println("Min(v1) = " + minV1 + " Max(v1) = " + maxV1);
		
		//for matrices: iterate over row or column vectors
		SimpleVector maxVec = new SimpleVector(M.getCols());
		for(int i = 0; i < M.getCols(); i++) {
			maxVec.setElementValue(i, M.getCol(i).max());
		}
		double maxM = maxVec.max();
		System.out.println("Max(M) = " + maxM);
		
		
		
		//Norms
		double matrixNormL1 = M.norm(MatrixNormType.MAT_NORM_FROBENIUS);
		System.out.println("||M||_F = " + matrixNormL1);
		double vecNormL2 = colVector.norm(VectorNormType.VEC_NORM_L2);
		System.out.println("||colVec||_2 = " + vecNormL2);
		
		//get normalized vector
		SimpleVector vecL2 = colVector.normalizedL2();
		System.out.println("colVector: " + colVector.toString());
		
		//normalize vector in-place
		colVector.normalizeL2();	
		System.out.println("Normalized colVector: " + colVector.toString());
		System.out.println("||colVec||_2 = " + colVector.norm(VectorNormType.VEC_NORM_L2));
	}

	public static void main(String arg[])
	{
		basicIntro();
		gridIntro();
		signalIntro();
	}
}
