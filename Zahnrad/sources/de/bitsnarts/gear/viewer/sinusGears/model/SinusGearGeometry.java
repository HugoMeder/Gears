package de.bitsnarts.gear.viewer.sinusGears.model;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.graphics.ArcByBezier;
import de.bitsnarts.gear.involuteIntersection.R2FunctionTester;
import de.bitsnarts.gear.viewer.sinusGears.RealFunction;
import de.bitsnarts.gear.viewer.sinusGears.ScaledSin;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.GenericGearCurve;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.GenericWorldCurve;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.TOfX;
import de.bitsnarts.gear.viewer.sinusGears.test.RealFunctionTester;

public class SinusGearGeometry {

	private SinusGearParameters params;
	private Path2D path;
	private GenericWorldCurve gwc;

	public SinusGearGeometry ( SinusGearParameters params ) {
		this.params = params ;
		path = getPath () ;
	}

	public Path2D getPath() {
		RealFunction f = params.profileFunction.getProfileFunction() ;
		double v = params.v ;
		double omega = params.omega ;
		double r = v/omega ;
		gwc = new GenericWorldCurve ( f, r ) ;
		if ( false ) {
			R2FunctionTester tester = new R2FunctionTester ( gwc ) ;
			if ( !tester.testAt( 0.5 ) ) {
				throw new Error ( "!tester.testAt( 0.0 )" ) ;
			}			
		}
		TOfX tOfX = params.profileFunction.getTOfX() ;
		if ( false ) {
			RealFunctionTester t = new RealFunctionTester ( tOfX ) ;
			if ( !t.testAt( 0.5 ) ) {
				throw new Error ( "!t.testAt( 0.5 )" ) ;
			}
		}
		GenericGearCurve ggc = new GenericGearCurve ( gwc, tOfX, omega ) ;
		if ( false ) {
			R2FunctionTester tester = new R2FunctionTester ( ggc ) ;
			if ( !tester.testAt( 0.5 ) ) {
				throw new Error ( "!tester.testAt( 0.0 )" ) ;
			}			
		}
		double dx = Math.PI*r/2.0 ;
//		double dx = Math.PI*r/params.z ;
		ArcByBezier ab = new ArcByBezier ( ggc, -dx*2.0, dx*2.0, 100*params.z ) ;
		Double rv = new Path2D.Double() ;
		ab.add(rv, 0, false, false, true, false );
		/*
		GenericWelzCurve2 wc = new GenericWelzCurve2 ( params ) ;
		Double rv = new Path2D.Double() ;
		double t0 ;
		double t1 ;
		if ( false ) {
	 		double d = 20 ;
			t0 = -d ;
			t1 = +d ;
		} else {
			double xi = 2.0/params.modul ;
			double h = Math.tan( params.flankenSteileitRadiant )/xi ;
			double hXi = h*xi ;
			double x = 0.5*Math.acos( -1.0/(hXi*hXi) )/xi ;
			double t = ((h*h*xi)*Math.sin( xi*x )*Math.cos( xi*x ) +x)/params.v ;
			double deriv = wc.derivative().eval( x ) ;
			double tMax = t ;//-0.001 ;
			t0 = -tMax ;
			t1 = +tMax ;
		}
		*/
		/*
		if ( false ) {
			ArcByBezier ab = new ArcByBezier ( wc, t0, t1, 10 ) ;
			ab.add(rv, 0, false, false, true, false);			
		} else {
			int n = 2000 ;
			double dt = (t1-t0)/n ;
			double coord[] = new double[2] ;
			
			double delta = 0.0001 ;
			wc.eval( delta, coord );
			wc.eval(-delta, coord );
			
			for ( int i = 0; i <= n ; i++ ) {
				double t = t0 + dt*i ;
				double phi = params.omega*t ;
				double c = Math.cos( phi ) ;
				double s = Math.sin( phi ) ;
				wc.eval( t, coord ) ;
				double x = coord[0]*c+coord[1]*s ;
				double y = coord[1]*c-coord[0]*s ;
				if ( i == 0 )
					rv.moveTo( x, y );
				else
					rv.lineTo( x, y );
			}			
		}
		*/
		return rv;
	}

	public GenericWorldCurve getGenericWorldCurve () {
		return gwc ;
	}
	
	public void paint(Graphics2D gr) {
		gr.draw( path );
	}
}
