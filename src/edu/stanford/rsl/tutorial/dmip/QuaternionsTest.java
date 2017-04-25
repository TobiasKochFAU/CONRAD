package edu.stanford.rsl.tutorial.dmip;

import edu.stanford.rsl.conrad.geometry.transforms.Quaternion;
import edu.stanford.rsl.conrad.numerics.SimpleVector;

public class QuaternionsTest {

	public static void main(String[] args) 
	{
		double w1 = 0;
		SimpleVector vec1 = new SimpleVector(1, 2, 3);
		Quaternion r1 = new Quaternion(w1, vec1);

		double w2 = 1;
		SimpleVector vec2 = new SimpleVector(2, 3, 4);
		Quaternion r2 = new Quaternion(w2, vec2);
		
		Quaternion q = r1.multipliedBy(r2);
		System.out.println(q.getScaler() + ", " + q.getVector());
		
		Quaternion r1Inv = r1.getInverse();
		Quaternion r2Inv = r2.getInverse();
		System.out.println(r1Inv.getScaler() + ", " + r1Inv.getVector());
		System.out.println(r2Inv.getScaler() + ", " + r2Inv.getVector());
	}
}
