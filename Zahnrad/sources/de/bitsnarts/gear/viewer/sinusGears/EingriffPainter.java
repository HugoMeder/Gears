package de.bitsnarts.gear.viewer.sinusGears;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.util.TreeSet;

import de.bitsnarts.gear.graphics.ArcByBezier;
import de.bitsnarts.gear.viewer.AnimatedPainter;
import de.bitsnarts.gear.viewer.sinusGears.model.SinusGearGeometry;
import de.bitsnarts.gear.viewer.sinusGears.model.SinusGearParameters;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.AnalyticalCurve;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.GenericWorldCurve;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.XOfT;

public class EingriffPainter  implements AnimatedPainter {

	private Double path;
	private SinusGearParameters parameters;
	private RealFunction f;
	private RealFunction fDeriv;
	private double v;
	private double r;

	public EingriffPainter(SinusGearParameters parameters) {
		this.parameters = parameters ;
		SinusGearGeometry geom = new SinusGearGeometry ( parameters ) ;
		GenericWorldCurve gwc = geom.getGenericWorldCurve() ;
		ArcByBezier ab = new ArcByBezier ( gwc, 0.0, 2.0*Math.PI/(parameters.omega*parameters.z), 100 ) ;
		path = new Path2D.Double() ;
		ab.add( path, 0, false, false, true, false );
	}

	@Override
	public void paint(double t, Graphics2D gr) {
		gr = (Graphics2D) gr.create() ;
		gr.setColor ( new Color ( 0f, 0.5f, 0f) ) ;
		gr.draw ( path ) ;
		gr.setColor ( new Color ( 1f, 0f, 0f) ) ;
		paintContacts ( t, gr ) ;
	}

	private void paintContacts(double t, Graphics2D gr) {
		XOfT tofx = parameters.profileFunction.getXOfT() ;
		TreeSet<java.lang.Double> xs = tofx.getXVals( t ) ;
		for ( double x : xs ) {
			paintContact ( t, x, gr ) ;
		}
	}

	private void paintContact(double t, double x, Graphics2D gr) {
		if ( fDeriv == null ) {
			f = parameters.profileFunction.getProfileFunction() ;
			r = parameters.modul*parameters.z/2.0 ;
			fDeriv = f.derivative() ;
			v = parameters.v ;
		}
		double y = f.eval ( x );
		double y_prime = fDeriv.eval( x ) ;
		double len = Math.sqrt( 1.0 + y_prime*y_prime ) ;
		double cl = 0.5 ;
		Line2D.Double line ;
		line = new Line2D.Double ( x+v*t-cl*y_prime/len, y+cl/len-r, x+v*t+cl*y_prime/len, y-cl/len-r ) ;
		gr.draw( line );
	}

}
