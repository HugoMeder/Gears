package de.bitsnarts.gear;

public interface Curve {
	void eval ( double t, double xy[] ) ;
	void grad(double t, double[] grad ) ;
}
