package de.bitsnarts.wheel.pläne;

public class Wheel2 {
	public static void main ( String args[] ) {
		double achtelRad = 2.840 ;
		double radMass = achtelRad*8.0 ;
		double fuenfBilder = 3.400 ; // 3.030 ;
		double bildMasse = fuenfBilder/5.0 ;
		int numImages = 52 ;
		double bilderMasse = bildMasse*numImages ;
		double dreieckSeitenLänge = 0.15 ; // m
		double h = dreieckSeitenLänge*Math.sqrt( 3.0/4.0 ) ;
		double c = h/3.0 ;
		double shift = c*radMass/bilderMasse ;
		
		
		double umf = Math.PI*(5.0+0.002) ;
		double umf_4 = umf / 4.0 ;
		double bb = 13.0*0.3 ;
		double diff = (umf_4-bb)/13.0 ; 
		System.out.println ( "shift = "+shift ) ;
		
		double massRadiusBilder = 5.0 + 0.01 ;
		double segmentFactor = Math.sqrt( 0.5 )/(Math.PI/4.0) ;
		double schwerPktBilderViertel = massRadiusBilder*segmentFactor ;
		double schwerPktMitBilderViertel = (bilderMasse/4.0)*schwerPktBilderViertel/(bilderMasse/4.0+radMass) ;
		System.out.println ( "schwerpunkt rad mit bilder viertel = "+schwerPktMitBilderViertel ) ;
	}
}
