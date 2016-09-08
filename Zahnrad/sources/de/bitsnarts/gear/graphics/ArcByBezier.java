package de.bitsnarts.gear.graphics;

import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.Curve;

public class ArcByBezier {

	private double[][] endPoints;
	private double[][] controlPoints;
	private double[][] endPointsRev;
	private double[][] controlPointsRev;

	public ArcByBezier ( Curve curve, double t1, double t2, int numSegments ) {
		endPoints = new double[numSegments+1][2] ;
		controlPoints = new double[2*numSegments][2] ;
		double[][] tangents = new double[numSegments+1][2] ;
		double dt = (t2-t1)/numSegments ;
		for ( int i = 0 ; i <= numSegments ; i++ ) {
			double t = t1+dt*i ;
			curve.eval(t, endPoints[i]);
			curve.grad(t, tangents[i] );
			tangents[i][0] *= dt ;
			tangents[i][1] *= dt ;
		}
		for ( int i = 0 ; i < numSegments ; i++ ) {
			double[] v1 = endPoints[i] ;
			double[] v2 = endPoints[i+1] ;
			double[] cp = controlPoints [2*i] ;
			double[] tan1 = tangents[i] ;
			double[] tan2 = tangents[i+1] ;
			cp[0] = v1[0]+tan1[0]/3.0 ;
			cp[1] = v1[1]+tan1[1]/3.0 ;
			cp = controlPoints [2*i+1] ;
			cp[0] = v2[0]-tan2[0]/3.0 ;
			cp[1] = v2[1]-tan2[1]/3.0 ;
		}		
		mkRevertedArrays () ;
	}
	
	private void mkRevertedArrays() {
		endPointsRev = mkRevertedArray ( endPoints ) ;
		controlPointsRev = mkRevertedArray ( controlPoints ) ;
	}

	private double[][] mkRevertedArray(double[][] pts ) {
		int n = pts.length ;
		double[][] rv = new double[n][] ;
		for ( int i = 0 ; i < n ; i++ ) {
			rv[i] = pts[n-1-i] ;
		}
		return rv;
	}

	public void add ( Path2D.Double path, double phi, boolean reverse, boolean hmirror, boolean addFirstPoint, boolean lineToFirstPoint ) {
		if ( reverse )
			add ( path, phi, endPointsRev, controlPointsRev, hmirror, addFirstPoint, lineToFirstPoint ) ;
		else
			add ( path, phi, endPoints, controlPoints, hmirror, addFirstPoint, lineToFirstPoint ) ;
	}

	private void add(Double path, double phi, double[][] endPoints, double[][] controlPoints, boolean hmirror, boolean addFirstPoint, boolean lineToFirstPoint) {
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		if ( addFirstPoint ) {
			double x = endPoints[0][0] ;
			double y = endPoints[0][1] ;
			if ( hmirror )
				x = -x ;
			double x_ = x*c + s*y ;
			double y_ = y*c - s*x ;
			if ( lineToFirstPoint ) {
				path.lineTo( x_, y_) ;				
			} else {
				path.moveTo( x_, y_) ;
			}
		}
		int n = endPoints.length-1 ;
		for ( int i = 0 ; i < n ; i++ ) {
			double x ;
			double y ;			
			x = endPoints[i+1][0] ;
			y = endPoints[i+1][1] ;
			if ( hmirror )
				x = -x ;
			double epx = x*c + s*y ;
			double epy = y*c - s*x ;
			x = controlPoints[i*2][0] ;
			y = controlPoints[i*2][1] ;
			if ( hmirror )
				x = -x ;
			double cp1x = x*c + s*y ;
			double cp1y = y*c - s*x ;
			x = controlPoints[i*2+1][0] ;
			y = controlPoints[i*2+1][1] ;
			if ( hmirror )
				x = -x ;
			double cp2x = x*c + s*y ;
			double cp2y = y*c - s*x ;
			path.curveTo(cp1x, cp1y, cp2x, cp2y, epx, epy);
		}
	}

	public double[] getStartVertex() {
		return endPoints[0];
	}
	
	public double[] getEndVertex() {
		return endPoints[endPoints.length-1];
	}
	
}
