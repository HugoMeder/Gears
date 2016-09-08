package de.bitsnarts.gear.viewer.sinusGears;

import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JComponent;

import de.bitsnarts.gear.viewer.AbstractDialogProperties;
import de.bitsnarts.gear.viewer.DialogPropertiesListener;
import de.bitsnarts.gear.viewer.GearsAndRackFactory;
import de.bitsnarts.gear.viewer.sinusGears.model.AbstractRackFunction;
import de.bitsnarts.gear.viewer.sinusGears.model.SinusGearParameters;
import de.bitsnarts.gear.viewer.sinusGears.model.SinusRackFunction;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.TOfX;
import de.bitsnarts.gear.viewer.sinusGears.model.generic.XOfT;
import de.bitsnarts.gear.viewer.standardGears.DialogProperties;

public class SinusProperties implements AbstractDialogProperties {

	private Vector<DialogPropertiesListener> listeners = new Vector<DialogPropertiesListener> () ;
	public double v = 1 ;
	//public double eingriff_deg = 50 ;
	public double flanken_deg = 70 ; // 90-eingriff_deg ;
	public double modul = 5 ;
	public WheelParams wheel1 = new WheelParams () ;
	public WheelParams wheel2 = new WheelParams () ;
	
	public class WheelParams {
		public int z = 5 ;
		
		SinusGearParameters getParameters () {
			SinusGearParameters rv = new SinusGearParameters () ;
			rv.z = z ;
			//rv.engriffsRadiants = eingriff_deg*Math.PI/180.0 ;
			rv.flankenSteileitRadiant = flanken_deg*Math.PI/180.0 ;
			rv.modul = modul ;
			rv.profileFunction = createProfileFunction () ;
			if ( false ) {
				XOfT xoft = rv.profileFunction.getXOfT() ;		
				double t = 0.2 ;
				TreeSet<Double> xs = xoft.getXVals( 0.2 ) ;
				TOfX tofx = rv.profileFunction.getTOfX() ;
				for ( double x : xs ) {
					double err = tofx.eval( x ) - t ;
					if ( err*err > 1e-20 ) {
						throw new Error ( "err*err > 1e-20" ) ;
					}
				}
			}
			rv.v = v ;
			double r = z*modul/2.0 ;
			rv.omega = v/r ;
			return rv ;
		}
	}
	
	@Override
	public void addDialogPropertiesListener(DialogPropertiesListener listener ) {
		listeners.add( listener ) ;
	}

	@Override
	public void removeDialogPropertiesListener(DialogPropertiesListener listener) {
		listeners.remove( listener ) ;
	}

	@Override
	public double getWheelDistance() {
		return (wheel1.z+wheel2.z)*modul/2.0 ;
	}

	@Override
	public double getWheel1Diameter() {
		return wheel1.z*modul ;
	}

	@Override
	public GearsAndRackFactory getGearsAndRackFactory() {
		return new GARFactory ( this );
	}

	@Override
	public JComponent createDialog() {
		return new PropertiesDialog ( this );
	}

	public RackParameters createRackParameters() {
		RackParameters rv = new RackParameters () ;
		rv.y =-getWheel1Diameter()/2.0 ;
		rv.modul = modul ;
		rv.profileFunction = createProfileFunction ().getProfileFunction() ;
		return rv;
	}

	private AbstractRackFunction createProfileFunction() {
		double xi = 2.0/modul ;
		double h = Math.tan( flanken_deg*Math.PI/180.0 )/xi ;
		/*
		return new ScaledSin ( xi, 0, h ) ;
		*/
		return new SinusRackFunction ( h, xi, v ) ;
	}

	public void apply() {
		for ( DialogPropertiesListener l : listeners ) {
			l.apply( this );
		}
	}

	@Override
	public void adoptSettingsFrom(AbstractDialogProperties props) {
		if ( props instanceof DialogProperties ) {
			DialogProperties dp = (DialogProperties) props ;
			this.flanken_deg = 90.0-dp.thetaDegrees ;
			this.modul = dp.modul; 
			this.wheel1.z = dp.wheel1.z ;
			this.wheel2.z = dp.wheel2.z ;
		}
	}

}
