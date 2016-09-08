package de.bitsnarts.gear.viewer;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import de.bitsnarts.gear.parameters.DerivedInvoluteParams;
import de.bitsnarts.gear.parameters.InvoluteParameter;

public class Flanke implements AnimatedPainter {

	private InvoluteParameter params;

	public Flanke ( DerivedInvoluteParams params ) {
		this.params = params.getParams() ;
	}
	
	@Override
	public void paint(double t, Graphics2D gr) {
		double x1 = params.v*t-params.r0*Math.cos( params.phi )*2.0 ;
		double y1 =-params.r0-params.r0*Math.sin( params.phi )*2.0 ;
		double x2 = params.v*t+params.r0*Math.cos( params.phi )*2.0 ;
		double y2 = -params.r0+params.r0*Math.sin( params.phi )*2.0 ;
		Double line = new Line2D.Double(x1, y1, x2, y2 ) ;
		gr.draw( line );
	}

}
