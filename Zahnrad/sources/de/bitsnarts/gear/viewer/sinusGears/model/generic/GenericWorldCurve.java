package de.bitsnarts.gear.viewer.sinusGears.model.generic;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;

public class GenericWorldCurve extends AnalyticalCurve {

	public GenericWorldCurve ( RealFunction f, double r ) {
		super ( new GenericWorldX ( f ), new GenericWorldY ( f, r ) ) ;
	}
}
