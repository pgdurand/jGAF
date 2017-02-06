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
package com.plealog.prefs4j.implem.ui.model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.plealog.genericapp.api.EZEnvironment;



/**
 * This class implements a GUI container for Item. In other words, if considering
 * the Model-View-Controller paradigm, ItemGUIContainer is the view part of an Item, 
 * the last being the model. Note that ItemGUIContainer is not a GUI component itself
 * but it wraps such a component.
 */
public class ItemGUIContainer {
    private JTextField   _txt;
    private JComboBox<ChoiceEntry>    _combo;
    private JSpinner     _spinner;
    private JCheckBox    _check;
    private int          _type;
    private Object       _userData;
    private Item         _item;
    private EditListener _editListener;
    private boolean      _edited;
    
    public static final int TXT_FIELD_TYPE = 1;
    public static final int COMBO_TYPE = 2;
    public static final int SPINNER_TYPE = 3;
    public static final int CHECKBOX_TYPE = 4;
    
    private static final Logger _logger = Logger.getLogger("ItemGUIContainer");

    private static final MessageFormat RANGE_ERR_FORMATTER = 
    	new MessageFormat(EZEnvironment.getMessage("__EZItem.control.error.notInRange"));
    
    /**
     * Creates the viewer of an Item.
     */
    public ItemGUIContainer(Item item){
        _item = item;
        _editListener = new EditListener();
    }
    
    /**
     * Returns the combo-box associated to this ItemGUIContainer. Such a combo-box
     * is defined if Item is a Choice, otherwise this method returns null.
     */
	public JComboBox<ChoiceEntry> getCombo() {
		return _combo;
	}
    /**
     * Returns the combo-box associated to this ItemGUIContainer.
     */
	public void setCombo(JComboBox<ChoiceEntry> combo) {
		if (this._combo!=null){
			this._combo.removeActionListener(_editListener);
		}
		this._combo = combo;
		this._combo.addActionListener(_editListener);
	}
    /**
     * Returns the text-field associated to this ItemGUIContainer. Such a text-field
     * is defined if Item is a Value, otherwise this method returns null.
     */
	public JTextField getTxtField() {
		return _txt;
	}
	/**
     * Returns the checkbox-field associated to this ItemGUIContainer. Such a checkbox-field
     * is defined if Item is a Value of type Boolean, otherwise this method returns null.
     */
	public JCheckBox getCheckBox(){
		return _check;
	}
	/**
     * Sets the checkbox-field associated to this ItemGUIContainer.
     */
	public void setCheckBox(JCheckBox cbox){
		if (this._check!=null){
			this._check.removeActionListener(_editListener);
		}
		this._check = cbox;
		this._check.addActionListener(_editListener);
	}
    /**
     * Sets the text-field associated to this ItemGUIContainer.
     */
	public void setTxtField(JTextField txt) {
		if (this._txt!=null){
			this._txt.getDocument().removeDocumentListener(_editListener);
		}
		this._txt = txt;
		this._txt.getDocument().addDocumentListener(_editListener);
	}
    
    /**
     * Returns the spinner associated to this ItemGUIContainer. Such a spinner
     * is defined if Item is a Value defined with a Range, otherwise this method 
     * returns null.
     */
	public JSpinner getSpinner() {
		return _spinner;
	}
    /**
     * Sets the spinner associated to this ItemGUIContainer.
     */
	public void setSpinner(JSpinner spinner) {
		if (this._spinner!=null){
			((JSpinner.DefaultEditor)this._spinner.getEditor()).getTextField().getDocument().removeDocumentListener(_editListener);
		}
		this._spinner = spinner;
		((JSpinner.DefaultEditor)this._spinner.getEditor()).getTextField().getDocument().addDocumentListener(_editListener);
	}
	/**
	 * Returns the GUI type of this ItemGUIContainer: combo-box, text-field, spinner.
	 * 
	 * @return one of XXX_TYPE constants defined in this class.
	 */
	public int getType() {
		return _type;
	}
	/**
	 * Sets the GUI type of this ItemGUIContainer: combo-box, text-field, spinner.
	 * 
	 * @param type one of XXX_TYPE constants defined in this class.
	 */
	public void setType(int type) {
		this._type = type;
	}
	/**
	 * Sets a user-data to this ItemGUIContainer.
	 */
    public void setUserData(Object obj){
        _userData = obj;
    }
	/**
	 * Returns the user-data associated to this ItemGUIContainer.
	 */
    public Object getUserData(){
        return _userData;   
    }
    /**
     * Returns the name of this container. The returned name is actually the
     * one of the Item associated to this ItemGUIContainer.
     */
    public String getName(){
        return _item.getName();   
    }
    /**
     * Returns the Item associated to this ItemGUIContainer.
     */
    public Item getItem(){
        return _item;   
    }
    /**
     * Figures out if the ItemGUIContainer is enabled.
     */
    public boolean isEnabled(){
        switch(_type){
            case TXT_FIELD_TYPE:
               return _txt.isEnabled();
            case COMBO_TYPE:
               return _combo.isEnabled();
            case SPINNER_TYPE:
               return _spinner.isEnabled();
            case CHECKBOX_TYPE:
            	return _check.isEnabled();
        }
        return false;
    }
    /**
     * Returns the value associated to the GUI component wrapped by this 
     * ItemGUIContainer.
     */
    public String getValue(){
        String val = null;
        switch(_type){
            case TXT_FIELD_TYPE:
               val = _txt.getText();
               break;
            case CHECKBOX_TYPE:
            	val = _check.isSelected()?"true":"false";
            	break;
            case COMBO_TYPE:
               val = ((ChoiceEntry)_combo.getSelectedItem()).getKey();
               if (val.equals("do_not_use"))
                    val=null;
               break;
            case SPINNER_TYPE:
               val = _spinner.getValue().toString();
               //it appears that the spinner model getValue() always returns a double
               //so, I added this code to get the correct representation: int or real
               try {
					if (_item.getDtype()==Item.DTYPE_INT)
					   val = new Integer(Double.valueOf(val).intValue()).toString();
					else if (_item.getDtype()==Item.DTYPE_REAL)
					   val = Double.valueOf(val).toString();
				} catch (NumberFormatException e) {
				}
        }
        return val;
    }
    
