package de.bitsnarts.gear.viewer.sinusGears.model.generic;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;

public class GenericWorldX implements RealFunction {

	private RealFunction f;
	private RealFunction f_prime;
	private RealFunction f_prime_prime;
	private RealFunction f_prime_prime_prime;
	private Deriv deriv = new Deriv () ;
	private Deriv2 deriv2 = new Deriv2 () ;
	
	class Deriv2 implements RealFunction {

		@Override
		public double eval(double x) {
			return -(3.0*f_prime.eval(x)*f_prime_prime.eval(x ) + f.eval(x)*f_prime_prime_prime.eval(x));
		}

		@Override
		public RealFunction derivative() {
			throw new Error ( "not implemented" ) ;
		}
		
	}
	
	class Deriv implements RealFunction {

		@Override
		public double eval(double x) {
			double fd = f_prime.eval( x ) ;
			return -(fd*fd+f.eval(x)*f_prime_prime.eval(x));
		}

		@Override
		public RealFunction derivative() {
			return deriv2 ;
		}
		
	}
	
	GenericWorldX ( RealFunction f ) {
		this.f = f ;
		this.f_prime = f.derivative() ;
		this.f_prime_prime = f_prime.derivative() ;
		this.f_prime_prime_prime = f_prime_prime.derivative() ;
	}
	
	@Override
	public double eval(double x) {
		return -f.eval(x)*f_prime.eval(x);
	}

	@Override
	public RealFunction derivative() {
		return deriv;
	}

}
