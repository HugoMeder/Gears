package de.bitsnarts.gear.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.Curve;
import de.bitsnarts.gear.Involute;
import de.bitsnarts.gear.Involute2;
import de.bitsnarts.gear.involuteIntersection.IntersectionComputer;
import de.bitsnarts.gear.involuteIntersection.NotConvergedException;
import de.bitsnarts.gear.parameters.DerivedInvoluteParams;
import de.bitsnarts.gear.parameters.GearParameters;
import de.bitsnarts.gear.parameters.InvoluteParameter;
import de.bitsnarts.gear.parameters.InvoluteParams2;

public class GearGeometry {

	private GearParameters gp;
	private InvoluteParameter invParams;
	private DerivedInvoluteParams derivParams;

	private Involute inv;
	private Involute2 inv2;
	private double invStart;
	private double invEnd;
	private double inv2Start;
	private double inv2End;
	private boolean gpathIsEmpty;
	private ArcByBezier arc1;
	private ArcByBezier arc2;
	private boolean failure;
	private Double path;
	private double[] t12;

	public GearGeometry ( GearParameters gp ) throws NotConvergedException {
		this.gp = gp ;
		invParams = new InvoluteParameter ( gp ) ;
		derivParams = new DerivedInvoluteParams ( invParams ) ;

		inv = new Involute ( derivParams ) ;
		
		initPath () ;
	}

	private void initPath() throws NotConvergedException {

		double ra = gp.da/2.0 ;
		double rf = gp.df/2.0 ;
		double rMin = inv.minR() ;
		double tf = 0 ;
		if ( rf < rMin ) {
			//throw new Error ( "rf < rMin" ) ;
			//rf = rMin ;
			//tf = inv.minT() ;
			//tf = initInvolute2 () ;
			try {
				tf = initInvolute2Vertices () ;
				rf = inv.rForT( tf ) ;				
			} catch ( NotConvergedException e ) {
				failure = true ;
			}
			if ( false ) {
				failure = true ;
				return ;			
			}
		} else {
			tf = inv.tForR(rf) ;			
		}
		double ta = inv.tForR(ra) ;
		initEdgeCurve ( ta, tf ) ;
		/*
		arcN = ec.length-1 ;
		double phi2 =-phi ( ec[0] ) ;
		double phi1 = phi ( ec[arcN] ) ;
		if ( phi2 < phi1 ) {
			phi2 += 2.0*Math.PI ;
		}
		double deltaPhi = Math.PI*2.0/gp.z ;
		double dPhi = deltaPhi*gp.xi ; 
		for ( int i = 0 ; i < gp.z ; i++ ) {
			double phi = deltaPhi*i-Math.PI/2.0 ;
			addEdgeCurve ( ec, i, true ) ;
			//addArc ( rf, phi+phi2, phi+dPhi-phi2 ) ;
			addEdgeCurve ( ec, i, false ) ;
		}
		gpath.closePath();
		*/
	}

	private double initInvolute2Vertices() throws NotConvergedException {
		InvoluteParams2 ip2 = new InvoluteParams2 () ;
		ip2.omega = derivParams.omega ;
		ip2.v = invParams.v ;
		double r = gp.d/2.0 ;
		double ha = (gp.da-gp.d)/2 ;
		double hf = (gp.d-gp.df)/2 ;
		ip2.y0 = -(r-hf) ;
//		ip2.y0 = -gp.df/2.0 ;
		ip2.x0 = (1.0/Math.tan( gp.phi ))*ha ;
		inv2 = new Involute2 ( ip2 ) ;
		int segs = 2000 ;
		double t0 = inv2.getIntersectionT ( inv ) ;
		
		t12 = new double[2] ;
		
		t12[0] = inv.minT()+0.5*(Math.PI*2.0/gp.z)/derivParams.omega ;
		t12[1] = inv2.minT() - (Math.PI*2.0/gp.z)/derivParams.omega ;;
		if ( false )
			return 0;
		
		boolean useiscmpt = true ;
		new IntersectionComputer ( inv, inv2, t12, 1e-10 ) ;
		t0 = t12[1] ;
		double t1 = 1 ;
		double dt = (t1-t0)/segs ;
		double[] xy = new double[2] ;
		double nphi = gp.p*gp.xi/gp.d ;
		double nx = -Math.sin ( nphi ) ;
		double ny = Math.cos ( nphi ) ;
		double lastD =-gp.da ;
		double endT = 0.0 ;
		for ( int i = 0 ; i <= segs ; i++ ) {
			double t = t0+dt*i ;
			inv2.eval(t, xy);
			double d = xy[0]*nx+xy[1]*ny ;
			if ( d < lastD )
				break ;
			lastD = d ;
			endT = t ;
		}
		inv2Start = t0 ;
		inv2End = endT ;
		if ( useiscmpt )
			return t12[0] ;
		else
			return t0 ;
	}

	private void initEdgeCurve(double ta, double tf) {
		invStart = ta ;
		invEnd = tf ;
	}
	
	public Path2D.Double createPath ( int numSegs1, int numSegs2 ) {
		if ( failure )
			return createInvolutes ( numSegs1, numSegs2 ) ;
		Double rv = new Path2D.Double () ;
		arc1 = new ArcByBezier ( inv, invStart, invEnd, numSegs1 ) ;
		if ( inv2 != null ) {
			arc2 = new ArcByBezier ( inv2, inv2Start, inv2End, numSegs2 ) ;
		}
		double phi2 =-phi ( arc1.getStartVertex() ) ;
		double phi1 = phi ( arc1.getEndVertex() ) ;
		if ( arc2 != null ) {
			phi1 = phi ( arc2.getEndVertex() ) ;
		}
		if ( phi2 < phi1 ) {
			phi2 += 2.0*Math.PI ;
		}
		gpathIsEmpty = true ;
		for ( int i = 0 ; i < gp.z ; i++ ) {
			addEdgeCurve ( rv, i, true ) ;
			addEdgeCurve ( rv, i, false ) ;
		}
		rv.closePath();
		path = rv ;
		return rv ;
	}

