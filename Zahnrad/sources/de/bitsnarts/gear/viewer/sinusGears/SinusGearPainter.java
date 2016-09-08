package de.bitsnarts.gear.viewer.sinusGears;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import de.bitsnarts.gear.viewer.AbstractGearPainter;
import de.bitsnarts.gear.viewer.sinusGears.model.SinusGearGeometry;
import de.bitsnarts.gear.viewer.sinusGears.model.SinusGearParameters;

public class SinusGearPainter extends AbstractGearPainter {

	private SinusGearGeometry geom;

	public SinusGearPainter(double x, double y, double phi0, double omega, SinusGearParameters params ) {
		super(x, y, phi0, omega);
		geom = new SinusGearGeometry ( params ) ;
	}

	@Override
	public Path2D getPath() {
		return geom.getPath () ;
	}

	@Override
	protected void paint(Graphics2D gr) {
		geom.paint ( gr ) ;
	}

}
