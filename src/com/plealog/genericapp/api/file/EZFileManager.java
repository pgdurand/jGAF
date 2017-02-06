/* Copyright (C) 2003-2017 Patrick G. Durand
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
package com.plealog.genericapp.api.file;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.implem.file.EZFileExtDescriptor;
import com.plealog.genericapp.implem.file.EZFileExtSelector;

/**
 * This class manages the access to file and directory as well as generic UI
 * File Chooser.
 */
public class EZFileManager {
  // a full file browser:
  // http://codereview.stackexchange.com/questions/4446/file-browser-gui
  // see also: http://www.randelshofer.ch/quaqua/

  /** default path to 'user' file resources. */
  private static String _szDefaultPath = EZFileManager.getUserHomeDirectory();

  private static boolean _useOSNativeFileDialog = (System.getProperty("os.name").indexOf("Mac OS X") >= 0);

  private EZFileManager() {
  }

  /**
   * Figures out whether or not this manager will use native OS dependent
   * FileChooser.
   * 
   * @param useNativeOSFileDialog
   *          passes in true to use {@link java.awt.FileDialog}, false to use
   *          {@link javax.swing.JFileChooser}. The former enables the display
   *          of an OS-dependent File Chooser, while the latter is the Java
   *          Swing specific File CHooser.
   */
  public static void useOSNativeFileDialog(boolean useNativeOSFileDialog) {
    _useOSNativeFileDialog = useNativeOSFileDialog;
  }

  /**
   * Figures out whether or not this manager uses native OS dependent
   * FileChooser.
   */
  public static boolean isUsingOSNativeFileDialog() {
    return _useOSNativeFileDialog;
  }

  /**
   * Returns the default directory path. It is the path used to initialize File
   * Chooser dialogues.
   */
  public static String getDefaultPath() {
    String szCurPath = _szDefaultPath;

    if (szCurPath == null)
      szCurPath = ".";
    return (szCurPath);
  }

  /**
   * Changes the default directory used in File Chooser dialogue boxes. At
   * startup, this default path is set to the user home directory.
   */
  public static void setDefaultPath(String path) {
    if (path != null)
      _szDefaultPath = path;
  }

  /**
   * Gets the user home directory. This method takes into account workaround
   * defined by http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4787931.
   */
  public static String getUserHomeDirectory() {
    String userHome = "";

    userHome = System.getProperty("user.home");

    // workaround bug #4787931 Windows specific
    // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4787931
    if (EZEnvironment.getOSType() == EZEnvironment.WINDOWS_OS) {
      if (userHome.indexOf("Windows") != -1) {
        userHome = System.getenv("USERPROFILE");
      }
    }

    return userHome;
  }

  /**
   * Manages a File Chooser to choose a file for open operations.
   * 
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForOpenAction() {
    return chooseFileForOpenAction(EZEnvironment.getParentFrame(), null, null);
  }

  /**
   * Manages a File Chooser to choose a file for open operations.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForOpenAction(String dialogTitle) {
    return chooseFileForOpenAction(EZEnvironment.getParentFrame(), dialogTitle, null);
  }

  /**
   * Manages a File Chooser to choose a file for open operations.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * 
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForOpenAction(String dialogTitle, EZFileFilter ff) {
    return chooseFileForOpenAction(EZEnvironment.getParentFrame(), dialogTitle, ff);
  }

  /**
   * Manages a File Chooser to choose a file for open operations.
   * 
   * @param parent
   *          the parent component of the File Chooser
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForOpenAction(Component parent, String dialogTitle, EZFileFilter ff) {
    File[] files;

    files = chooseFilesForOpenAction(parent, dialogTitle, ff, false);

    if (files == null)
      return null;
    else
      return files[0];
  }

  /**
   * Manages a File Chooser to choose files for open operations.
   * 
   * @return a file array or null if dialogue has been canceled.
   */
  public static File[] chooseFilesForOpenAction() {
    return chooseFilesForOpenAction(EZEnvironment.getParentFrame(), null, null, true);
  }

  /**
   * Manages a File Chooser to choose files for open operations.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @return a file array or null if dialogue has been canceled.
   */
  public static File[] chooseFilesForOpenAction(String dialogTitle) {
    return chooseFilesForOpenAction(EZEnvironment.getParentFrame(), dialogTitle, null, true);
  }

  /**
   * Manages a File Chooser to choose files for open operations.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * 
   * @return a file array or null if dialogue has been canceled.
   */
  public static File[] chooseFilesForOpenAction(String dialogTitle, EZFileFilter ff) {
    return chooseFilesForOpenAction(EZEnvironment.getParentFrame(), dialogTitle, ff, true);
  }

  /**
   * Manages a File Chooser to choose several files for open operations.
   * 
   * @param parent
   *          the parent component of the File Chooser
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * @return a file array or null if dialogue has been canceled.
   */
  public static File[] chooseFilesForOpenAction(Component parent, String dialogTitle, EZFileFilter ff) {
    return chooseFilesForOpenAction(parent, dialogTitle, ff, true);
  }

