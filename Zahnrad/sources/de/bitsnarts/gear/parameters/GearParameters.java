package de.bitsnarts.gear.parameters;

public class GearParameters {
	public double d ;
	public double da ;
	public double df ;
	public int z ;
	public double m ;
	public double p ;
	public double phi;
	public double xi;
	public double c;

	public GearParameters ( double m, int z ) {
		this ( m, z, (90.0-20.0)*Math.PI/180.0, 0.5, 0.167 ) ;
	}

	public GearParameters ( double m, int z, double c ) {
		this ( m, z, (90.0-20.0)*Math.PI/180.0, 0.5, c ) ;
	}

	public GearParameters ( double m, int z, double phi, double xi, double c ) {
		this.d = m*z ;
		this.p = d*Math.PI/z ;
		this.m = m ;
		this.da = d+2.0*m ;
		this.c = c ;
		this.df = d - 2.0*m*(1.0 + c) ;
		this.phi = phi ;
		this.z = z ;
		this.xi = xi ;
	}
	
}
