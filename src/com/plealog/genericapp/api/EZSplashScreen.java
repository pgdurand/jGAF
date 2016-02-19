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
package com.plealog.genericapp.api;

import java.awt.Dimension;

/**
 * A splash screen. To create a concrete instance, use {@link EZSplashScreenFactory} class.
 */
public interface EZSplashScreen {
	/**
	 * Sets the location of the splash screen. Default is the center of the desktop.
	 */
	public void setLocation(int x, int y);
    /**
     * Returns the current size of the splash screen.
     */
	public Dimension getSize();
    
    /**
     * Sets the progression of the splash. When progress bar is activated, use this method
     * to increment the progress bar. Value is in the range 0 to 100 percent.
     * 
     * @param i pregression value
     */
	public void setProgressPercent(int i);

	/**
	 * Sets a progression message.
	 */
    public void setMessage(String s);
    
    /**
     * Call this method to close the splash screen.
     */
    public void finish();
}
