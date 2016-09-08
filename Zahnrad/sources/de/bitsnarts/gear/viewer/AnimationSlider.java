package de.bitsnarts.gear.viewer;

import java.awt.BorderLayout;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnimationSlider extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 7864947145021810002L;
	private DefaultBoundedRangeModel brm;
	private Viewer v;
	private double scale;

	public AnimationSlider ( Viewer v, double scale ) {
		super () ;
		this.scale = scale ;
		this.v = v ;
		setLayout ( new BorderLayout () ) ;
		brm = new DefaultBoundedRangeModel ( 0, 0, -1000, 1000 ) ;
		JSlider sl = new JSlider ( brm ) ;
		add ( sl ) ;
		brm.addChangeListener( this );
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		int val = brm.getValue() ;
		v.setT( val*scale/1000.0 );
		//System.out.println( "val "+val );
	}
}
