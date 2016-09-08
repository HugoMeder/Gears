package de.bitsnarts.gear.viewer.sinusGears;

import de.bitsnarts.gear.involuteIntersection.NotConvergedException;
import de.bitsnarts.gear.viewer.AbstractGearPainter;
import de.bitsnarts.gear.viewer.AbstractRackPainter;
import de.bitsnarts.gear.viewer.AnimatedPainter;
import de.bitsnarts.gear.viewer.GearsAndRackFactory;
import de.bitsnarts.gear.viewer.sinusGears.model.SinusGearGeometry;

public class GARFactory implements GearsAndRackFactory {

	private SinusProperties props;

	GARFactory ( SinusProperties props ) {
		this.props = props ;
	}
	
	@Override
	public AbstractGearPainter createGearPainter1() throws NotConvergedException {
		return new SinusGearPainter (0, 0, 0, props.v/(props.wheel1.z*props.modul/2.0), props.wheel1.getParameters() ) ;
	}

	@Override
	public AbstractGearPainter createGearPainter2() throws NotConvergedException {
		//return null ;
		return new SinusGearPainter (0, -props.getWheelDistance(), Math.PI,-props.v/(props.wheel2.z*props.modul/2.0), props.wheel2.getParameters() ) ;
	}

	@Override
	public AbstractRackPainter getRackPainter() {
		return new RackPainter ( props.v, props.createRackParameters() );
	}

	@Override
	public AnimatedPainter createEingriffPainter() {
		return new EingriffPainter ( props.wheel1.getParameters() ) ;
		
		/*
		double d = props.wheel1.z*props.modul ;
		return new EingriffPainter ( props.eingriff_deg*Math.PI/180.0, d/2, -d/2 ) ;
		*/
	}

}