	private Double createInvolutes(int numSegs1, int numSegs2) {
		Double rv = new Path2D.Double () ;
		arc1 = new ArcByBezier ( inv, -40, +40, numSegs1 ) ;
		arc2 = new ArcByBezier ( inv2,-40, +40, numSegs2 ) ;
		arc1.add( rv, 0, true, false, true, false);			
		arc2.add( rv, 0, true, false, true, false);			
		return rv ;
	}

	private void addEdgeCurve(Double path, int toothNumber, boolean rising ) {
		double phi ;
		
		double deltaPhi = Math.PI*2.0/gp.z ; 
		phi = deltaPhi*toothNumber ;
		if ( !rising ) {
			phi += deltaPhi*gp.xi ;
		}
		// 	public void add ( Path2D.Double path, double phi, boolean reverse, boolean hmirror, boolean addFirstPoint, boolean lineToFirstPoint ) {
		if ( !rising ) {
			arc1.add( path, phi, rising, !rising, true, !gpathIsEmpty);
			gpathIsEmpty = false ;
			if ( arc2 != null ) {
				arc2.add( path, phi, rising, !rising, true, true);			
			}			
		} else {
			if ( arc2 != null ) {
				arc2.add( path, phi, rising, !rising, true, !gpathIsEmpty);			
				gpathIsEmpty = false ;
			}						
			arc1.add( path, phi, rising, !rising, true, !gpathIsEmpty);
			gpathIsEmpty = false ;
		}
		/*
		arc1.add( path, phi, !rising, rising, true, true );
		*/
		/*
		c = Math.cos( phi ) ;
		s = Math.sin( phi ) ;			
		int n = arc.length ;
		if ( rising ) {
			for ( int i = n-1 ; i >= 0 ; i-- ) {
				double x = arc[i][0] ;
				double y = arc[i][1] ;
				double x_ = x*c + s*y ;
				double y_ = y*c - s*x ;
				if ( gpathIsEmpty ) {
					gpathIsEmpty = false ;
					gpath.moveTo( x_,  y_ );
				} else {
					gpath.lineTo( x_,  y_ );				
				}
			}
		} else {
			for ( int i = 0 ; i < n ; i++ ) {
				double x =-arc[i][0] ;
				double y = arc[i][1] ;
				double x_ = x*c + s*y ;
				double y_ = y*c - s*x ;
				if ( gpathIsEmpty ) {
					gpathIsEmpty = false ;
					gpath.moveTo( x_,  y_ );
				} else {
					gpath.lineTo( x_,  y_ );				
				}
			}
		}
		*/
	}


	private double phi(double[] vertex ) {
		return Math.atan2( vertex[0],-vertex[1] ) ;
	}

	public void draw(Graphics2D gr) {
		if ( path == null )
			path = createPath ( 10, 10 ) ;
		if ( failure )
			drawFailed ( gr ) ;
		else
			gr.draw( path );
	}

	private void drawFailed(Graphics2D gr) {
		Color red = new Color ( 1f, 0f, 0f ) ;
		Color green = new Color ( 0f, 1f, 0f ) ;

		drawPartition ( gr, inv, -40, 40, t12[0], 10, red, green ) ;
		drawPartition ( gr, inv2, -40, 40, t12[1], 10, red, green ) ;
		/*
		arc1 = new ArcByBezier ( inv, -40, +40, 10 ) ;
		Double p1 = new Path2D.Double() ;
		arc1.add( p1, 0, true, false, true, false);
		gr.setColor( red );
		gr.draw( p1 );

		Double p2 = new Path2D.Double() ;
		arc2 = new ArcByBezier ( inv2,-40, +40, 10 ) ;
		arc2.add( p2, 0, true, false, true, false);			
		gr.setColor( green );
		gr.draw( p2 );
		*/
	}

	private void drawPartition(Graphics2D gr, Curve c, double t0, double t1, double t, int segs, Color red, Color green) {
		if ( (t0 < t && t < t1) || (t0 > t && t > t1) ) {
			drawSigned ( gr, c, t0, t, segs, red, green ) ;
			drawSigned ( gr, c, t1, t, segs, red, green ) ;
		} else {
			Color col ;
			if ( t0 < t1 ) {
				if ( t1 < t ) {
					col = red ;
				} else {
					col = green ;
				}
			} else {
				if ( t0 > t ) {
					col = green ;
				} else {
					col = red ;
				}				
			}
			ArcByBezier arc = new ArcByBezier ( c, t0, t1, segs ) ;
			Double p = new Path2D.Double() ;
			arc.add( p, 0, true, false, true, false);
			gr.setColor( col );
			gr.draw( p );
		}
	}

	private void drawSigned(Graphics2D gr, Curve c, double t0, double t1, int segs, Color red, Color green) {
		ArcByBezier arc = new ArcByBezier ( c, t0, t1, segs ) ;
		Double p = new Path2D.Double() ;
		arc.add( p, 0, true, false, true, false);
		if ( t0 < t1 )
			gr.setColor( red );
		else
			gr.setColor( green );
		gr.draw( p );
	}


}
