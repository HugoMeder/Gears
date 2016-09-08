package de.bitsnarts.gear.viewer.scenes;

import javax.swing.JFrame;

import de.bitsnarts.gear.parameters.DerivedInvoluteParams;
import de.bitsnarts.gear.parameters.InvoluteParameter;
import de.bitsnarts.gear.parameters.RackParameters;
import de.bitsnarts.gear.viewer.EdgePainter;
import de.bitsnarts.gear.viewer.Viewer;
import de.bitsnarts.gear.viewer.standardGears.InvolutePainter;
import de.bitsnarts.gear.viewer.standardGears.RackPainter;

public class FlankenTest {

	
	public static void main ( String args[] ) {
		//edgeTest () ;
		rackTest () ;
	}

	private static void rackTest() {
		double phi = (90.0-20.0)*Math.PI/180.0 ;
		InvoluteParameter params = new InvoluteParameter ( 1.0, 1.0, phi ) ;
		DerivedInvoluteParams derivedParams = new DerivedInvoluteParams ( params ) ;
		RackParameters rp = new RackParameters () ;
		rp.y = -params.r0 ;
		rp.lower_height = params.r0*0.1 ; 
		rp.upper_height = params.r0*0.1 ;
		rp.p = params.r0*0.3 ;
		rp.numTeeth = 10 ;
		rp.numLeftTeeth = 5 ;
		rp.phi = (90.0-20.0)*Math.PI/180.0 ;
		rp.v = params.v ;
		rp.xi = 0.5 ;
		RackPainter rptr = new RackPainter ( rp ) ; 
		InvolutePainter ep = new InvolutePainter ( derivedParams ) ;
		Viewer v = new Viewer () ;
		//v.setT(-1.0 );
		v.addPainter ( rptr ) ;
		v.addPainter( ep );
		JFrame f = new JFrame ( "Rack Test" ) ;
		f.setJMenuBar( v.getMenuBar() );
		f.add( v );
		f.setSize ( 1024, 1024 ) ;
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setVisible( true );
	}

	private static void edgeTest() {
		double phi = (90.0-20.0)*Math.PI/180.0 ;
		InvoluteParameter params = new InvoluteParameter ( 1.0, 1.0, phi ) ;
		DerivedInvoluteParams derivedParams = new DerivedInvoluteParams ( params ) ;
		//Flanke fl = new Flanke ( derivedParams ) ;
		EdgePainter fl = new EdgePainter ( derivedParams.getParams() ) ;
		//SimpleWheelPainter swp = new SimpleWheelPainter ( derivedParams ) ;
		InvolutePainter ep = new InvolutePainter ( derivedParams ) ;
		Viewer v = new Viewer () ;
		//v.setT(-1.0 );
		v.addPainter ( fl ) ;
		v.addPainter( ep );
		JFrame f = new JFrame ( "Flanken Test" ) ;
		f.setJMenuBar( v.getMenuBar() );
		f.add( v );
		f.setSize ( 1024, 1024 ) ;
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setVisible( true );
	}
}
 