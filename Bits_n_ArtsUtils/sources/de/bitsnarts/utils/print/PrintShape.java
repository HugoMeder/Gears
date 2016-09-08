package de.bitsnarts.utils.print;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JOptionPane;

public class PrintShape implements Printable {

	private Shape shape;
	private double gap;
	private double inch = 25.4 ;//mm
	private double dot = inch / 72.0 ;
	private Rectangle2D bounds;
	private double dscale;
	private double scale = 1.0 ;
	private boolean filled;
	private boolean noScale;

	protected PrintShape ( Shape shape, boolean filled ) {
		this ( shape, filled, false ) ;
	}
	protected PrintShape ( Shape shape, boolean filled, boolean noScale ) {
		this.shape = shape ;
		this.filled = filled ;
		this.noScale = noScale ;
	}
	
	protected PrintShape ( Shape shape, boolean filled, double scale ) {
		this.shape = shape ;
		this.filled = filled ;
		this.noScale = true ;
		this.scale = scale ;
	}
	
	protected void print () {
		bounds = shape.getBounds2D() ;
		PrinterJob job = PrinterJob.getPrinterJob() ;
		boolean OK = job.printDialog() ;
		if ( !OK ) {
			return ;
		}
		/*
		Paper paper = new Paper () ;
		double w = bounds.getWidth() ;
		double h = bounds.getHeight() ;
		double wDot = w/dot ;
		double hDot = h/dot ;
		double borderMM = 10.0 ;
		double borderW = borderMM/dot ;
		double gapmm = 1 ;
		gap = gapmm/dot ;
		
		System.out.println( "borderW "+borderMM );
		System.out.println( "borderW "+gapmm );
		
		paper.setSize(wDot+2.0*(borderW+gap), hDot+2.0*(borderW+gap));
		paper.setImageableArea( borderW, borderW, wDot, hDot );
		
		printPaperProperties ( "desired" , paper ) ;
		
		PageFormat pf = new PageFormat () ;
		pf.setPaper( paper );
		PageFormat pf2 = job.validatePage( pf ) ;
		printPaperProperties ( "validated" , pf2.getPaper() ) ;
		*/
		job.setJobName( "A Shape" );
		job.setPrintable( this, job.defaultPage() );
		try {
			job.print();
			if ( !noScale ) {
			if ( scale != 1 )
				JOptionPane.showMessageDialog( null, "Die Graphik wurde um den Faktor "+scale+ " verkleinert", "Skalierung beim Drucken", JOptionPane.OK_OPTION ) ;
			else
				JOptionPane.showMessageDialog( null, "Die Graphik wurde in Originalgrösse gedruckt", "Skalierung beim Drucken", JOptionPane.OK_OPTION ) ;
			}
		} catch (PrinterException e) {
			e.printStackTrace();
		}			
		//System.out.println( "OK?");		
	}
	
	private void printPaperProperties(String title, Paper paper) {
		System.out.println( "Paper Properties "+title );
		System.out.println( "\twidth "+paper.getWidth()*dot );
		System.out.println( "\theight "+paper.getHeight()*dot );
		System.out.println( "\timg x "+paper.getImageableX()*dot );
		System.out.println( "\timg y "+paper.getImageableY()*dot );
		System.out.println( "\timg width "+paper.getImageableWidth()*dot );
		System.out.println( "\timg height "+paper.getImageableHeight()*dot );
	}

	public static void main ( String[] args ) {
//		Double shape = new Ellipse2D.Double( 100.0, 100.0, 0, 0 ) ;
		Shape shape = new Rectangle2D.Double( 0, 0, 10000.0, 10000.0 ) ;
		PrintShape ps = new PrintShape ( shape, true ) ;
		System.out.println( "done" );
	}

	@Override
	public int print(Graphics gr, PageFormat pform, int pageIndex) throws PrinterException {
		Graphics2D gr2 = (Graphics2D) gr ;
		//System.out.println( "Page "+pageIndex );
		if ( pageIndex == 0 ) {
			Paper paper = pform.getPaper() ;
			//printPaperProperties ( "printing" , paper ) ;
			if ( false ) {
				gr2.setColor( new Color ( 1f, 0f, 0f ) ) ;
				gr2.drawString( "hello", (int)(paper.getWidth()/2.0), (int) (paper.getHeight()/2.0) );
				java.awt.geom.Rectangle2D.Double rect = new Rectangle2D.Double( paper.getImageableX()+gap, paper.getImageableY()+gap, paper.getImageableWidth()-2.0*gap, paper.getImageableHeight()-2.0*gap ) ;
				gr2.draw ( rect ) ;				
			}
			
			double iw = paper.getImageableWidth()-2*gap ;
			double ih = paper.getImageableHeight()-2*gap ;
			
			//double iw_mm = iw*dot ;
			//double ih_mm = ih*dot ;
			
			double sw = bounds.getWidth() ;
			double sh = bounds.getHeight() ;
			dscale = scale/dot ;
			if ( !noScale ) {
				while ( iw <= sw*dscale || ih <= sh*dscale ) {
					dscale /= 10 ;
					scale *= 10 ;
				}				
			}
			double sx = bounds.getX()*dscale ;
			double sy = bounds.getY()*dscale ;
//			AffineTransform at = new AffineTransform ( scale, 0.0, 0.0, scale, -sx+paper.getImageableX()+gap, -sy+paper.getImageableY()+gap ) ;
			AffineTransform at = new AffineTransform ( dscale, 0.0, 0.0, dscale, -(sx+sw*dscale/2.0)+paper.getImageableX()+paper.getImageableWidth()/2.0, -(sy+sh*dscale/2.0)+paper.getImageableY()+paper.getImageableHeight()/2.0 ) ;
			if ( false ) {
				try {
					at = at.createInverse() ;
				} catch (NoninvertibleTransformException e ) {
					throw new Error ( e ) ;
				}				
			}
			if ( false ) {
				Point2D.Double ptSrc = new Point2D.Double( bounds.getX(), bounds.getY() ) ;
				Point2D.Double ptDst = new Point2D.Double () ;
				at.transform(ptSrc, ptDst) ;
				System.out.println ( "dst "+ptDst ) ;
			}
			gr2.transform( at );
			gr2.setColor( new Color ( 0f, 0f, 0f ) ) ;
			draw ( gr2, filled ) ;
			return Printable.PAGE_EXISTS ;
		}
		return Printable.NO_SUCH_PAGE ;
	}
	
	protected void draw( Graphics2D gr, boolean filled ) {
		if ( filled ) {
			gr.fill( shape );
		} else {
			BasicStroke str = new BasicStroke ( (float) (0.1f/scale) ) ;
			gr.setStroke( str );
			gr.draw ( shape ) ;
		}
	}

	public static void print ( Shape shape, boolean filled ) {
		PrintShape ps = new PrintShape ( shape, filled ) ;
		ps.print();
	}

	public static void print ( Shape shape, boolean filled, boolean noScale ) {
		PrintShape ps = new PrintShape ( shape, filled, noScale ) ;
		ps.print();
	}

	public static void print ( Shape shape, boolean filled, double scale ) {
		PrintShape ps = new PrintShape ( shape, filled, scale ) ;
		ps.print();
	}

}
