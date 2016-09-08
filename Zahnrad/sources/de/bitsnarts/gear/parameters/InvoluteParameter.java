package de.bitsnarts.gear.parameters;

public class InvoluteParameter {
	public double r0 ;
	public double v ;
	public double phi ;
	public double lambda_dot ;
	private static double deafult_v = 1.0 ;
	
	public InvoluteParameter ( double r0, double v, double phi, double lambda_dot ) {
		this.r0 = r0 ;
		this.v = v ;
		this.phi = phi ;
		this.lambda_dot = lambda_dot ;
	}
	
	public InvoluteParameter ( double A, double v, double phi ) {
		this ( A, v, phi, ThetaByPhi.lambdaDotByVFromPhi(phi)*v ) ;
	}
	
	public InvoluteParameter ( GearParameters gp ) {
		this ( gp.d/2.0, deafult_v, gp.phi, ThetaByPhi.lambdaDotByVFromPhi(gp.phi)*deafult_v ) ;		
	}
}
