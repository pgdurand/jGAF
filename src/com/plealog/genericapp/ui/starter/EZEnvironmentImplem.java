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
package com.plealog.genericapp.ui.starter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.plealog.genericapp.api.EZApplicationBranding;
import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.EZUIStarterListener;
import com.plealog.genericapp.api.log.EZLogger;
import com.plealog.genericapp.ui.menu.EZActionManager;
import com.plealog.resources.Accessor;

/**
 * This class contains accessory methods to manage resource bundles.
 * 
 * @author Patrick G. Durand
 * @since September 10, 2003
 */
public abstract class EZEnvironmentImplem {

  /** generic font for the entire GUI */
  private static Font                _mainFont;
  private static Color               _systemTextColor   = Color.BLACK;

  /** generic font for the entire GUI */
  private static final Font          MAINFONT           = new Font("SansSerif",
                                                            Font.PLAIN, 12);

  /** an unknown String value for not defined key (?) */
  public static final String         UNKNOWNSTRING      = "?";

  /** an unknown Integer value for not defined key (0) */
  public static final Integer        UNKNOWNINTEGER     = new Integer(0);

  /** an unknown Double value for not defined key (0.0) */
  public static final Double         UNKNOWNDOUBLE      = new Double(0.0);

  /** an unknown Boolean value for not defined key (false) */
  public static final Boolean        UNKNOWNBOOLEAN     = null;

  /** contains string objects which respresents the search paths */
  private static ArrayList<String>   _imgPaths          = new ArrayList<String>();

  /** contains string objects which respresents the search paths */
  private static ArrayList<Class<?>> _imgClasses        = new ArrayList<Class<?>>();

  private static final Cursor        WAIT_CURSOR        = new Cursor(
                                                            Cursor.WAIT_CURSOR);
  private static final Cursor        NORMAL_CURSOR      = new Cursor(
                                                            Cursor.DEFAULT_CURSOR);
  private static final Cursor        HAND_CURSOR        = new Cursor(
                                                            Cursor.HAND_CURSOR);

  private static final String        SYSTEM_BUNDLE_NAME = Accessor.class
                                                            .getPackage()
                                                            .getName()
                                                            + ".messages";
  private static ResourceBundle      _userDefinedMessagesBundle;
  private static ResourceBundle      _userDefinedActionsBundle;
  private static EZActionManager     _actionManager;
  private static String[]            _applicationArguments;
  private static EZUIStarterListener _appListener;

  private static enum CONF_PNL_ALIGN {
    left, center
  };

  private static CONF_PNL_ALIGN       _defAlign          = CONF_PNL_ALIGN.left;

  private static final ResourceBundle RESOURCE_BUNDLE    = ResourceBundle
                                                             .getBundle(SYSTEM_BUNDLE_NAME);

  private static String               _masterConfigurationFile;
  private static Frame                _parent;
  private static boolean              _confirmBeforeExit = true;

  static {
    _imgClasses.add(EZEnvironmentImplem.class);
  }

  /**
   * Retrieves a String value for a specific key from a resource bundle.
   * 
   * @param bundle
   *          a resource bundle
   * @param key
   *          a resource key
   * @return the value associated to key. Returns "?" is key if not found or not
   *         defined, or if Properties file is not found.
   */
  public static String getString(ResourceBundle rb, String key) {
    String szVal = UNKNOWNSTRING;

    try {
      szVal = rb.getString(key);
    } catch (MissingResourceException mre) {
      // do nothing here
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is null: " + npe.getMessage());
    }
    return (szVal.trim());
  }

  /**
   * Set system text color
   * 
   * @param color
   */
  public static void setSystemTextColor(Color color) {
    if (color != null)
      _systemTextColor = color;
  }

  /**
   * Return system text color
   * 
   * @return system text color
   */
  public static Color getSystemTextColor() {
    return _systemTextColor;
  }

  /**
   * Retrieves an Integer value for a specific key from a resource bundle.
   * 
   * @param bundle
   *          a resource bundle
   * @param key
   *          a resource key
   * @return the value associated to key. Returns 0 (zero) if key is not found
   *         or not defined, or if Properties file is not found.
   */
  public static Integer getInteger(ResourceBundle rb, String key) {
    Integer val = UNKNOWNINTEGER;
    String szVal;

    try {
      szVal = rb.getString(key);
      val = Integer.valueOf(szVal.trim());
    } catch (MissingResourceException mre) {
      // do nothing here
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is not defined: " + npe.getMessage());
    } catch (NumberFormatException nfe) {
      EZLogger.warn("Value for key '" + key + "' does not contain an integer: "
          + nfe.getMessage());
    }
    return (val);
  }

