package de.bitsnarts.gear.viewer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import de.bitsnarts.gear.parameters.DerivedInvoluteParams;

public abstract class AbstractWheelPainter implements AnimatedPainter {

	private double omega;
	private double x;
	private double y;
	private double phi0;

	public AbstractWheelPainter ( double omega ) {
		this ( 0, 0, 0, omega ) ;
		
	}
	public AbstractWheelPainter ( double x, double y, double phi0, double omega ) {
		this.x = x ;
		this.y = y ;
		this.phi0 = phi0 ;
		this.omega = omega ;
	}

	public AbstractWheelPainter ( DerivedInvoluteParams derived ) {
		this.omega = derived.omega ;
	}

	@Override
	public void paint(double t, Graphics2D gr) {
		gr = (Graphics2D) gr.create() ;
		double phi = omega*t+phi0 ;
		double c = Math.cos( phi ) ;
		double s = Math.sin( phi ) ;
		/*
		double sx = (1-c)*x + s*y ;
		double sy = (1-c)*y - s*x ;
		*/
		AffineTransform tr = new AffineTransform ( c, s, -s, c, x, y ) ;
		gr.transform( tr );
		paint ( gr ) ;
	}

	abstract protected void paint(Graphics2D gr) ;

}
