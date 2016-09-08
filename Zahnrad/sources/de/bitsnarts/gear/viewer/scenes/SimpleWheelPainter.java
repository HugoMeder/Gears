package de.bitsnarts.gear.viewer.scenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import de.bitsnarts.gear.parameters.DerivedInvoluteParams;
import de.bitsnarts.gear.viewer.AbstractWheelPainter;

public class SimpleWheelPainter extends AbstractWheelPainter {

	public SimpleWheelPainter(DerivedInvoluteParams derived) {
		super(derived);
	}

	@Override
	protected void paint(Graphics2D gr) {
		gr.setColor ( new Color ( 1f, 0f, 0f ) ) ;
		Line2D line = new Line2D.Double( 0.0, 1.0, 0.0, -1.0 ) ;
		gr.draw( line ); 
	}

}