  /**
   * Retrieves a Boolean value for a specific key from a resource bundle.
   * 
   * @param bundle
   *          a resource bundle
   * @param key
   *          a resource key
   * 
   * @return the value associated to key. Returns false if key is not found or
   *         not defined, or if Properties file is not found.
   */
  public static Boolean getBoolean(ResourceBundle rb, String key) {
    Boolean val = UNKNOWNBOOLEAN;
    String szVal;

    try {
      szVal = rb.getString(key);
      szVal = szVal.trim();
      if (szVal.toLowerCase().equals("true"))
        val = Boolean.TRUE;
      else if (szVal.toLowerCase().equals("false"))
        val = Boolean.FALSE;
    } catch (MissingResourceException mre) {
      // do nothing here
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is not defined: " + npe.getMessage());
    }
    return (val);
  }

  /**
   * Retrieves a Double value for a specific key from a resource bundle.
   * 
   * @param bundle
   *          a resource bundle
   * @param key
   *          a resource key
   * @return the value associated to key. Returns 0 (zero) if key is not found
   *         or not defined, or if Properties file is not found.
   */
  public static Double getDouble(ResourceBundle rb, String key) {
    Double val = UNKNOWNDOUBLE;
    String szVal;

    try {
      szVal = rb.getString(key);
      val = Double.valueOf(szVal.trim());
    } catch (MissingResourceException mre) {
      // do nothing here
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is not defined: " + npe.getMessage());
    } catch (NumberFormatException nfe) {
      EZLogger.warn("Value for key '" + key + "' does not contain a double: "
          + nfe.getMessage());
    }
    return (val);
  }

  /**
   * Converts color (R,G,B) to Color object.
   */
  private static Color convertColor(String szColor) {
    StringTokenizer tokenizer;
    String token;
    int r, g, b;

    tokenizer = new StringTokenizer(szColor, ", \t\n\r\f");
    if (tokenizer.hasMoreTokens() == false)
      return Color.black;
    token = tokenizer.nextToken();
    r = Integer.valueOf(token).intValue();
    if (tokenizer.hasMoreTokens() == false)
      return Color.black;
    token = tokenizer.nextToken();
    g = Integer.valueOf(token).intValue();
    if (tokenizer.hasMoreTokens() == false)
      return Color.black;
    token = tokenizer.nextToken();
    b = Integer.valueOf(token).intValue();

    return (new Color(r, g, b));
  }

  /**
   * Retrieves a Color value for a specific key from a resource bundle.
   * 
   * @param bundle
   *          a resource bundle
   * @param key
   *          a resource key
   * @return the value associated to key. Returns Color.black if key is not
   *         found or not defined, or if Properties file is not found.
   */
  public static Color getColor(ResourceBundle rb, String key) {
    Color val = Color.black;
    String szVal;

    try {
      szVal = rb.getString(key);
      val = convertColor(szVal.trim());
    } catch (MissingResourceException mre) {
      // do nothing here
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is not defined: " + npe.getMessage());
    }
    return (val);
  }

  /**
   * Returns a default font to be used through the entire GUI.
   * 
   * @param bundle
   *          a resource bundle.
   */
  public static Font getMainFont(ResourceBundle rb) {
    String szFntName;
    Font fnt = MAINFONT;
    int size;

    try {
      if (rb != null) {
        if (_mainFont == null) {
          szFntName = rb.getString("app.font.name");
          size = Integer.valueOf(rb.getString("app.font.size")).intValue();
          _mainFont = new Font(szFntName, Font.PLAIN, size);
        }
        fnt = _mainFont;
      }
    } catch (MissingResourceException mre) {
      EZLogger.warn("Unable to find resources: " + mre.getMessage());
    } catch (NumberFormatException nfe) {
      EZLogger.warn("Value for font size does not contain an integer: "
          + nfe.getMessage());
    }
    return (fnt);
  }

