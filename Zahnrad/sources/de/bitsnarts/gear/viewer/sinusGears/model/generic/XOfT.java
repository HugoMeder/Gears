package de.bitsnarts.gear.viewer.sinusGears.model.generic;

import java.util.TreeSet;

import de.bitsnarts.gear.viewer.sinusGears.RealFunction;
import de.bitsnarts.gear.viewer.sinusGears.model.AbstractRackFunction;

public class XOfT {

	private AbstractRackFunction arf;
	private TOfX tofX;
	private RealFunction tofXDer;
	private double p;
	private MonotoneSegment[] segs;
	private double v;
	private double tau;

	class MonotoneSegment {
		
		private double x1;
		private double x2;
		private double t1;
		private double t2;
		private double tmin;
		private double tmax;
		private boolean reverted;

		MonotoneSegment ( double xMin, double xMax ) {
			this.x1 = xMin ;
			this.x2 = xMax ;
			this.t1 = tofX.eval( x1 ) ;
			this.t2 = tofX.eval( x2 ) ;
			if ( false ) {
				double d = tofXDer.eval( x1 ) ;
				if ( d*d > 1e-20 )
					throw new Error ( "d*d > 1e-20" ) ;
				d = tofXDer.eval( x2 ) ;
				if ( d*d > 1e-20 )
					throw new Error ( "d*d > 1e-20" ) ;
			}
			if ( t1 < t2 ) {
				tmin = t1 ;
				tmax = t2 ;
				reverted = false ;
			} else {
				tmin = t2 ;
				tmax = t1 ; 				
				reverted = true ;
			}
		}

		public void getXVals(TreeSet<Double> rv, double t) {
			int index = 0 ;
			
			while ( t > tmin ) {
				t -= tau ;
				index++ ;
			}
			while ( t < tmin ) {
				t += tau ;
				index-- ;
			}
			if ( t == tmax ) {
				if ( !reverted )
					rv.add( x2-index*p ) ;
				else
					rv.add( x1-index*p ) ;
				return ;
			}
			if ( t == tmin ) {
				if ( !reverted )
					rv.add( x1-index*p ) ;
				else
					rv.add( x2-index*p ) ;
				return ;
			}
			while ( t < tmax ) {
				double x = (x1+x2)/2.0 ;
				for (;;) {
					double res = tofX.eval( x )-t ;
					if ( res*res < 1e-20 ) {
						rv.add( x-index*p ) ;
						break ;
					}
					double dx = -res/tofXDer.eval(x) ;
					double new_x = x+dx ;
					if ( new_x <= x1 ) {
						x = (x+x1)/2 ;
					} else if ( new_x >= x2 ) {
						x = (x+x2)/2 ;
					} else {
						x = new_x ;
					}
				}
				t += tau;
				index-- ;
			}
		}
		
	}
	public XOfT ( AbstractRackFunction arf ) {
		this.arf = arf ;
		init () ;
	}

	private void init() {
		tofX = arf.getTOfX() ;
		tofXDer = tofX.derivative() ;
		p = arf.getP () ;
		v = arf.getV () ;
		tau = p/v ;
		double[][] stat = arf.getStationaryTOfXIntervals() ;
		if ( stat == null || stat.length == 0 )
			throw new Error ( "stat == null || stat.length == 0" ) ;
		int n = stat.length ;
		segs = new MonotoneSegment[n] ;
		for ( int i = 0 ; i < n-1 ; i++ ) {
			segs[i] = new MonotoneSegment ( stat[i][1], stat[i+1][0] ) ;
		}
		segs[n-1] = new MonotoneSegment ( stat[n-1][1], stat[0][0]+p ) ; 
	}
	
	public TreeSet<Double> getXVals ( double t ) {
		TreeSet<Double> rv = new TreeSet<Double> () ;
		for ( MonotoneSegment s : segs ) {
			s.getXVals ( rv, t ) ;
		}
		return rv ;
	}
}
