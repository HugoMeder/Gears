package de.bitsnarts.wheel.pläne;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.utils.print.PrintShape;

public class Bohrplan {

	private double umf_4;
	private double outerDist;
	private double bildBreite;
	private double rahmenBreite;
	private int numBilder;
	private int numSchellen;
	private double[] raster;
	private static double scale = 1000.0;
	
	public Bohrplan(double umf_4) {
		this.umf_4 = umf_4*scale ;
		outerDist = 0.05*scale ;
		bildBreite = 0.3*scale ;
		rahmenBreite = 0.035*scale ;
		numBilder = 13 ;
		numSchellen = 5 ;
		createraster () ;
		printraster () ;
	}

	private void createraster() {
		int n = 5 ;
		raster = new double[n] ;
		double bohrochdist = umf_4 / (n-1) ;
		for ( int i = 1 ; i < (n-1) ; i++ ) {
			raster[i] = i * bohrochdist ;
		}
		raster[0] = outerDist ;
		raster[n-1] = umf_4-outerDist ;
	}

	private void printraster() {
		/*
		int n = 4 ; // (4*8) ;
		double bohrochdist = umf_4 / n ;
		for ( int i = 0 ; i <= n ; i++ ) {
			System.out.printf( "%5.1f\n", (i+0.0)*bohrochdist*100 );
		}
		*/
		int n = raster.length ;
		for ( int i = 0 ; i < n ; i++ ) {
			System.out.printf( "%5.1f\n", raster[i]*100 );
		}
	}

	private void drawPlan() {
		Path2D.Double path = new Path2D.Double () ;
		drawBilder ( path ) ;
		drawRaster ( path ) ;
		PrintShape.print( path, false, 200.0/umf_4 );
	}

	private void drawRaster(Double path) {
		double x1 = -0.10*scale ;
		double x2 = 0.03*scale ;
		for ( double y : raster ) {
			path.moveTo( x1,  y );
			path.lineTo( x2,  y );
		}
	}

	private void drawBilder(Double path) {
		double zwischenraum = (umf_4-bildBreite*numBilder)/(numBilder-1) ;
		for ( int i = 0 ; i < numBilder ; i++ ) {
			printBild ( path, (bildBreite+zwischenraum )*i ) ;
		}
	}

	
	private void printBild(Double path, double pos ) {
		double dx = 0.015*scale ;
		path.moveTo( dx, pos+rahmenBreite );
		path.lineTo( 0, pos+rahmenBreite );
		path.lineTo( 0, pos );
		path.lineTo( dx, pos );
		path.lineTo( dx, pos+bildBreite );
		path.lineTo( 0, pos+bildBreite );
		path.lineTo( 0, pos+bildBreite-rahmenBreite );
		path.lineTo( dx, pos+bildBreite-rahmenBreite );
	}

	public static void main ( String args [] ) {
		double brettDicke = 0.006 ;
		double umf = Math.PI*(5.0+brettDicke/2.0) ;
		double umf_4 = umf / 4.0 ;
		Bohrplan bp = new Bohrplan ( umf_4 ) ;
		bp.drawPlan () ;
	}

}
