package edu.stanford.rsl.tutorial.tkoch;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;

/**
 * My phantom class? :)
 * 
 * @author Tobias Koch
 */
public class MyPhantom extends Grid2D
{
	/**
	 * Constructor.
	 * 
	 * @param iWidth
	 * @param iHeight
	 * @param dSpacingX
	 * @param dSpacingY
	 * @param dOriginX
	 * @param dOriginY
	 */
	public MyPhantom(int iWidth, int iHeight, double dSpacingX, double dSpacingY, double dOriginX, double dOriginY) 
	{
		super(iWidth, iHeight);
		this.setSpacing(dSpacingX, dSpacingY);
		this.setOrigin(dOriginX, dOriginY);
		
		this.DrawEllipse(256, 256, 150, 200, 1);
		this.DrawEllipse(180, 200, 30, 20, 2);
		this.DrawEllipse(332, 200, 30, 20, 2);
		this.DrawRectangle(230, 256, 26, 50, 3);
	}
	
	/**
	 * Draws a rectangle.
	 * 
	 * @param iX
	 * @param iY
	 * @param iWidth
	 * @param iHeight
	 * @param iVal
	 */
	private void DrawRectangle(int iX, int iY, int iWidth, int iHeight, float fVal)
	{
		for(int i = iX; i < iWidth; ++i)
		{
			for(int j = iY; j < iHeight; ++j) {
				this.putPixelValue(i, j, fVal);
			}
		}
	}
	
	/**
	 * Draws an ellipse.
	 * Basics taken from:
	 * https://de.wikipedia.org/wiki/Bresenham-Algorithmus
	 * 
	 * @param iX
	 * @param iY
	 * @param iA
	 * @param iB
	 * @param iVal
	 */
	private void DrawEllipse(int iX, int iY, int iA, int iB, float fVal)
	{
		int iDx = 0;
		int iDy = iB;
		long lA2 = iA * iA;
		long lB2 = iB * iB;
		long lErr = lB2 - (2 * iB - 1) * lA2;
		long lE2;
		
		do
		{
			for(int i = iX - iDx; i <= iX + iDx; ++i) {
				this.putPixelValue(i, iY + iDy, fVal);
			}
			for(int i = iX - iDx; i <= iX + iDx; ++i) {
				this.putPixelValue(i, iY - iDy, fVal);
			}
			
			lE2 = 2 * lErr;
			if(lE2 < (2 * iDx + 1) * lB2) 
			{
				++iDx; 
				lErr += (2 * iDx + 1) * lB2;
			}
			
			if(lE2 > -(2 * iDy - 1) * lA2) 
			{ 
				--iDy; 
				lErr -= (2 * iDy - 1) * lA2; 
			}
		} while(iDy >= 0);
		
		while(++iDx < iA)
		{
			for(int i = iX - iDx; i <= iX + iDx; ++i) {
				this.putPixelValue(i, iY, fVal);
			}
		}
	}
}