  /**
   * Retrieves of value for a specific key from the Properties file.
   * 
   * @param key
   *          a resource key
   * @return the value associated to key. Returns "?" is key if not found or not
   *         defined, or if Properties file is not found.
   */
  public static String getFileContent(ResourceBundle rb, String key) {
    StringBuffer szBuf;
    BufferedReader in = null;
    URL url;
    String szVal = UNKNOWNSTRING, line;

    try {
      url = EZEnvironmentImplem.class.getResource(rb.getString(key));
      in = new BufferedReader(new InputStreamReader(url.openStream()));
      szBuf = new StringBuffer();
      while ((line = in.readLine()) != null) {
        szBuf.append(line);
        szBuf.append("\n");
      }
      szVal = szBuf.toString();
    } catch (MissingResourceException mre) {
      // do nothing here
    } catch (NullPointerException npe) {
      EZLogger.warn("Key is null: " + npe.getMessage());
    } catch (IOException ioe) {
      EZLogger.warn("IO Exception: " + ioe.getMessage());
    }
    try {
      if (in != null)
        in.close();
    } catch (IOException ex) {
    }
    return (szVal);
  }

  /**
   * Returns an Image given its name, null if not found. See this.getImageIcon()
   * for more information.
   */
  public static Image getImage(String imageName) {
    ImageIcon img = getImageIcon(imageName);
    if (img != null)
      return img.getImage();
    else
      return null;
  }

  /**
   * Returns an ImageIcon given its name, null if not found.
   * 
   * @param imageName
   *          an image name which just consists of name follows by a period and
   *          an extension. Example: new.gif. If an absolute path is required to
   *          get the image, consider adding the path using
   *          this.addSearchPath().
   */
  public static ImageIcon getImageIcon(String imageName) {
    int i, size, j, size2;
    URL url;
    Class<?> cl;

    if (imageName != null && imageName.length() > 1) {
      size = _imgPaths.size();
      // try by path
      for (i = 0; i < size; i++) {
        url = EZEnvironmentImplem.class.getResource(_imgPaths.get(i)
            + imageName.trim());
        if (url != null) {
          return new ImageIcon(url);
        }
      }
      // try by class (for JAR)
      size = _imgClasses.size();
      for (i = 0; i < size; i++) {
        size2 = _imgClasses.size();
        for (j = 0; j < size2; j++) {
          cl = (Class<?>) _imgClasses.get(j);
          url = cl.getResource(imageName.trim());
          if (url != null) {
            return new ImageIcon(url);
          }
        }
      }
    }
    return null;
  }

  /**
   * Loads an image given a file name.
   */
  public static ImageIcon loadImageIcon(File f) {
    ImageIcon ii = null;

    if (f == null || f.exists() == false || f.length() == 0l)
      return null;
    try {
      ii = new ImageIcon(f.toURI().toURL());
      if (ii.getIconHeight() <= 0 && ii.getIconWidth() <= 0) {
        ii = null;
      }
    } catch (Exception e) {
    }
    return ii;
  }

  /**
   * Add a class used to enable JRE to locate resources such as images.
   */
  public static void addResourceLocator(Class<?> cl) {
    addSearchClass(cl);
    addSearchPath("/" + cl.getPackage().getName().replace('.', '/'));
  }

  /**
   * Remove a class used to enable JRE to locate resources such as images.
   */
  public static void removeResourceLocator(Class<?> cl) {
    removeSearchClass(cl);
    removeSearchPath("/" + cl.getPackage().getName().replace('.', '/'));
  }

  /**
   * Clear the resource locator.
   */
  public static void clearResourceLocator() {
    clearSearchPath();
    clearSearchClass();
  }

  /**
   * Adds a path in the Image search path list. A 'path' is actually the name of
   * a package that is supposed to also contain image ressources. A path has to
   * start with '/' and has to use '/' instead of '.'. Example: you can pass in
   * here "/my/package/" when you want to get images from the package
   * "my.package".
   */
  public static void addSearchPath(String path) {
    if (path == null)
      return;

    if (path.endsWith(File.separator) == false) {
      path += File.separator;
    }

    _imgPaths.add(path);
  }

  /**
   * Removes Image 'path' from the Image search path list.
   */
  private static void removeSearchPath(String path) {
    _imgPaths.remove(path);
  }

  /**
   * Clears the Image search path list.
   */
  private static void clearSearchPath() {
    _imgPaths.clear();
  }

