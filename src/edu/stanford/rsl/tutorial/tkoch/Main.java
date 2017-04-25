package edu.stanford.rsl.tutorial.tkoch;

import ij.IJ;

public class Main {

	public static void main(String[] args) 
	{
		IJ ij = new IJ();
		MyPhantom ph = new MyPhantom(512, 512, 1.0, 1.0, 0, 0);
		ph.show();
	}

}
