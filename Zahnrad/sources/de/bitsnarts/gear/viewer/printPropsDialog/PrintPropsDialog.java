package de.bitsnarts.gear.viewer.printPropsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.bitsnarts.fileformats.printshape.PrintProperties;

public class PrintPropsDialog extends JPanel {

	private static final long serialVersionUID = 6885822369809974188L;
	private PrintProperties pp;
	private JCheckBox printCrossCB;
	private JTextField crossDiameter;
	private boolean finished;
	private JCheckBox printHoleCB;
	private JTextField holeDiameter;
	private JDialog d;
	private JButton ok;
	private boolean cancled;

	class OK extends AbstractAction {
		private static final long serialVersionUID = -538229299634110032L;

		OK () {
			super ( "OK" ) ;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			check () ;
		}
	}
	
	class Cancel extends AbstractAction {
		private static final long serialVersionUID = -538229299634110032L;

		Cancel () {
			super ( "Abbruch" ) ;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			cancel () ;
		}
	}
	
	PrintPropsDialog ( PrintProperties pp ) {
		super () ;
		this.pp = pp ;
		
		Box hb = Box.createHorizontalBox() ;
		printCrossCB = new JCheckBox ( "Kreuz drucken" ) ;
		printCrossCB.setSelected( pp.printCross );
		crossDiameter = new JTextField ( 10 ) ;
		crossDiameter.setText( ""+pp.crossDiameter );
		
		if ( pp.filled ) {
			printCrossCB.setSelected( false );
			printCrossCB.setEnabled( false );
			crossDiameter.setEnabled( false );
		}
		hb.add ( printCrossCB ) ;
		hb.add( new JLabel ( " Kreuz Durchmesser " ) ) ;
		hb.add ( crossDiameter ) ;

		add ( hb ) ;
		hb = Box.createHorizontalBox() ;

		printHoleCB = new JCheckBox ( "Loch drucken" ) ;
		printHoleCB.setSelected( pp.printHole );
		holeDiameter = new JTextField ( 10 ) ;
		holeDiameter.setText( ""+pp.holeDiameter );

		hb.add( printHoleCB ) ;
		hb.add( new JLabel ( " Loch Durchmesser " ) ) ;
		hb.add( holeDiameter ) ;
		
		add ( hb ) ;
		
		hb = Box.createHorizontalBox() ;

		ok = new JButton ( new OK ()) ;
		JButton can = new JButton ( new Cancel ()) ;
		
		hb.add( ok ) ;
		hb.add( can ) ;
		
		add ( hb ) ;
	}
	
	public void cancel() {
		cancled = true ;
		d.dispose();
	}

	public void check() {
		try {
			pp.crossDiameter = Double.parseDouble( crossDiameter.getText() ) ;			
			pp.holeDiameter = Double.parseDouble( holeDiameter.getText() ) ;
			pp.printCross = printCrossCB.isSelected() ;
			pp.printHole = printHoleCB.isSelected() ;
		} catch ( Throwable th ) {
			JOptionPane.showConfirmDialog( this, "Fehler "+th.getMessage() ) ;
			return ;
		}
		d.dispose();
	}

	@Override
	public void addNotify () {
		super.addNotify();
		ok.requestFocus();
	}
	
	@Override
	public void removeNotify () {
		finish () ;
		super.removeNotify();
	}
	
	private void finish() {
		synchronized ( this ) {
			finished = true ;
			this.notifyAll();
		}
	}

	private void waitFinished() {
		synchronized ( this ) {
			while ( !finished ) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setDialog(JDialog d) {
		this.d = d ;
	}


	public static boolean show ( PrintProperties pp ) {
//		JDialog d = new JDialog ( (JFrame) null, " " ) ;
		PrintPropsDialog ppd = new PrintPropsDialog ( pp ) ;
		JDialog d = new JDialog () ;
		d.setTitle( "Druck optionen");
		d.setSize( 400, 300 );
		d.setModal( true );
		d.add ( ppd ) ;
		d.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
		ppd.setDialog ( d ) ;
		d.setVisible( true );
		ppd.waitFinished () ;
		return !ppd.cancled ;
	}


}
