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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JPanel;


/**
 * A basic implementation of a JPanel with a gradient colored background.
 *  
 * @author Patrick G. Durand
 */

public class GradientPanel extends JPanel {
  private static final long serialVersionUID = 3645214319959566285L;
  private boolean _selected;
  private int     _orientation;
  private Color   _color1;
  private Color   _color2;

  public static final int GRAD_ORIENTATION_LtoR   = 0;
  public static final int GRAD_ORIENTATION_TLtoBR = 1;

  public GradientPanel(Color color1, Color color2) {
    super();
    _color1 = color1;
    _color2 = color2;
  }
  public void setGradientOrientation(int orientation){
    _orientation = orientation;
  }
  public void setSelected(boolean sel){
    _selected = sel;
  }
  public boolean isSelected(){
    return _selected;
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (!isOpaque() || isSelected()) {
      return;
    }
    int width  = getWidth();
    int height = getHeight();

    Graphics2D g2 = (Graphics2D) g;
    Paint storedPaint = g2.getPaint();
    GradientPaint gp;
    switch(_orientation){
      case GRAD_ORIENTATION_TLtoBR:
        gp = new GradientPaint(0, 0, _color1, width, height, _color2);
        break;
      default:
        gp = new GradientPaint(0, 0, _color1, width, 0, _color2);
    }
    g2.setPaint(gp);

    g2.fillRect(0, 0, width, height);
    g2.setPaint(storedPaint);
  }
}
