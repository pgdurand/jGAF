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

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;

/**
 * Utilities for capture UI components (or screen regions into image files.)
 * 
 * @author Patrick G. Durand
 */
public class ImageCapture {
  private static final Color QUERY_CELL_BK_COLOR = new Color(184,207,229);

  /**
   * Create a BufferedImage for Swing components.
   * 
   * @param component Swing component to create image from 
   * @param fileName name of file to be created or null 
   * @return image the image for the given region 
   * @exception IOException if an error occurs during writing
   */
  public static BufferedImage createImage(JComponent component,
      String fileName) throws IOException {
    if (component instanceof JTable)
      return createTableImage((JTable) component, fileName);
    Dimension d = component.getSize();
    checkMemory(d.width, d.height);
    BufferedImage image = new BufferedImage(d.width, d.height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    component.paintAll(g2d);
    g2d.dispose();
    ImageCapture.writeImage(image, fileName);
    return image;
  }
  /**
   * Create a BufferedImage for Swing components.
   * 
   * @param components Swing components to create image from 
   * @param fileName name of file to be created or null 
   * @return image the image for the given region 
   * @exception IOException if an error occurs during writing
   */
  public static BufferedImage createImage(JComponent[] components,
      String fileName) throws IOException {
    Dimension       dMain, dColHeader, dRowHeader;
    BufferedImage   image;
    Graphics2D      g2d;
    int             decal = 0;//1;

    dMain = components[0].getSize();
    if (components[1]!=null)
      dColHeader = components[1].getSize();
    else
      dColHeader = new Dimension(0,0);
    dRowHeader = components[2].getSize();
    checkMemory(dMain.width+dRowHeader.width+decal, dMain.height+dColHeader.height+decal);
    image = new BufferedImage(
        dMain.width+dRowHeader.width+decal, 
        dColHeader.height+dMain.height+decal,
        BufferedImage.TYPE_INT_RGB);
    g2d = image.createGraphics();
    g2d.setBackground(QUERY_CELL_BK_COLOR);
    g2d.clearRect(0,0,dRowHeader.width,dMain.height+dColHeader.height+decal);
    g2d.translate(0, dColHeader.height+decal);
    components[2].paintAll(g2d);
    g2d.translate(dRowHeader.width+decal, -(dColHeader.height+decal));
    if (components[1]!=null)
      components[1].paintAll(g2d);
    g2d.translate(0, dColHeader.height+decal);
    components[0].paintAll(g2d);
    g2d.translate(-(dRowHeader.width+decal), -(dColHeader.height+decal));
    g2d.dispose();
    ImageCapture.writeImage(image, fileName);
    return image;
  }
  private static void checkMemory(int w, int h) throws OutOfMemoryError{
    long freeMem = Runtime.getRuntime().freeMemory();
    long imSize = w*h;
    if (imSize>freeMem)
      throw new OutOfMemoryError("Not enough memory to create an image.");
  }
  private static BufferedImage createTableImage(JTable table,
      String fileName) throws IOException {
    Dimension       dTable, dHeader;
    BufferedImage   image;
    Graphics2D      g2d;
    JTableHeader    tabHeader;

    dTable = table.getSize();
    tabHeader = table.getTableHeader();
    if (tabHeader!=null){
      dHeader = tabHeader.getSize();
    }
    else{
      dHeader = new Dimension(-1, -1);
    }
    checkMemory(dTable.width, dHeader.height+dTable.height+1);
    image = new BufferedImage(dTable.width, dHeader.height+dTable.height+1,
        BufferedImage.TYPE_INT_RGB);
    g2d = image.createGraphics();
    if (tabHeader!=null){
      table.getTableHeader().paintAll(g2d);
      g2d.translate(0, dHeader.height+1);
      table.paintAll(g2d);
      g2d.translate(0, -(dHeader.height+1));
    }
    else{
      table.paintAll(g2d);
    }
    g2d.dispose();
    ImageCapture.writeImage(image, fileName);
    return image;
  }

  /**
   * Create a BufferedImage for AWT components.
   * 
   * @param component AWT component to create image from 
   * @param fileName name of file to be created or null 
   * @return image the image for the given region 
   * @exception IOException if an error occurs during writing
   */
  public static BufferedImage createImage(Component component, String fileName)
      throws AWTException, IOException {
    Point p = new Point(0, 0);
    SwingUtilities.convertPointToScreen(p, component);
    Rectangle region = component.getBounds();
    region.x = p.x;
    region.y = p.y;
    checkMemory(region.width-region.x, region.height-region.y);
    return ImageCapture.createImage(region, fileName);
  }


  /**
   * Create a BufferedImage for AWT components.
   * 
   * @param component AWT component to create image from 
   * @param fileName name of file to be created or null 
   * @return image the image for the given component
   * @exception IOException if an error occurs during writing
   */
  public static BufferedImage createImageFromComponent(Component component, String fileName) throws IOException {
    if(component==null){return null;}
    int width = component.getWidth();
    int height = component.getHeight();
    checkMemory(width, height);
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    component.paintAll(g);
    g.dispose();
    writeImage(image, fileName);
    return image;
  }

  /**
   * Create a BufferedImage from a rectangular region on the screen.
   * 
   * @param region region on the screen to create image from
   * @param fileName name of file to be created or null
   * @return image the image for the given region
   * @exception AWTException see Robot class constructors
   * @exception IOException if an error occurs during writing
   */
  public static BufferedImage createImage(Rectangle region, String fileName)
      throws AWTException, IOException {
    BufferedImage image = new Robot().createScreenCapture(region);
    ImageCapture.writeImage(image, fileName);
    return image;
  }

  /**
   * Convenience method to create a BufferedImage of the desktop
   * 
   * @param fileName name of file to be created or null
   * @return image the image for the given region
   * @exception AWTException see Robot class constructors
   * @exception IOException if an error occurs during writing
   */
  public static BufferedImage createDesktopImage(String fileName)
      throws AWTException, IOException {
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle region = new Rectangle(0, 0, d.width, d.height);
    return ImageCapture.createImage(region, fileName);
  }

  /**
   * Write a BufferedImage to a File.
   * 
   * @param image image to be written
   * @param fileName name of file to be created or null
   * @exception IOException if an error occurs during writing
   */
  public static void writeImage(BufferedImage image, String fileName)
      throws IOException {
    if (fileName == null)
      return;

    int offset = fileName.lastIndexOf(".");
    String type = offset == -1 ? "jpg" : fileName.substring(offset + 1);

    ImageIO.write(image, type, new File(fileName));
  }

}
