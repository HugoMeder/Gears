package de.bitsnarts.gear;

import de.bitsnarts.gear.parameters.DerivedInvoluteParams;
import de.bitsnarts.gear.parameters.InvoluteParameter;

public class Involute implements Curve {

	DerivedInvoluteParams derived;
	InvoluteParameter params;
	private double a;
	private double b;
	private double acos;

	public Involute ( DerivedInvoluteParams derived ) {
		this.derived = derived ;
		this.params = derived.getParams() ;
		a = params.r0 ;
		b = Math.sqrt( derived.progress_x*derived.progress_x+derived.progress_y*derived.progress_y) ;
		acos = (-params.r0*derived.progress_y)/b ;
	}
	
	@Override
	public void eval ( double t, double xy[] ) {
		double x = derived.progress_x*t ;
		double y = -params.r0+derived.progress_y*t ;
		double phi = -derived.omega*t ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		xy[0] = x*c - s*y ;
		xy[1] = x*s + c*y ;
	}
	
	public double rForT ( double t ) {
		double x = derived.progress_x*t ;
		double y = -params.r0+derived.progress_y*t ;
		return Math.sqrt( x*x + y*y ) ;
	}
	
	public double tForR( double r ) {
		double tmp = r*r-a*a+acos*acos ;
		return ( Math.sqrt( tmp ) -acos )/b ;
	}
	
	public double minR () {
		return Math.sqrt( a*a-acos*acos ) ;
	}

	public double minT() {
		return -acos/b ;
	}

	@Override
	public void grad(double t, double[] grad ) {
		double phi = -derived.omega*t ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		grad[0] = derived.progress_x*c - derived.progress_y*s ;
		grad[1] = derived.progress_x*s + derived.progress_y*c ;
		double x = derived.progress_x*t ;
		double y = -params.r0+derived.progress_y*t ;
		grad[0] += (-x*s - c*y)*(-derived.omega) ;
		grad[1] += (x*c - s*y)*(-derived.omega) ;
	}
}
