package de.bitsnarts.gear.viewer.sinusGears;

public class ScaledCos implements RealFunction {

	private double omega;
	private double phi0;
	private double h;
	private ScaledSin der;

	ScaledCos ( double omega, double phi0, double h ) {
		this.omega = omega ;
		this.phi0 = phi0 ;
		this.h = h ;
	}
	
	@Override
	public double eval(double x) {
		return Math.cos( omega*x+phi0)*h;
	}

	@Override
	public RealFunction derivative() {
		if ( der == null )
			der = new ScaledSin ( omega, phi0, -h*omega ) ;
		return der;
	}
}
