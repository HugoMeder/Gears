package de.bitsnarts.gear;

import de.bitsnarts.gear.parameters.InvoluteParams2;

public class Involute2 implements Curve {

	private InvoluteParams2 params;
	private double a;
	private double b;
	private double acos;

	public Involute2 ( InvoluteParams2 params ) {
		this.params = params ;
		a = Math.sqrt( params.x0*params.x0+params.y0*params.y0 ) ;
		b = Math.abs( params.v ) ;
		acos = (params.x0*params.v)/b ;
	}
	
	@Override
	public void eval ( double t, double xy[] ) {
		double x = params.x0 + params.v*t ;
		double y = params.y0 ;
		double phi = -params.omega*t ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		xy[0] = x*c - s*y ;
		xy[1] = x*s + c*y ;
	}

	/* Involute:
	public void eval ( double t, double xy[] ) {
		double x = derived.progress_x*t ;
		double y = -params.r0+derived.progress_y*t ;
		double phi = -derived.omega*t ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		xy[0] = x*c - s*y ;
		xy[1] = x*s + c*y ;
	}
	 */
	
	public double getIntersectionT(Involute inv) {
		if ( params.omega != inv.derived.omega )
			throw new Error ( "params.omega != inv.derived.omega" ) ;
		// solve for t:
		// params.y0 = -inv.params.r0+inv.derived.progress_y*t ;
		// 
		return (params.y0+inv.params.r0)/inv.derived.progress_y;
	}

	@Override
	public void grad(double t, double[] grad ) {
		double phi = -params.omega*t ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		grad[0] = params.v * c ;
		grad[1] = params.v * s ;
		double x = params.x0 + params.v*t ;
		double y = params.y0 ;
		grad[0] += (-x*s - c*y)*(-params.omega) ;
		grad[1] += (x*c - s*y)*(-params.omega) ;
	}

	public double minT() {
		return -acos/b ;
	}
	

}
