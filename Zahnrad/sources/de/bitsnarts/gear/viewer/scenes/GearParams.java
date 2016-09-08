package de.bitsnarts.gear.viewer.scenes;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GearParams extends JDialog {

	private static final long serialVersionUID = 1L;

	GearParams () {
		JPanel p = new JPanel () ;
		add ( p ) ;
		p.add ( new JTextField ( "4823678534" ) ) ;
	}
	
	public static void main ( String args[] ) {
		GearParams d = new GearParams () ;
		d.setTitle( "Zahnrad Parameter" );
		d.setSize( 500, 500 ) ;
		d.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		d.setVisible ( true ) ;
	}
}
