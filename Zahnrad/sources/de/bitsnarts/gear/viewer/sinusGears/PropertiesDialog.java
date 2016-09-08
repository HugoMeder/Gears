package de.bitsnarts.gear.viewer.sinusGears;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import de.bitsnarts.gear.viewer.AbstractDialogProperties;
import de.bitsnarts.gear.viewer.ActionAndFocusListener;
import de.bitsnarts.gear.viewer.DialogPropertiesListener;
import de.bitsnarts.gear.viewer.sinusGears.SinusProperties.WheelParams;

public class PropertiesDialog extends JPanel implements ActionAndFocusListener {

	private GridBagLayout gridBag;
	private JPanel pane;
	private SinusProperties props;
	private Apply apply;
	private JButton applyButton;
	private static Double[] moduleList1 =
			{
		0.05, 	0.06, 	0.08, 	0.1, 	0.12, 	0.16, 	0.2, 	0.25, 	0.3, 	0.4, 	0.5, 	0.6, 	0.7, 	0.8, 	0.9, 	1.0, 	1.25,
		1.5, 	2.0, 	2.5, 	3.0, 	4.0, 	5.0, 	6.0, 	8.0, 	10.0, 	12.0, 	16.0, 	20.0, 	25.0, 	32.0, 	40.0, 	50.0, 	60.0
			} ;

	private RadProps radProps1 ; 
	private RadProps radProps2;

	private JComboBox<Double> cb_modul;
	private JLabel bezeichnung_label;
	private JLabel comment_label;
	private JTextField eingr_ang;

	class RadProps implements ActionAndFocusListener, DialogPropertiesListener {
		private WheelParams wheel;
		private JTextField field_z;
		//private JTextField field_modul;
		
		public RadProps(WheelParams wheel ) {
			this.wheel = wheel ;
			field_z = new JTextField ( ""+wheel.z ) ;
			addRow ( "Anzahl Zähne", "z", field_z, this ) ;
			props.addDialogPropertiesListener( this );
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			handleSource ( arg0.getSource() ) ;
		}

		private void handleSource ( Object src ) {
			if ( src == field_z ) {
				z_field_changed () ;
			} else {
				throw new Error ( "unkown source" ) ;
			}			
		}
		
		private void z_field_changed() {
			String txt = field_z.getText() ;
			int val = Integer.parseInt ( txt ) ;
			if ( val != wheel.z ) {
				wheel.z = val ;
				propsChanged () ;
			}
		}

		@Override
		public void apply(AbstractDialogProperties props) {
		}

		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			handleSource ( e.getSource() ) ;
		}



	}
	
	class Apply extends AbstractAction {

		private static final long serialVersionUID = -6639880343992949566L;
		Apply () {
			super ( "Anwenden" ) ;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				props.apply();
			} catch ( Throwable th ) {
				th.printStackTrace(); 
			}
			apply.setEnabled( false );
		}
		
	}
	
	PropertiesDialog (AbstractDialogProperties props2) {
		this.props = (SinusProperties) props2 ;
		setLayout ( new BorderLayout () ) ;
		pane = new JPanel () ;
		JScrollPane sp = new JScrollPane ( pane ) ;
		add ( sp ) ;
		gridBag = new GridBagLayout () ;
		pane.setLayout ( gridBag ) ;
		
		cb_modul = new JComboBox<Double> ( moduleList1 ) ;
		cb_modul.setSelectedItem( props.modul );
		//field_modul = new JTextField ( ""+props.modul ) ;
		addRow ( "Zahnrad Modul", "m", cb_modul, " mm", this ) ;
		
		eingr_ang = new JTextField ( ""+props.flanken_deg ); 
		addRow ( "Eingriffswinkel", "phi", eingr_ang, " Grad", this ) ;

		addTitle ( "Rad1" ) ;
		radProps1 = new RadProps ( props.wheel1 ) ;

		addTitle ( "Rad2" ) ;
		radProps2 = new RadProps ( props.wheel2 ) ;

		apply = new Apply() ;
		apply.setEnabled( false );
		applyButton = new JButton ( apply ) ;
		addRow ( "", "", applyButton, this ) ; 
	}

	private void addRow(String bezeichnung, String symbol, Component comp, ActionAndFocusListener listener ) {
		addRow ( bezeichnung, symbol, comp, "", listener ) ;
	}
	
	@SuppressWarnings("rawtypes")
	private void addRow(String bezeichnung, String symbol, Component comp, String comment, ActionAndFocusListener listener) {
		GridBagConstraints c ;
		
		c = new GridBagConstraints () ;
		c.fill = GridBagConstraints.BOTH ;
		c.weightx = 1 ;
		bezeichnung_label = new JLabel ( bezeichnung ) ;
		gridBag.setConstraints( bezeichnung_label, c );
		pane.add ( bezeichnung_label ) ;

		bezeichnung_label = new JLabel ( symbol ) ;
		gridBag.setConstraints( bezeichnung_label, c );
		pane.add ( bezeichnung_label ) ;

		if ( comment != null ) {
			gridBag.setConstraints( comp, c );
			pane.add ( comp ) ;

			comment_label = new JLabel ( comment ) ;
			c.gridwidth = GridBagConstraints.REMAINDER;
			gridBag.setConstraints( comment_label, c );
			pane.add ( comment_label ) ;
		} else {
			c.gridwidth = GridBagConstraints.REMAINDER;
			gridBag.setConstraints( comp, c );
			pane.add ( comp ) ;			
		}
		
		if ( comp instanceof JTextField  ) {
			((JTextField) comp).addActionListener( listener );
			((JTextField) comp).addFocusListener( listener );
		} else if ( comp instanceof JComboBox ) {
			((JComboBox) comp).addActionListener( listener );
			((JComboBox) comp).addFocusListener( listener );
		}
	}

	private void addTitle(String string) {
		
		addRow ( "------------------", "", new JLabel ( string ), "-------------", null ) ;
		/*
		GridBagConstraints c ;
		
		c = new GridBagConstraints () ;
		c.gridwidth = GridBagConstraints.REMAINDER;
		//JLabel l = new JLabel ( string ) ;
		JSeparator l = new JSeparator () ;
		gridBag.setConstraints( l, c );
		pane.add ( l ) ;
		*/			
	}


	public static void main ( String args[] ) {
		PropertiesDialog d = new PropertiesDialog ( new SinusProperties() ) ;
		JFrame f = new JFrame ( "Zahnrad Parameter" ) ;
		f.add ( d );
		f.setSize ( 1024, 1024 ) ;
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setVisible( true );
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		handleSource ( e.getSource() ) ;
	}

	void handleSource ( Object src ) {
		if ( src == this.cb_modul ) {
			modul_cb_changed () ;
		} else if ( src == this.eingr_ang ) {
			eingr_ang_changed () ;
		} else {
			throw new Error ( "unkown source" ) ;
		}		
	}
	
	private void eingr_ang_changed() {
		String str = eingr_ang.getText() ;
		double val = Double.parseDouble( str ) ;
		if ( val != props.flanken_deg ) {
			props.flanken_deg = val ;
			propsChanged () ;
		}
	}

	private void modul_cb_changed() {
		Double val = cb_modul.getItemAt( cb_modul.getSelectedIndex() ) ;
		if ( val != props.modul ) {
			props.modul = val ;
			propsChanged () ;
		}
	}

	private void propsChanged() {
		apply.setEnabled ( true ) ;
		this.applyButton.requestFocus();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		handleSource ( arg0.getSource() ) ;
	}
}
