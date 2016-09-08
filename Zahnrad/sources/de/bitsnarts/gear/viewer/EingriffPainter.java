package de.bitsnarts.gear.viewer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import de.bitsnarts.gear.parameters.GearParameters;

public class EingriffPainter  implements AnimatedPainter {

	private double c;
	private double s;
	private double r;
	private double y;

	public EingriffPainter ( double theta, double r, double y ) {
		c = Math.cos( theta ) ;
		s = Math.sin( theta ) ;
		this.r = r ;
		this.y = y ;
	}
	
	public EingriffPainter ( GearParameters gp ) {
		double theta = Math.PI/2.0-gp.phi ;
		c = Math.cos( theta ) ;
		s = Math.sin( theta ) ;
		r = gp.d/2.0 ;
		y =-gp.d/2.0 ;
	}
	
	@Override
	public void paint(double t, Graphics2D gr) {
		gr = (Graphics2D) gr.create() ;
		gr.setColor ( new Color ( 0f, 0.5f, 0f) ) ;
		Double line = new Line2D.Double ( -c*r, y+s*r, c*r, y-s*r ) ;
		gr.draw( line ) ;
		line = new Line2D.Double ( -c*r, y-s*r, c*r, y+s*r ) ;
		gr.draw( line ) ;
	}

}
