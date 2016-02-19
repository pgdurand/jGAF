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

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.resources.Accessor;

/**
 * This class implements a basic text editor. It provides a toolbar (undo/redo 
 * and cut/copy/paste actions) as well as a text-based search facility.
 * 
 * @author Patrick G. Durand
 * */
@SuppressWarnings("serial")
public class SimpleTextEditor extends JPanel {
	private JTextPane           _editor;
	private UndoManager         _undoManager;
	private UndoAction          _undoAction;
	private RedoAction          _redoAction;
	private PatternSearchFacility      _searchFacility;
	private HashMap<String,Action> _componentActions;
	private JButton             _undoBtn;
	private JButton             _redoBtn;
	private boolean             _isEdited;
	private boolean             _lockEdit;
	
	public SimpleTextEditor(){
		JToolBar  toolBar;
		ImageIcon icon;
		Action    act;
		JButton   btn;
		JPanel    mainBtnPanel, mainPanel, searchPanel;
		
		//init the Undo Manager
		_undoManager = new UndoManager();
		
		//creates the Editor panel
		_editor = new JTextPane()  {
	         public boolean getScrollableTracksViewportWidth()  {
	             return false;   // force display of horizontal scroll bar
	          }
	          public EditorKit createDefaultEditorKit()  {
	             return new StyledEditorKit(); 
	          }
	       }; 
		_editor.setFont(new Font("courier", Font.PLAIN, 12));
		_editor.getDocument().addDocumentListener(new EditorDocumentListener());
		_editor.getDocument().addUndoableEditListener(new PrefEntryUndoableEditListener());

		_searchFacility = new PatternSearchFacility(_editor);

		//adds custom key accelerators
		addBindings(_editor);
		//retrieve default Actions map from th editor
		createActionTable(_editor);
		//creates the custom toolbar
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		EZEnvironmentImplem.addSearchClass(Accessor.class);
		//undo btn
		icon = EZEnvironment.getImageIcon("undo.png");
		if (icon!=null){
			_undoAction = new UndoAction("", icon);
        }
        else{
        	_undoAction = new UndoAction(EZEnvironment.getMessage("__EZPrefEditor.undo.btn"));
        }
		_undoBtn=toolBar.add(_undoAction);
		_undoBtn.setToolTipText(EZEnvironment.getMessage("__EZPrefEditor.undo.tip"));
		
		//redo btn
		icon = EZEnvironment.getImageIcon("redo.png");
		if (icon!=null){
			_redoAction = new RedoAction("", icon);
        }
        else{
        	_redoAction = new RedoAction(EZEnvironment.getMessage("__EZPrefEditor.redo.btn"));
        }
		_redoBtn=toolBar.add(_redoAction);
		_redoBtn.setToolTipText(EZEnvironment.getMessage("__EZPrefEditor.redo.tip"));

		toolBar.addSeparator();
		
		//cut btn
		act = getActionByName(DefaultEditorKit.cutAction);
		if (act!=null){
			btn=toolBar.add(act);
			icon = EZEnvironment.getImageIcon("cut.png");
			if (icon!=null){
				btn.setIcon(icon);
				btn.setText("");
	        }
	        else{
	        	btn.setText(EZEnvironment.getMessage("__EZPrefEditor.cut.btn"));
	        }
			btn.setToolTipText(EZEnvironment.getMessage("__EZPrefEditor.cut.tip"));
		}

		//copy btn
		act = getActionByName(DefaultEditorKit.copyAction);
		if (act!=null){
			btn=toolBar.add(act);
			icon = EZEnvironment.getImageIcon("copy.png");
			if (icon!=null){
				btn.setIcon(icon);
				btn.setText("");
	        }
	        else{
	        	btn.setText(EZEnvironment.getMessage("__EZPrefEditor.copy.btn"));
	        }
			btn.setToolTipText(EZEnvironment.getMessage("__EZPrefEditor.copy.tip"));
		}

		//paste btn
		act = getActionByName(DefaultEditorKit.pasteAction);
		if (act!=null){
			btn=toolBar.add(act);
			icon = EZEnvironment.getImageIcon("paste.png");
			if (icon!=null){
				btn.setIcon(icon);
				btn.setText("");
	        }
	        else{
	        	btn.setText(EZEnvironment.getMessage("__EZPrefEditor.paste.btn"));
	        }
			btn.setToolTipText(EZEnvironment.getMessage("__EZPrefEditor.paste.tip"));
		}

		mainBtnPanel = new JPanel(new BorderLayout());
		mainBtnPanel.add(toolBar, BorderLayout.WEST);
		searchPanel = new JPanel(new BorderLayout());
		_searchFacility = new PatternSearchFacility(_editor);
		searchPanel.add(_searchFacility.getSearchForm(), BorderLayout.CENTER);
		mainBtnPanel.add(searchPanel, BorderLayout.CENTER);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(mainBtnPanel, BorderLayout.SOUTH);
		mainPanel.add(new JScrollPane(_editor), BorderLayout.CENTER);
		
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
	}
	
