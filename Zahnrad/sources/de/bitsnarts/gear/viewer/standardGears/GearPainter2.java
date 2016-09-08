package de.bitsnarts.gear.viewer.standardGears;

import java.awt.Graphics2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.graphics.GearGeometry;
import de.bitsnarts.gear.involuteIntersection.NotConvergedException;
import de.bitsnarts.gear.parameters.GearParameters;
import de.bitsnarts.gear.viewer.AbstractGearPainter;

public class GearPainter2  extends AbstractGearPainter {

	private GearParameters gp;
	private GearGeometry geom;
	
	public GearPainter2 ( GearParameters gp, double x, double y, double phi0, double omega ) throws NotConvergedException {
		super( x, y, phi0, omega );
		this.gp = gp ;
		initPath () ;
	}
	
	private void initPath() throws NotConvergedException {
		geom = new GearGeometry ( gp ) ;
	}

	@Override
	protected void paint(Graphics2D gr) {
		
		geom.draw ( gr ) ;
/*		if ( true ) {
		}else {
			gr.setColor ( new Color ( 1f, 0f, 0f) ) ;
			PathIterator iter = path.getPathIterator(null, gp.d/1000.0 ) ;
			double[] params = new double[6] ;
			double first_x = 0.0 ;
			double first_y = 0.0 ;
			double last_x = 0.0 ;
			double last_y = 0.0 ;
			
			do {
				int code = iter.currentSegment(params) ;
				switch ( code ) {
				case PathIterator.SEG_LINETO: {
					double x = params[0] ; 
					double y = params[1] ;
					java.awt.geom.Line2D.Double line = new Line2D.Double( last_x, last_y, x, y ) ;
					gr.draw( line );			
					last_x = x ;
					last_y = y ;
					break ;
				}
				case PathIterator.SEG_MOVETO: {
					first_x = params[0] ; 
					first_y = params[1] ;
					last_x = first_x ;
					last_y = first_y ;
					break ;
				}
				case PathIterator.SEG_CLOSE: {
					java.awt.geom.Line2D.Double line = new Line2D.Double( last_x, last_y, first_x, first_y ) ;
					gr.draw( line );
					break ;
				}
				default:
					throw new Error ( "unexpected code "+code ) ;
				}
				iter.next();
			} while ( !iter.isDone() ) ;			
		}
*/	
	}

	public Double getPath() {
		return geom.createPath ( 10, 10 ) ;
	}

}
