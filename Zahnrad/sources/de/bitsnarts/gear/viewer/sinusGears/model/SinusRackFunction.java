package de.bitsnarts.gear.viewer.sinusGears.model;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;
import de.bitsnarts.gear.viewer.sinusGears.ScaledSin;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.TOfX;

public class SinusRackFunction extends AbstractRackFunction {

	private double k;
	private double h;

	public SinusRackFunction( double h, double k, double v ) {
		super( new ScaledSin ( k, 0, h ) , v, 2.0*Math.PI/k );
		this.k = k ;
		this.h = h ;
	}

	@Override
	public double[][] getStationaryTOfXIntervals() {
		double y = -1.0/(k*k*h*h) ;
		if ( y < -1.0 ) {
			return new double[0][] ;
		}
		if ( y == -1.0 ) {
			double[][] rv;
			rv = new double[2][2] ;
			rv[0][0] = p/4.0 ;
			rv[0][1] = p/4.0 ;
			rv[1][0] =-p/4.0 ;
			rv[1][1] =-p/4.0 ;
			return rv ;
		}
		double[][] rv;
		rv = new double[4][2] ;
		double x_1_ = Math.acos( y )/(2.0*k) ;
		double delta = Math.acos( -y )/(2.0*k) ;
		double x_1 =-(p/4.0+delta) ;
		double x_2 =-(p/4.0-delta) ;
		double x_3 = p/4.0-delta ;
		double x_4 = p/4.0+delta ;
		rv[0][0] = x_1 ;
		rv[0][1] = x_1 ;
		rv[1][0] = x_2 ;
		rv[1][1] = x_2 ;
		rv[2][0] = x_3 ;
		rv[2][1] = x_3 ;
		rv[3][0] = x_4 ;
		rv[3][1] = x_4 ;
		
		if ( true ) {
			TOfX tofx = new TOfX ( f, v ) ;
			RealFunction tofxDer = tofx.derivative() ;
			boolean error = false ;
			for ( int i = 0  ; i < 4 ; i++ ) {
				for ( int j = 0 ; j < 2 ; j++ ) {
					double x = rv[i][j] ;
					double val = tofx.eval( x ) ;
					double val2 = -((k*h*h)*Math.sin(2.0*k*x)/2.0+x)/v ;
					double d = val-val2 ;
					if ( d*d > 1e-20 ) {
						error = true ;
					}
					d = tofxDer.eval( x ) ;
					if ( d*d > 1e-20 ) {
						error = true ;
					}
				}
			}
			if ( error )
				throw new Error ( "inconsisent" ); 
			else
				System.out.println( "getStationaryTOfXIntervals OK" );
		}
		return rv ;
	}

}
