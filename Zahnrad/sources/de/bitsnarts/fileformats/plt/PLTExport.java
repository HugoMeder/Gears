package de.bitsnarts.fileformats.plt;

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D ;
import java.awt.geom.PathIterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PLTExport {

	private File file;
	private PrintWriter out;
	private boolean isUp;
	private int last_xi;
	private int last_yi;

	public PLTExport ( File file ) throws IOException {
		this.file = file ;
		FileWriter fw = new FileWriter ( file ) ;
		out = new PrintWriter ( fw );
		out.println ( "INPU;" ) ;
		out.println ( "SP1;" ) ;
		isUp = true ;
		up () ;
	}
	
	private void up() {
		if ( !isUp ) {
			out.println ( "PU;" ) ;
			isUp = true ;
		}
	}

	private void down() {
		if ( isUp ) {
			out.println ( "PD;" ) ;
			isUp = false ;
		}
	}

	public void write ( GeneralPath path ) {
		write ( path.getPathIterator( null ) ) ;
	}

	public void write(Path2D path) {
		write ( path.getPathIterator( null, 0.025 ) ) ;		
	}

	public void write(PathIterator iter ) {
		double[] params = new double[6] ;
		double first_x = 0;
		double first_y = 0 ;
		while ( !iter.isDone() ) {
			int code = iter.currentSegment( params ) ;
			switch ( code ) {
			case PathIterator.SEG_LINETO: {
				lineTo ( params[0], params[1] ) ;				
				break ;
			}
			case PathIterator.SEG_MOVETO: {
				first_x = params[0] ; 
				first_y = params[1] ;
				moveTo ( first_x, first_y ) ;
				break ;
			}
			case PathIterator.SEG_CLOSE: {
				lineTo ( first_x, first_y ) ;
				break ;
			}
			default:
				throw new Error ( "unexpected code "+code ) ;
			}
			iter.next();
		}
		
	}

	public void close () {
		up () ;
		out.close () ;
	}
	private void lineTo(double x, double y) {
		down () ;
		int xi = (int)(x/0.025) ;
		int yi = (int)(y/0.025) ;
		if ( isDifferent ( xi, yi ) ) {
			out.println( "PA"+xi+","+yi+";" );			
		}
	}

	private boolean isDifferent(int xi, int yi) {
		if ( last_xi == xi && last_yi == yi ) {
			return false; 
		}
		last_xi = xi ;
		last_yi = yi ;
		return true;
	}

	private void moveTo(double x, double y) {
		up () ;
		int xi = (int)(x/0.025) ;
		int yi = (int)(y/0.025) ;
		if ( isDifferent ( xi, yi ) ) {
			out.println( "PA"+xi+","+yi+";" );
		}
	}

}
