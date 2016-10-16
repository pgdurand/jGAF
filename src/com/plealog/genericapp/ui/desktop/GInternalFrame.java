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
package com.plealog.genericapp.ui.desktop;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 * Implements a JInternalFrame ready to be used along with a Documents Menu.
 *
 * @author Patrick G. Durand
 * @since 2006
 */
public class GInternalFrame extends JInternalFrame {
  private static final long serialVersionUID = 367682856415702132L;

  /**
   * No default constructor available
   */
  private GInternalFrame() {
    super();
  }

  /**
   * Constructor
   * 
   * @param doc
   *          the document to display
   * @throws IllegalArgumentException
   *           if 'doc' is null.
   */
  public GInternalFrame(JComponent viewer) {
    this();
    initInternalFrame(viewer);
  }

  /**
   * Constructor
   * 
   * @param doc
   *          the document to display
   * @param title
   * @throws IllegalArgumentException
   *           if 'doc' is null.
   */
  public GInternalFrame(JComponent viewer, String title) {
    super(title);
    initInternalFrame(viewer);
  }

  /**
   * Constructor
   * 
   * @param doc
   *          the document to display
   * @param title
   * @param resizable
   * @throws IllegalArgumentException
   *           if 'viewer' is null.
   */
  public GInternalFrame(JComponent viewer, String title, boolean resizable) {
    super(title, resizable);
    initInternalFrame(viewer);
  }

  /**
   * Constructor
   * 
   * @param doc
   *          the document to display
   * @param title
   * @param resizable
   * @param closable
   * @throws IllegalArgumentException
   *           if 'viewer' is null.
   */
  public GInternalFrame(JComponent viewer, String title, boolean resizable, boolean closable) {
    super(title, resizable, closable);
    initInternalFrame(viewer);
  }

  /**
   * Constructor
   * 
   * @param doc
   *          the document to display
   * @param title
   * @param resizable
   * @param closable
   * @param maximizable
   * @throws IllegalArgumentException
   *           if 'viewer' is null.
   */
  public GInternalFrame(JComponent viewer, String title, boolean resizable, boolean closable, boolean maximizable) {
    super(title, resizable, closable, maximizable);
    initInternalFrame(viewer);
  }

  /**
   * Constructor
   * 
   * @param doc
   *          the document to display
   * @param title
   * @param resizable
   * @param closable
   * @param maximizable
   * @param iconifiable
   * @throws IllegalArgumentException
   *           if 'viewer' is null.
   */
  public GInternalFrame(JComponent viewer, String title, boolean resizable, boolean closable, boolean maximizable,
      boolean iconifiable) {
    super(title, resizable, closable, maximizable, iconifiable);
    initInternalFrame(viewer);
  }

  /**
   * Creates the frame GUI.
   */
  private void initInternalFrame(JComponent viewer) {

    if (viewer == null)
      throw new IllegalArgumentException("viewer is null");
    this.getContentPane().add(viewer);
    this.pack();
    this.setVisible(true);
  }
}
