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
package com.plealog.genericapp.ui.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * The class implements the viewer fo the RadioaChooserDialog system.
 * 
 * @author Patrick G. Durand
 */
public class RadioChooserPanel extends JPanel {
  private static final long serialVersionUID = 4095202813716247417L;
  private List<RadioChooserEntry> _entries;
  private ButtonGroup             _radioGroup;
  private JComponent              _internalPanel;

  /**
   * Constructor.
   * 
   * @param entries the list of entries
   * */
  public RadioChooserPanel(List<RadioChooserEntry> entries){
    _entries = entries;
    buildGUI();
  }

  /**
   * Create the UI.
   * */
  private void buildGUI(){
    JTabbedPane        jtp = null;
    JPanel             pnl=null, pnl2;
    JRadioButton       radioBtn;
    JTextArea          lbl;
    ArrayList<String>  categories;
    String             str;
    boolean            produceTabs;

    //check for categories
    categories = new ArrayList<String>();
    for(RadioChooserEntry entry : _entries){
      str = entry.getCategoryName();
      //not optimal, but we do not have lots of names
      if (!categories.contains(str)){
        categories.add(str);
      }
    }
    _radioGroup = new ButtonGroup();
    produceTabs = (categories.size()!=1);
    if (produceTabs) {
      jtp = new JTabbedPane();
      jtp.setFocusable(false);
    }  
    //create UI
    for(String category : categories){
      pnl = new JPanel();
      pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
      for(RadioChooserEntry entry : _entries){
        if (category.equals(entry.getCategoryName())){
          pnl2 = new JPanel(new BorderLayout());
          radioBtn = new JRadioButton(entry.getLabel());
          _radioGroup.add(radioBtn);
          pnl2.add(radioBtn, BorderLayout.WEST);
          pnl.add(pnl2);
          str = entry.getDescription();
          if (str!=null){
            lbl = new JTextArea(str);
            lbl.setLineWrap(true);
            lbl.setWrapStyleWord(true);
            lbl.setEditable(false);
            lbl.setOpaque(false);
            //lbl.setForeground(DDResources.getSystemTextColor());
            lbl.setFont(UIManager.getFont("ToolTip.font"));
            lbl.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
            FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
            int txtWidth = 3*fm.stringWidth(str)/2;
            int nLines = Math.max(1, txtWidth / 345) + 1;
            lbl.setPreferredSize(new Dimension(320, nLines*fm.getHeight()+10));
            pnl.add(lbl);
          }
        }
      }
      if (produceTabs){
        pnl.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        JPanel pnl3 = new JPanel(new BorderLayout());
        pnl3.add(pnl, BorderLayout.NORTH);
        jtp.add(category, pnl3);
      }
    }
    _radioGroup.getElements().nextElement().setSelected(true);
    this.setLayout(new BorderLayout());
    _internalPanel = produceTabs?jtp:pnl;
    this.add(_internalPanel, BorderLayout.NORTH);
  }
  /**
   * @return the current dimension of the panel
   * */
  public Dimension getCurrentSize(){
    return _internalPanel.getSize();
  }

  /**
   * Return the entry selected by the user.
   * 
   * @return selected entry
   * */
  public RadioChooserEntry getSelectedEntry(){
    Enumeration<AbstractButton> elements;
    int                         i=0;

    elements = _radioGroup.getElements();
    while(elements.hasMoreElements()){
      if (elements.nextElement().isSelected()){
        return _entries.get(i);
      }
      i++;
    }
    return null;
  }
}
