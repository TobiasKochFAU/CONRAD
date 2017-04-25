package edu.stanford.rsl.tutorial.tkoch;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;

public class MyPhantom extends Grid2D
{

	public MyPhantom(int iWidth, int iHeight, double dSpacingX, double dSpacingY, double dOriginX, double dOriginY) 
	{
		super(iWidth, iHeight);
		this.setSpacing(dSpacingX, dSpacingY);
		this.setOrigin(dOriginX, dOriginY);
		
		this.setRectangle(50, 50, 100, 100, 255);
	}
	
	private void setRectangle(int iX, int iY, int iWidth, int iHeight, int iVal)
	{
		for(int i = iX; i < iWidth; ++i)
		{
			for(int j = iY; j < iHeight; ++j) {
				this.setAtIndex(i, j, 255);
			}
		}
	}
}
