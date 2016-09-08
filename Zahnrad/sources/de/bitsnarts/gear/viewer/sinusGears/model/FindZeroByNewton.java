package de.bitsnarts.gear.viewer.sinusGears.model;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;

public class FindZeroByNewton {

	private RealFunction rf;
	private RealFunction rfDeriv;

	FindZeroByNewton ( RealFunction rf ) {
		this.rf = rf ;
		this.rfDeriv = rf.derivative() ;
	}
	
	double findZero ( double x ) {
		double res = rf.eval( x ) ;
		int iter = 0 ;
		while ( res*res > 1e-20 ) {
			double deriv = rfDeriv.eval( x ) ;
			x -= res/deriv ;
			res = rf.eval( x ) ;
			if ( iter++ > 100 ) {
				return 0 ;
				//throw new Error ( "iter++ > 10" ) ;
			}
		}
		return x;
	}
}
