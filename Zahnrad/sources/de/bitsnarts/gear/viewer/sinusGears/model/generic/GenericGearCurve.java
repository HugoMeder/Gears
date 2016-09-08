package de.bitsnarts.gear.viewer.sinusGears.model.generic;

import de.bitsnarts.gear.Curve;
import de.bitsnarts.gear.viewer.sinusGears.RealFunction;

public class GenericGearCurve implements Curve {

	private Curve worldSpaceCurve;
	private RealFunction tOfX;
	private double omega;
	private double[] tmp = new double[2] ;
	private double x;
	private double y;
	private double x_prime;
	private double y_prime;
	private RealFunction tOfX_prime;

	public GenericGearCurve ( Curve worldSpaceCurve, RealFunction tOfX, double omega ) {
		this.worldSpaceCurve = worldSpaceCurve ;
		this.tOfX = tOfX ;
		this.tOfX_prime = tOfX.derivative() ;
		this.omega = omega ;
	}
	
	@Override
	public void eval(double x_, double[] xy) {
		double phi = -tOfX.eval( x_ )*omega ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		ws_xy ( x_ ) ;
		xy[0] = x*c-y*s ;
		xy[1] = x*s+y*c ;
	}

	private void ws_xy(double x) {
		worldSpaceCurve.eval(x, tmp);
		this.x = tmp[0] ;
		this.y = tmp[1] ;
	}

	private void ws_xy_prime(double x ) {
		worldSpaceCurve.grad(x, tmp);
		this.x_prime = tmp[0] ;
		this.y_prime = tmp[1] ;
	}

	@Override
	public void grad(double t, double[] grad) {
		double phi = -tOfX.eval( t )*omega ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		ws_xy ( t ) ;
		ws_xy_prime ( t ) ;
		double t_prime = tOfX_prime.eval(t) ;
		double x_ = this.x_prime+omega*t_prime*this.y ;
		double y_ = this.y_prime-omega*t_prime*this.x ;
		grad[0] = x_*c-y_*s ;
		grad[1] = x_*s+y_*c ;
	}


}
