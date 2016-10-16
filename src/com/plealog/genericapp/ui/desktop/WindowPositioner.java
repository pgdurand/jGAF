/**
 * Copyright (c) 2005 Guy Davis
 * davis@guydavis
 * http://www.guydavis.ca/projects/oss/
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.plealog.genericapp.ui.desktop;

import java.awt.Point;
import java.util.List;

import javax.swing.JInternalFrame;

/**
 * Interface for classes which will be used to determine the position of windows
 * as they are added to the desktop. Useful if the type of windows you're adding
 * require a custom layout.
 */
public interface WindowPositioner {

    /**
     * Determines the best position to place a new frame on the desktop based on
     * the position of the existing windows.
     * 
     * @param newFrame The new frame being added to the desktop.
     * @param frames The list of existing, visible frames on the desktop.
     * @return The position in the container to place the new frame.
     */
    Point getPosition(JInternalFrame newFrame, List<JInternalFrame> frames);
}
