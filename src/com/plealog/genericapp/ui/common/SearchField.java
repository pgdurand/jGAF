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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.Caret;

import com.plealog.genericapp.api.EZEnvironment;

/**
 * This class provides a compact search field.
 * 
 * @author Patrick G. Durand
 */
public class SearchField extends JPanel{
  /**
   * 
   */
  private static final long serialVersionUID = 6977747292306810413L;
  protected MyTextField _textField;
  private JLabel       _headerIcon;
  private JButton      _resetIcon;
  private Object[]     _options;
  private Object       _selectedOption;
  private String       _helper;
  private JPanel       _commandPnl;
  private JPanel       _userCommandPnl;

  public final static String PROPERTY_TEXT = "text";
  public final static String PROPERTY_OPTION = "option";

  public SearchField(){
    this(null, null, null);	
  }

  public SearchField(String text){
    this(text, null, null);
  }

  public SearchField(String text, Object[] options){
    this(text, options, null);
  }	
  public SearchField(String text, Object[] options, Object selectedOption){
    super();
    setOpaque(false);		
    initBorder();
    initComponents();

    if(text != null){
      setText(text);
    }
    if(options != null){
      setOptions(options);
    }
    if(selectedOption != null){
      setSelectedOption(selectedOption);
    }
  }
  public void setHelperText(String helper){
    _helper = helper;
  }
  protected JPopupMenu createOptionPopup() {
    final JPopupMenu pop = new JPopupMenu();
    ButtonGroup bg = new ButtonGroup();
    for (Object o : _options) {
      final JCheckBoxMenuItem c = new JCheckBoxMenuItem(o.toString());

      c.addActionListener(new ActionListener() {			
        public void actionPerformed(ActionEvent e) {
          int i = pop.getComponentIndex(c);
          setSelectedOption(_options[i]);
        }			
      });
      if(o == _selectedOption){
        c.setSelected(true);
      }
      bg.add(c);
      pop.add(c);			
    }

    return pop;
  }

  protected void initComponents() {
    _textField = new MyTextField();
    _textField.setBorder(null);
    //_textField.setAutoscrolls(false);
    _textField.addKeyListener(new KeyAdapter() {		
      public void keyReleased(KeyEvent e) {				
        if(getText() == null || getText().length() <=0 ){
          _resetIcon.setVisible(false);
          firePropertyChange(PROPERTY_TEXT, "not empty", getText());
          repaint();
        }else{
          if(!_resetIcon.isVisible()){
            _resetIcon.setVisible(true);
            repaint();
          }
          firePropertyChange(PROPERTY_TEXT, "", getText());
        }
      }		
    });
    _headerIcon = new JLabel(EZEnvironment.getImageIcon("search.png"));
    _resetIcon = new JButton(new AbortAction());
    _resetIcon.setIcon(EZEnvironment.getImageIcon("erase.png"));
    _resetIcon.setOpaque(true);
    _resetIcon.setMargin(new Insets(0,0,0,0));
    _resetIcon.setFocusable(false);
    _resetIcon.setBorder(null);
    _resetIcon.setBorderPainted(false);
    _resetIcon.setBackground(Color.white);
    _resetIcon.setVisible(false);

    _commandPnl = new JPanel(new BorderLayout());
    _commandPnl.add(_resetIcon, BorderLayout.EAST);

    _userCommandPnl = new JPanel();
    _userCommandPnl.setBorder(null);
    _userCommandPnl.setOpaque(true);
    _userCommandPnl.setBackground(Color.white);
    _commandPnl.add(_userCommandPnl, BorderLayout.WEST);

    setBackground(_textField.getBackground());
    setLayout(new BorderLayout());
    add(_headerIcon, BorderLayout.WEST);
    add(_textField, BorderLayout.CENTER);
    add(_commandPnl, BorderLayout.EAST);
  }
  public JButton addUserAction(ImageIcon icon, Action act){
    JButton btn = new JButton(act);
    btn.setIcon(icon);
    btn.setOpaque(true);
    btn.setBackground(Color.white);
    btn.setMargin(new Insets(0,0,0,0));
    btn.setFocusable(false);
    btn.setBorder(null);
    btn.setBorderPainted(false);
    btn.setVisible(true);
    _userCommandPnl.add(btn);
    return btn;
  }
  protected void initBorder() {
    setBorder(new RoundedBorder());
  }

