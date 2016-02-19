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
package com.plealog.genericapp.ui.menu;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import com.plealog.genericapp.api.EZApplicationBranding;
import com.plealog.genericapp.api.EZDefaultActionHandler;
import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.log.EZLogger;
import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.prefs4j.api.PreferenceModel;
import com.plealog.prefs4j.api.PreferenceModelFactory;
import com.plealog.prefs4j.ui.PreferenceUIFactory;

/** 
 * EZActionManager handles the creation of menu and tool bars given
 * a resource bundle file. This file is passed to the constructor of 
 * this class in order to automatically creates a set of Actions (read
 * from the bundle file) which are then used to build a menu and a tool bars.
 */
public class EZActionManager implements PropertyChangeListener {
  //created September 10, 2003
  /*
   * The following values are suffixes used to read the menu/tool
   * bars definition from the resource bundle file.
   */
  private static final String SUFFIX_ICON = ".img";
  private static final String SUFFIX_LABEL = ".label";
  private static final String SUFFIX_TIP = ".tip";
  private static final String SUFFIX_MENU = ".menu";
  private static final String SUFFIX_MNEMONIC = ".mnemonic";
  private static final String SUFFIX_TEXT = ".text";
  private static final String SUFFIX_ACCELERATOR = ".accel";
  private static final String SUFFIX_CLASS = ".class";
  private static final String SUFFIX_OS = ".osMenu";

  /**Contains the set of Actions created from the resource bundle file.*/
  private ActionMap       _map;
  /**the resource bundle*/
  private ResourceBundle  _bundle;

  private PropertyChangeSupport _changeSupport;

  private EZDefaultActionHandler _defaultActionHandler;

  private static final String ERR1 = "unsupported menu accelerator: ";
  
  /**No default constructor available.*/
  private EZActionManager(){
    _changeSupport = new PropertyChangeSupport(this);
    _defaultActionHandler = new MyEZDefaultActionHandler();
  }

  /**
   * Default constructor.
   * 
   * @param bundle the resource bundle 'file' containing
   *        the definition of the menu/tool bars.
   * @throws Exception if the resource bundle cannot be located or if this
   *        bundle contains wrongly formatted information.
   */
  public EZActionManager(ResourceBundle bundle) throws Exception{
    this();
    if (bundle==null)
      throw new NullPointerException("Resource bundle is not defined.");
    _bundle = bundle;
    initialize();
  }

  /**
   * Initializes the ImageLoader utility class.
   */
  private void initImageLoader(ResourceBundle bundle){
    String         locator;
    String[]       keys;
    int            i;

    locator = EZEnvironmentImplem.getString(bundle, "ImageLocator");
    keys = tokenize(locator);
    for (i = 0; i < keys.length; i++) {
      EZEnvironmentImplem.addSearchPath(keys[i]);
    }    	
  }
  /**
   * Returns a short tag identifying the current OS.
   * @return one of win, linux, mac or unk.
   */
  private String getOSTag(){
    Properties  props;
    String      osName, osTag;

    props = System.getProperties();
    osName = props.getProperty("os.name");
    osName = osName.toLowerCase();
    if (osName.indexOf("windows")>=0){
      osTag="win";
    }
    else if (osName.indexOf("linux")>=0){
      osTag="linux";
    }
    else if (osName.equals("mac os x")){
      osTag="mac";
    }
    else{
      osTag="unk";
    }
    return osTag;
  }
  /**
   * Figures out if a particular Action can be displayed in Menus
   * for the current OS. That OS is actually identified by the
   * String osTag.
   */
  private boolean validForThisOS(String actionName, String osTag){
    String   osList;

    osList = EZEnvironmentImplem.getString(_bundle, actionName + SUFFIX_OS);
    if (osList.equals("?"))
      return true;
    if (osList.indexOf("all")>=0)
      return true;
    if (osList.indexOf(osTag)>=0)
      return true;
    return false;
  }
  /**
   * Gets all the Actions defined in the bundle.
   */
  private ActionMap initActions(ResourceBundle bundle) throws Exception{
    ActionMap             map;
    Enumeration<String>   keys;
    String                action, actioName, actionClass, key, osTag;
    Class<?>              cl = null;
    EZBasicAction         ddAction;    
    int                   pos;

    osTag = getOSTag();
    map = new ActionMap();
    keys = bundle.getKeys();
    while (keys.hasMoreElements()){
      key = keys.nextElement();
      pos = key.indexOf(SUFFIX_LABEL);
      if (pos==-1)
        continue;
      actioName = key.substring(0,pos).trim();
      actionClass = actioName+SUFFIX_CLASS;
      if(bundle.containsKey(actionClass)){
        action = bundle. getString(actionClass);  
      }
      else{
        action=null;
      }
      if (action!=null && action.length()>1){
        cl = Class.forName(action);
        ddAction = (EZBasicAction) cl.newInstance();
      }
      else{
        ddAction = new EZBasicAction(actioName);
      }
      ddAction.setKeyName(actioName);
      ddAction.validForThisOsMenu(validForThisOS(actioName, osTag));
      ddAction.addPropertyChangeListener(this);
      map.put(ddAction.getKeyName(), ddAction);
    }
    return map;
  }

