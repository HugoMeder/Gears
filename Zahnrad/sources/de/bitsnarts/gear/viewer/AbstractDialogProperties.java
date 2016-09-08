package de.bitsnarts.gear.viewer;

import javax.swing.JComponent;

public interface AbstractDialogProperties {

	void addDialogPropertiesListener(DialogPropertiesListener listener);
	void removeDialogPropertiesListener(DialogPropertiesListener listener);
	double getWheelDistance();
	double getWheel1Diameter();
	GearsAndRackFactory getGearsAndRackFactory();
	JComponent createDialog();
	void adoptSettingsFrom(AbstractDialogProperties props);

}