  /**
   * Adds a Class in the Image search Class list. This is to be used when
   * application is shipped within a JAR: search path seems not to work, so use
   * this method to add a class that is located is the same directory as the
   * image to load.
   */
  public static void addSearchClass(Class<?> cl) {
    _imgClasses.add(cl);
  }

  /**
   * Removes Image 'Class' from the Image search Class list.
   */
  private static void removeSearchClass(Class<?> cl) {
    _imgClasses.remove(cl);
  }

  /**
   * Clears the Image search class list.
   */
  private static void clearSearchClass() {
    _imgClasses.clear();
  }

  public static ImageIcon setMenuTagOnIcon(ImageIcon icon) {
    BufferedImage bi;
    Graphics2D g;
    Color clr;
    Image image;
    int width, height;
    int[] xP, yP;

    image = icon.getImage();
    width = icon.getIconWidth();
    height = icon.getIconHeight();
    xP = new int[] { width - 5, width - 3, width, width - 5 };
    yP = new int[] { 0, 3, 0, 0 };
    bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    g = bi.createGraphics();
    g.drawImage(image, 0, 0, new Color(255, 255, 255, 0), null);
    clr = g.getColor();
    g.setColor(Color.BLACK);
    g.fillPolygon(xP, yP, 4);
    g.setColor(clr);
    return new ImageIcon(bi);
  }

  public static String[] tokenize(String input) {
    return tokenize(input, ",\t\n\r\f");
  }

  public static String[] tokenize(String input, String delim) {
    StringTokenizer tokenizer;
    String str[];
    int i = 0;

    if (input == null)
      return new String[] {};

    tokenizer = new StringTokenizer(input, delim);

    if (tokenizer.countTokens() == 0)
      return new String[] {};

    str = new String[tokenizer.countTokens()];
    while (tokenizer.hasMoreTokens()) {
      str[i] = (String) tokenizer.nextToken().trim();
      i++;
    }

    return str;
  }

  /**
   * Returns the Operating System identifier.
   * 
   * @return one of the XXX_OS constants defined in this class
   */
  public static int getOSType() {
    String osName;

    osName = System.getProperty("os.name");
    osName = osName.toLowerCase();
    if (osName.indexOf("windows") >= 0) {
      return EZEnvironment.WINDOWS_OS;
    } else if (osName.indexOf("linux") >= 0) {
      return EZEnvironment.LINUX_OS;
    } else if (osName.equals("mac os x")) {
      return EZEnvironment.MAC_OS;
    } else {
      return EZEnvironment.OTHER_OS;
    }
  }

  /**
   * Returns the OS name.
   */
  public static String getOSName() {
    return EZEnvironment.OS_NAMES[getOSType()];
  }

  public static void setUserDefinedMessagesResourceBundle(ResourceBundle rb) {
    _userDefinedMessagesBundle = rb;
  }

  public static void setUserDefinedActionsResourceBundle(ResourceBundle rb) {
    _userDefinedActionsBundle = rb;
  }

  public static EZActionManager getActionsManager() {
    if (_actionManager == null) {
      try {
        _actionManager = new EZActionManager(
            _userDefinedActionsBundle != null ? _userDefinedActionsBundle
                : ResourceBundle.getBundle(Accessor.class.getPackage()
                    .getName() + ".ui"));
      } catch (Exception ex2) {
        _actionManager = null;
        EZLogger.warn(ex2.toString());
      }
    }
    return _actionManager;
  }

  public static void setHandCursor() {
    if (_parent != null)
      _parent.setCursor(HAND_CURSOR);
  }

  public static void setDefaultCursor() {
    if (_parent != null)
      _parent.setCursor(NORMAL_CURSOR);
  }

  public static void setWaitCursor() {
    if (_parent != null)
      _parent.setCursor(WAIT_CURSOR);
  }

  public static String inputValueMessage(Component parent, String msg,
      String initValue) {
    Object obj = JOptionPane.showInputDialog(parent == null ? _parent
        : SwingUtilities.windowForComponent(parent), msg, EZApplicationBranding
        .getAppName(), JOptionPane.QUESTION_MESSAGE, null, null, initValue);
    return (obj != null ? obj.toString() : null);
  }

