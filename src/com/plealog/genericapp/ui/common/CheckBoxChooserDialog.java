/* Copyright (C) 2019 Patrick G. Durand
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
package com.plealog.genericapp.ui.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import com.plealog.genericapp.api.EZEnvironment;

/**
 * This class implements dialog box where the user can choose among various
 * options by way of JCheckBoxes. These options are symbolized by
 * CheckBoxModelItem objects.
 * 
 * @author Patrick G. Durand
 */
public class CheckBoxChooserDialog extends JDialog {
  private static final long serialVersionUID = 8901889540260600059L;
  private JCheckBoxList _chooser;
  private List<CheckBoxModelItem> _entries;
  private boolean _cancelled = false;
  /**
   * Constructor.
   * 
   * @param owner the owner of this dialog box
   * @param title the dialog box title
   * @param entries the list of entries
   * */
  public CheckBoxChooserDialog(Frame owner, String title, List<CheckBoxModelItem> entries){
    super(owner, 
        title,
        true);
    _entries = entries;
    buildGUI(entries);
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
   * 
   * @param entries the list of entries
   */
  private void buildGUI(List<CheckBoxModelItem> entries){

    JPanel    mainPanel, btnPanel;
    JScrollPane scroll;
    JButton   ok, cancel;
    Border    eBorder;
    boolean   macOS;

    eBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    DefaultListModel<JCheckBox> model = new DefaultListModel<JCheckBox>();
    _chooser = new JCheckBoxList(model);
    JCheckBox cBox;
    
    for(CheckBoxModelItem entry : _entries) {
      cBox = new JCheckBox(entry.getLabel());
      cBox.setSelected(entry.isSelected());
      model.addElement(cBox);
    }
    scroll = new JScrollPane(_chooser);
    
    //btn panel
    cancel = new JButton("Cancel");
    cancel.addActionListener(new CloseDialogAction());
    ok = new JButton("OK");
    ok.addActionListener(new OkDialogAction());
    Dimension dim = cancel.getPreferredSize();
    ok.setPreferredSize(dim);

    btnPanel = new JPanel();
    btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
    btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

    macOS = EZEnvironment.getOSType()==EZEnvironment.MAC_OS;

    btnPanel.add(Box.createHorizontalGlue());
    btnPanel.add(macOS?cancel:ok);
    btnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    btnPanel.add(macOS?ok:cancel);
    if (!macOS)
      btnPanel.add(Box.createHorizontalGlue());

    //assemble the whole thing
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(scroll, BorderLayout.CENTER);
    mainPanel.add(btnPanel, BorderLayout.SOUTH);
    mainPanel.setBorder(eBorder);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(mainPanel, BorderLayout.CENTER);
  }

  /**
   * Figures out whether the user click on Cancel button.
   * 
   * @return true is dialogue was cancelled
   */
  public boolean isCancelled() {
    return _cancelled;
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
  private class OkDialogAction extends AbstractAction{
    private static final long serialVersionUID = -8975702864471257012L;
    public OkDialogAction(){
    }
    /**
     * Manages JButton action
     * 
     * @param e action event
     */
    public void actionPerformed(ActionEvent e){
      ListModel<JCheckBox> model = _chooser.getModel();
      int i, size;
      
      size = model.getSize();
      for(i=0 ; i<size; i++) {
        _entries.get(i).setSelected(model.getElementAt(i).isSelected());
      }
      _cancelled = false;
      dispose();
    }
  }

  /**
   * This inner class manages actions coming from
   * the JButton CloseDialog
   */
  private class CloseDialogAction extends AbstractAction{
    private static final long serialVersionUID = -1526622440345558949L;

    /**
     * Manages JButton action
     * 
     * @param e action event
     */
    public void actionPerformed(ActionEvent e){
      _cancelled = true;
      dispose();
    }
  }
}