  private static File[] chooseFilesForOpenAction(Component parent, String dialogTitle, EZFileFilter ff,
      boolean enableMultipleSelection) {

    File[] files = null;

    if (parent == null) {
      parent = EZEnvironment.getParentFrame();
    }
    if (_useOSNativeFileDialog) {
      FileDialog jfc;
      if (parent instanceof Dialog) {
        jfc = new FileDialog((Dialog) parent);
      } else if (parent instanceof Frame) {
        jfc = new FileDialog((Frame) parent);
      } else {
        jfc = new FileDialog(EZEnvironment.getParentFrame());
      }

      jfc.setMode(FileDialog.LOAD);
      // dlg title
      if (dialogTitle != null && dialogTitle.length() > 0)
        jfc.setTitle(dialogTitle);
      else
        jfc.setTitle("Open");
      // default directory
      jfc.setDirectory(EZFileManager.getDefaultPath());

      jfc.setMultipleMode(enableMultipleSelection);

      // to select only file
      System.setProperty("apple.awt.fileDialogForDirectories", "false");

      jfc.setVisible(true);

      if (enableMultipleSelection) {
        if (jfc.getDirectory() != null && jfc.getFiles().length != 0) {
          String folderName = jfc.getDirectory();
          EZFileManager.setDefaultPath(folderName);
          files = jfc.getFiles();
        }
      } else {
        if (jfc.getDirectory() != null && jfc.getFile() != null) {
          String folderName = jfc.getDirectory();
          folderName += jfc.getFile();
          File file = new File(folderName);
          EZFileManager.setDefaultPath(file.getParent());
          files = new File[1];
          files[0] = file;
        }
      }

    } else {
      JFileChooser jfc = new JFileChooser();
      // file filter
      if (ff != null)
        jfc.setFileFilter(ff);
      // dlg title
      if (dialogTitle != null && dialogTitle.length() > 0)
        jfc.setDialogTitle(dialogTitle);
      else
        jfc.setDialogTitle("Open");
      // default directory
      jfc.setCurrentDirectory(new File(EZFileManager.getDefaultPath()));
      jfc.setFileHidingEnabled(false);
      jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      jfc.setMultiSelectionEnabled(enableMultipleSelection);

      // show dialog
      int returnVal = jfc.showOpenDialog(parent);
      // ok?
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        if (enableMultipleSelection) {
          files = jfc.getSelectedFiles();
          EZFileManager.setDefaultPath(files[0].getParent());
        } else {
          File file = jfc.getSelectedFile();
          EZFileManager.setDefaultPath(file.getParent());
          files = new File[1];
          files[0] = file;
        }
      }
    }
    return files;
  }

  /**
   * Manages a File Chooser to choose a file for save operations.
   * 
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForSaveAction() {
    return chooseFileForSaveAction(EZEnvironment.getParentFrame(), null, null);
  }

  /**
   * Manages a File Chooser to choose a file for save operations.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForSaveAction(String dialogTitle) {
    return chooseFileForSaveAction(EZEnvironment.getParentFrame(), dialogTitle, null);
  }

  /**
   * Manages a File Chooser to choose a file for save operations.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForSaveAction(String dialogTitle, EZFileFilter ff) {
    return chooseFileForSaveAction(EZEnvironment.getParentFrame(), dialogTitle, ff);
  }

  /**
   * Manages a File Chooser to choose a file for save operations.
   * 
   * @param parent
   *          the parent component of the File Chooser
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForSaveAction(Component parent, String dialogTitle, EZFileFilter ff) {
    return chooseFileForSaveAction(parent, dialogTitle, ff, null);
  }

  /**
   * Manages a File Chooser to choose a file for save operations.
   * 
   * @param parent
   *          the parent component of the File Chooser
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * @param types
   *          list of file types to restrict display of the dialog box to them
   * @return a file or null if dialogue has been canceled.
   */
  public static File chooseFileForSaveAction(Component parent, String dialogTitle, EZFileFilter ff,
      List<EZFileExtDescriptor> types) {
    File file = null;
    EZFileExtSelector extSelector = null;

    if (parent == null) {
      parent = EZEnvironment.getParentFrame();
    }
    if (_useOSNativeFileDialog) {
      FileDialog jfc;
      if (parent instanceof Dialog) {
        jfc = new FileDialog((Dialog) parent);
      } else if (parent instanceof Frame) {
        jfc = new FileDialog((Frame) parent);
      } else {
        jfc = new FileDialog(EZEnvironment.getParentFrame());
      }

      jfc.setMode(FileDialog.SAVE);
      // file filter
      if (ff != null) {
        jfc.setFilenameFilter(ff);
      }
      // dlg title
      if (dialogTitle != null && dialogTitle.length() > 0)
        jfc.setTitle(dialogTitle);
      else
        jfc.setTitle("Save");

      // default directory
      jfc.setDirectory(EZFileManager.getDefaultPath());

      // to select only file
      System.setProperty("apple.awt.fileDialogForDirectories", "false");

      jfc.setVisible(true);

      if (jfc.getDirectory() != null && jfc.getFile() != null) {
        String folderName = jfc.getDirectory();
        EZFileManager.setDefaultPath(folderName);
        folderName += jfc.getFile();
        file = new File(folderName);
      }
    } else {
      @SuppressWarnings("serial")
      JFileChooser jfc = new JFileChooser() {
        @Override
        public void approveSelection() {
          File f = getSelectedFile();
          if (f.exists() && getDialogType() == SAVE_DIALOG) {
            int result = JOptionPane.showConfirmDialog(getTopLevelAncestor(),
                "The selected file already exists. " + "Do you want to overwrite it?", "The file already exists",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (result) {
            case JOptionPane.YES_OPTION:
              super.approveSelection();
              return;
            case JOptionPane.NO_OPTION:
              return;
            case JOptionPane.CANCEL_OPTION:
              cancelSelection();
              return;
            }
          }
          super.approveSelection();
        }
      };
      // file filter
      if (ff != null)
        jfc.setFileFilter(ff);
      // dlg title
      if (dialogTitle != null && dialogTitle.length() > 0)
        jfc.setDialogTitle(dialogTitle);
      else
        jfc.setDialogTitle("Save");
      // default directory
      jfc.setCurrentDirectory(new File(EZFileManager.getDefaultPath()));
      jfc.setFileHidingEnabled(false);
      // extra file extension selector
      if (types != null) {
        extSelector = new EZFileExtSelector(types);
        jfc.setAccessory(extSelector);
      }
      // show dialog
      int returnVal = jfc.showSaveDialog(parent);
      // ok?
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        EZFileManager.setDefaultPath(jfc.getSelectedFile().getParent());
        file = jfc.getSelectedFile();
        if (extSelector != null) {
          file = EZFileUtils.forceFileExtension(file, extSelector.getSelectedFType().getExtension());
        }
      }
    }
    return file;
  }

  /**
   * Manages a File Chooser to choose a directory.
   * 
   * @return a directory or null if dialogue has been canceled.
   */
  public static File chooseDirectory() {
    return EZFileManager.chooseDirectory(EZEnvironment.getParentFrame(), null, null);
  }

  /**
   * Manages a File Chooser to choose a directory.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @return a directory or null if dialogue has been canceled.
   */
  public static File chooseDirectory(String dialogTitle) {
    return EZFileManager.chooseDirectory(EZEnvironment.getParentFrame(), dialogTitle, null);
  }

  /**
   * Manages a File Chooser to choose a directory.
   * 
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * @return a directory or null if dialogue has been canceled.
   */
  public static File chooseDirectory(String dialogTitle, EZFileFilter ff) {
    return EZFileManager.chooseDirectory(EZEnvironment.getParentFrame(), dialogTitle, null);
  }

  /**
   * Manages a File Chooser to choose a directory.
   * 
   * @param parent
   *          the parent component of the File Chooser
   * @param dialogTitle
   *          title of the dialogue box
   * @param ff
   *          a file filter. Use {@link EZFileTypes} to manage file filters.
   * @return a directory or null if dialogue has been canceled.
   */
  public static File chooseDirectory(Component parent, String dialogTitle, EZFileFilter ff) {
    File file = null;
    if (parent == null) {
      parent = EZEnvironment.getParentFrame();
    }
    if (_useOSNativeFileDialog) {
      FileDialog jfc;
      if (parent instanceof Dialog) {
        jfc = new FileDialog((Dialog) parent);
      } else if (parent instanceof Frame) {
        jfc = new FileDialog((Frame) parent);
      } else {
        jfc = new FileDialog(EZEnvironment.getParentFrame());
      }

      jfc.setMode(FileDialog.LOAD);
      // file filter
      if (ff != null) {
        jfc.setFilenameFilter(ff);
      }
      // dlg title
      if (dialogTitle != null && dialogTitle.length() > 0)
        jfc.setTitle(dialogTitle);
      else
        jfc.setTitle("Select a directory");

      // default directory
      jfc.setDirectory(EZFileManager.getDefaultPath());

      // to select only directory:
      System.setProperty("apple.awt.fileDialogForDirectories", "true");

      jfc.setVisible(true);

      if (jfc.getDirectory() != null && jfc.getFile() != null) {
        String folderName = jfc.getDirectory();
        EZFileManager.setDefaultPath(folderName);
        folderName += jfc.getFile();
        file = new File(folderName);
      }
    } else {
      JFileChooser jfc = new JFileChooser();
      // file filter
      if (ff != null)
        jfc.setFileFilter(ff);
      // dlg title
      if (dialogTitle != null && dialogTitle.length() > 0)
        jfc.setDialogTitle(dialogTitle);
      else
        jfc.setDialogTitle("Select a directory");
      // default directory
      jfc.setCurrentDirectory(new File(EZFileManager.getDefaultPath()));
      jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      jfc.setFileHidingEnabled(false);
      if (jfc.showOpenDialog(parent == null ? EZEnvironment.getParentFrame() : parent) == JFileChooser.APPROVE_OPTION) {
        file = jfc.getSelectedFile();
        EZFileManager.setDefaultPath(file.getAbsolutePath());
      }
    }

    return file;
  }

}
