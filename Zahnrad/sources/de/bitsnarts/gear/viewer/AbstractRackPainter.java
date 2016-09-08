package de.bitsnarts.gear.viewer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import de.bitsnarts.gear.parameters.DerivedInvoluteParams;

public abstract class AbstractRackPainter implements AnimatedPainter {

	private double v;

	public AbstractRackPainter ( double v ) {
		this.v = v ;
	}

	@Override
	public void paint(double t, Graphics2D gr) {
		gr = (Graphics2D) gr.create() ;
		AffineTransform tr = new AffineTransform ( 1, 0, 0, 1, v*t, 0 ) ;
		gr.transform( tr );
		paint ( gr ) ;
	}

	abstract protected void paint(Graphics2D gr) ;

}
