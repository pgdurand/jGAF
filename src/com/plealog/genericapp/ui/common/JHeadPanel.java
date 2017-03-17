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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.plealog.genericapp.api.EZEnvironment;


/**
 * This class defines a foldable JPanel.
 * 
 * @author Patrick G. Durand
 */
public class JHeadPanel extends JPanel {

  private static final long serialVersionUID = 4834539083143954031L;

  public static final String FOLDER_STATE_PROPERTY = "foldState";

  private static final int BUTTON_WIDTH = 14;
  private static final String FOLDEDPANE = "folded";
  private static final String EXTENDEDPANE = "extended";
  private static final Color BK_COLOR_REF = new Color(204, 204, 204);
  private static final Color HEADER_BK_COLOR_DEF = new Color(153,186,243);
  private static final ImageIcon FOLDED_ARROW = EZEnvironment.getImageIcon("foldedarrow.png");
  private static final ImageIcon UNFOLDED_ARROW = EZEnvironment.getImageIcon("unfoldedarrow.png");

  private JLabel        _titleLabel;
  private GradientPanel _gradientPanel;
  private JPanel        headerPanel;
  private boolean       _isSelected;
  private JPanel        _cardPane;
  private JPanel        _emptyPane;
  private JPanel        _pane;
  private JButton       _foldButton;
  private boolean       _folded;
  private JPanel        _headerPanel;
  private Component     _content;

  /**
   * Constructs a <code>JHeadPanel</code> with the specified title.
   * 
   * @param title the initial title
   */
  public JHeadPanel(String title) {
    this(null, title, null, false, false);
  }


  /**
   * Constructs a <code>JHeadPanel</code> with the specified 
   * icon, and title.
   * 
   * @param icon   the initial icon
   * @param title  the initial title
   */
  public JHeadPanel(Icon icon, String title) {
    this(icon, title, null, false, false);
  }


  /**
   * Constructs a <code>JHeadPanel</code> with the specified 
   * title, tool bar, and content panel.
   * 
   * @param title       the initial title
   * @param content     the initial content pane
   */
  public JHeadPanel(String title, Component content) {
    this(null, title, content, false, false);
  }


  /**
   * Constructs a <code>JHeadPanel</code> with the specified 
   * icon, title, tool bar, and content panel.
   * 
   * @param icon        the initial icon
   * @param title       the initial title
   * @param content     the initial content pane
   */
  public JHeadPanel(Icon icon, String title, Component content) {
    this (icon, title, content, false, false);
  }

  /**
   * Constructs a <code>JHeadPanel</code> with the specified 
   * icon, title, tool bar, and content panel.
   * 
   * @param icon        the initial icon
   * @param title       the initial title
   * @param content     the initial content pane
   */
  public JHeadPanel(Icon icon, String title, Component content, 
      boolean folder, boolean folded) {

    JPanel     foldPanel, foldPanel2;
    CardLayout cardlayout;

    _isSelected = false;
    _titleLabel = new JLabel(title, icon, SwingConstants.LEADING);
    _folded = folded;

    _cardPane = new JPanel();
    _pane = new JPanel();
    _pane.setLayout(new BorderLayout());
    _emptyPane = new JPanel();

    cardlayout = new CardLayout();
    _cardPane.setLayout(cardlayout);
    _cardPane.add(_emptyPane, FOLDEDPANE);
    _cardPane.add(_pane, EXTENDEDPANE);
    if(_folded)
      cardlayout.show(_cardPane, FOLDEDPANE);
    else
      cardlayout.show(_cardPane, EXTENDEDPANE);

    foldPanel = new JPanel(new BorderLayout());

    _foldButton = new FoldButton(new FoldAction());
    _headerPanel = buildHeader(_titleLabel);

    if (folder)
      foldPanel.add(_foldButton, BorderLayout.WEST);
    foldPanel.add(_headerPanel, BorderLayout.CENTER);
    foldPanel2 = new JPanel(new BorderLayout());
    foldPanel2.add(foldPanel, BorderLayout.NORTH);

    setLayout(new BorderLayout());
    add(foldPanel2,BorderLayout.NORTH);
    add(_cardPane,BorderLayout.CENTER);

    if (content != null) {
      setContent(content);
    }
    //setBorder(new ShadowBorder());
    //setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    setSelected(true);
    updateHeader();

  }
  /**
   * Adds an additional panel on the east side of this HeadPanel.
   */
  public void setToolPanel(JComponent pnl){
    _gradientPanel.add(pnl, BorderLayout.EAST);
  }

