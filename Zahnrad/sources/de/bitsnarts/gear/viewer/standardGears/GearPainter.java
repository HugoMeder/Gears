package de.bitsnarts.gear.viewer.standardGears;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D.Double;
import java.util.Vector;

import de.bitsnarts.gear.Involute;
import de.bitsnarts.gear.Involute2;
import de.bitsnarts.gear.involuteIntersection.IntersectionComputer;
import de.bitsnarts.gear.involuteIntersection.NotConvergedException;
import de.bitsnarts.gear.parameters.DerivedInvoluteParams;
import de.bitsnarts.gear.parameters.GearParameters;
import de.bitsnarts.gear.parameters.InvoluteParameter;
import de.bitsnarts.gear.parameters.InvoluteParams2;
import de.bitsnarts.gear.viewer.AbstractWheelPainter;

public class GearPainter extends AbstractWheelPainter {

	private InvoluteParameter invParams;
	private DerivedInvoluteParams derivParams;
	private GearParameters gp;
	private Involute inv;
	private double t1;
	private double t2;
	private Double gpath;
	private Double involute2;
	private boolean gpathIsEmpty;
	private Vector<double[]> involute2Vertices;

	public GearPainter( GearParameters gp, double v ) throws NotConvergedException {
		super( v/(gp.d/2.0) );
		this.gp = gp ;
		invParams = new InvoluteParameter ( gp ) ;
		derivParams = new DerivedInvoluteParams ( invParams ) ;

		inv = new Involute ( derivParams ) ;
		
		initPath () ;
	    //initPathArc () ;
	}

	private void initPath() throws NotConvergedException {
		double ra = gp.da/2.0 ;
		double rf = gp.df/2.0 ;
		double rMin = inv.minR() ;
		double tf ;
		if ( rf < rMin ) {
			//throw new Error ( "rf < rMin" ) ;
			//rf = rMin ;
			//tf = inv.minT() ;
			tf = initInvolute2 () ;
			tf = initInvolute2Vertices () ;
			rf = inv.rForT( tf ) ;
		} else {
			tf = inv.tForR(rf) ;			
		}
		double ta = inv.tForR(ra) ;
		gpath = new GeneralPath.Double () ;
		gpathIsEmpty = true ;
		int arcN = 2000 ;
		double[][]ec = initEdgeCurve ( ta, tf, arcN ) ;
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
	}

	private double initInvolute2() throws NotConvergedException {
		InvoluteParams2 ip2 = new InvoluteParams2 () ;
		ip2.omega = derivParams.omega ;
		ip2.v = invParams.v ;
		double r = gp.d/2.0 ;
		double ha = (gp.da-gp.d)/2 ;
		double hf = (gp.d-gp.df)/2 ;
		ip2.y0 = -(r-hf) ;
//		ip2.y0 = -gp.df/2.0 ;
		ip2.x0 = (1.0/Math.tan( gp.phi ))*ha ;
		Involute2 iv2 = new Involute2 ( ip2 ) ;
		involute2 = new GeneralPath.Double () ;
		int segs = 2000 ;
		double t0 = iv2.getIntersectionT ( inv ) ;
		
		double[] t12 = new double[2] ;
		t12[0] = inv.minT()/2.0 ;
		t12[1] = inv.minT() ;
		boolean useiscmpt = true ;
		IntersectionComputer ic ;
		if ( useiscmpt ) {
			ic = new IntersectionComputer ( inv, iv2, t12, 1e-10 ) ;
			t0 = t12[1] ;
		}
		double t1 = 1 ;
		double dt = (t1-t0)/segs ;
		double[] xy = new double[2] ;
		double nphi = gp.p*gp.xi/gp.d ;
		double nx = -Math.sin ( nphi ) ;
		double ny = Math.cos ( nphi ) ;
		double lastD =-gp.da ;
		for ( int i = 0 ; i <= segs ; i++ ) {
			double t = t0+dt*i ;
			iv2.eval(t, xy);
			double d = xy[0]*nx+xy[1]*ny ;
			if ( d < lastD )
				break ;
			lastD = d ;
			if ( i == 0 ) {
				involute2.moveTo( xy[0], xy[1] );
			} else {
				involute2.lineTo( xy[0], xy[1] );				
			}
		}
		if ( useiscmpt )
			return t12[0] ;
		else
			return t0 ;
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
		Involute2 iv2 = new Involute2 ( ip2 ) ;
		int segs = 2000 ;
		double t0 = iv2.getIntersectionT ( inv ) ;
		
		double[] t12 = new double[2] ;
		t12[0] = inv.minT()/2.0 ;
		t12[1] = inv.minT() ;
		boolean useiscmpt = true ;
		IntersectionComputer ic ;
		if ( useiscmpt ) {
			ic = new IntersectionComputer ( inv, iv2, t12, 1e-10 ) ;
			t0 = t12[1] ;
		}
		double t1 = 1 ;
		double dt = (t1-t0)/segs ;
		double nphi = gp.p*gp.xi/gp.d ;
		double nx = -Math.sin ( nphi ) ;
		double ny = Math.cos ( nphi ) ;
		double lastD =-gp.da ;
		Vector<double[]> verts = new Vector<double[]> () ;
		for ( int i = 0 ; i <= segs ; i++ ) {
			double[] xy = new double[2] ;
			double t = t0+dt*i ;
			iv2.eval(t, xy);
			double d = xy[0]*nx+xy[1]*ny ;
			if ( d < lastD )
				break ;
			lastD = d ;
			verts.add( xy ) ;
		}
		involute2Vertices = verts ; 
		if ( useiscmpt )
			return t12[0] ;
		else
			return t0 ;
	}

