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

import javax.swing.ImageIcon;

import com.plealog.genericapp.ui.starter.EZSplashScreenImplem;

/**
 * Factory of splash screen.
 */
public class EZSplashScreenFactory {

  private EZSplashScreenFactory(){}
  
	/**
	 * Creates a concrete splash screen using the provide image.
	 *
	 * @param image the image to display within the splash screen.
	 */
	public static EZSplashScreen startSplashSreen(ImageIcon image){
		return new EZSplashScreenImplem(image);
	}
	/**
	 * Creates a concrete splash screen using the provide image and activate a progress bar.
	 *
	 * @param image the image to display within the splash screen.
	 * @param showProgressBar set whether or not a progress bar has to be displayed.
	 */
	public static EZSplashScreen startSplashSreen(ImageIcon image, boolean showProgressBar){
		return new EZSplashScreenImplem(image, showProgressBar);
	}
}
