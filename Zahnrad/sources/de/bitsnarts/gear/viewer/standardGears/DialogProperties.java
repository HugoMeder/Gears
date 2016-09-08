package de.bitsnarts.gear.viewer.standardGears;

import java.util.Vector;

import javax.swing.JComponent;

import de.bitsnarts.gear.viewer.AbstractDialogProperties;
import de.bitsnarts.gear.viewer.DialogPropertiesListener;
import de.bitsnarts.gear.viewer.GearsAndRackFactory;
import de.bitsnarts.gear.viewer.sinusGears.SinusProperties;

public class DialogProperties implements AbstractDialogProperties {
	public double modul = 5.0 ;
	public double thetaDegrees = 20 ; 
	public WheelParams wheel1 = new WheelParams () ;
	public WheelParams wheel2 = new WheelParams () ;
	
	public class WheelParams {
		public int z = 10 ;		
		public double c = 0.167 ;
		public double xi = 0.5 ;
	}
	
	
	private Vector<DialogPropertiesListener> listeners = new Vector<DialogPropertiesListener> () ;
	
	void apply () {
		for ( DialogPropertiesListener l : listeners ) {
			l.apply( this );
		}
	}
	
	@Override
	public void addDialogPropertiesListener ( DialogPropertiesListener l ) {
		listeners.add ( l ) ;
	}
	
	@Override
	public void removeDialogPropertiesListener ( DialogPropertiesListener l ) {
		listeners.remove ( l ) ;
	}

	public GearsAndRackFactory getGearsAndRackFactory() {
		return new StandardGearsAndRackFactory ( this );
	}

	@Override
	public double getWheelDistance() {
		return modul*wheel1.z/2.0;
	}

	@Override
	public double getWheel1Diameter() {
		return modul*wheel1.z;
	}

	@Override
	public JComponent createDialog() {
		return new PropertiesDialog ( this );
	}

	@Override
	public void adoptSettingsFrom(AbstractDialogProperties props) {
		if ( props instanceof SinusProperties ) {
			SinusProperties dp = (SinusProperties) props ;
			this.thetaDegrees = 90.0-dp.flanken_deg ;
			this.modul = dp.modul; 
			this.wheel1.z = dp.wheel1.z ;
			this.wheel2.z = dp.wheel2.z ;
		}
	}

}
