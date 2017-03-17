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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

/**
 * This is a custom border that has a shadow on the right and lower sides.
 * 
 * @author Patrick G. Durand
 */
public class ShadowBorder extends AbstractBorder {
  private static final long   serialVersionUID = 6131669415893400535L;
  private static final Insets INSETS           = new Insets(1, 1, 3, 3);

  public Insets getBorderInsets(Component c) {
    return INSETS;
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {

    Color shadow = UIManager.getColor("controlShadow");
    if (shadow == null) {
      shadow = Color.GRAY;
    }
    Color lightShadow = new Color(shadow.getRed(), shadow.getGreen(),
        shadow.getBlue(), 170);
    Color lighterShadow = new Color(shadow.getRed(), shadow.getGreen(),
        shadow.getBlue(), 70);
    g.translate(x, y);

    g.setColor(shadow);
    g.fillRect(0, 0, w - 3, 1);
    g.fillRect(0, 0, 1, h - 3);
    g.fillRect(w - 3, 1, 1, h - 3);
    g.fillRect(1, h - 3, w - 3, 1);
    // Shadow line 1
    g.setColor(lightShadow);
    g.fillRect(w - 3, 0, 1, 1);
    g.fillRect(0, h - 3, 1, 1);
    g.fillRect(w - 2, 1, 1, h - 3);
    g.fillRect(1, h - 2, w - 3, 1);
    // Shadow line2
    g.setColor(lighterShadow);
    g.fillRect(w - 2, 0, 1, 1);
    g.fillRect(0, h - 2, 1, 1);
    g.fillRect(w - 2, h - 2, 1, 1);
    g.fillRect(w - 1, 1, 1, h - 2);
    g.fillRect(1, h - 1, w - 2, 1);
    g.translate(-x, -y);
  }
}
