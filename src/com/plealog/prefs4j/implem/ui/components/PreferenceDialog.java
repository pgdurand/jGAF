/* Copyright (C) 2003-2016 Patrick G. Durand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plealog.prefs4j.implem.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.api.PreferenceModel;
import com.plealog.prefs4j.ui.PreferenceEditor;

/**
 * This class implements a Preferences Editor dialog box.
 * 
 * @author Patrick G. Durand
 */
public class PreferenceDialog extends JDialog {

	private PreferencePanel _confPanel;
	private String             _title = DEFAULT_TITLE;
	private int                _width;
	private int                _height;
	
	private static final long serialVersionUID = -8199700248414757024L;

	protected static final String DEFAULT_TITLE = EZEnvironment.getMessage("__EZConfDlg.header");

	public PreferenceDialog(Frame owner, String title, PreferenceModel model){
		this(owner, title, model, PreferencePanel.DEFAULT_WIDTH, PreferencePanel.DEFAULT_HEIGHT, null);
	}
	public PreferenceDialog(Frame owner, String title, PreferenceModel model,
			Set<PreferenceEditor.PROPERTY> props){
		this(owner, title, model, PreferencePanel.DEFAULT_WIDTH, PreferencePanel.DEFAULT_HEIGHT, props);
	}
	public PreferenceDialog(Frame owner, String title, PreferenceModel model,
			int dlgWidth, int dlgHeight, Set<PreferenceEditor.PROPERTY> props){
		super(owner, 
				  title==null?DEFAULT_TITLE:title,
			      true);
		_width = dlgWidth;
		_height = dlgHeight;
		if (title!=null)
			_title = title;
		buildGUI(model, props);
		this.setPreferredSize(new Dimension(PreferencePanel.DEFAULT_WIDTH, PreferencePanel.DEFAULT_HEIGHT));
		this.pack();
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ConfDialogAdapter());
	}
	
    /**
     * Shows the dialog box on screen.
     */
    public void showDlg(){
        centerOnScreen();
        setVisible(true);
    }

    /**
     * Centers the dialog on the screen. 
     */
    private void centerOnScreen(){
        Dimension screenSize = this.getToolkit().getScreenSize();
        Dimension dlgSize = this.getSize();
        
        this.setLocation(screenSize.width/2 - dlgSize.width/2,
            screenSize.height/2 - dlgSize.height/2);
    }
    
    
    /**
     * Creates the GUI.
     */
    private void buildGUI(PreferenceModel model, Set<PreferenceEditor.PROPERTY> props){
    	JPanel    mainPanel, btnPanel, footerPanel,
    	          okPanel;
    	JButton   ok, cancel;
    	Border    eBorder;
    	boolean   macos;
    	
    	eBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    	
    	// config panel
    	_confPanel = new PreferencePanel(model, _width, _height, props);
    	
    	macos = EZEnvironment.getOSType()==EZEnvironment.MAC_OS;
    	//btn panel
    	btnPanel = new JPanel();
    	btnPanel.setLayout(new BorderLayout());
    	cancel = new JButton(EZEnvironment.getMessage("__EZConfDlg.cancelBtn"));
    	cancel.addActionListener(new CloseDialogAction());
    	ok = new JButton(EZEnvironment.getMessage("__EZConfDlg.okBtn"));
    	ok.addActionListener(new OkDialogAction());
    	footerPanel = new JPanel();
    	footerPanel.setLayout(new BorderLayout());
    	btnPanel = new JPanel();
    	btnPanel.setLayout(new BorderLayout());
    	okPanel = new JPanel();
    	okPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    	okPanel.add(macos?cancel:ok);
    	okPanel.add(macos?ok:cancel);
    	btnPanel.add(okPanel, BorderLayout.WEST);
        footerPanel.add(btnPanel, BorderLayout.EAST);
        footerPanel.setBorder(eBorder);
    	
    	//assemble the whole thing
    	mainPanel = new JPanel();
    	mainPanel.setLayout(new BorderLayout());
    	mainPanel.add(_confPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(eBorder);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Listener of JDialog events.
     */
    private class ConfDialogAdapter extends WindowAdapter{
        /**
         * Manages windowClosing event: hide the dialog.
         */
        public void windowClosing(WindowEvent e){
            dispose();
        }
    }        

    /**
     * This inner class manages actions coming from
     * the JButton OkDialog
     */
    @SuppressWarnings("serial")
	private class OkDialogAction extends AbstractAction{
        public OkDialogAction(){
        }
    	/**
         * Manages JButton action
         */
        public void actionPerformed(ActionEvent e){
        	try{
        		_confPanel.saveData();	
        		dispose();
        	}
        	catch(PreferenceException ex){
        		JOptionPane.showMessageDialog(
                        PreferenceDialog.this,
                        EZEnvironment.getMessage("__EZConfDlf.err.saveData")+": "+
                        ex.getMessage(),
                        _title,
                        JOptionPane.WARNING_MESSAGE);        	}
        }
    }

    /**
     * This inner class manages actions coming from
     * the JButton CloseDialog
     */
    @SuppressWarnings("serial")
	private class CloseDialogAction extends AbstractAction{
        /**
         * Manages JButton action
         */
        public void actionPerformed(ActionEvent e){
            dispose();
        }
    }
    //not yet exposed
    protected void setRestoreDefaultsAction(ActionListener al){
    	if (al==null)
    		return;
    	_confPanel.setRestoreDefaultsAction(al);
    }
}