    /**
     * Sets a value to this GUI item.
     */
    public void setValue(Object obj){
    	SpinnerNumberModel sModel;
    	ChoiceEntry        ce;
    	String             str;
    	int                i, size;

    	if (obj==null)
    		return;
    	
    	str = obj.toString();
    	switch(_type){
	        case TXT_FIELD_TYPE:
	           _txt.setText(str);
	           break;
	        case CHECKBOX_TYPE:
	           _check.setSelected("true".equalsIgnoreCase(str));
	        	break;
	        case COMBO_TYPE:
	        	//A Comco contains a List of ChoiceEntry and we have to compare
	        	//their Name field with str
	        	size = _combo.getItemCount();
	        	for(i=0;i<size;i++){
	        		ce = (ChoiceEntry) _combo.getItemAt(i);
	        		if (ce.getKey().equals(str)){
	        			_combo.setSelectedIndex(i);
	        			break;
	        		}
	        	}
	        	break;
	        case SPINNER_TYPE:
	        	//Right now, only SpinnerNumberModel is used. Since the control of
	        	//valid values is not done by the model, it is done here.
	        	Number val, min, max;
	        	try {
	        		val = Double.valueOf(str);
	        		sModel = (SpinnerNumberModel) _spinner.getModel();
	        		min = (Number) sModel.getMinimum();
	        		max = (Number) sModel.getMaximum();
					if (val.doubleValue()>=min.doubleValue() && 
						val.doubleValue()<=max.doubleValue()){
						_spinner.setValue(val);
					}
					else{
						_logger.warning(_item.getName()+": "+str+": "+
								RANGE_ERR_FORMATTER.format(new Object[]{min,max}));
						
					}
				} catch (Exception e) {
					_logger.warning(_item.getName()+": "+str+": "+
							EZEnvironment.getMessage("__EZGUIContainer.validity.err4"));
				}
				break;
        }
    }
    /**
     * Sets this Item as being modified.
     */
    public void setEdited(boolean edited){
    	_edited = edited;
    }
    /**
     * Figures out whether this Item has been modified.
     */
    public boolean isEdited(){
    	return _edited;
    }
    
    public void setBackgroundColor(Color clr){
    	switch(_type){
        case TXT_FIELD_TYPE:
           _txt.setBackground(clr);
           break;
        case COMBO_TYPE:
           _combo.setBackground(clr);
           break;
        case SPINNER_TYPE:
            _spinner.setBackground(clr);
            break;
        case CHECKBOX_TYPE:
            _check.setBackground(clr);
            break;
    	}
    }
    /**
     * Returns the GUI component wrapped by this ItemGUIContainer.
     * Such a component is either a JComboBox or a JTextField or a JSpinner.
     */
    public JComponent getComponent(){
        switch(_type){
            case TXT_FIELD_TYPE:
               return _txt;
            case COMBO_TYPE:
               return _combo;
            case SPINNER_TYPE:
                return _spinner;
            case CHECKBOX_TYPE:
                return _check;
        }
        return null;
    }
    /**
     * Inner class listening to Item edition.
     */
    private class EditListener implements ActionListener, DocumentListener{
    	public void actionPerformed(ActionEvent event){
    		setEdited(true);
    	}
    	public void insertUpdate(DocumentEvent e){
    		setEdited(true);
    	}
    	public void removeUpdate(DocumentEvent e){
    		setEdited(true);
    	}
    	public void changedUpdate(DocumentEvent e){
    		setEdited(true);
    	}
    }

}