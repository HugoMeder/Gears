package de.bitsnarts.gear.parameters;

public class ThetaByPhi {

	static double f ;
	
	static double theta ( double phi ) {
		return Math.atan( f*Math.sin( phi )/(1.0+f*Math.cos( phi )) )  ;
	}
	
	public static double lambdaDotByVFromPhi ( double phi ) {
		double theta = Math.PI/2.0-phi ;
		double alpha = Math.PI/2.0 ;
		return -Math.sin( theta ) / Math.sin( alpha ) ;		
	}
	
	public static double lambdaDotByVFromPhiOld ( double phi ) {
		double theta = Math.PI/2.0-phi ;
		double alpha = Math.PI/2.0+2.0*theta ;
		return Math.sin( theta ) / Math.sin( alpha ) ;		
	}
	
	public static void main ( String args[] ) {
		double theta = 20.0*Math.PI/180.0 ;
		double phi = Math.PI/2.0-theta ;
		f = lambdaDotByVFromPhi ( phi ) ;
		double theta2 = theta ( phi ) ;
		System.out.println( "theta "+theta+", theta2 "+theta2 );
	}
}
