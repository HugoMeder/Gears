package de.bitsnarts.wheel.pläne;

import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.graphics.Circle;
import de.bitsnarts.utils.print.PrintShape;

public class Aufhaengung {

	class WheelPart {
		double outerDiameter = 5000.0 ;
		double tubeDiameter = 25.0 ;
		double tubeDistance = 150.0 ;
		double innerTubeCenterRadius = outerDiameter/2.0-tubeDiameter/2.0-tubeDistance ;
		double intermediateTubeRadius = 4.0 ;
		private double effectiveRadius;
		private double thrdTubeCenterRadius;
		private double thrdTubeCenterHorOffset;

		WheelPart ( double phi ) {
			effectiveRadius = innerTubeCenterRadius - (tubeDiameter/2.0)*Math.sin ( phi ) ;
			thrdTubeCenterRadius = innerTubeCenterRadius + 0.5*tubeDistance ;
			thrdTubeCenterHorOffset = tubeDistance*Math.sqrt( 3.0/4.0 ) ;
		}
	}
	
	class MotorPart {
		double effectiveRadius;
		double innerRadius;
		double phi;
		double tubeRadius;

		MotorPart ( double innerRadius, double phi, double tubeRadius ) {
			this.innerRadius = innerRadius ;
			this.phi = phi ;
			this.tubeRadius = tubeRadius ;
			double s = Math.sin( phi ) ;
			effectiveRadius = innerRadius+tubeRadius*(1.0/s-s) ;
		}
	}

	private WheelPart wp;
	private MotorPart mp;
	
	Aufhaengung ( double motorRotationTime, double phiDeg ) {
		double phi = phiDeg *Math.PI/180.0 ;
		wp = new WheelPart ( phi ) ;
		
		double rotationsPerMinute = 0.25 ;
		double rotationsPerSecond = rotationsPerMinute/60 ;
		double motorRotationsPerSecond = 1.0/motorRotationTime ;
		double ratio = motorRotationsPerSecond/rotationsPerSecond ;
		
		double motorEffectiveRadius = wp.effectiveRadius/ratio ;
		
		double tubeRadius = wp.tubeDiameter/2.0 ;
		double s = Math.sin( phi ) ;
		double innerRadius = motorEffectiveRadius-tubeRadius*(1.0/s-s) ;
		
		mp = new MotorPart ( innerRadius, phi, tubeRadius ); 
		
	}

	private Double getDrawing() {
		Path2D.Double path = new Path2D.Double () ;
		double motorAxisY = -(wp.effectiveRadius-mp.effectiveRadius) ;
		drawMotorPart ( path, motorAxisY ) ;
		drawWheelPart ( path ) ;
		return path ;
	}

	private void drawWheelPart(Double path) {
		double[][] centers = new double[3][2] ;
		
		centers[0][1] = -(wp.outerDiameter-wp.tubeDiameter )/2.0 ;
		centers[1][1] = -wp.innerTubeCenterRadius ;
		centers[2][1] = -wp.thrdTubeCenterRadius ;
		centers[2][0] = wp.thrdTubeCenterHorOffset ;
		for ( int i = 0  ; i < 3 ; i++ ) {
			Circle c = new Circle ( centers[i][0], centers[i][1], wp.tubeDiameter/2.0, 100 ) ;
			c.addTo(path);
		}
		for ( int i = 0  ; i < 3 ; i++ ) {
			drawIntermediateConnection ( path, centers[i], centers[(i+1)%3]) ;
		}
		/*
		double outerTubeCenter =-(wp.outerDiameter-wp.tubeDiameter )/2.0 ;
		Circle c = new Circle ( 0, outerTubeCenter, wp.tubeDiameter/2.0, 100 ) ;
		c.addTo( path );
		double innerTubeCenter =-wp.innerTubeCenterRadius ;
		c = new Circle ( 0, innerTubeCenter, wp.tubeDiameter/2.0, 100 ) ;
		c.addTo( path );
		double thrdTubeCenter =-wp.thrdTubeCenterRadius ;
		c = new Circle ( wp.thrdTubeCenterHorOffset, thrdTubeCenter, wp.tubeDiameter/2.0, 100 ) ;
		c.addTo( path );
		*/
	}

	private void drawIntermediateConnection(Double path, double[] c1, double[] c2) {
		double dx = c2[0]-c1[0] ;
		double dy = c2[1]-c1[1] ;
		double xDir = dx / wp.tubeDistance ;
		double yDir = dy / wp.tubeDistance ;
		double nX = -yDir ;
		double nY = xDir ;
		double dl = Math.sqrt( wp.tubeDiameter*wp.tubeDiameter/4.0 - wp.intermediateTubeRadius*wp.intermediateTubeRadius ) ;
		double x ;
		double y ;
		x = c1[0]+nX*wp.intermediateTubeRadius+xDir*dl ;
		y = c1[1]+nY*wp.intermediateTubeRadius+yDir*dl ;
		path.moveTo(x, y);
		x = c2[0]+nX*wp.intermediateTubeRadius-xDir*dl ;
		y = c2[1]+nY*wp.intermediateTubeRadius-yDir*dl ;
		path.lineTo(x, y);
		x = c1[0]-nX*wp.intermediateTubeRadius+xDir*dl ;
		y = c1[1]-nY*wp.intermediateTubeRadius+yDir*dl ;
		path.moveTo(x, y);
		x = c2[0]-nX*wp.intermediateTubeRadius-xDir*dl ;
		y = c2[1]-nY*wp.intermediateTubeRadius-yDir*dl ;
		path.lineTo(x, y);
		
		
	}

	private void drawMotorPart(Double path, double motorAxisY) {
		double h0 = mp.innerRadius ;
		double dx = mp.tubeRadius*2.0 ;
		double h1 = h0+dx/Math.tan( mp.phi ) ;
		path.moveTo( -dx, h1+motorAxisY ); 
		path.lineTo( 0, h0+motorAxisY ); 
		path.lineTo( dx, h1+motorAxisY ); 
		path.lineTo( dx,-h1+motorAxisY ); 
		path.lineTo( 0,-h0+motorAxisY ); 
		path.lineTo(-dx,-h1+motorAxisY ); 
		path.lineTo(-dx, h1+motorAxisY );
		path.moveTo( -dx, motorAxisY ); 
		path.lineTo(   dx, motorAxisY ); 
	}

	public static void main ( String[] args ) {
		double motorRotationTime = (1.65 + 3.3 )/2.0 ;
		Aufhaengung a = new Aufhaengung ( motorRotationTime, 45.0 ) ;
		Double path = a.getDrawing () ;
		PrintShape.print( path, false, true ) ;
		System.out.println( "done" );
	}

}
