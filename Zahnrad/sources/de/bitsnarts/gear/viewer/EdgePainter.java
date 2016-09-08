package de.bitsnarts.gear.viewer;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import de.bitsnarts.gear.parameters.InvoluteParameter;

public class EdgePainter extends AbstractRackPainter {

	private InvoluteParameter params;

	public EdgePainter ( InvoluteParameter params ) {
		super ( params.v ) ;
		this.params = params ;
	}
	
	@Override
	protected void paint(Graphics2D gr) {
		double x1 = -params.r0*Math.cos( params.phi )*2.0 ;
		double y1 =-params.r0-params.r0*Math.sin( params.phi )*2.0 ;
		double x2 = params.r0*Math.cos( params.phi )*2.0 ;
		double y2 = -params.r0+params.r0*Math.sin( params.phi )*2.0 ;
		Double line = new Line2D.Double(x1, y1, x2, y2 ) ;
		gr.draw( line );
	}

}
