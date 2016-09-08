package de.bitsnarts.gear.viewer.standardGears;

import de.bitsnarts.gear.involuteIntersection.NotConvergedException;
import de.bitsnarts.gear.parameters.GearParameters;
import de.bitsnarts.gear.parameters.InvoluteParameter;
import de.bitsnarts.gear.parameters.RackParameters;
import de.bitsnarts.gear.viewer.AbstractGearPainter;
import de.bitsnarts.gear.viewer.AbstractRackPainter;
import de.bitsnarts.gear.viewer.EingriffPainter;
import de.bitsnarts.gear.viewer.GearsAndRackFactory;

public class StandardGearsAndRackFactory implements GearsAndRackFactory {

	private DialogProperties props;
	private GearParameters gp;
	private RackParameters rp;
	private InvoluteParameter params;
	private GearParameters gp2;
	private InvoluteParameter params2;

	public StandardGearsAndRackFactory(DialogProperties props) {
		this.props = props ;
		gp = new GearParameters ( props.modul, props.wheel1.z, (90.0-props.thetaDegrees)*Math.PI/180, props.wheel1.xi, props.wheel1.c ) ;
		params = new InvoluteParameter ( gp ) ;
		
		rp = new RackParameters () ;
		rp.y = -gp.d/2.0 ;
		rp.lower_height = (gp.da-gp.d)/2.0 ; 
		rp.upper_height = rp.lower_height ;
		rp.p = gp.p ;
		rp.numTeeth = 10 ;
		rp.numLeftTeeth = 5 ;
		rp.phi = (90.0-props.thetaDegrees)*Math.PI/180 ;
		rp.v = params.v ;
		rp.xi = gp.xi ;
		
		// 	public GearParameters ( double m, int z, double phi, double xi, double c ) {
		gp2 = new GearParameters ( props.modul, props.wheel2.z, (90.0-props.thetaDegrees)*Math.PI/180, props.wheel2.xi, props.wheel2.c ) ;
		params2 = new InvoluteParameter ( gp2 ) ;
		
	}

	@Override
	public AbstractGearPainter createGearPainter1() throws NotConvergedException {
		return new GearPainter2 ( gp, 0, 0, 0.0, params.v/(gp.d/2.0) ) ;
	}

	@Override
	public AbstractGearPainter createGearPainter2() throws NotConvergedException {
		return new GearPainter2 ( gp2, 0, rp.y-gp2.d/2.0, Math.PI, -params2.v/(gp2.d/2.0) ) ;
	}

	@Override
	public AbstractRackPainter getRackPainter() {
		return new RackPainter ( rp ) ;
	}

	@Override
	public EingriffPainter createEingriffPainter() {
		return new EingriffPainter ( gp ) ;		
	}
	
	

}
