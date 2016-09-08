package de.bitsnarts.gear.parameters;

public class DerivedInvoluteParams {

	public double omega;
	public double progress_x;
	public double progress_y;
	private InvoluteParameter params;

	public DerivedInvoluteParams ( InvoluteParameter params ) {
		this.params = params ;
		omega = params.v/params.r0 ;
		progress_x = params.v+params.lambda_dot*Math.cos( params.phi ) ; 
		progress_y = params.lambda_dot*Math.sin( params.phi ) ; 
	}
	
	public InvoluteParameter getParams () {
		return params ;
	}
}
