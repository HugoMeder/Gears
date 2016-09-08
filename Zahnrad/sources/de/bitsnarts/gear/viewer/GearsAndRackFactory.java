package de.bitsnarts.gear.viewer;

import de.bitsnarts.gear.involuteIntersection.NotConvergedException;

public interface GearsAndRackFactory {

	AbstractGearPainter createGearPainter1() throws NotConvergedException;

	AbstractGearPainter createGearPainter2() throws NotConvergedException;

	AbstractRackPainter getRackPainter();
	
	AnimatedPainter createEingriffPainter () ;

}
