package de.icido.math.utils;
// SHA1 File Hash, BEGIN
// hash=U-4TYylEZ1d40KcJd_8-F0x3JvNA
//     date=Sat Jan 19 19:27:22 CET 2013
//     svn log comment=physics prototype: java2cpp, checkin of hashes without history
//     svn revision=23302
// hash=T-SdsqL7q6GyxBN3pcS1WbHO3bPA
//     date=Tue Oct 22 13:20:47 CEST 2013
//     svn log comment=(TAG) new tags (mods due to import icido.Math)
//     svn revision=27627
// hash=mFYlJoPDCtJfTX-nmQ--LCB31LNA
//     date=Thu Feb 26 12:25:59 CET 2015
//     svn log comment=(TAG) hashtags
//     svn revision=33628
// SHA1 File Hash, END


/**
 * <h3>Line Search Algorithm</h3>
 * <p>
 * Given a function f(lambda) and f0 = f(0), f0_dot = derivation at zero, f0_dot < 0.
 * </p>
 * <p>
 * Find a lambda, lambdaMin < lambda <= 1.0, such that f(lambda)< f0 + f0_dot*lambda*alpha
 * </p>
 * @author hum
 *
 */
public abstract class LineSearch {
	
	private final double alpha;
	private final double l;
	private double u0;//upper bound for first iteration
	private double lambdaMin;
	private int maxIter;
	private int n;
	private String doDerviationTest;
	private double derviationTestDeltaLambda;
	
	class DT extends DerivationTester {

		@Override
		protected double eval(double x) {
			return getLambda ( x );
		}
		
	}
	
	public void enableDerivationTestForNextGetLambda ( String printSuccessComment ) {
		enableDerivationTestForNextGetLambda ( printSuccessComment, 0.1 ) ;
	}
	
	public void enableDerivationTestForNextGetLambda(String printSuccessComment, double derviationTestDeltaLambda) {
		doDerviationTest = printSuccessComment ;
		this.derviationTestDeltaLambda = derviationTestDeltaLambda ;
	}

	
	public LineSearch () {
		this ( 1e-4, 0.1, 0.001, 10 ) ;
	}
	
	public LineSearch ( double lambdaMin ) {
		this ( 1e-4, 0.1, lambdaMin, 10 ) ;
	}
	
	/**
	 * 
	 * @param alpha
	 * @param l lower bound: lambda[k+1] >= l*lambda[k], typically 0.1 
	 * @param lambdaMin
	 * @param maxIter
	 */
	public LineSearch ( double alpha, double l, double lambdaMin, int maxIter ) {
		this.alpha = alpha ;
		this.l = l ;
		this.u0 = 0.5/(1-alpha) ;
		this.lambdaMin = lambdaMin ;
		this.maxIter = maxIter ;
	}
	
	/**
	 * 
	 * @param f0 value at lambda = 0
	 * @param f0_dot derivation at lambda = 0
	 * @return =0 : maxIter exceeded or lambda < lambdaMin<br>>0 a valid lambda otherwise
	 */
	
	public double getLambda ( double f0, double f0_dot ) {
		if ( doDerviationTest != null ) {
			DT dt = new DT () ;
			String txt = doDerviationTest ;
			doDerviationTest = null ;
			if ( dt.testDerivation( 0, derviationTestDeltaLambda, f0_dot) ) {
				System.out.println ( txt ) ;
			} else {
				throw new Error ( "Derivation Test failed in LineSearch.getLambda" ) ;
			}
		}
		if ( f0_dot >= 0.0 )
			throw new Error ( "f0_dot >= 0.0" ) ;
		double lambda = 1.0 ;
		double lastLambda = 0;
		double lastValue = 0 ;
		n = 0 ;
		for(;;) {
			double f = getLambda ( lambda ) ;
			if ( lambda <= lambdaMin )
				return 0 ;
			if ( f <= f0 + f0_dot*lambda*alpha )
				return lambda ;
			if ( n > maxIter )
				return 0 ;
			n++ ;
			double newLambda ;
			if ( n == 1 ) {
				// this is the first iteration, we model the function by a quadratic function
				// f(lambda) = (f-f0-f0_dot)*lambda^2+f0_dot*lambda+f0 ;
				// minimum is at :
				newLambda = -f0_dot/(2.0*(f-f0-f0_dot)) ;
				if ( newLambda > u0 )
					throw new Error ( "internal error (newLambda > u0)" ) ;
				if ( newLambda < l )
					newLambda = l ;
			} else {
				// we have two values, (lastLambda,lastValue) and (lambda,f) now
				// the function is modeled by a cubic function now
				// f(lambda) = a*lambda^3+b*lambda^2+f0_dot*lambda+f0
				double x1 = (f-f0-f0_dot*lambda)/(lambda*lambda) ;
				double x2 = (lastValue-f0-f0_dot*lastLambda)/(lastLambda*lastLambda) ;
				double d = lambda-lastLambda ;
				double a = (x1-x2)/d ;
				double b = (-lastLambda*x1+lambda*x2)/d ;
				if ( false ) {
					double e1 = ((a*lambda+b)*lambda+f0_dot)*lambda+f0 - f ;
					double e2 = ((a*lastLambda+b)*lastLambda+f0_dot)*lastLambda+f0 - lastValue ;
					if ( e1*e1+e2*e2 > 1e-10 )
						throw new Error ( "internal error (e1*e1+e2*e2 > 1e-20)" ) ;
				}
				newLambda = (-b+Math.sqrt(b*b-3.0*f0_dot*a))/(3.0*a) ;
				if ( false ) {
					double f_tmp = getLambda ( newLambda ) ;
					double e = (3.0*a*newLambda+2.0*b)*newLambda+f0_dot ;
					if ( e*e > 1e-10 )
						throw new Error ( "internal error (e*e > 1e-10)" ) ;
					if ( newLambda <= 0.0 || newLambda >= lambda )
						throw new Error ( "newLambda <= 0.0 || newLambda >= lambda" ) ;
				}
				if ( newLambda > 0.5*lambda )
					newLambda = 0.5*lambda ;
				else if ( newLambda < l*lambda )
					newLambda = l*lambda ;
				else {
					if ( false ) {
						double e = (3.0*newLambda*a+2.0*b)*newLambda+f0_dot ;
						if ( e*e > 1e-10 )
							throw new Error ( "internal error (>e*e > 1e-20)" ) ;
					}
				}
			}
			lastLambda = lambda ;
			lastValue = f ;
			lambda = newLambda ;
		}
	}
	
	public int getLastIterationCount () {
		return n ;
	}
	
	/**
	 * function to search
	 * @param lambda
	 * @return f(lambda)
	 */
	abstract public double getLambda( double  lambda ) ;
}
