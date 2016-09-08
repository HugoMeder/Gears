package de.bitsnarts.gear.viewer.sinusGears;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.viewer.AbstractRackPainter;

public class RackPainter extends AbstractRackPainter {

	private double x_max;
	private int n;
	private double y0;
	private Double path;
	private RealFunction rf ;
	
	public RackPainter( double v, RackParameters params ) {
		super( v );
		rf = params.profileFunction ;
		n = 2000 ;
		x_max = 10*params.modul+Math.PI ;
		y0 = params.y ;
		path = createPath () ;
	}

	Double createPath () {
		GeneralPath.Double rv = new GeneralPath.Double () ;
		double dx = 2*x_max/n ;
		double x0 = -x_max ;
		for ( int i = 0 ; i <= n ; i++ ) {
			double x = x0+dx*i ;
			double y = y0+rf.eval( x ) ;
			//double y = y0+h*Math.sin( x ) ;
			if ( i == 0 )
				rv.moveTo( x,  y);
			else
				rv.lineTo(x, y);
		}
		return rv; 
	}
	
	@Override
	protected void paint(Graphics2D gr) {
		gr = (Graphics2D) gr.create() ;
		gr.setColor( new Color ( 0f, 1f, 1f ));
		gr.draw ( path ) ;
	}

}
