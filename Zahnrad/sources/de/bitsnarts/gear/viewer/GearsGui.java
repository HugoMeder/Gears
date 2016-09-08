package de.bitsnarts.gear.viewer;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.bitsnarts.fileformats.plt.PLTExport;
import de.bitsnarts.fileformats.printshape.PrintProperties;
import de.bitsnarts.fileformats.printshape.PrintShapeWithProperties;
import de.bitsnarts.gear.involuteIntersection.NotConvergedException;
import de.bitsnarts.gear.viewer.printPropsDialog.PrintPropsDialog;
import de.bitsnarts.gear.viewer.sinusGears.SinusProperties;
import de.bitsnarts.gear.viewer.standardGears.DialogProperties;

public class GearsGui extends JFrame implements DialogPropertiesListener, ActionListener, Runnable {

	private static final long serialVersionUID = -3374846689574939816L;
	private AbstractDialogProperties props;
	private Viewer v;
	private JFrame animSlider;
	private JFrame propertiesDialogue;
	private AbstractGearPainter gptr;
	private JFileChooser fc = new JFileChooser () ;
	private AbstractGearPainter gptr2;
	private GearType gearType = GearType.Standard ;
	private JComboBox<GearType> gearTypeCB;
	private PrintProperties pp = new PrintProperties () ;

	enum GearType {
		
		Standard ( "Standard" ), Sinus ( "Sinus" ) ;
		
		private String name;

		GearType ( String name ) {
			this.name = name ;
		}
		
		public String toString ( ) {
			return name ;
		}
	}

	class ExportPlot extends AbstractAction {

		private static final long serialVersionUID = -368494458852175564L;

		ExportPlot () {
			super ( "Plot Datei exportieren" ) ;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			exportPlot () ;
		}		
	}
	
	class PrintWheel extends AbstractAction {

		private static final long serialVersionUID = 5083773302389483415L;

		PrintWheel () {
			super ( "Zahnrad-Kontur drucken" ) ;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printWheel ( false ) ;
		}		
	}
	
	class PrintWheelFilled extends AbstractAction {

		private static final long serialVersionUID = 5083773302389483415L;

		PrintWheelFilled () {
			super ( "Zahnrad als schwarze Fläche drucken" ) ;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printWheel ( true ) ;
		}		
	}
	
	class OpenSlider extends AbstractAction {

		private static final long serialVersionUID = 7560623749093298453L;
		OpenSlider () {
			super ( "Animations Schieberegler öffnen" ) ; 
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			openAnimationSlider () ;
		}
	}
	
	class OpenProperties extends AbstractAction{
		
		private static final long serialVersionUID = 6619261868915739329L;

		OpenProperties () {
			super ( "Rad-Parameter öffnen" ) ;
		} 

		@Override
		public void actionPerformed(ActionEvent arg0) {
			openProperties () ;
		}
		
	}
	
	GearsGui () {
		super ( "Zahnrad Profil" ) ;
		JMenuBar mb = new JMenuBar () ;
		JMenu file = new JMenu ( "Datei" ) ;
		mb.add( file ) ;
		file.add( new JMenuItem ( new ExportPlot() ) ) ;
		file.add( new JMenuItem ( new PrintWheel() ) ) ;
		file.add( new JMenuItem ( new PrintWheelFilled() ) ) ;
		JMenu windows = new JMenu ( "Fenster" ) ;
		mb.add( windows ) ;
		windows.add( new JMenuItem ( new OpenSlider ()) ) ;
		windows.add( new JMenuItem ( new OpenProperties ()) ) ;
		
		//JMenu typ = new JMenu ( "Zahnrad-Typ" );
		GearType gts[] = {GearType.Standard, GearType.Sinus} ;
		gearTypeCB = new JComboBox<GearType> ( gts ) ;
		gearTypeCB.setSelectedItem( gearType );
		gearTypeCB.addActionListener( this );
		//typ.add( gearTypeCB ) ;
		mb.add( gearTypeCB ) ;
		setJMenuBar ( mb ) ;
		try {
			createProps ( gearType ) ;
			props = new DialogProperties () ;
			props.addDialogPropertiesListener( this );
			addViewer () ;
		} catch (NotConvergedException e) {
			JOptionPane.showMessageDialog( null, "Newton-Iteration konvergiert nicht", "Fehler beim Berechnen des Involuten-Schnittpunkts("+e+")", JOptionPane.OK_OPTION ) ;
		}
		setSize ( 1024, 1024 ) ;
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setVisible( true );
	}

