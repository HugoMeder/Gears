package de.bitsnarts.wheel.pl�ne;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.graphics.Circle;
import de.bitsnarts.utils.print.PrintShape;

public class Bauplan {

	private double bildBreite;
	private double rahmenBreite;
	private int numBilder;
	double ceilHeight = 5597 ;
	double balkenUnterkannte = 5290.0 ;
	double balkenH�he = ceilHeight-balkenUnterkannte ;
	double balkenBreite = balkenH�he ;
	double xMin = -balkenBreite ;
	double balkenDist = 3460 ;
	double radCenterX = balkenDist/2.0 ;
	double radCenterY =-ceilHeight/2.0 ;
	double brettDicke = 006.0 ;
	double umf = Math.PI*(5000.0+brettDicke/2.0) ;
	double umf_4 = umf / 4.0 ;
	double achtelRad = 2.840 ;//kg
	double radMass = achtelRad*8.0 ;//kg
	double fuenfBilder = 3.400 ;//3.030 ;//kg
	double bildMasse = fuenfBilder/5.0 ;//kg
	int numImages = 50 ;
	double bilderMasse = bildMasse*numImages ;//kg
	double dreieckSeitenL�nge = 150.0 ; // m
	double h = dreieckSeitenL�nge*Math.sqrt( 3.0/4.0 ) ;
	double c = h/3.0 ;
	double shift = c*radMass/bilderMasse ;

	public Bauplan() {
		//outerDist = 50.0 ;
		bildBreite = 300.0 ;
		rahmenBreite = 350.0 ;
		numBilder = 13 ;
	}

	private void drawPlanFront() {
		Path2D.Double path = new Path2D.Double () ;
		drawBottomAndCeil ( path ) ;
		printWheel ( path ) ;
		printGer�st ( path ) ;
		PrintShape.print( path, false, 190.0/6000.0 );
	}

	private void printGer�st(Double path) {
		double ger�stL�nge = 2450 ;
		path.moveTo( radCenterX-ger�stL�nge/2.0, 0);
		path.lineTo( radCenterX-ger�stL�nge/2.0, -5200 );
		path.lineTo( radCenterX+ger�stL�nge/2.0, -5200 );
		path.lineTo( radCenterX+ger�stL�nge/2.0, 0 );
		path.moveTo( radCenterX-ger�stL�nge/2.0, -4200);
		path.lineTo( radCenterX+ger�stL�nge/2.0, -4200);
	}

	private void printWheel(Double path) {
		Circle c ;
		c = new Circle ( radCenterX, radCenterY, 2500.0, 100 ) ;
		c.addTo( path );
		c = new Circle ( radCenterX, radCenterY, 2500.0+25.0, 100 ) ;
		c.addTo( path );
		
		c = new Circle ( radCenterX, radCenterY, 2500.0-dreieckSeitenL�nge, 100 ) ;
		c.addTo( path );
		c = new Circle ( radCenterX, radCenterY, 2500.0+25.0-dreieckSeitenL�nge, 100 ) ;
		c.addTo( path );
		
		c = new Circle ( radCenterX, radCenterY, 2500.0-dreieckSeitenL�nge/2, 100 ) ;
		c.addTo( path );
		c = new Circle ( radCenterX, radCenterY, 2500.0+25.0-dreieckSeitenL�nge/2, 100 ) ;
		c.addTo( path );
		
		
	}

	private void drawBottomAndCeil(Double path) {
		path.moveTo( xMin, -ceilHeight );
		path.lineTo( xMin, -balkenUnterkannte );
		path.lineTo( 0, -balkenUnterkannte );
		path.lineTo( 0, -ceilHeight );
		path.lineTo( balkenDist, -ceilHeight );
		path.lineTo( balkenDist, -balkenUnterkannte );
		path.lineTo( balkenDist+balkenBreite, -balkenUnterkannte );
		path.lineTo( balkenDist+balkenBreite, -ceilHeight );
		double xMax = balkenDist+balkenBreite ;
		path.moveTo( xMin, 0 ); 
		path.lineTo( xMax, 0 ); 
	}

	private void drawBilder(Double path) {
		double zwischenraum = (umf_4-bildBreite*numBilder)/(numBilder-1) ;
		for ( int i = 0 ; i < numBilder ; i++ ) {
			printBild ( path, (bildBreite+zwischenraum )*i ) ;
		}
	}

	
	private void printBild(Double path, double pos ) {
		double dx = 15.0 ;
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
		Bauplan bp = new Bauplan () ;
		bp.drawPlanFront () ;
	}

}
