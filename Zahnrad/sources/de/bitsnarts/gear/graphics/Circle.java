package de.bitsnarts.gear.graphics;

import java.awt.geom.Path2D;

public class Circle {

	private double centerX;
	private double centerY;
	private double radius;
	private int numSegments;
	private double dPhi;

	/**
	 *  
	 * @param A negative radius means to pain the circle in clockwise orientation ;
	 */
	
	public Circle ( double centerX, double centerY, double radius, int numSegments ) {
		this.centerX = centerX ;
		this.centerY = centerY ;
		this.radius = radius ;
		this.numSegments = numSegments ;
		this.dPhi = Math.PI*2.0/numSegments ; 
		if ( radius < 0.0 ) {
			this.radius = -radius ;
			dPhi = -dPhi ;
		}
	}
	
	public void addTo ( Path2D.Double path ) {
		path.moveTo( radius+centerX, centerY );
		for ( int i = 0 ; i < numSegments ; i++ ) {
			double phi = dPhi*i ;
			double x0 = radius*Math.cos( phi )+centerX ;
			double y0 = radius*Math.sin( phi )+centerY ;
			double x_prime = -dPhi*radius*Math.sin( phi ) ;
			double y_prime =  dPhi*radius*Math.cos( phi ) ;
			double x1 = x0 + x_prime/3.0 ; 
			double y1 = y0 + y_prime/3.0 ;
			phi+= dPhi ;
			double x3 = radius*Math.cos( phi )+centerX ;
			double y3 = radius*Math.sin( phi )+centerY ;
			x_prime = -dPhi*radius*Math.sin( phi ) ;
			y_prime =  dPhi*radius*Math.cos( phi ) ;
			double x2 = x3 - x_prime/3.0 ; 
			double y2 = y3 - y_prime/3.0 ;
			path.curveTo ( x1, y1, x2, y2, x3, y3 ) ;
		}
		path.closePath();
	}
}