  public void setTitleFont(Font fnt){
    if (fnt!=null)
      this._titleLabel.setFont(fnt);
  }

  /**
   * Returns the frame's icon.
   * 
   * @return the frame's icon
   */
  public Icon getFrameIcon() {
    return _titleLabel.getIcon();
  }


  /**
   * Sets a new frame icon.
   * 
   * @param newIcon   the icon to be set
   */
  public void setFrameIcon(Icon newIcon) {
    Icon oldIcon = getFrameIcon();
    _titleLabel.setIcon(newIcon);
    firePropertyChange("frameIcon", oldIcon, newIcon);
  }


  /**
   * Returns the frame's title text.
   *
   * @return String   the current title text
   */
  public String getTitle() {
    return _titleLabel.getText();
  }


  /**
   * Sets a new title text.
   *
   * @param newText  the title text tp be set
   */
  public void setTitle(String newText) {
    String oldText = getTitle();
    _titleLabel.setText(newText);
    firePropertyChange("title", oldText, newText);
  }


  /**
   * Returns the content - null, if none has been set.
   * 
   * @return the current content
   */
  public Component getContent() {
    return (_content);
  }


  /**
   * Sets a new panel content; replaces any existing content, if existing.
   *
   * @param newContent   the panel's new content
   */
  public void setContent(Component newContent) {
    Component oldContent;
    if (_content==newContent)
      return;
    if (_content!=null)
      _pane.remove(_content);
    oldContent = _content;
    _content = newContent;
    if (newContent!=null)
      _pane.add(newContent, BorderLayout.CENTER);
    newContent.addComponentListener(
        new ComponentAdapter(){
          public void componentHidden(ComponentEvent e){
          }
          public void componentShown(ComponentEvent e){
          }

        }
        );
    firePropertyChange("content", oldContent, newContent);
  }


  /**
   * Answers if the panel is currently selected (or in other words active)
   * or not. In the selected state, the header background will be
   * rendered differently.
   * 
   * @return boolean  a boolean, where true means the frame is selected 
   *                  (currently active) and false means it is not  
   */
  public boolean isSelected() {
    return _isSelected;
  }


  /**
   * This panel draws its title bar differently if it is selected,
   * which may be used to indicate to the user that this panel
   * has the focus, or should get more attention than other
   * simple internal frames.
   *
   * @param newValue  a boolean, where true means the frame is selected 
   *                  (currently active) and false means it is not
   */
  public void setSelected(boolean newValue) {
    boolean oldValue = isSelected();
    _isSelected = newValue;
    updateHeader();
    firePropertyChange("selected", oldValue, newValue);
  }


