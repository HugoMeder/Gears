package de.bitsnarts.gear.involuteIntersection;

import de.bitsnarts.gear.Involute;
import de.bitsnarts.gear.Involute2;

public class IntersectionComputer {
	
	private Involute inv;
	private Involute2 inv2;
	private Intersection inters;
	private NewtonStepper newtonStepper;
	private static boolean testDerivs = false ;
	
	public IntersectionComputer ( Involute inv, Involute2 inv2, double t[], double err ) throws NotConvergedException {
		this.inv = inv ;
		this.inv2 = inv2 ;
		inters = new Intersection ( inv, inv2 ) ;
		if ( testDerivs && !inters.testGradients ( t.clone() ) )
			throw new NotConvergedException ( "gradient wrong" ) ;
		newtonStepper = new NewtonStepper ( inters, testDerivs ) ;
		iterate ( t, err ) ;
	}

	private void iterate(double[] t, double err) throws NotConvergedException {
		int iter = 1 ;
		do {
			/*if ( iter == 3 ) {
				System.out.println ( "!!! iter "+iter ) ;				
			}
			System.out.println ( "iter "+iter ) ;
			*/
			if ( testDerivs ) {
				if ( !inters.testGradients ( t.clone() ) ) {
					System.out.println( "error in iteration "+iter ) ;
					throw new NotConvergedException ( "gradient wrong" ) ;
				}
			}
			newtonStepper.step( t );
			if ( testDerivs ) {
				System.out.println( "t1 "+t[0]+", t2 "+t[1]);				
			}
			iter++ ;
		} while ( newtonStepper.lastMerit() > err*err ) ;
	}
}
