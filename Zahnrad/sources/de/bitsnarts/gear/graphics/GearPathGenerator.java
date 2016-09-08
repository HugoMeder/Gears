package de.bitsnarts.gear.graphics;

import java.awt.geom.Path2D;

import de.bitsnarts.gear.Involute;
import de.bitsnarts.gear.Involute2;

public class GearPathGenerator {
	
	private Involute inv;
	private Involute2 inv2;
	private double invStart;
	private double invEnd;
	private double inv2Start;
	private double inv2End;
	private Path2D.Double gp;


	GearPathGenerator ( Involute inv, double invStart, double invEnd, Involute2 inv2, double inv2Start, int inv2End ) {
		this.inv = inv ;
		this.inv2 = inv2 ;
		this.inv2Start = inv2Start ;
		this.inv2End = inv2End ;
		this.invStart = invStart ;
		this.invEnd = invEnd ;
	}
	
	Path2D createPathByBeziers ( int segs, int segs2 ) {
		gp = new Path2D.Double() ;
		return gp ;
	}
}
