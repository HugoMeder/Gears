package de.bitsnarts.gear.involuteIntersection;

import de.bitsnarts.gear.Involute;
import de.bitsnarts.gear.Involute2;

public class Intersection implements R2function {

	private Involute inv;
	private Involute2 inv2;
	private double[] val;
	private double[] val2;

	public Intersection ( Involute inv, Involute2 inv2 ) {
		this.inv = inv ;
		this.inv2 = inv2 ;
		val = new double[2] ;
		val2 = new double[2] ;
	}

	@Override
	public void eval(double[] t, double[] v ) {
		inv.eval(t[0], val );
		inv2.eval(t[1], val2 );
		v[0] = val[0]-val2[0] ;
		v[1] = val[1]-val2[1] ;
	}

	@Override
	public void jacobi(double[] t, double[][] J) {
		inv.grad ( t[0], val ) ;
		inv2.grad ( t[1], val2 ) ;
		J[0][0] = val[0] ;
		J[1][0] = val[1] ;
		J[0][1] =-val2[0] ;
		J[1][1] =-val2[1] ;
	}

	public boolean testGradients( double t12[] ) {
		double[] grad = new double[2] ;
		R2FunctionTester t = new R2FunctionTester ( inv ) ;
		inv.grad( t12[0], grad);
		if ( !t.testAt( t12[0], grad) ) {
			System.out.println ( "inv grad wrong" ) ;
			return false ;
		}
		t = new R2FunctionTester ( inv2 ) ;
		inv2.grad( t12[0], grad);
		if ( !t.testAt( t12[0], grad) ) {
			System.out.println ( "inv2 grad wrong" ) ;
			return false ;
		}
		System.out.println ( "gradients OK" ) ;
		return true ;
	}

}