	/**
	 * Figures out whether the text has been edited.
	 */
	public boolean isEdited(){
		return _isEdited;
	}
	
	public void setEdited(boolean edited){
		_isEdited = edited;
	}
	/**
	 * Sets the text that can then be edited.
	 */
	public void setText(String txt){
		_lockEdit=true;
		_searchFacility.resetSearch();
		_editor.setText(txt!=null ? txt : "");
		_editor.setCaretPosition(0);
		_lockEdit=false;
		_undoManager.discardAllEdits();
        _undoAction.updateUndoState();
        _redoAction.updateRedoState();
	}
	/**
	 * Returns the text contained in this editor.
	 */
	public String getText(){
		return _editor.getText();
	}
	
    /**
     * Retrieves an action given its name. Call this method after a call to
     * createActionTable.
     */
    private Action getActionByName(String name) {
        return (Action)(_componentActions.get(name));
    }
	/**
	 * Adds particular key accelerators to the editor component.
	 */
	protected void addBindings(JTextPane editor) {
        InputMap inputMap = editor.getInputMap();

        //Ctrl-c to copy current selection
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.copyAction);
        
        //Ctrl-v to paste clipboard content
        key = KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.pasteAction);

        //Ctrl-x to cut current selection
        key = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.cutAction);
    }
	/**
	 * Retrieves the action associated to a particular text component.
	 */
    private void createActionTable(JTextComponent textComponent) {
        _componentActions = new HashMap<String, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            _componentActions.put(a.getValue(Action.NAME).toString(), a);
        }
    }

    private class EditorDocumentListener implements DocumentListener{
		private void docEdited(){
			if (_lockEdit)
				return;
			_isEdited = true;
			_searchFacility.resetSearch();
		}
		public void insertUpdate(DocumentEvent e){
			docEdited();
		}
		public void removeUpdate(DocumentEvent e){
			docEdited();
		}
		public void changedUpdate(DocumentEvent e){
			docEdited();
		}
	}
	/**
	 * This class handles document editions that can be undone.
	 */
    protected class PrefEntryUndoableEditListener implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            _undoManager.addEdit(e.getEdit());
            _undoAction.updateUndoState();
            _redoAction.updateRedoState();
        }
    }
	/**
	 * This class handles undo.
	 */
    private class UndoAction extends AbstractAction {
        public UndoAction(String name) {
            super(name);
            setEnabled(false);
        }
        public UndoAction(String name, Icon icon) {
        	super(name, icon);
            setEnabled(false);
        }
        
        public void actionPerformed(ActionEvent e) {
            try {
                _undoManager.undo();
            } catch (CannotUndoException ex) {
            }
            updateUndoState();
            _redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (_undoManager.canUndo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }

	/**
	 * This class handles redo.
	 */
    private class RedoAction extends AbstractAction {
        public RedoAction(String name) {
            super(name);
            setEnabled(false);
        }

        public RedoAction(String name, Icon icon) {
            super(name, icon);
            setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
            try {
                _undoManager.redo();
            } catch (CannotRedoException ex) {
            }
            updateRedoState();
            _undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (_undoManager.canRedo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }

}
