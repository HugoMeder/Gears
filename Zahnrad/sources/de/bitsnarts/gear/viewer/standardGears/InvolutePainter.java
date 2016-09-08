package de.bitsnarts.gear.viewer.standardGears;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import de.bitsnarts.gear.Involute;
import de.bitsnarts.gear.parameters.DerivedInvoluteParams;
import de.bitsnarts.gear.parameters.InvoluteParameter;
import de.bitsnarts.gear.viewer.AbstractWheelPainter;

public class InvolutePainter extends AbstractWheelPainter {

	private DerivedInvoluteParams derived;
	private java.awt.geom.Path2D.Double gp;
	private double t1;
	private double t2;

	public InvolutePainter(DerivedInvoluteParams derived ) {
		this ( derived, -1, +1 ) ;
	}
	
	public InvolutePainter(DerivedInvoluteParams derived, double t1, double t2 ) {
		super(derived);
		this.derived = derived ;
		this.t1 = t1 ;
		this.t2 = t2 ; 
		initPath () ;
	}

	private void initPath() {
		Involute involute = new Involute ( derived ) ;
		gp = new GeneralPath.Double () ;
		/*
		gp.moveTo( 0,  0 );
		gp.lineTo( 1.0, 0.0 );
		gp.lineTo( 0.0, 1.0 );
		*/
		int n = 2000 ;
		double dt = (t2-t1)/n ;
		double xy[] = new double[2] ;
		for ( int i = 0 ; i <= n ; i++ ) {
			double t = t1+dt*i ;
			involute.eval( t, xy );
			/*
			double x = derived.progress_x*t ;
			double y = -params.r0+derived.progress_y*t ;
			double phi = -derived.omega*t ;
			double c = Math.cos( phi ) ;
			double s = Math.sin( phi ) ;
			double x_ = x*c - s*y ;
			double y_ = x*s + c*y ;
			*/
			if ( i == 0 ) {
				gp.moveTo( xy[0],  xy[1] );
			} else {
				gp.lineTo( xy[0],  xy[1] );				
			}
		}
	}

	@Override
	protected void paint(Graphics2D gr) {
		gr.draw( gp );
	}

}
