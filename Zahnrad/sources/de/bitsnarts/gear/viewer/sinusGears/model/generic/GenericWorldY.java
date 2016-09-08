package de.bitsnarts.gear.viewer.sinusGears.model.generic;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;

public class GenericWorldY implements RealFunction {

	private RealFunction f;
	private double r;

	GenericWorldY ( RealFunction f, double r ) {
		this.f = f ;
		this.r = r ;
	}
	
	@Override
	public double eval(double x) {
		return f.eval(x)-r;
	}

	@Override
	public RealFunction derivative() {
		return f.derivative();
	}

}
