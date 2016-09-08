package de.bitsnarts.gear.viewer.sinusGears.test;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;
import de.icido.math.utils.DerivationTester;

public class RealFunctionTester extends DerivationTester {

	private RealFunction f;

	public RealFunctionTester ( RealFunction f ) {
		this.f = f ;
	}

	public boolean testAt ( double x ) {
		return testDerivation(x, 0.1, f.derivative().eval(x) ) ;
	}
	
	@Override
	protected double eval(double x) {
		return f.eval(x);
	}
}
