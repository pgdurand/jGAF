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
package com.plealog.genericapp.ui.starter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.plealog.genericapp.api.EZApplicationBranding;
import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.EZUIStarterListener;
import com.plealog.genericapp.ui.apple.EZAppleConfigurator;
import com.plealog.genericapp.ui.menu.EZActionManager;
import com.plealog.resources.Accessor;

public class EZUIStarterImplem {
	
	@SuppressWarnings("serial")
	private class EZFrame extends JFrame{ 
		
		public EZFrame(){
			super();
			
			if(EZEnvironment.getOSType()==EZEnvironment.MAC_OS){
				EZAppleConfigurator.enableFullScreenMode(this);
			}
			JMenuBar   menuBar = null;
			EZEnvironment.setParentFrame(this);
			
	    	EZActionManager aManager = EZEnvironment.getActionsManager();
	    	if (aManager!=null){
	        	menuBar = aManager.createMenubar();
	    	}
			this.setTitle(EZApplicationBranding.getAppName()+" - "+EZApplicationBranding.getAppVersion());
			this.setIconImage(EZApplicationBranding.getAppIcon().getImage());
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			if(menuBar!=null){
				this.setJMenuBar(menuBar);
			}
			EZUIStarterListener listener = EZEnvironment.getUIStarterListener(); 
			if (listener!=null){
			  Component cp = listener.getApplicationComponent();
			  if (cp !=null){
          this.getContentPane().add(cp);
			  }
			}
			this.addWindowListener(new MainWindowAdapter());
			this.pack();
		}
	}
	private class MainWindowAdapter extends WindowAdapter{
        public MainWindowAdapter(){
        }
        
        public void windowClosing(WindowEvent e){
        	EZEnvironment.getActionsManager().getDefaultActionHandler().handleExit();
        }
    }
	private class UIStarter extends Thread {
		public void run() {
			EZFrame frame;
			
			frame = new EZFrame();
			Runnable runner = new FrameShower(frame);
	        EventQueue.invokeLater(runner);
		}
	}
	
  private class UIStarterListenerDisplayedhandler extends Thread {
    public void run() {
      if(EZEnvironment.getUIStarterListener()!=null){
        EZEnvironment.getUIStarterListener().frameDisplayed();
      }
    }
  }
	
	private void center(Window frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation(
            screenSize.width / 2 - (frameSize.width / 2),
            screenSize.height / 2 - (frameSize.height / 2));
    }
    private Dimension getFrameStarterDimension(){
    	Dimension      dim, screen;
    	int            maxWidth, maxHeight;

        // Get max screen size from toolkit
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        //System.out.println(screen);
        maxWidth = screen.width-200;//100 = 50 pixels on each side (50 * 2)
        maxHeight = screen.height-200;
        //890, 650: size for manual screenshots
		dim = new Dimension(
			Math.max(890, maxWidth),
			Math.max(650, maxHeight));
		return dim;
    	
    }
    private class FrameShower implements Runnable {
        final JFrame frame;
        public FrameShower(JFrame frame) {
            this.frame = frame;
        }
        public void run() {
        	String frameBounds = EZEnvironment.getApplicationProperty(EZConstants.PROPERTY_FRAME_BOUNDS);

        	if(frameBounds!=null){
        		StringTokenizer tokenizer = new StringTokenizer(frameBounds,",");
        		int x = Integer.valueOf(tokenizer.nextToken());
        		int y = Integer.valueOf(tokenizer.nextToken());
        		int width = Integer.valueOf(tokenizer.nextToken());
        		int height = Integer.valueOf(tokenizer.nextToken());
        		frame.setSize(width, height);
        		frame.setLocation(x, y);
        		
        	}
        	else{
	        	frame.setSize(getFrameStarterDimension());
	            center(frame);
	            Rectangle rect = frame.getBounds();
	            String value = rect.x+","+rect.y+","+rect.width+","+rect.height;
	            EZEnvironment.setApplicationProperty(EZConstants.PROPERTY_FRAME_BOUNDS, value);
        	}
            
			if(EZEnvironment.getUIStarterListener()!=null){
				EZEnvironment.getUIStarterListener().postStart();
			}
			frame.addComponentListener(new ComponentAdapter(){
	            private void saveSize(){
	            	Rectangle rect = frame.getBounds();
		            String value = rect.x+","+rect.y+","+rect.width+","+rect.height;
		            EZEnvironment.setApplicationProperty(EZConstants.PROPERTY_FRAME_BOUNDS, value);
	            }
				public void componentResized(ComponentEvent e) {
					saveSize();
	            }
	            public void componentMoved(ComponentEvent e) {
	            	saveSize();
	            }
	        });
			frame.setVisible(true);
			EventQueue.invokeLater(new UIStarterListenerDisplayedhandler());
        }
    }
    public void startApplication(String[] args){
		EZEnvironment.setApplicationArguments(args);
		
		EZEnvironment.addResourceLocator(Accessor.class);
		
		if(EZEnvironment.getUIStarterListener()!=null){
			EZEnvironment.getUIStarterListener().preStart();
		}
		
		UIStarter jStarter= new UIStarter();
        jStarter.start();
    	
    }

}
