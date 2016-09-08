package de.bitsnarts.gear.involuteIntersection;

import de.icido.math.utils.LineSearch;

public class NewtonStepper {
	
	private R2function f;
	private double[] res;
	private double[][] J;
	private double[][] JInv;
	private double m2;
	private double[] deltaT;
	private double[] t0;
	private double[] t;
	private LS ls = new LS () ;
	private Linesearch2 ls2 = new Linesearch2 () ;
	private boolean testDervs;

	class LS extends LineSearch {

		@Override
		public double getLambda(double lambda) {
			t[0] = t0[0]+lambda*deltaT[0]  ;
			t[1] = t0[1]+lambda*deltaT[1]  ;
			f.eval(t, res);
			m2 = merit() ;
			return m2 ;
		}
	}
	
	class Linesearch2 {
		double getLambda ( double m ) {
			double lambda = 1.0 ;
			f.eval ( t0, res ) ;
			double m0 = merit () ;
			int iter = 0 ;
			for(;;) {
				t[0] = t0[0]+lambda*deltaT[0]  ;
				t[1] = t0[1]+lambda*deltaT[1]  ;
				f.eval(t, res);
				m2 = merit() ;
				if ( m2 < m )
					return lambda ;
				lambda /= 2.0 ;
				iter++ ;
				if ( lambda == 0.0 || !Double.isFinite( lambda ) )
					return 0 ;
			}
		}
	}
	
	NewtonStepper ( R2function f, boolean testDerivs ) {
		this.f = f ;
		this.res = new double[2] ; 
		this.J = new double[2][2] ; 
		this.JInv = new double[2][2] ;
		this.t0 = new double[2] ;
		this.deltaT = new double[2] ;
		this.testDervs = testDerivs ;
	}
	
	void step ( double t[] ) throws NotConvergedException {
		t0[0] = t[0] ;
		t0[1] = t[1] ;
		this.t = t ;
		f.eval(t, res );
		double m1 = merit () ;
		//System.out.println( "m1 "+m1 );
		f.jacobi(t, J);
		invert () ; 
		deltaT[0] =-(JInv[0][0]*res[0]+JInv[0][1]*res[1]) ;
		deltaT[1] =-(JInv[1][0]*res[0]+JInv[1][1]*res[1]) ;
		/*
		f.eval(t, res );
		m2 = merit () ;
		if ( m2 > m1 )
			throw new Error ( "m2 > m1" ) ;
		*/
		//if ( testDervs )
		//	ls.enableDerivationTestForNextGetLambda( "derivation test OK" );
		double lambda = ls.getLambda( m1, -m1*2.0 ) ;
		if ( lambda == 0 ) {
			printJacobi () ;
			lambda = ls2.getLambda( m1 ) ;
			if ( lambda == 0 )
				throw new NotConvergedException ( "lambda == 0" ) ;
			System.out.println( "lambda "+lambda );
		}
	}

	private void printJacobi() {
		System.out.println( "Jacobi" ) ;
		System.out.println( "\t"+J[0][0]+" "+J[0][1]); 
		System.out.println( "\t"+J[1][0]+" "+J[1][1]);
		System.out.println( "\tdet "+(J[0][0]*J[1][1]-J[1][0]*J[0][1]));
	}

	private double merit() {
		return (res[0]*res[0]+res[1]*res[1])/2.0 ;
	}

	private void invert() {
		double det = J[0][0]*J[1][1]-J[1][0]*J[0][1] ;
		JInv[0][0] = J[1][1]/det ;
		JInv[1][1] = J[0][0]/det ;
		JInv[0][1] =-J[0][1]/det ;
		JInv[1][0] =-J[1][0]/det ;
	}
	
	double lastMerit () {
		return m2 ;
	}
}
