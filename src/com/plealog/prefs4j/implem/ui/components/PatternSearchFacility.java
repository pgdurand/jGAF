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
package com.plealog.prefs4j.implem.ui.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;

import com.plealog.genericapp.api.EZEnvironment;

public class PatternSearchFacility {
	private JButton             _search;
	private JTextComponent      _txtC;
	private boolean             _inSearch;
    private int                 _lastSearchPosition = -1;
    private String              _helper = "Enter a pattern to search for";
    private SearchField         _sf;
    
	public PatternSearchFacility(JTextComponent txtC){
		setTxtComponent(txtC);
	}
	public void resetSearch(){
		_lastSearchPosition = -1;
		_inSearch = false;
		_txtC.getHighlighter().removeAllHighlights();
	}
	public void setTxtComponent(JTextComponent txtC){
		_txtC = txtC;
		resetSearch();
	}
 
	/**
	 * This class handles the save button actions.
	 */
	@SuppressWarnings("serial")
	private class SearchButtonActionListener extends AbstractAction{
		private Matcher matcher;

		public void actionPerformed(ActionEvent e){
			Pattern seed;
			String  str, userPattern;
			int     start, end;

			userPattern = _sf.getText();
			if (_txtC==null || userPattern.length()<1){
				return;
			}
			str = userPattern;
			if (_inSearch==false){
				try{
					seed = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
					matcher = seed.matcher(_txtC.getText());
				}
				catch(Exception ex){
					JOptionPane.showMessageDialog(
							JOptionPane.getFrameForComponent(_search),
			                "Invalid pattern: "+str,
			                "Pattern search",
			                JOptionPane.WARNING_MESSAGE);
					return;
				}
				_lastSearchPosition = -1;
				_inSearch = true;
			}
			
			if (!matcher.find(_lastSearchPosition+1)){
				_inSearch = false;
				return;
			}
			//returned values are zero-based, but end is the char located after the last pattern match
			_lastSearchPosition = start = matcher.start();
			end = matcher.end();
			try {
				_txtC.getHighlighter().removeAllHighlights();
				_txtC.getHighlighter().addHighlight( start, end,
						DefaultHighlighter.DefaultPainter );
				_txtC.setCaretPosition(start);
				_lastSearchPosition = end;
			} catch (BadLocationException e1) {
				resetSearch();
			}
		}
	}
	public SearchField getSearchForm(){
		
		_sf = new SearchField();
		_sf.setHelperText(_helper);
		_search = _sf.addUserAction(EZEnvironment.getImageIcon("run.png"), new SearchButtonActionListener());
		_sf.addPropertyChangeListener(SearchField.PROPERTY_TEXT,
				new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String l = evt.getNewValue().toString();
				_search.setEnabled((l!=null && l.length()>0));
				if (_lastSearchPosition!=-1){
					resetSearch();
				}
			}
		});
		_search.setEnabled(false);
		_sf.addKeyListener(new MyKeyListener());
		Dimension dim = _sf.getPreferredSize();
		dim.width = 260;
		_sf.setPreferredSize(dim);
		_sf.setMaximumSize(dim);
		return _sf;
	}
	private class MyKeyListener extends KeyAdapter {
    	public void keyReleased(KeyEvent e){
    		super.keyReleased(e);
    		if (e.getKeyCode()==KeyEvent.VK_ENTER){
    			_search.doClick();
    		}
    	}
    }
}