  /**
   * Reads the resource bundle content.
   */
  private void initialize() throws Exception{
    try {
      initImageLoader(_bundle);
      _map = initActions(_bundle);
    }
    catch (Exception ex){
      throw new Exception("Unable to create map of actions: "+ex.toString());
    }
  }

  private String[] tokenize(String input) {
    StringTokenizer tokenizer;
    String          str[];
    int             i = 0;

    if (input == null)
      return new String[]{};

    tokenizer = new StringTokenizer(input, ",\t\n\r\f");

    str = new String[tokenizer.countTokens()];
    while (tokenizer.hasMoreTokens()){
      str[i] = (String) tokenizer.nextToken().trim();
      i++;
    }

    return str;
  }

  /**
   * Helper method to set a text/icon/tooltip to a button. This method
   * gets all resources from the resource bundle.
   */
  private boolean createToolbarBtn(ResourceBundle resource, 
      AbstractButton button, String actionCommand){
    Icon   icon;
    String tooltip;

    if (resource==null || button==null || actionCommand==null)
      return false;

    icon = EZEnvironmentImplem.getImageIcon(
        EZEnvironmentImplem.getString(
            resource,
            actionCommand + SUFFIX_ICON)
        );
    if (icon != null) {
      button.setIcon(icon);
      button.setText(null);
    }
    else {
      button.setIcon(null);
      button.setText(
          EZEnvironmentImplem.getString(
              resource,
              actionCommand + SUFFIX_LABEL)
          );
    }
    button.setFont(EZEnvironmentImplem.getMainFont(null));
    tooltip = EZEnvironmentImplem.getString(
        resource,
        actionCommand + SUFFIX_TIP);
    if (tooltip.equals("?")==false){
      button.setToolTipText(tooltip);
    }
    return true;
  }

