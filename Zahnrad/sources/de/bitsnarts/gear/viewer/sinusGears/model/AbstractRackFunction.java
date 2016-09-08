package de.bitsnarts.gear.viewer.sinusGears.model;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.TOfX;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.XOfT;

public abstract class AbstractRackFunction {

	protected RealFunction f;
	protected double v;
	protected double p;
	private TOfX tOfX;
	private XOfT xOfT;
	
	public AbstractRackFunction ( RealFunction f, double v, double p ) {
		this.f = f ;
		this.v = v ;
		this.p = p ;
		this.tOfX = new TOfX ( f, v ) ;
	}
	
	public RealFunction getProfileFunction() {
		return f ;
	}

	public TOfX getTOfX () {
		return tOfX ;
	}
	
	public XOfT getXOfT () {
		if ( xOfT == null ) {
			xOfT = new XOfT ( this ) ;
		}
		return xOfT ;
	}
	
	/**
	 * @return closed intervals where the TofX function is stationary. If no stationary point exist, an array of length zero sould be returned.
	 */
	public abstract double[][] getStationaryTOfXIntervals () ;

	public double getP() {
		return p ;
	}

	public double getV() {
		return v ;
	}
}
