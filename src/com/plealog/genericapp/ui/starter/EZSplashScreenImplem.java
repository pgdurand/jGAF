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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import com.plealog.genericapp.api.EZSplashScreen;

/**
 * This is a Splash-Screen. It displays a nice window during the start-up
 * of an application.
 *
 * @author Patrick G. Durand
 */
public class EZSplashScreenImplem extends JWindow implements EZSplashScreen {
	private static final long serialVersionUID = 7346621589220549461L;
	protected JProgressBar _progressBar;
    protected MyJLabelAA   _message;
    protected ImageIcon    _image;
    protected int          _progressVal;
    protected int          _progressHeight;
    protected boolean      _paintProgressBar;


    public EZSplashScreenImplem(ImageIcon imageicon){
        this(imageicon, new Color(0.0F, 0.0F, 0.0F, 1.0F), true);
    }

    public EZSplashScreenImplem(ImageIcon imageicon, boolean showProgressBar){
        this(imageicon, new Color(0.0F, 0.0F, 0.0F, 1.0F), showProgressBar);
    }

    public EZSplashScreenImplem(ImageIcon imageicon, Color color, boolean showProgressBar){
        Dimension    dim;
        MyJLabelAA   jlabel;
        JLayeredPane jlayeredpane;
        int          i, j;
        
        _progressVal = 0;
        _progressHeight = 18;
        _paintProgressBar = showProgressBar;
        _image = imageicon;
        i = _image.getIconHeight();
        j = _image.getIconWidth();
        dim = getToolkit().getScreenSize();
        setLocation(dim.width / 2 - j / 2, dim.height / 2 - i / 2);
        jlabel = new MyJLabelAA(_image);
        jlayeredpane = new JLayeredPane();
        jlayeredpane.setBackground(new Color(1.0F, 1.0F, 1.0F, 0.0F));
        jlabel.setBounds(0, 0, j, i);
        jlayeredpane.add(jlabel, new Integer(0));
        _message = new MyJLabelAA("Initializing...");
        _message.setForeground(color);
        _message.setFont(_message.getFont().deriveFont(2));
        _message.setBounds(2, i - _message.getPreferredSize().height, j, _message.getPreferredSize().height);
        if(_paintProgressBar){
            _progressBar = new JProgressBar(0, 100);
            _progressBar.setBounds(0, i, j, 18);
            jlayeredpane.add(_progressBar, new Integer(1));
            jlayeredpane.add(_message, new Integer(2));
        } else{
            jlayeredpane.add(_message, new Integer(1));
        }
        setLayeredPane(jlayeredpane);
        setSize(j, i + (_paintProgressBar ? _progressHeight : 0));
        setVisible(true);
        toFront();
    }

    public EZSplashScreenImplem(ImageIcon imageicon, Color color){
        this(imageicon, color, true);
    }


    public void setLocation(int x, int y){
    	super.setLocation(x, y);
    }
    public Dimension getSize(){
    	return super.getSize();
    }
    //progress bar ranges from 0 to 100%
    public void setProgressPercent(int i){
        _progressVal = i;
        if(_paintProgressBar)
            _progressBar.setValue(_progressVal);
    }

    public void setMessage(String s){
        _message.setText(s);
    }

    public void finish(){
        setVisible(false);
        dispose();
    }
    private class MyJLabelAA extends JLabel{
		private static final long serialVersionUID = 3329479900536960994L;
		public MyJLabelAA(Icon icon){
    		super(icon);
    	}
    	public MyJLabelAA(String str){
    		super(str);
    	}
    	public void paintComponent(Graphics g){
    		Graphics2D g2 = ((Graphics2D)g);
    		Object old = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                    RenderingHints.VALUE_ANTIALIAS_ON);
    		super.paintComponent(g);
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, old);
    	}
    }
}
