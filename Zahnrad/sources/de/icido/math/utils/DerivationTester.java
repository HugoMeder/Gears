package de.icido.math.utils;
// SHA1 File Hash, BEGIN
// hash=gbEN9EYUdxI9cmaAcuBz0QrKG6EA
//     date=Sat Jan 19 19:27:22 CET 2013
//     svn log comment=physics prototype: java2cpp, checkin of hashes without history
//     svn revision=23302
// hash=AKcTFIg9tpJPVZr2CbnxUO84TXGA
//     date=Tue Oct 22 13:20:47 CEST 2013
//     svn log comment=(TAG) new tags (mods due to import icido.Math)
//     svn revision=27627
// hash=7d-5z5Xs9ceYzi_1fUdhXBEcwOOA
//     date=Thu Aug 21 18:30:57 CEST 2014
//     svn log comment=(TAG) an even better working version
//     svn revision=31508
// SHA1 File Hash, END


/**
 * From Numerical Recipes: The Art of Scientific Computing, see <a href="http://www.nr.com/" target="_blank">www.nr.com</a>
 */
public abstract class DerivationTester {
	
	static double epsylonRound = getEpsylonRound () ;
	static double epsylonRoundSqrt = Math.sqrt( epsylonRound ) ;
	static final int ntab=10;
	static final double con=1.4 ;
	static final double con2=(con*con);
	static final double big=Double.MAX_VALUE ;
	static final double safe=2.0;
	private double calculated;
	private double estimatedError;
	private int dfridrLoops;
	private double[][] a = new double[ntab][ntab];
	private double analyticalDerivation;
	
	public boolean testDerivation ( double x, double scale, double analyticalDerivation ) {

		this.analyticalDerivation = analyticalDerivation ;
		double x0 = eval ( x ) ;
		dfridr ( x, scale ) ;
		int loopCntr = 0 ;
		double abs = (Math.abs( analyticalDerivation )+Math.abs( calculated ))/2.0 ;
		//while ( estimatedError > 1E-8 && estimatedError/abs > 1E-8 ) {
		while ( estimatedError > 1E-8 && 
				( estimatedError/abs > 1E-8 ||
						analyticalDerivation != 0.0 && estimatedError/Math.abs( analyticalDerivation ) > 1E-3
				)
			) {
			if ( loopCntr == 6 ) {
				double absError = Math.abs( analyticalDerivation-calculated ) ;
				if ( absError == 0.0 )
					return true ;
				double relError = absError*2.0/(Math.abs(analyticalDerivation)+Math.abs(calculated)) ;
				if ( relError < 1e-5 )
					return true ;
				return false ;
			}
			loopCntr++ ;
			scale /= 10  ;
			dfridr ( x, scale ) ;
			abs = (Math.abs( analyticalDerivation )+Math.abs( calculated ))/2.0 ;
		}
		double err = Math.abs( analyticalDerivation-calculated ) ;
		double relerr = Math.abs( err )/estimatedError ;
		if ( estimatedError == 0 || relerr > 100.0 ) {
			double relerr2 = Math.abs(err) / abs ;
			if ( Math.abs( err ) < 1E-8 || relerr2 < 0.01 )
				return true ;
			else {
				x0 = Math.abs ( x0 ) ;
				double v = err/x0 ;
				if ( x0 > 0 && v < 1E-10 )
					return true ;
				return false ;
			}
		}
		//System.out.println ( "scale, "+scale+", estimated error "+estimatedError+", relerror "+relerr+", loops "+dfridrLoops ) ;
		return true ;
	}

	
	public double calculatedValue () {
		return calculated ;
	}
	
	public double getExpectedValue () {
		return this.analyticalDerivation ;
	}
	
	private static double getEpsylonRound() {
		double e = Double.longBitsToDouble( 0x3ff0000000000001L ) ;
		e = e-1.0 ;
		return e;
	}

	void dfridr( double x, double h )
	{
		double err ;
		
		int i,j;
		double errt,fac,hh,ans=0;
		if (h == 0.0) throw new Error ("h must be nonzero in dfridr.");
		hh=h;
		a[0][0]=(eval(x+hh)-eval(x-hh))/(2.0*hh);
		err=big;
		for (i=1;i<ntab;i++) {
			hh /= con;
			a[0][i]=(eval(x+hh)-eval(x-hh))/(2.0*hh);
			fac=con2;
			for (j=1;j<=i;j++) {
				a[j][i]=(a[j-1][i]*fac-a[j-1][i-1])/(fac-1.0);
				fac=con2*fac;
				errt=Math.max(Math.abs(a[j][i]-a[j-1][i]),Math.abs(a[j][i]-a[j-1][i-1]));
				if (errt <= err) {
					err=errt;
					ans=a[j][i];
				}
			}
			dfridrLoops = i ;
			if (Math.abs(a[i][i]-a[i-1][i-1]) >= safe*err) break;
		}
		calculated = ans ;
		estimatedError = err ;
	}

	protected abstract double eval(double x) ;
}