	public void printWheel( boolean filled ) {
		pp.shape = gptr.getPath() ;
		pp.filled = filled ; 
		if ( !getProperties () )
			return ;
		/*
		pp.filled = filled ;
		pp.printCross = true ;
		pp.holeDiameter = 10.0 ;
		pp.printHole = true ;
		pp.crossDiameter = 5.0 ;
		*/
		PrintShapeWithProperties.print( pp );
		//PrintShape.print( gptr.getPath(), filled );
	}

	private boolean getProperties() {
		return PrintPropsDialog.show ( pp ) ;
	}

	public void exportPlot() {
		fc.setDialogTitle( "Plot Datei" );
		if ( fc.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION ) {
			try {
				PLTExport exp = new PLTExport ( fc.getSelectedFile() ) ;
				exp.write( gptr.getPath () );
				exp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void openProperties() {
		if ( propertiesDialogue != null ) {
			propertiesDialogue.dispose();
		}
		//PropertiesDialog d = new PropertiesDialog ( props ) ;
		JComponent c = props.createDialog () ;
		propertiesDialogue = new JFrame ( "Zahnrad Parameter" ) ;
		propertiesDialogue.add ( c );
		propertiesDialogue.setSize ( 700, 300 ) ;
		propertiesDialogue.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		propertiesDialogue.setVisible( true );
	}

	public void openAnimationSlider() {
		if ( animSlider != null ) {
			animSlider.dispose();
		}
		animSlider = new JFrame ( "animation slider" ) ;
		animSlider.add ( new AnimationSlider ( v, props.getWheelDistance () ) ) ;
		animSlider.setSize( 1024, 100 );
		animSlider.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		animSlider.setVisible( true );
	}

	private void gearTypeCBChanged() {
		if ( gearTypeCB.getSelectedItem() != gearType ) {
			try {
				setGearType ( (GearType) gearTypeCB.getSelectedItem() ) ;
			} catch (NotConvergedException e) {
				e.printStackTrace();
			}
		}
	}

	private void setGearType(GearType newGearType ) throws NotConvergedException {
		AbstractDialogProperties newProps = createProps ( newGearType ) ;
		props.removeDialogPropertiesListener( this );
		newProps.adoptSettingsFrom ( props ) ;
		props = newProps ;
		props.addDialogPropertiesListener( this );
		gearType = newGearType ;
		addViewer () ;
		SwingUtilities.invokeLater( this );
		if ( propertiesDialogue != null ) {
			Point loc = propertiesDialogue.getLocation() ;
			openProperties() ;
			propertiesDialogue.setLocation( loc );
		}
	}
	

	@Override
	public void run() {
		v.repaint();
	}

	private AbstractDialogProperties createProps(GearType gearType) {
		switch ( gearType ) {
		case Standard : return new DialogProperties () ;
		case Sinus : return new SinusProperties () ;
		default: throw new Error ( "Unexpected Gear Type" ) ;
		}
	}

	private void addViewer() throws NotConvergedException {
		Viewer old_v = v ;
		if ( v != null ) {
			remove ( v ) ;
			v = null ;
		}
		
		if ( animSlider != null ) {
			animSlider.dispose();
			animSlider = null ;
		}

		v = new Viewer () ;
		
		GearsAndRackFactory f = props.getGearsAndRackFactory () ;
		
		gptr = f.createGearPainter1 () ;
		gptr2 = f.createGearPainter2 () ;
		AbstractRackPainter rptr = f.getRackPainter () ;
		AnimatedPainter eg = f.createEingriffPainter() ;
		
		double d = props.getWheel1Diameter() ;
				
		if ( old_v != null ) {
			v.getViewFrom ( old_v ) ;
		} else {
			v.setScale ( d*1.2 ) ;			
		}

		add( v );
		

		v.addPainter( eg );
		v.addPainter( rptr );
		v.addPainter( gptr );
		v.addPainter( gptr2 );

		

		validate () ;
	}
	
	public static void main ( String args[] ) {
		new GearsGui () ;
	}

	@Override
	public void apply( AbstractDialogProperties props) {
		
		try {
			addViewer () ;
		} catch (NotConvergedException e) {
			JOptionPane.showMessageDialog( null, "Newton-Iteration konvergiert nicht", "Fehler beim Berechnen des Involuten-Schnittpunkts("+e+")", JOptionPane.OK_OPTION ) ;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource() ;
		if ( src == gearTypeCB ) {
			gearTypeCBChanged () ;
		}
	}


}