  /**
   * Helper method to set a text/icon/tooltip to a menu item. This method
   * gets all resources from the resource bundle.
   */
  private boolean initMenuItem(ResourceBundle resource, JMenuItem item, 
      String actionCommand){
    String    tooltip, accel, mnemonic;
    KeyStroke stroke = null;
    Icon      icon;
    int       val;
    
    if (resource==null || item==null || actionCommand==null)
      return false;

    item.setText(
        EZEnvironmentImplem.getString(
            resource,
            actionCommand + SUFFIX_LABEL)
        );

    item.setFont(EZEnvironmentImplem.getMainFont(null));

    icon = EZEnvironmentImplem.getImageIcon(
        EZEnvironmentImplem.getString(
            resource,
            actionCommand + SUFFIX_ICON)
        );
    if (icon != null) {
      item.setHorizontalTextPosition(JButton.RIGHT);
      item.setIcon(icon);
    }

    tooltip = EZEnvironmentImplem.getString(
        resource,
        actionCommand + SUFFIX_TIP);
    if (tooltip.equals("?")==false){
      item.setToolTipText(tooltip);
    }

    accel = EZEnvironmentImplem.getString(
        resource,
        actionCommand + SUFFIX_ACCELERATOR);
    if (accel.equals("?")==false && accel.length()>0){
      
      int idx = accel.indexOf(" ");
      if(idx==-1){//accel is a single char
        //from: http://alvinalexander.com/apple/mac/java-mac-native-look/Converting_Control_keystrok.shtml
        val = getKey(accel.charAt(0));
        if (val!=-1){
          stroke = KeyStroke.getKeyStroke(val, 
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        }
        else{
          EZLogger.warn(ERR1+accel.charAt(0));
        }
      }
      else{
        int    modifiersValue, i, size;
        String modifiers;
        
        modifiers=accel.substring(0, idx).toLowerCase();
        modifiersValue = getModifier(modifiers.charAt(0));
        size = modifiers.length();
        for(i=1;i<size;i++){
          modifiersValue |= getModifier(modifiers.charAt(i));
        }
        val = getKey(accel.charAt(idx+1));
        if (val!=-1){
          stroke = KeyStroke.getKeyStroke(val, modifiersValue);
        }
        else{
          EZLogger.warn(ERR1+accel.charAt(idx+1));
        }
      }
      
      if (stroke!=null){
        //System.out.println("stroke: "+stroke.toString());
        item.setAccelerator(stroke);
      }
    }

    mnemonic = EZEnvironmentImplem.getString(
        resource,
        actionCommand + SUFFIX_MNEMONIC);
    if (mnemonic.equals("?")==false){
      item.setMnemonic(mnemonic.charAt(0));
    }
    return true;
  }
  private int getModifier(char ch){
    switch(ch){
      case 'a':
        return InputEvent.ALT_DOWN_MASK;
      case 'm':
        return InputEvent.META_DOWN_MASK;
      case 's':
        return InputEvent.SHIFT_DOWN_MASK;
      case 'c':
      default:
        return InputEvent.CTRL_DOWN_MASK;
    }
  }
  /**
   * Helper method to create a single menu item.
   */
  private Component createMenuItem(ResourceBundle resource, String key, 
      ActionMap actions) {
    String        subMenu;
    String[]      itemKeys;
    EZBasicAction a;
    JMenuItem     item;

    subMenu = EZEnvironmentImplem.getString(
        resource,
        key + SUFFIX_MENU
        );

    if (subMenu.equals("?")==false) {
      itemKeys = tokenize(subMenu);
      return (createMenu(resource, key, itemKeys, actions));
    } 
    else {
      a = (EZBasicAction) actions.get(key);
      if (a == null || !a.isValidForThisOsMenu())
        return null;
      item = new JMenuItem(a);
      initMenuItem(resource, item, key);
      return item ;
    }
  }

  /**
   * Helper method to create a single pulldown menu.
   */
  private JMenu createMenu(ResourceBundle resource, String key, 
      String[] itemKeys, ActionMap actions){
    Component compo;
    String    mnemonic, mName;
    JMenu     menu;
    int       i, size;
    ArrayList<Component> items;

    menu = new JMenu();

    mName = EZEnvironmentImplem.getString(
        resource,
        key + SUFFIX_TEXT);
    menu.setText(mName);
    menu.setFont(EZEnvironmentImplem.getMainFont(null));
    items = new ArrayList<Component>();
    for (i = 0; i < itemKeys.length; i++) {
      if (itemKeys[i].equals("-")) {
        //menu.addSeparator();
        items.add(new JSeparator());
      } else {
        compo = createMenuItem(resource, itemKeys[i], actions);
        if (compo!=null){
          //menu.add(compo);
          items.add(compo);
        }
      }
    }
    size = items.size();
    for(i=0;i<size;i++){
      compo = items.get(i);
      if (compo instanceof JSeparator == false){
        menu.add(compo);
      }
      else if ((i+1)<size){
        if (items.get(i+1)instanceof JSeparator == false){
          menu.addSeparator();
        }
      }
    }
    if (menu.getItemCount()==0)
      return null;
    // set mnemonic for the JMenus
    mnemonic = EZEnvironmentImplem.getString(
        resource,
        key + SUFFIX_MNEMONIC
        );
    if (!mnemonic.equals("?") && mnemonic.length() > 0)
      menu.setMnemonic(mnemonic.toCharArray()[0]);

    return menu;
  }

  /**
   * Creates a JMenuBar.
   */
  public JMenuBar createMenubar() {
    String      menubar;
    JMenuBar    mb;
    JMenu       m;
    String[]    menuKeys, itemKeys;
    ActionMap   actions;
    int         i;

    actions = _map;
    mb = new JMenuBar();
    menubar = EZEnvironmentImplem.getString(_bundle, "MenuBar");
    menuKeys = tokenize(menubar);
    for (i = 0; i < menuKeys.length; i++) {
      itemKeys = tokenize(
          EZEnvironmentImplem.getString(_bundle, menuKeys[i] + SUFFIX_MENU)
          );
      m = createMenu(_bundle, menuKeys[i], itemKeys, actions);
      if (m != null){
        mb.add(m);
      }
    }

    return mb;
  }

  /**
   * Creates a JToolBar.
   */
  public JToolBar createToolbar() {
    AbstractButton button;
    String         toolbar;
    JToolBar       tb;
    String[]       menuKeys;
    Action         a;
    int            i;

    tb = new JToolBar();
    toolbar = EZEnvironmentImplem.getString(_bundle, "ToolBar");
    menuKeys = tokenize(toolbar);
    for (i = 0; i < menuKeys.length; i++) {
      if (menuKeys[i].equals("-")){
        tb.addSeparator();
      }
      else{
        a = _map.get(menuKeys[i]);
        if (a != null){
          button = new JButton(a);
          createToolbarBtn(_bundle, button,menuKeys[i]);
          tb.add(button);
        }
      }
    }

    return tb;
  }

  /**
   * Updates all Actions. 
   */
  public void update() {
    Object[] keys;
    int      i;

    keys = _map.keys();
    for (i = 0; i < keys.length; i++) {
      ((EZBasicAction)_map.get(keys[i])).update();
    }
  }    

  /**
   * Utility method to pass a user object to all Actions.
   */
  public void setUserObject(Object obj){
    Object[] keys;
    int      i;

    keys = _map.keys();
    for (i = 0; i < keys.length; i++) {
      ((EZBasicAction)_map.get(keys[i])).setUserObject(obj);
    }
  }

  /**
   * Returns the ActionMap obtained from the resource bundle.
   */
  public ActionMap getActionMap(){
    return (_map);
  }

  /**
   * Returns an action by name.
   */
  public EZBasicAction getAction(String name){
    return ((EZBasicAction) _map.get(name));
  }

  /**
   * Implementation of java.beans.PropertyChangeListener interface.
   * 
   * By default, this method only handles message for the property
   * DDBasicAction.GLOBAL_UPDT_PROP. When receiving such a message,
   * this method just call this.update(), i.e. it asks all Actions
   * to update themselves.
   */
  public void propertyChange(PropertyChangeEvent evt){
    if (evt.getPropertyName().equals(EZBasicAction.GLOBAL_UPDT_PROP)){
      EZActionManager.this.update();
    }
    else{
      //special action first
      if (evt.getPropertyName().equals("EZActionPreferences")){
        _defaultActionHandler.handlePreferences();
      }
      else if (evt.getPropertyName().equals("EZActionShowAboutDlg")){
        _defaultActionHandler.handleAbout();
      }
      else if (evt.getPropertyName().equals("EZActionExit")){
        boolean bret = _defaultActionHandler.handleExit();

        //Mac only stuff:
        //we come here ONLY if we unfortunately use a File/exit command 
        //not respecting standard UI rules of Mac OS X
        if(bret){
          System.exit(0);
        }
      }
      else{
        _changeSupport.firePropertyChange(evt);
      }
    }
  }

  public void addActionMenuListener(PropertyChangeListener listener){
    _changeSupport.addPropertyChangeListener(listener);
  }
  public void removeActionMenuListener(PropertyChangeListener listener){
    _changeSupport.removePropertyChangeListener(listener);
  }
  public EZDefaultActionHandler getDefaultActionHandler(){
    return _defaultActionHandler;
  }
  public void setDefaultActionHandler(EZDefaultActionHandler handler){
    _defaultActionHandler = handler;
  }
  public class MyEZDefaultActionHandler implements EZDefaultActionHandler {

    public void handleAbout(){
      JOptionPane.showMessageDialog(
          EZEnvironmentImplem.getParentFrame(), 
          EZApplicationBranding.getAppName() + " - "+
              EZApplicationBranding.getAppVersion()+ 
              "\n"+
              EZApplicationBranding.getCopyRight(), 
              EZEnvironmentImplem.getMessage("__EZ.msg2"), 
              JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean handleExit(){
      boolean bRet = false;

      if (EZEnvironment.getUIStarterListener()!=null){
        if (EZEnvironment.getUIStarterListener().isAboutToQuit() == false){
          return false;
        }
      }

      if (EZEnvironment.confirmBeforeExit()==false){
        bRet = true;
      }
      else{
        int ret = JOptionPane.showConfirmDialog(
            EZEnvironmentImplem.getParentFrame(), 
            EZEnvironmentImplem.getMessage("__EZ.msg1"), 
            EZApplicationBranding.getAppName(), 
            JOptionPane.YES_NO_OPTION);
        if (ret==JOptionPane.YES_OPTION){
          bRet = true;
        }
      }

      //for MacOs X, let the AppleCOnnector do the job
      if (EZEnvironmentImplem.getOSType()==EZEnvironment.MAC_OS){
        return bRet;
      }

      //for all other systems, do the System.exit() if needed
      if(bRet){
        System.exit(0);
      }
      return bRet;
    }

    public void handlePreferences(){
      String confFile;

      confFile = EZEnvironmentImplem.getMasterConfigurationFile();
      if (confFile!=null){
        try {
          PreferenceModel model = PreferenceModelFactory.getModel(confFile);
          PreferenceUIFactory.showPreferenceDialog(null, EZApplicationBranding.getAppName()+" "+
              EZEnvironmentImplem.getMessage("__EZ.msg3"), model);
        } catch (Exception e) {
          EZLogger.warn(EZEnvironmentImplem.getMessage("__EZ.err1")+e);
        }
      }
      else{
        EZEnvironment.displayInfoMessage(EZEnvironment.getParentFrame(), 
            EZEnvironmentImplem.getMessage("__EZ.err2"));
      }
    }
  }
  
  private int getKey(char ch){
    int val = -1;
    try {
      if (Character.isLetter(ch)){
        val = Integer.valueOf(KeyEvent.class.getDeclaredField("VK_"+Character.toUpperCase(ch)).get(String.class).toString());
      }
      else if (Character.isDigit(ch)){
        val = Integer.valueOf(KeyEvent.class.getDeclaredField("VK_"+ch).get(String.class).toString());
      }
    } catch (Exception e) {//not bad
    }
    return val;
    /*switch(ch){
      case 'A': case 'a': return KeyEvent.VK_A;
      case 'B': case 'b': return KeyEvent.VK_B;
      case 'C': case 'c': return KeyEvent.VK_C;
      case 'D': case 'd': return KeyEvent.VK_D;
      case 'E': case 'e': return KeyEvent.VK_E;
      case 'F': case 'f': return KeyEvent.VK_F;
      case 'G': case 'g': return KeyEvent.VK_G;
      case 'H': case 'h': return KeyEvent.VK_H;
      case 'I': case 'i': return KeyEvent.VK_I;
      case 'J': case 'j': return KeyEvent.VK_J;
      case 'K': case 'k': return KeyEvent.VK_K;
      case 'L': case 'l': return KeyEvent.VK_L;
      case 'M': case 'm': return KeyEvent.VK_M;
      case 'N': case 'n': return KeyEvent.VK_N;
      case 'O': case 'o': return KeyEvent.VK_O;
      case 'P': case 'p': return KeyEvent.VK_P;
      case 'Q': case 'q': return KeyEvent.VK_Q;
      case 'R': case 'r': return KeyEvent.VK_R;
      case 'S': case 's': return KeyEvent.VK_S;
      case 'T': case 't': return KeyEvent.VK_T;
      case 'U': case 'u': return KeyEvent.VK_U;
      case 'V': case 'v': return KeyEvent.VK_V;
      case 'W': case 'w': return KeyEvent.VK_W;
      case 'X': case 'x': return KeyEvent.VK_X;
      case 'Y': case 'y': return KeyEvent.VK_Y;
      case 'Z': case 'z': return KeyEvent.VK_Z;
      case '0': return KeyEvent.VK_NUMPAD0;
      case '1': return KeyEvent.VK_NUMPAD1;
      case '2': return KeyEvent.VK_NUMPAD2;
      case '3': return KeyEvent.VK_NUMPAD3;
      case '4': return KeyEvent.VK_NUMPAD4;
      case '5': return KeyEvent.VK_NUMPAD5;
      case '6': return KeyEvent.VK_NUMPAD6;
      case '7': return KeyEvent.VK_NUMPAD7;
      case '8': return KeyEvent.VK_NUMPAD8;
      case '9': return KeyEvent.VK_NUMPAD9;
    }
    return -1;*/
  }
}
