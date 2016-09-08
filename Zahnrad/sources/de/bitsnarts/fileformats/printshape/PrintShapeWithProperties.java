package de.bitsnarts.fileformats.printshape;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import de.bitsnarts.gear.graphics.Circle;
import de.bitsnarts.utils.print.PrintShape;

public class PrintShapeWithProperties extends PrintShape {
	
	private PrintProperties props;

	protected PrintShapeWithProperties ( PrintProperties props ) {
		super ( props.shape, props.filled ) ;
		this.props = props ;
	}
	
	protected void draw( Graphics2D gr, boolean filled ) {
		Shape shape = mkFullShape () ;
		if ( filled ) {
			gr.fill( shape );
		} else {
			BasicStroke str = new BasicStroke ( 0.0001f ) ;
			gr.setStroke( str );
			gr.draw ( shape ) ;
		}
	}

	private Shape mkFullShape() {
		Double sh = (Path2D.Double) props.shape ;
		sh = (Double) sh.clone () ;
		if ( !props.filled && props.printCross ) {
			sh.moveTo( 0.0, -props.crossDiameter/2.0 );
			sh.lineTo( 0.0, props.crossDiameter/2.0 );
			sh.moveTo( -props.crossDiameter/2.0, 0.0  );
			sh.lineTo( props.crossDiameter/2.0, 0.0 );
		}
		if ( props.printHole ) {
			Circle circ = new Circle ( 0, 0, props.holeDiameter/2.0, 40 ) ;
			circ.addTo( sh );
		}
		return sh;
	}

	public static void print ( PrintProperties props ) {
		PrintShapeWithProperties sp = new PrintShapeWithProperties ( props ) ;
		sp.print();
	}
	

}
