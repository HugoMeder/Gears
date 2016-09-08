package de.bitsnarts.gear.viewer.sinusGears.model.generic;

import de.bitsnarts.gear.Curve;
import de.bitsnarts.gear.viewer.sinusGears.RealFunction;

public class AnalyticalCurve implements Curve {

	private RealFunction x;
	private RealFunction y;
	private RealFunction x_prime;
	private RealFunction y_prime;

	public AnalyticalCurve ( RealFunction x, RealFunction y ) {
		this.x = x ;
		this.y = y ;
		this.x_prime = x.derivative() ;
		this.y_prime = y.derivative() ;
	}
	
	@Override
	public void eval(double t, double[] xy) {
		xy[0] = x.eval( t ) ;
		xy[1] = y.eval( t ) ;
	}

	@Override
	public void grad(double t, double[] grad) {
		grad[0] = x_prime.eval( t ) ;
		grad[1] = y_prime.eval( t ) ;
	}

	public AnalyticalCurve derivative () {
		return new AnalyticalCurve ( x_prime, y_prime ) ;
	}
}
