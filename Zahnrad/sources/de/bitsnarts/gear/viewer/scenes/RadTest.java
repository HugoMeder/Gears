package de.bitsnarts.gear.viewer.scenes;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.bitsnarts.gear.involuteIntersection.NotConvergedException;
import de.bitsnarts.gear.parameters.GearParameters;
import de.bitsnarts.gear.parameters.InvoluteParameter;
import de.bitsnarts.gear.parameters.RackParameters;
import de.bitsnarts.gear.viewer.Viewer;
import de.bitsnarts.gear.viewer.standardGears.GearPainter;
import de.bitsnarts.gear.viewer.standardGears.InvolutePainter;
import de.bitsnarts.gear.viewer.standardGears.RackPainter;

public class RadTest {

	
	public static void main ( String args[] ) {

		GearParameters gp = new GearParameters ( 2.0/10, 10, 0.5 ) ;
		InvoluteParameter params = new InvoluteParameter ( gp ) ;
		
		RackParameters rp = new RackParameters () ;
		rp.y = -gp.d/2.0 ;
		rp.lower_height = (gp.da-gp.d)/2.0 ; 
		rp.upper_height = rp.lower_height ;
		rp.p = gp.p ;
		rp.numTeeth = 10 ;
		rp.numLeftTeeth = 5 ;
		rp.phi = (90.0-20.0)*Math.PI/180.0 ;
		rp.v = params.v ;
		rp.xi = gp.xi ;
		RackPainter rptr = new RackPainter ( rp ) ;
		GearPainter gptr = null ;
		try {
			gptr = new GearPainter ( gp, params.v ) ;
		} catch (NotConvergedException e) {
			e.printStackTrace();
		}
		//InvolutePainter ep = new InvolutePainter ( gptr.getDerivedParameters() ) ;

		Viewer v = new Viewer () ;
		//v.setT(-1.0 );
		v.addPainter ( rptr ) ;
		v.addPainter( gptr );
		//v.addPainter( ep );
		JFrame f = new JFrame ( "Rack Test" ) ;
		f.setJMenuBar( v.getMenuBar() );
		f.add( v );
		f.setSize ( 1024, 1024 ) ;
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setVisible( true );
	}
}
 