  public void setOptions(Object[] options){
    this._options = options;		
    if(options != null){
      _headerIcon.setIcon(EZEnvironment.getImageIcon("search.png"));
      if(_selectedOption == null){
        setSelectedOption(options[0]);
      }
      _headerIcon.addMouseListener(optionListener);
    }else{
      _headerIcon.setIcon(EZEnvironment.getImageIcon("search.png"));
      setSelectedOption(null);
      _headerIcon.removeMouseListener(optionListener);
    }

  }
  private class AbortAction extends AbstractAction{
    /**
     * 
     */
    private static final long serialVersionUID = 4654484865986385770L;

    public void actionPerformed(ActionEvent e){
      setText("");
      setTextForeground(Color.black);
      setTextBackground(Color.white);
    }
  }
  public void focusOnTextfield(){
    this._textField.setSelectionStart(0);
    this._textField.setSelectionEnd(this._textField.getText().length());
    this._textField.requestFocusInWindow();
  }

  public void addActionListener(ActionListener l) {
    _textField.addActionListener(l);
  }

  public void addKeyListener(KeyListener l) {
    _textField.addKeyListener(l);
  }

  public String getSelectedText() {
    return _textField.getSelectedText();
  }

  public int getSelectionEnd() {
    return _textField.getSelectionEnd();
  }

  public int getSelectionStart() {
    return _textField.getSelectionStart();
  }

  public void setCaret(Caret c) {
    _textField.setCaret(c);
  }

  public void setEditable(boolean b) {
    _textField.setEditable(b);
  }
  public void setEnabled(boolean enabled){
    super.setEnabled(enabled);
    _headerIcon.setEnabled(enabled);
    _textField.setEnabled(enabled);
    _resetIcon.setEnabled(enabled);
  }
  public void setToolTipText(String text){
    _textField.setToolTipText(text);
  }
  public void setText(String t) {
    firePropertyChange(PROPERTY_TEXT, _textField.getText(), t == null ? "" : t);
    _textField.setText(t);
    if(t == null || t.length() <= 0){
      _resetIcon.setVisible(false);
      revalidate();
    }else{
      if(!_resetIcon.isVisible()){
        _resetIcon.setVisible(true);
        revalidate();
      }
    }		
  }
  public void setTextForeground(Color clr){
    _textField.setForeground(clr);
  }
  public void setTextBackground(Color clr){
    _textField.setBackground(clr);
  }
  public String getText(){
    return _textField.getText();
  }

  public Object getSelectedOption() {
    return _selectedOption;
  }

  public void setSelectedOption(Object selectedOption) {
    firePropertyChange(PROPERTY_OPTION, this._selectedOption, selectedOption);
    this._selectedOption = selectedOption;
  }

  public Object[] getOptions() {
    return _options;
  }

  public Font getFont() {
    return _textField != null && _textField.getFont() != null ? _textField.getFont() : super.getFont();
  }

  public void setFont(Font f) {
    super.setFont(f);
    if(_textField != null){
      _textField.setFont(f);
    }
  }

  public void requestFocusForSearchfield() {
    _textField.requestFocusInWindow();
  }

  private MouseListener optionListener = new MouseAdapter(){		
    public void mousePressed(MouseEvent e) {
      if(_options != null){
        JPopupMenu pop = createOptionPopup();
        pop.show(_headerIcon, 0, _headerIcon.getHeight() + 1);
      }
    }	
  };

  private class RoundedBorder implements Border{	
    int thickness = 19;
    Insets insets = new Insets(2,5,2,5);
    public Insets getBorderInsets(Component c) {
      return insets;			
    }

    public boolean isBorderOpaque() {
      return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      Color oldColor = g.getColor();

      ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      g.setColor(c.getBackground());
      g.fillRoundRect(x, y, width-1, height-1, thickness, thickness);

      g.setColor(c.getBackground().darker().darker());
      g.drawRoundRect(x, y, width-1, height-1, thickness, thickness);

      ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
      g.setColor(oldColor);	        
    }		
  }
  private class MyTextField extends JTextField{
    /**
     * 
     */
    private static final long serialVersionUID = 2247364901010780336L;

    public void paintComponent(Graphics g){
      super.paintComponent(g);

      String txt = this.getText();
      if (_helper==null || txt==null || txt.length()!=0)
        return;
      FontMetrics fm = this.getFontMetrics(this.getFont());
      Color oldClr = g.getColor();
      Font  oldFnt = g.getFont();
      g.setColor(Color.LIGHT_GRAY);
      g.setFont(this.getFont());
      g.drawString(_helper, 2, (this.getBounds().height+fm.getAscent()) / 2 - 1);
      g.setColor(oldClr);
      g.setFont(oldFnt);
    }
  }
}
