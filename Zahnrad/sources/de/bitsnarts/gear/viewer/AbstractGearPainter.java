package de.bitsnarts.gear.viewer;

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public abstract class AbstractGearPainter extends AbstractWheelPainter {

	public AbstractGearPainter(double x, double y, double phi0, double omega) {
		super(x, y, phi0, omega);
	}

	public abstract Path2D getPath() ;

}
