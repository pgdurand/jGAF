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

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * Positions the windows in a JDesktopPane MDI by cascading them down one by one
 * from the upper left.  This class only sets the positions, the size is set by the window itself.
 */
public class CascadingWindowPositioner implements WindowPositioner {

	/** The desktop whose windows will be positioned */
	protected JDesktopPane desktop;

	/**
	 * @param desktop The desktop to arrange
	 */
	public CascadingWindowPositioner(JDesktopPane desktop) {
		this.desktop = desktop;
	}

	public Point getPosition(JInternalFrame newFrame,
			List<JInternalFrame> frames) {
		int x = 0;
		int y = 0;

		if (frames.size() == 0) {
			return new Point(x, y);
		}

		outer: while (true) {
			for (JInternalFrame frame : frames) {
				if ((frame.getLocation().x == x)
						&& (frame.getLocation().y == y)) {
					x += 25;
					y += 25;

					if ((x + newFrame.getWidth()) > this.desktop.getWidth()) {
						x = 0;
					}

					if ((y + newFrame.getHeight()) > this.desktop.getHeight()) {
						y = 0;
					}

					if ((x == 0) && (y == 0)) {
						break outer;
					}
				}
				else { // Found a clear location
					break outer;
				}
			}
		}
		return new Point(x, y);
	}
}
