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
package com.plealog.genericapp.api.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.log.EZLoggerManager.LogLevel;
import com.plealog.genericapp.ui.common.ClipBoardTextTransfer;
import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.resources.Accessor;

//http://stackoverflow.com/questions/10785560/write-logger-message-to-file-and-textarea-while-maintaining-default-behaviour-in
/**
 * Utility class serving as a gateway between logger handler and UI component. For internal use only.
 */
public class EZUIHandler extends java.util.logging.Handler {
  private JTextPane  textArea;
  private JComboBox<String>  levelSelector;
  private JComponent component;
  
  private int sizeLimit = 1; //unit is mega-char. To avoid filling the UI component forever

  private final int FONT_SIZE = 12;
  private static final String FONT_NAME = "SansSerif";
  
  //TODO: pro-version: freeze on/off content to start/stop receving msg in the console
  
  public EZUIHandler(){
    this(1);
  }
  public EZUIHandler(int sizeLimit){
    super();
    
    //enable resource load
    EZEnvironmentImplem.addSearchClass(Accessor.class);
    
    this.sizeLimit = sizeLimit;
    textArea = new JTextPane();
    textArea.setEditable(false);
    textArea.setBackground(new Color(255,255,228));
    initStyles();
    
    component = setupComponent();
  }
  @Override
  public void publish(final LogRecord record) {
    Document doc = textArea.getDocument();
    if((doc.getLength()/1000000)>sizeLimit){
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          textArea.setText("");
        }
      });
    }
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {

        Document doc = textArea.getDocument();
        Style    style;
        Level    lvl;

        lvl = record.getLevel();
        if (lvl.equals(Level.SEVERE)){
          style = textArea.getStyle(LogLevel.error.name());
        }
        else if (lvl.equals(Level.WARNING)){
          style = textArea.getStyle(LogLevel.warn.name());
        }
        else if (lvl.equals(Level.INFO)){
          style = textArea.getStyle(LogLevel.info.name());
        }
        else{
          style = textArea.getStyle(LogLevel.debug.name());
        }
        try{
          doc.insertString(doc.getLength(), getFormatter().format(record), 
              style);
        }
        catch (BadLocationException e){
        }
      }
    });
  }
  public JComponent getComponent() {
    return component;
  }
  private JComponent setupComponent() {
    JPanel mainPnl, tbarPnl;

    mainPnl = new JPanel(new BorderLayout());
    tbarPnl = new JPanel(new BorderLayout());

    tbarPnl.add(getToolbar(), BorderLayout.WEST);
    tbarPnl.add(prepareLevelSelector(), BorderLayout.EAST);
    mainPnl.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
    mainPnl.add(tbarPnl, BorderLayout.NORTH);

    return mainPnl;
  }

  @Override
  public void close() throws SecurityException {

  }

  @Override
  public void flush() {
  }

  private void initStyles(){
    StyleContext context = StyleContext.getDefaultStyleContext();
    Style        defaultStyle = context.getStyle(StyleContext.DEFAULT_STYLE);
    Style        newStyle;

    //error : red
    newStyle = textArea.addStyle(LogLevel.error.name(), defaultStyle);
    StyleConstants.setFontFamily(newStyle, FONT_NAME);
    StyleConstants.setForeground(newStyle, Color.MAGENTA);
    StyleConstants.setFontSize(newStyle, FONT_SIZE);

    //warning : orange
    newStyle = textArea.addStyle(LogLevel.warn.name(), defaultStyle);
    StyleConstants.setFontFamily(newStyle, FONT_NAME);
    StyleConstants.setForeground(newStyle, Color.RED);
    StyleConstants.setFontSize(newStyle, FONT_SIZE);

    //info : green
    newStyle = textArea.addStyle(LogLevel.info.name(), defaultStyle);
    StyleConstants.setFontFamily(newStyle, FONT_NAME);
    StyleConstants.setForeground(newStyle, Color.GREEN.darker().darker().darker());
    StyleConstants.setFontSize(newStyle, FONT_SIZE);

    //debug : gray
    newStyle = textArea.addStyle(LogLevel.debug.name(), defaultStyle);
    StyleConstants.setFontFamily(newStyle, FONT_NAME);
    StyleConstants.setForeground(newStyle, Color.GRAY);
    StyleConstants.setFontSize(newStyle, FONT_SIZE);
  }

  protected void changeLevel(LogLevel lvl){
    if (lvl.equals(LogLevel.error)){
      levelSelector.setSelectedIndex(0);
    }
    else if (lvl.equals(LogLevel.warn)){
      levelSelector.setSelectedIndex(1);
    }
    else if (lvl.equals(LogLevel.info)){
      levelSelector.setSelectedIndex(2);
    }
    else {
      levelSelector.setSelectedIndex(3);
    }
  }
  private JComponent prepareLevelSelector(){
    JPanel         pnl;
    Vector<String> levels;
    
    pnl = new JPanel(new BorderLayout());
    levels = new Vector<String>();
    levels.add(EZEnvironment.getMessage(("__EZLogger.level.error")));
    levels.add(EZEnvironment.getMessage(("__EZLogger.level.warn")));
    levels.add(EZEnvironment.getMessage(("__EZLogger.level.info")));
    levels.add(EZEnvironment.getMessage(("__EZLogger.level.debug")));
    levelSelector = new JComboBox<>(levels);
    levelSelector.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent event) {
        String lvl = levelSelector.getSelectedItem().toString();
        //System.out.println("levelSelector: "+lvl);
        if (lvl.equals(EZEnvironment.getMessage(("__EZLogger.level.error")))){
          EZLoggerManager.updateLevel(LogLevel.error);
        }
        else if (lvl.equals(EZEnvironment.getMessage(("__EZLogger.level.warn")))){
          EZLoggerManager.updateLevel(LogLevel.warn);
        }
        else if (lvl.equals(EZEnvironment.getMessage(("__EZLogger.level.info")))){
          EZLoggerManager.updateLevel(LogLevel.info);
        }
        else {
          EZLoggerManager.updateLevel(LogLevel.debug);
        }
      }
    });
    changeLevel(EZLoggerManager.getLevel());
    
    pnl.add(new JLabel(EZEnvironment.getMessage("__EZLogger.level.selector")), BorderLayout.WEST);
    pnl.add(levelSelector, BorderLayout.EAST);
    return pnl;
  }
  private JToolBar getToolbar(){
    JToolBar       jtb;
    ImageIcon      icon;
    AbstractAction act;
    JButton        btn;

    jtb = new JToolBar();
    jtb.setFloatable(false);

    icon = EZEnvironment.getImageIcon("erase.png");
    if (icon!=null){
      act = new ResetAction("", icon);
    }
    else{
      act = new ResetAction(EZEnvironment.getMessage("__EZUIHandler.btn.clear"));
    }
    btn = jtb.add(act);
    btn.setToolTipText(EZEnvironment.getMessage("__EZUIHandler.btn.clear.tip"));

    icon = EZEnvironment.getImageIcon("copy.png");
    if (icon!=null){
      act = new CopyClipBoardAction("", icon);
    }
    else{
      act = new CopyClipBoardAction(EZEnvironment.getMessage("__EZUIHandler.btn.copy"));
    }
    btn = jtb.add(act);
    btn.setToolTipText(EZEnvironment.getMessage("__EZUIHandler.btn.copy.tip"));

    //For testing purpose only
    /*jtb.addSeparator();
    jtb.add(new DebugAction());
    jtb.add(new InfoAction());
    jtb.add(new WarnAction());
    jtb.add(new ErrorAction());
    jtb.addSeparator();
    jtb.add(new SetDebugAction());
    jtb.add(new SetInfoAction());
    jtb.add(new SetWarnAction());
    jtb.add(new SetErrorAction());*/
    return jtb;
  }
  @SuppressWarnings("serial")
  private class ResetAction extends AbstractAction{
    /**
       * Action constructor.
       * 
       * @param name the name of the action.
       */
      public ResetAction(String name) {
          super(name);
      }
      /**
       * Action constructor.
       * 
       * @param name the name of the action.
       * @param icon the icon of the action.
       */
      public ResetAction(String name, Icon icon) {
          super(name, icon);
      }
      public void actionPerformed(ActionEvent event){
        textArea.setText("");
        System.gc();
      }
  }
  @SuppressWarnings("serial")
  private class CopyClipBoardAction extends AbstractAction{
    /**
       * Action constructor.
       * 
       * @param name the name of the action.
       */
      public CopyClipBoardAction(String name) {
          super(name);
      }
      /**
       * Action constructor.
       * 
       * @param name the name of the action.
       * @param icon the icon of the action.
       */
      public CopyClipBoardAction(String name, Icon icon) {
          super(name, icon);
      }
      public void actionPerformed(ActionEvent event){
        ClipBoardTextTransfer cbtt = new ClipBoardTextTransfer();
        cbtt.setClipboardContents(textArea.getText());
      }
  }
  
  //For testing purpose only
  /*
  @SuppressWarnings("serial")
  private class DebugAction extends AbstractAction{
    public DebugAction() {super("debug");}public void actionPerformed(ActionEvent event){EZLogger.debug("DebugAction");}
  }
  @SuppressWarnings("serial")
  private class InfoAction extends AbstractAction{
    public InfoAction() {super("info");}public void actionPerformed(ActionEvent event){EZLogger.info("InfoAction");}
  }
  @SuppressWarnings("serial")
  private class WarnAction extends AbstractAction{
    public WarnAction() {super("warn");}public void actionPerformed(ActionEvent event){EZLogger.warn("WarnAction");}
  }
  @SuppressWarnings("serial")
  private class ErrorAction extends AbstractAction{
    public ErrorAction() {super("error");}public void actionPerformed(ActionEvent event){EZLogger.error("ErrorAction");}
  }

  @SuppressWarnings("serial")
  private class SetDebugAction extends AbstractAction{
    public SetDebugAction() {super("set debug");}public void actionPerformed(ActionEvent event){EZLoggerManager.setLevel(LogLevel.debug);;}
  }
  @SuppressWarnings("serial")
  private class SetInfoAction extends AbstractAction{
    public SetInfoAction() {super("set info");}public void actionPerformed(ActionEvent event){EZLoggerManager.setLevel(LogLevel.info);;}
  }
  @SuppressWarnings("serial")
  private class SetWarnAction extends AbstractAction{
    public SetWarnAction() {super("set warn");}public void actionPerformed(ActionEvent event){EZLoggerManager.setLevel(LogLevel.warn);;}
  }
  @SuppressWarnings("serial")
  private class SetErrorAction extends AbstractAction{
    public SetErrorAction() {super("set error");}public void actionPerformed(ActionEvent event){EZLoggerManager.setLevel(LogLevel.error);;}
  }*/
  
}