  /**
   * Creates and answers the header panel, that consists of:
   * an icon, a title label, a tool bar, and a gradient background.
   * 
   * @param label   the label to paint the icon and text
   * @return the panel's built header area
   */
  private JPanel buildHeader(JLabel label) {
    _gradientPanel =
        new GradientPanel(getHeaderBackground(), UIManager.getColor("control"));

    _gradientPanel.setLayout(new BorderLayout());
    label.setOpaque(false);

    _gradientPanel.add(label, BorderLayout.WEST);
    _gradientPanel.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 1));

    headerPanel = new JPanel(new BorderLayout());
    headerPanel.add(_gradientPanel, BorderLayout.CENTER);
    headerPanel.setBorder(new RaisedHeaderBorder());
    headerPanel.setOpaque(false);
    return headerPanel;
  }

  /**
   * Updates the header.
   */
  private void updateHeader() {
    _gradientPanel.setBackground(getHeaderBackground());
    _gradientPanel.setOpaque(isSelected());
    _titleLabel.setForeground(getTextForeground(isSelected()));
    headerPanel.repaint();
  }

  private Dimension getHeaderSize(){
    Dimension dim;
    Insets    insets;

    dim = headerPanel.getSize();
    insets = headerPanel.getBorder().getBorderInsets(headerPanel);
    dim = new Dimension(
        dim.width+insets.left+insets.right,
        dim.height+insets.top+insets.bottom
        );
    return (dim);
  }

  public Dimension getPreferredSize()
  {
    Dimension dim, dim2;

    dim2 = getHeaderSize();
    if(_folded){
      dim = dim2;
    }
    else{
      dim = _cardPane.getPreferredSize();
      dim.height+=dim2.height;
    }
    return dim;
  }

  /**
   * Updates the UI. In addition to the superclass behavior, we need
   * to update the header component.
   */
  public void updateUI() {
    super.updateUI();
    if (_titleLabel != null) {
      updateHeader();
    }
  }


  /**
   * Determines and answers the header's text foreground color.
   * Tries to lookup a special color from the L&amp;F.
   * In case it is absent, it uses the standard internal frame forground.
   * 
   * @param selected   true to lookup the active color, false for the inactive
   * @return the color of the foreground text
   */
  protected Color getTextForeground(boolean selected) {
    /*Color c =
            UIManager.getColor(
                selected
                    ? "SimpleInternalFrame.activeTitleForeground"
                    : "SimpleInternalFrame.inactiveTitleForeground");
        if (c != null) {
            return c;
        }
        return UIManager.getColor(
            selected 
                ? "InternalFrame.activeTitleForeground" 
                : "Label.foreground");*/
    return selected ? Color.black: Color.white;

  }

  protected void fireFoldStateChanged(){
    firePropertyChange(FOLDER_STATE_PROPERTY,
        _folded ? Boolean.FALSE:Boolean.TRUE,
            _folded ? Boolean.TRUE:Boolean.FALSE);
  }

  /**
   * Determines and answers the header's background color.
   * Tries to lookup a special color from the L&amp;F.
   * In case it is absent, it uses the standard internal frame background.
   * 
   * @return the color of the header's background
   */
  protected Color getHeaderBackground() {
    /*Color c =
            UIManager.getColor("List.selectionBackground");
        System.out.println(c.getRed()+","+c.getGreen()+","+c.getBlue());
        if (c != null)
            return c;
        return (UIManager.getColor("InternalFrame.activeTitleBackground"));*/
    return HEADER_BK_COLOR_DEF;
  }


  private class FoldButton extends JButton{

    private static final long serialVersionUID = 5581507710029957913L;
    private Color backColor = BK_COLOR_REF;
    private boolean pressedState;

    FoldButton(Action action){
      super(action);

      addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e){
          if (!pressedState)
            backColor = UIManager.getDefaults().getColor("ProgressBar.foreground").brighter();
          else
            backColor = BK_COLOR_REF.darker();
          repaint();
        }
        public void mouseExited(MouseEvent e){
          backColor = BK_COLOR_REF;
          repaint();
        }
        public void mousePressed(MouseEvent e){
          backColor = BK_COLOR_REF.darker();
          pressedState=true;
          repaint();
        }
        public void mouseReleased(MouseEvent e){
          backColor = BK_COLOR_REF;
          pressedState=false;
          repaint();
        }
      });
    }

    public Dimension getPreferredSize(){
      return (new Dimension(BUTTON_WIDTH, _headerPanel.getPreferredSize().height));
    }
    public void paintBorder(Graphics g){

    }
    public void paintComponent(Graphics g){
      Rectangle r = getBounds();
      g.setColor(backColor);
      g.clearRect(0, 0, r.width, r.height);
      g.fillRect(0, 0, r.width, r.height);
      g.setColor(Color.white);
      g.drawLine(0, 0, 0, r.height);
      g.drawLine(0, 0, r.width, 0);
      g.setColor(Color.black);
      g.drawLine(r.width - 1, 0, r.width - 1, r.height);
      g.drawLine(r.width, r.height - 1, 0, r.height - 1);
      if(_folded){
        g.drawImage(FOLDED_ARROW.getImage(), 2, 2, null, null);
      }
      else {
        g.drawImage(UNFOLDED_ARROW.getImage(), 1, 4, null, null);
      }
    }

  }

  private class FoldAction extends AbstractAction {
    private static final long serialVersionUID = -7601928154947975069L;

    public void actionPerformed(ActionEvent actionevent){
      CardLayout cardlayout = (CardLayout)_cardPane.getLayout();
      if(_folded)
        cardlayout.show(_cardPane, EXTENDEDPANE);
      else
        cardlayout.show(_cardPane, FOLDEDPANE);

      _folded = !_folded;
      getParent().invalidate();
      getParent().validate();
      getTopLevelAncestor().validate();
      fireFoldStateChanged();
    }
  }


}
