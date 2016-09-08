package de.bitsnarts.wheel.pläne;

public class Wheel {
	public static void main ( String args[] ) {
		double massperLength = 1.156/(2.0*0.3) ; //kg/m
		double diameter = 10.0 ; //m ;
		double r = diameter/2.0 ;
		double circumference = Math.PI*2.0*r ;
		double numImages = circumference/0.3 ;
		double totalMass = circumference*massperLength ;
		double g = 9.81 ; // m/s^2
		double newtonPerKg = g ;
		double totalForce = newtonPerKg*totalMass ;//N
		double maxStressTension = 50.0*1e6 ;//Pa
		double critArea = totalForce/maxStressTension ;
		double circRadius = Math.sqrt ( critArea/(Math.PI*2.0) ) ;
		System.out.println ( "done" ) ;
	}
}
