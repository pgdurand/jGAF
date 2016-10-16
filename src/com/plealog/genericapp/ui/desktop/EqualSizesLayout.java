/**
 * Copyright (c) 2005 Santhosh Kumar
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.SwingConstants;

/**
 * A layout manager which places equally sized items in a single row that has a
 * maximum size. Useful for task bar like layouts. Original author was Santhosh
 * Kumar at https://myswing.dev.java.net/MyBlog/
 */
public class EqualSizesLayout implements LayoutManager, SwingConstants {

    /** The number of pixels to place between components */
    private int gap;

    /** The alignment of the layout (LEFT or RIGHT) */
    private int alignment;

    /**
     * Creates a layout manager with the given layout and gap.
     * 
     * @param alignment Either LEFT or RIGHT
     * @param gap Pixels to put between components
     */
    public EqualSizesLayout(int alignment, int gap) {
        setGap(gap);
        setAlignment(alignment);
    }

    /**
     * @return The alignment of the layout manager (LEFT or RIGHT).
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * @param alignment The alignment of the layout manager (LEFT or RIGHT).
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    /**
     * @return The gap to place between components in pixels.
     */
    public int getGap() {
        return gap;
    }

    /**
     * @param gap The gap to place between components in pixels.
     */
    public void setGap(int gap) {
        this.gap = gap;
    }

    /**
     * Get the preferred size of the biggest component and the size of the
     * overall layout.
     * 
     * @param children The children on this component.
     * @return The preferred sizes of components and the overall layout.
     */
    private Dimension[] dimensions(Component children[]) {
        int maxWidth = 0;
        int maxHeight = 0;
        int visibleCount = 0;
        Dimension componentPreferredSize;

        for (int i = 0, c = children.length; i < c; i++) {
            if (children[i].isVisible()) {
                componentPreferredSize = children[i].getPreferredSize();
                maxWidth = Math.max(maxWidth, componentPreferredSize.width);
                maxHeight = Math.max(maxHeight, componentPreferredSize.height);
                visibleCount++;
            }
        }

        int usedWidth = maxWidth * visibleCount + gap * (visibleCount - 1);
        int usedHeight = maxHeight;
        return new Dimension[] { new Dimension(maxWidth, maxHeight),
                new Dimension(usedWidth, usedHeight), };
    }

    /**
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public void layoutContainer(Container container) {
        Insets insets = container.getInsets();

        Component[] children = container.getComponents();
        Dimension dim[] = dimensions(children);

        int allowedWidth = container.getWidth();
        int maxWidth = dim[0].width;
        int maxHeight = dim[0].height;
        int usedWidth = dim[1].width;

        // Narrow the components from their preferred size if we're limited for
        // space
        if (usedWidth > allowedWidth) {
            usedWidth = allowedWidth;
            maxWidth = (allowedWidth / children.length)
                    - (gap * children.length);
        }

        switch (alignment) {
        case LEFT:
        case TOP:
            for (int i = 0, c = children.length; i < c; i++) {
                if (!children[i].isVisible())
                    continue;
                children[i].setBounds(insets.left + (maxWidth + gap) * i,
                        insets.top, maxWidth, maxHeight);
            }
            break;
        case RIGHT:
        case BOTTOM:
            for (int i = 0, c = children.length; i < c; i++) {
                if (!children[i].isVisible())
                    continue;
                children[i].setBounds(container.getWidth() - insets.right
                        - usedWidth + (maxWidth + gap) * i, insets.top,
                        maxWidth, maxHeight);
            }
            break;
        }
    }

    /**
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public Dimension minimumLayoutSize(Container c) {
        return preferredLayoutSize(c);
    }

    /**
     * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
     */
    public Dimension preferredLayoutSize(Container container) {
        Insets insets = container.getInsets();

        Component[] children = container.getComponents();
        Dimension dim[] = dimensions(children);

        int usedWidth = dim[1].width;
        int usedHeight = dim[1].height;

        return new Dimension(insets.left + usedWidth + insets.right, insets.top
                + usedHeight + insets.bottom);
    }

    /**
     * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
     *      java.awt.Component)
     */
    public void addLayoutComponent(String string, Component comp) {
    }

    /**
     * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
     */
    public void removeLayoutComponent(Component c) {
    }
}