	private void addArc(double r, double phi1, double phi2 ) {
		int segs = 1 ;
		double dphi = (phi2-phi1)/segs ;
		for ( int i = 0 ; i < segs ; i++ ) {
			double phi = dphi*i ;
			gpath.lineTo(r*Math.sin ( phi ), r*Math.cos( phi ));
		}
	}

	private void addEdgeCurve(double[][] arc, int toothNumber, boolean rising ) {
		double phi ;
		double c ;
		double s ;
		
		double deltaPhi = Math.PI*2.0/gp.z ; 
		phi = deltaPhi*toothNumber ;
		if ( !rising ) {
			phi += deltaPhi*gp.xi ;
		}
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
	}

	private double phi(double[] vertex ) {
		return Math.atan2( vertex[0],-vertex[1] ) ;
	}

	private double[][] initEdgeCurve(double t1, double t2, int numSegs ) {
		double[][] rv = new double[numSegs+1][2] ;
		double dt = (t2-t1)/numSegs ;
		for ( int i = 0 ; i <= numSegs ; i++ ) {
			double t = t1+i*dt ;
			inv.eval(t, rv[i]);
		}
		if ( involute2Vertices != null ) {
			rv = addVertices ( rv, involute2Vertices ) ;
		}
		return rv ;
	}

	private double[][] addVertices( double[][] orig, Vector<double[]> vertices) {
		int n_orig = orig.length ;
		int n_verts = vertices.size() ;
		double[][] rv = new double[n_orig+n_verts-1][] ;
		int index = 0 ;
		for ( int i = 0 ; i < n_orig ; i++ ) {
			rv[index++] = orig[i] ;
		}
		for ( int i = 1 ; i < n_verts ;i++ ) {
			rv[index++] = vertices.get( i ) ;
		}
		return rv;
	}

	@Override
	protected void paint(Graphics2D gr) {
		gr.draw ( gpath ) ;
		if ( involute2 != null ) {
			gr.draw( involute2 );
		}
	}

    public	DerivedInvoluteParams getDerivedParameters () {
		return derivParams ;
	}
    
	private void initPathArc() {
		System.out.println( "ra = "+gp.da/2.0 ); 
		System.out.println( "rf = "+gp.df/2.0 );
		double h = (gp.da-gp.d)/2.0 ;
		double r2 = gp.d/2.0-h ;
		System.out.println( "r2 = "+r2 );
		double minR = inv.minR () ;
		System.out.println( "minR = "+minR );
		
		double ta = inv.tForR(gp.da/2.0) ;
		System.out.println( "ta = "+ta );
		double ra_ = inv.rForT( ta ) ;
		System.out.println( "ra_ = "+ra_ );
		
		double tf = inv.tForR(gp.df/2.0) ;
		System.out.println( "tf = "+tf );
		double rf_ = inv.rForT( tf ) ;
		System.out.println( "rf_ = "+rf_ );
		
		t1 = tf ;
		t2 = ta ;
		
		gpath = new GeneralPath.Double () ;
		/*
		gp.moveTo( 0,  0 );
		gp.lineTo( 1.0, 0.0 );
		gp.lineTo( 0.0, 1.0 );
		*/
		int n = 2000 ;
		double dt = (t2-t1)/n ;
		double[][] arc = initEdgeCurve ( t1, t2, 2000 ) ;
		for ( int i = 0 ; i <= n ; i++ ) {
			double t = t1+dt*i ;
			double[] xy = arc[i] ;
			if ( i == 0 ) {
				gpath.moveTo( xy[0],  xy[1] );
			} else {
				gpath.lineTo( xy[0],  xy[1] );				
			}
		}
	}

	public Double getPath() {
		return gpath ;
	}

}
