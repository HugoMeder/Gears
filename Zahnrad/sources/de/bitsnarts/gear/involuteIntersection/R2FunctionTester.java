package de.bitsnarts.gear.involuteIntersection;

import de.bitsnarts.gear.Curve;
import de.icido.math.utils.DerivationTester;

public class R2FunctionTester {

	private Curve f;
	private DT dt = new DT () ;
	
	class DT extends DerivationTester {

		private int coordIndex;
		private double coords[] = new double[2] ;
		
		void define ( int coordIndex ) {
			this.coordIndex = coordIndex ;
		}
		@Override
		protected double eval(double t) {
			f.eval(t, coords );
			return coords[coordIndex];
		}
		
	}
	
	public R2FunctionTester ( Curve f ) {
		this.f = f ;
	}
	
	public boolean testAt ( double t ) {
		double[] grad = new double[2] ;
		f.grad(t, grad);
		return testAt ( t, grad ) ;
		
	}
	public boolean testAt ( double t, double[] grad ) {
		dt.define( 0 );
		if ( !dt.testDerivation(t, 0.1, grad[0] ) ) {
			System.out.println( "derivation by x is wrong" );
			return false ;
		}
		dt.define( 1 );
		if ( !dt.testDerivation(t, 0.1, grad[1] ) ) {
			System.out.println( "derivation by y is wrong" );
			return false ;
		}
		return true ;
		
	}
}