  public static String inputValueMessage(Component parent, String msg) {
    return JOptionPane.showInputDialog(parent == null ? _parent
        : SwingUtilities.windowForComponent(parent), msg, EZApplicationBranding
        .getAppName(), JOptionPane.QUESTION_MESSAGE);
  }

  public static boolean confirmMessage(Component parent, String msg) {
    int ret;

    EZEnvironmentImplem.setDefaultCursor();
    ret = JOptionPane.showConfirmDialog(parent == null ? _parent
        : SwingUtilities.windowForComponent(parent), msg, EZApplicationBranding
        .getAppName(), JOptionPane.YES_NO_OPTION);
    return (ret == JOptionPane.YES_OPTION);
  }

  public static void displayErrorMessage(Component parent, String msg) {
    EZEnvironmentImplem.setDefaultCursor();
    JOptionPane.showMessageDialog(
        parent == null ? _parent : SwingUtilities.windowForComponent(parent),
        msg, EZApplicationBranding.getAppName(), JOptionPane.ERROR_MESSAGE);
  }

  public static void displayWarnMessage(Component parent, String msg) {
    EZEnvironmentImplem.setDefaultCursor();
    JOptionPane.showMessageDialog(
        parent == null ? _parent : SwingUtilities.windowForComponent(parent),
        msg, EZApplicationBranding.getAppName(), JOptionPane.WARNING_MESSAGE);
  }

  public static void displayInfoMessage(Component parent, String msg) {
    EZEnvironmentImplem.setDefaultCursor();
    JOptionPane.showMessageDialog(
        parent == null ? _parent : SwingUtilities.windowForComponent(parent),
        msg, EZApplicationBranding.getAppName(),
        JOptionPane.INFORMATION_MESSAGE);
  }

  public static Frame getParentFrame() {
    return _parent;
  }

  public static void setParentFrame(Frame compo) {
    _parent = compo;
  }

  public static String getMessage(String key) {
    try {
      if (key.startsWith("__EZ")) {
        return RESOURCE_BUNDLE.getString(key);
      } else {
        return _userDefinedMessagesBundle.getString(key);
      }
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }

  public static void setApplicationArguments(String[] args) {
    _applicationArguments = args;
  }

  public static String[] getApplicationArguments() {
    return _applicationArguments;
  }

  public static void setUIStarterListener(EZUIStarterListener listener) {
    _appListener = listener;
  }

  public static EZUIStarterListener getUIStarterListener() {
    return _appListener;
  }

  public static Properties loadProperties(InputStream is) throws IOException {
    Properties props = null;
    Enumeration<Object> e;
    String key, value;

    props = new Properties();
    props.load(is);
    e = props.keys();
    while (e.hasMoreElements()) {
      key = e.nextElement().toString();
      value = props.getProperty(key).trim();
      props.setProperty(key, value);
    }
    return props;
  }

  public static boolean saveProperties(Properties props, OutputStream os,
      String comments) {
    boolean bRet = false;

    try {
      props.store(os, comments);
      bRet = true;
    } catch (Exception ex) {
      EZLogger.warn(EZEnvironmentImplem.getMessage("__EZKResources.err4")
          + ": " + ex);
    }
    return bRet;
  }

  public static JPanel getTitlePanel(String title) {
    JLabel lbl;
    JPanel pnl, mainPnl;

    pnl = new JPanel();
    pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
    lbl = new JLabel(title != null ? title : "Feature Editor");
    lbl.setOpaque(false);
    // lbl.setForeground(Color.WHITE);
    if (_defAlign.equals(CONF_PNL_ALIGN.center)) {
      pnl.add(Box.createHorizontalGlue());
    }
    pnl.add(lbl);
    pnl.add(Box.createHorizontalGlue());
    // pnl.setBackground(Color.DARK_GRAY);
    pnl.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
    mainPnl = new JPanel(new BorderLayout());
    mainPnl.add(pnl, BorderLayout.CENTER);
    mainPnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
    return mainPnl;
  }

  public static void setMasterConfigurationFile(String confFile) {
    _masterConfigurationFile = confFile;
  }

  public static String getMasterConfigurationFile() {
    return _masterConfigurationFile;
  }

  public static boolean confirmBeforeExit() {
    return _confirmBeforeExit;
  }

  public static void setConfirmBeforeExit(boolean confirm) {
    _confirmBeforeExit = confirm;
  }

}
