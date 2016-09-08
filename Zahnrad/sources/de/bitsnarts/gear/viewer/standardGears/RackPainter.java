package de.bitsnarts.gear.viewer.standardGears;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import de.bitsnarts.gear.parameters.RackParameters;
import de.bitsnarts.gear.viewer.AbstractRackPainter;

public class RackPainter extends AbstractRackPainter {

	private RackParameters params;
	
	public RackPainter ( RackParameters params ) {
		super ( params.v ) ;
		this.params = params ;
		}
	
	private GeneralPath.Double createPath() {
		GeneralPath.Double rv = new GeneralPath.Double () ;
		double x0 = -params.numLeftTeeth*params.p ;
		double cotanPhi = 1.0/Math.tan( params.phi ) ;
		double d = params.p*params.xi ;
		for ( int i = 0 ; i < params.numTeeth ; i++ ) {
			double x ;
			double y ;
			x = x0+i*params.p-params.lower_height*cotanPhi ;
			y = params.y-params.lower_height ;
			if ( i == 0 ) {
				rv.moveTo(x, y);
			} else {
				rv.lineTo(x, y);				
			}
			x = x0+i*params.p+params.upper_height*cotanPhi ;
			y = params.y+params.upper_height ;
			rv.lineTo(x, y);				
			x = x0+i*params.p+d-params.upper_height*cotanPhi ;
			rv.lineTo(x, y);				
			x = x0+i*params.p+d+params.lower_height*cotanPhi ;
			y = params.y-params.lower_height ;
			rv.lineTo(x, y);
			/*
			if ( i == params.numTeeth-1 ) {
				x = x0+(i+1)*params.p-params.lower_height*cotanPhi ;
				y = params.y-params.lower_height ;				
				rv.lineTo(x, y);				
			}*/
		} 
		return rv;
	}

	@Override
	protected void paint(Graphics2D gr) {
		gr = (Graphics2D) gr.create() ;
		gr.setColor( new Color ( 0f, 0f, 1f ));
		GeneralPath.Double path = createPath () ;
		gr.draw ( path ) ;
	}

}
