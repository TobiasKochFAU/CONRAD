package edu.stanford.rsl.tutorial.dmip;

import java.awt.Color;

import edu.stanford.rsl.conrad.data.generic.datatypes.Complex;
import edu.stanford.rsl.conrad.numerics.SimpleMatrix;
import edu.stanford.rsl.conrad.numerics.SimpleMatrix.InversionType;
import edu.stanford.rsl.conrad.numerics.SimpleOperators;
import edu.stanford.rsl.conrad.numerics.SimpleVector;
import ij.gui.Plot;


/**
 * Exercise 8-1 of Diagnostic Medical Image Processing (DMIP)
 * @author Bastian Bier, Frank Schebesch
 *
 */
public class exercise81 {
	
	/**
	 * Registration method
	 * Point Correspondences are used to calculate the rotation and translation
	 * Transformation from p to q
	 * @param p reference point cloud
	 * @param q point cloud to be registered
	 * @return translation and rotation for the registration (phi, t1, t2)
	 */
	private SimpleVector registrationUsingPointCorrespondences(SimpleMatrix p, SimpleMatrix q){
		
		int numPoints = p.getRows();
		
		// Build up measurement matrix m
		SimpleMatrix m = new SimpleMatrix(numPoints * 2, 4);
		
		for(int i = 0; i < numPoints * 2; i++)
		{
			if(i < numPoints)
			{
				// TODO
				SimpleVector qVec = q.getRow(i);
				m.setElementValue(i, 0, qVec.getElement(0));
				m.setElementValue(i, 1, -qVec.getElement(1));
				m.setElementValue(i, 2, 1);
				m.setElementValue(i, 3, 0);
			}
			if(i >= numPoints)
			{
				// TODO
				SimpleVector qVec = q.getRow(i%numPoints);
				m.setElementValue(i, 0, qVec.getElement(1));
				m.setElementValue(i, 1, qVec.getElement(0));
				m.setElementValue(i, 2, 0);
				m.setElementValue(i, 3, 1);
			}
		}
		
		// Build up solution vector b
		SimpleVector b = new SimpleVector(2 * numPoints);
		
		for(int i = 0; i < 2 * numPoints; i++)
		{
			if(i < numPoints)
			{
				// TODO
				b.setElementValue(i, p.getRow(i).getElement(0));
			}
			
			if(i >= numPoints)
			{
				// TODO
				b.setElementValue(i, p.getRow(i%numPoints).getElement(1));
			}
		}
		
		// Calculate Pseudo Inverse of m
		SimpleMatrix m_inv;
		m_inv = m.inverse(InversionType.INVERT_SVD);
		
		// Calculate Parameters with the help of the Pseudo Inverse
		SimpleVector x;
		x = SimpleOperators.multiply(m_inv, b);
		
		double r1 = x.getElement(0);
		double r2 = x.getElement(1);
		double t1 = x.getElement(2);
		double t2 = x.getElement(3);
		
		// TODO
		double abs_r = Math.sqrt(Math.pow(r1, 2) + Math.pow(r2, 2));
		
		// TODO: normalize r
		Complex cplx = new Complex(r1, r2);
		cplx.div(abs_r);

		// TODO
		double phi = Math.atan2(cplx.getImag(), cplx.getReal());
		
		// Write the result for the translation and the rotation into the result vector
		SimpleVector result = new SimpleVector(phi, t1, t2);
		
		return result;
	}
	
	
	private SimpleMatrix applyTransform(SimpleMatrix points, double phi, SimpleVector translation){
		
		SimpleMatrix r = new SimpleMatrix(2,2);
		
		// TODO: fill the rotation matrix
		r.setElementValue(0, 0, Math.cos(phi));
		r.setElementValue(0, 1, -Math.sin(phi));
		r.setElementValue(1, 0, Math.sin(phi));
		r.setElementValue(1, 1, Math.cos(phi));
		

		SimpleMatrix transformedPoints = new SimpleMatrix(points.getRows(), points.getCols());
				
		for(int i = 0; i < transformedPoints.getRows(); i++)
		{
			// TODO: transform points
			SimpleVector newRow = SimpleOperators.multiply(r, points.getRow(i));
			newRow.add(translation);
			transformedPoints.setRowValue(i, newRow);
		}
		
		return transformedPoints;
	}
	
	public static void main(String[] args){
		
		// Define Point Cloud 
		SimpleMatrix p_k = new SimpleMatrix(4,2);
		SimpleMatrix q_k = new SimpleMatrix(4,2);
		
		p_k.setRowValue(0, new SimpleVector(1,2));
		p_k.setRowValue(1, new SimpleVector(3,6));
		p_k.setRowValue(2, new SimpleVector(4.5,5.5));
		p_k.setRowValue(3, new SimpleVector(3,3.5));
		
		q_k.setRowValue(0, new SimpleVector(-0.7, 2.1));
		q_k.setRowValue(1, new SimpleVector(-2.1, 6.4));
		q_k.setRowValue(2, new SimpleVector(-1.4, 7.1));
		q_k.setRowValue(3, new SimpleVector(-0.7, 4.9));
		
		// Plot Point Cloud
		Plot plot = new Plot("Regression Line", "X", "Y", Plot.DEFAULT_FLAGS);
		plot.setLimits(-5, 5, 0, 10);
		plot.setSize(512, 512);
		plot.setScale(2.0f);
		plot.setLineWidth(1.5f);
		plot.addLegend("cloud {p_k}\ncloud {q_k}\nregistered {q_k}\nd");
		plot.addPoints(p_k.getCol(0).copyAsDoubleArray(), p_k.getCol(1).copyAsDoubleArray(), Plot.BOX);
		plot.setColor(Color.blue);
		plot.addPoints(q_k.getCol(0).copyAsDoubleArray(), q_k.getCol(1).copyAsDoubleArray(), Plot.CIRCLE);
		
		// Calculate registration parameter
		exercise81 ex81obj = new exercise81();
		SimpleVector parameter = ex81obj.registrationUsingPointCorrespondences(p_k, q_k);
		
		// Rotation and translation
		double phi = parameter.getElement(0);
		SimpleVector translation = new SimpleVector(parameter.getElement(1), parameter.getElement(2));
		
		// Transform points
		SimpleMatrix transformedPoints = ex81obj.applyTransform(q_k, phi, translation);
		
		// Add transformed point cloud into the plot
		plot.setColor(Color.red);
		plot.addPoints(transformedPoints.getCol(0).copyAsDoubleArray(), transformedPoints.getCol(1).copyAsDoubleArray(), Plot.CROSS);
		plot.show();
		
	}
	
	
}
