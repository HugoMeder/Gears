package de.bitsnarts.gear.viewer.sinusGears.model.generic;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;

public class TOfX implements RealFunction {

	private RealFunction f;
	private RealFunction f_prime;
	private RealFunction f_prime_prime;
	private double v;
	private Deriv deriv = new Deriv () ;
	
	class Deriv implements RealFunction {

		@Override
		public double eval(double x) {
			double df = f_prime.eval(x) ;
			return -(df*df+f.eval(x)*f_prime_prime.eval(x)+1)/v;
		}

		@Override
		public RealFunction derivative() {
			throw new Error ( "not implemented" ) ;
		}
		
	}
	
	public TOfX ( RealFunction f, double v ) {
		this.f = f ;
		this.f_prime = f.derivative() ;
		this.f_prime_prime = f_prime.derivative() ;
		this.v = v ;
	}
	
	@Override
	public double eval(double x) {
		return -(f.eval(x)*f_prime.eval(x)+x)/v;
	}

	@Override
	public RealFunction derivative() {
		return deriv;
	}

}
