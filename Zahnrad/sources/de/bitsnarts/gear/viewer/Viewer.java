package de.bitsnarts.gear.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Viewer extends JPanel implements MouseWheelListener, ComponentListener, MouseMotionListener {

	private static final long serialVersionUID = -4716603413607171228L;
	private Vector<AnimatedPainter> painters = new Vector<AnimatedPainter> () ;
	private double t;
	private double windowHeight = 2.0 ;
	private double centerX = 0 ;
	private double centerY = 0 ;
	private AffineTransform atr;
	private boolean dragging;
	private double centerX0;
	private double centerY0;
	private int mouseX0;
	private int mouseY0;
	private double scale;
	
	class OpenSlider extends AbstractAction {

		OpenSlider () {
			super ( "open animation slider" ) ; 
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			openAnimationSlider () ;
		}
		
	}
	public Viewer () {
		super () ;
		setBackground( new Color (0f, 0f, 0f ) ) ; 
		setForeground( new Color (1f, 1f, 1f ) ) ;
		addMouseWheelListener ( this ) ;
		addComponentListener ( this ) ;
		addMouseMotionListener ( this ) ;
		computeTransf () ;
	}

	public void openAnimationSlider() {
		JFrame fr = new JFrame ( "animation slider" ) ;
		fr.add ( new AnimationSlider ( this, 1.0 ) );
		fr.setSize( 1024, 100 );
		fr.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		fr.setVisible( true );
	}

	public void addPainter ( AnimatedPainter painter ) {
		if ( painter != null )
			painters.add( painter ) ;
	}
	
	protected void paintComponent ( Graphics gr ) {
		super.paintComponent( gr );
		Graphics2D gr2 = (Graphics2D)gr ;
		if ( true ) {
			gr2.setTransform( atr );			
		} else {
			try {
				gr2.setTransform( atr.createInverse() );
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
			}		
		}
		BasicStroke str = new BasicStroke ( 0.0001f ) ;
		gr2.setStroke( str );
		
		/*
		double r = 0.1 ;
		Ellipse2D el = new Ellipse2D.Double ( 2.0*r, 2.0*r, -r, -r ) ;
		gr2.draw( el );
		Rectangle2D rect = new Rectangle2D.Double( 2.0*r, 2.0*r, 0, 0 ) ;
		gr2.fill( rect );
		gr2.drawRect(0, 0, 1, 1 );
		*/
		
		for ( AnimatedPainter p : painters ) {
			p.paint(t, gr2 );
		}
		drawCoords ( gr2 ) ;
	}

	private void drawCircle(Graphics2D gr2, double centerX, double centerY, double radius ) {
		Path2D.Double gp = new Path2D.Double() ;
		int n = 100 ;
		double dphi = Math.PI*2.0/n ;
		gp.moveTo( centerX+radius, centerY );
		for ( int i = 1 ; i < n ; i++ ) {
			double phi = dphi*i ;
			gp.lineTo(centerX+radius*Math.cos ( phi ), centerY+radius*Math.sin( phi ));
		}
		gp.closePath();
		gr2.draw( gp );
	}

	private void drawCoords(Graphics2D gr2) {
		gr2 = (Graphics2D) gr2.create() ;
		gr2.setColor( new Color ( 0f, 0f, 1f ));
		Line2D.Double lineX = new Line2D.Double( -1.0, 0.0, 1.0, 0.0 ) ;
		Line2D.Double lineY = new Line2D.Double( 0.0, -1.0, 0.0, 1.0 ) ;
		gr2.draw( lineX );
		gr2.draw( lineY );
		drawCircle ( gr2, 0, 0, 1.0 ) ;
	}

	public void setT(double t) {
		this.t = t ;
		repaint () ;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		int rot = arg0.getWheelRotation() ;
		double f = Math.pow( 1.1,-rot );
		windowHeight *= f ;
		computeTransf () ;
		repaint () ;
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		computeTransf () ;
    }
	

	private void computeTransf() {
		// (centerX,centerY) is mapped to the center of the window
		// (centerX,centerY-1) is mapped to the center of the top boundary
		//AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12)

		scale = getHeight()/windowHeight ;
		double m00 = scale ;
		double m11 =-scale ;
		double m02 = getWidth()/2.0 ;
		double m12 = getHeight()/2.0 ;
		atr = new AffineTransform( m00, 0.0, 0.0, m11, m02-centerX*scale, m12+centerY*scale ) ;
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if ( !dragging ) {
			mouseX0 = arg0.getX() ;
			mouseY0 = arg0.getY() ;
			centerX0 = centerX ;
			centerY0 = centerY ;
			dragging = true ;
		} else {
			int dx = arg0.getX()-mouseX0 ;
			int dy = arg0.getY()-mouseY0 ;
			centerX = centerX0-dx/scale ;
			centerY = centerY0+dy/scale ;
			computeTransf () ;
			repaint () ;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		dragging = false ;
	}

	public JMenuBar getMenuBar () {
		JMenuBar rv = new JMenuBar () ;
		rv.add( new JMenuItem ( new OpenSlider() ) )  ;
		return rv ;
	}

	public void setScale(double d) {
		this.windowHeight = d  ;
	}

	public void getViewFrom(Viewer v) {
		centerX = v.centerX ;
		centerY = v.centerY ;
		windowHeight = v.windowHeight ;
	}
}
