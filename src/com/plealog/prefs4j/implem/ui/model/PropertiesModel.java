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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.prefs4j.ui.ItemException;

/**
 * This class is responsible for managing the Blast parameters. Those parameters are read
 * by this class from a well-formatted properties file.
 * 
 * @author Patrick G. Durand
 */
public class PropertiesModel {
    /**the resource bundle*/
    private ResourceBundle         _bundle;
    private Hashtable<String,Item> _items;
    private ArrayList<FormItem>    _forms;
    private ResourceBundle         _localizedMessages;
    private String                 _name;
    private String                 _description;
    
	private static final String NAME_KEY = "section.name";
	private static final String DESCRIPTION_KEY = "section.desc";
	private static final String RES_KEY = "res:";
    private static final String ITEMS_KEY = "items";
    private static final String ITEM_KEY = "item.";
    private static final String NAME_SUFFIX = ".name";
    private static final String KEY_SUFFIX = ".key";
    private static final String DESC_SUFFIX = ".desc";
    private static final String CTYPE_SUFFIX = ".ctype";
    private static final String DTYPE_SUFFIX = ".dtype";
    private static final String DEF_SUFFIX = ".default";
    private static final String OPTIONAL_SUFFIX = ".optional";
    private static final String RANGE_FROM_SUFFIX = ".atom.range.from";
    private static final String RANGE_TO_SUFFIX = ".atom.range.to";
    private static final String RANGE_DEF_SUFFIX = ".atom.range.default";
    private static final String CHOICE_ENTRIES_SUFFIX = ".choice.entries";
    private static final String CHOICE_ENTRY_SELECTOR = ".choice.entry.";
    private static final String CHOICE_DEFAULT_ENTRY = ".choice.default";
    private static final String ACTION_KEY = "action";
    private static final String SET_VALUE_ACTION_KEY = "value";
    private static final String SET_ENABLED_ACTION_KEY = "enable";
    private static final String SELECT_ACTION_KEY = "select";
    private static final String FORMS_KEY = "forms";
    private static final String FORM_KEY = "form.";
    private static final String FORM_ITEMS_KEY = "."+ITEMS_KEY;
    private static final String FORM_FOLDABLE_SUFFIX = ".foldable";
    private static final String FORM_FOLDED_SUFFIX = ".folded";
    
	private PropertiesModel(){}
	
	/**
	 * Default constructor.
	 * 
	 * @param resourcesUrl URL pointing to the bundle
	 * @param bunble the resource bundle containing the model descriptor parameters.
	 */
    public PropertiesModel(ResourceBundle localizedMessages, ResourceBundle model) throws ItemException{
        this();
    	_bundle = model;
        _localizedMessages = localizedMessages;
        _items=new Hashtable<String,Item>();
        _forms=new ArrayList<FormItem>();
        initialize();
    }


    private void setValue(String iKey, Item item){
        Integer     ival;
        IntRange    irange;
        Double      dval;
        String      sval;
        Boolean     bval;
        DoubleRange drange;
        
        switch(item.getDtype()){
            case Item.DTYPE_INT: 
                ival = EZEnvironmentImplem.getInteger(_bundle, ITEM_KEY+iKey+RANGE_FROM_SUFFIX);
                if (ival!=EZEnvironmentImplem.UNKNOWNINTEGER){
                    irange = new IntRange();
                    irange.setRangeFrom(ival.intValue());
                    ival = EZEnvironmentImplem.getInteger(_bundle, ITEM_KEY+iKey+RANGE_TO_SUFFIX);
                    if (ival!=EZEnvironmentImplem.UNKNOWNINTEGER){
                        irange.setRangeTo(ival.intValue());
                        ival = EZEnvironmentImplem.getInteger(_bundle, ITEM_KEY+iKey+RANGE_DEF_SUFFIX);
                        if (ival!=EZEnvironmentImplem.UNKNOWNINTEGER){
                            irange.setRangeDef(ival.intValue());
                            item.setRange(irange);
                            return;
                        }
                        throw new ItemException("Range is wrongly define: default is missing");
                    }
                    throw new ItemException("Range is wrongly define: to is missing");
                }
                else{
                    ival = EZEnvironmentImplem.getInteger(_bundle, ITEM_KEY+iKey+DEF_SUFFIX);
                    if (ival!=EZEnvironmentImplem.UNKNOWNINTEGER){
                        item.setDefault(ival);   
                    }
                }
                break;
            case Item.DTYPE_REAL: 
                dval = EZEnvironmentImplem.getDouble(_bundle, ITEM_KEY+iKey+RANGE_FROM_SUFFIX);
                if (dval!=EZEnvironmentImplem.UNKNOWNDOUBLE){
                    drange = new DoubleRange();
                    drange.setRangeFrom(dval.doubleValue());
                    dval = EZEnvironmentImplem.getDouble(_bundle, ITEM_KEY+iKey+RANGE_TO_SUFFIX);
                    if (dval!=EZEnvironmentImplem.UNKNOWNDOUBLE){
                        drange.setRangeTo(dval.doubleValue());
                        dval = EZEnvironmentImplem.getDouble(_bundle, ITEM_KEY+iKey+RANGE_DEF_SUFFIX);
                        if (dval!=EZEnvironmentImplem.UNKNOWNDOUBLE){
                            drange.setRangeDef(dval.doubleValue());
                            item.setRange(drange);
                            return;
                        }
                        throw new ItemException("Range is wrongly define: default is missing");
                    }
                    throw new ItemException("Range is wrongly define: to is missing");
                }
                else{
                    dval = EZEnvironmentImplem.getDouble(_bundle, ITEM_KEY+iKey+DEF_SUFFIX);
                    if (dval!=EZEnvironmentImplem.UNKNOWNDOUBLE){
                        item.setDefault(dval);   
                    }
                }
                break;
            case Item.DTYPE_STRING:
            case Item.DTYPE_FOLDER:
                sval = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+DEF_SUFFIX);
                if (sval!=EZEnvironmentImplem.UNKNOWNSTRING){
                    item.setDefault(sval);   
                }
                break;
            case Item.DTYPE_BOOLEAN: 
                bval = EZEnvironmentImplem.getBoolean(_bundle, ITEM_KEY+iKey+DEF_SUFFIX);
                if (bval!=EZEnvironmentImplem.UNKNOWNBOOLEAN){
                    item.setDefault(bval);   
                }
                break;
        }
    }

    private FormAction createFormAction(String cEntryKey, String actionKey){
        FormAction fa=null;
        String[]   actionItems, values;
        String     val, actionName;
        ArrayList<String>  list;
        int        i;
        
        val = EZEnvironmentImplem.getString(_bundle, ACTION_KEY+"."+actionKey);
        if (val==EZEnvironmentImplem.UNKNOWNSTRING)
            throw new ItemException("Choice entry is wrongly define: action not found for "+cEntryKey+": "+actionKey);
        actionItems = EZEnvironmentImplem.tokenize(val,":\t\n\r\f");
        if (actionItems.length!=3)
            throw new ItemException("Choice entry is wrongly define: action does not contain the 3 items for "+cEntryKey+": "+actionKey);
        actionName = actionItems[1];
        if (actionName.equals(SET_VALUE_ACTION_KEY)){
            values = EZEnvironmentImplem.tokenize(actionItems[2]);
            if (values.length==0)
                throw new ItemException("Choice entry is wrongly define: action type setEnabled does not contain any values for "+cEntryKey+": "+actionKey);
            list = new ArrayList<String>();
            for (i = 0; i < values.length; i++) {
                list.add(values[i]);
            }
            fa = new FormActionSetValue(
                    actionKey,
                    actionItems[0],
                    list
            );
        }
        else if (actionName.equals(SET_ENABLED_ACTION_KEY)){
            fa = new FormActionSetEnable(
                    actionKey,
                    actionItems[0],
                    actionItems[2].equals("true")
            );
        }
        else if (actionName.equals(SELECT_ACTION_KEY)){
            fa = new FormActionSelectValue(
                    actionKey,
                    actionItems[0],
                    actionItems[2]
            );
        }
        else {
            throw new ItemException("Choice entry is wrongly define: action type unknown for "+cEntryKey+": "+actionKey+": "+actionName);
        }
            
        return fa;
    }
    
    private void readAction(ChoiceEntry ce, String cEntryKey, String actions){
        String[]    actionKeys;
        FormAction  fAction;
        int         i;
        
        actionKeys = EZEnvironmentImplem.tokenize(actions);
        for (i = 0; i < actionKeys.length; i++) {
            fAction = createFormAction(cEntryKey, actionKeys[i]);
            if (fAction != null){
                ce.addAction(fAction);
            }
        }
    }

    private ChoiceEntry createChoiceEntry(String iKey, String cEntryKey){
        ChoiceEntry ce;
        String      val;
        
        ce = new ChoiceEntry();
        val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+CHOICE_ENTRY_SELECTOR+cEntryKey+NAME_SUFFIX);
        if (val==EZEnvironmentImplem.UNKNOWNSTRING)
            throw new ItemException("Choice entry is wrongly define: name is missing for "+cEntryKey);
        ce.setName(getMessage(val));
        val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+CHOICE_ENTRY_SELECTOR+cEntryKey+KEY_SUFFIX);
        if (val==EZEnvironmentImplem.UNKNOWNSTRING)
            throw new ItemException("Choice entry is wrongly define: key is missing for "+cEntryKey);
        ce.setKey(val);
        val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+CHOICE_ENTRY_SELECTOR+cEntryKey+DESC_SUFFIX);
        if (val==EZEnvironmentImplem.UNKNOWNSTRING)
            throw new ItemException("Choice entry is wrongly define: description is missing for "+cEntryKey);
        ce.setDescription(getMessage(val));
        val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+CHOICE_ENTRY_SELECTOR+cEntryKey+"."+ACTION_KEY);
        if (val!=EZEnvironmentImplem.UNKNOWNSTRING){
            readAction(ce, cEntryKey, val);   
        }
        return ce;
    }
    
    private void setChoice(String iKey, Item item){
        String      choices,val;
        String[]    choicesKeys;
        ChoiceEntry entry;
        Choice      choice;
        int         i;
        
        choices = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+CHOICE_ENTRIES_SUFFIX);
        choicesKeys = EZEnvironmentImplem.tokenize(choices);
        choice = new Choice();
        for (i = 0; i < choicesKeys.length; i++) {
            entry = createChoiceEntry(iKey,choicesKeys[i]);
            if (entry != null){
                choice.addEntry(choicesKeys[i], entry);
            }
        }
        val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+CHOICE_DEFAULT_ENTRY);
        if (val==EZEnvironmentImplem.UNKNOWNSTRING)
            throw new ItemException("Choice is wrongly define: default value is missing");
        choice.setDefault(val);
        item.setChoice(choice);
    }
    
    private Item createItem(String iKey){
        Item    item;
        String  val;
        Boolean optional;
        
        item = new Item();
        try {
            val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+NAME_SUFFIX);
            if (val==EZEnvironmentImplem.UNKNOWNSTRING)
                throw new ItemException("name is missing");
            item.setName(getMessage(val));
            val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+KEY_SUFFIX);
            if (val==EZEnvironmentImplem.UNKNOWNSTRING)
                throw new ItemException("key is missing");
            item.setKey(val);
            val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+DESC_SUFFIX);
            if (val==EZEnvironmentImplem.UNKNOWNSTRING)
                throw new ItemException("description is missing");
            item.setDescription(getMessage(val));
            optional = EZEnvironmentImplem.getBoolean(_bundle, ITEM_KEY+iKey+OPTIONAL_SUFFIX);
            if (optional==EZEnvironmentImplem.UNKNOWNBOOLEAN)
            	item.setOptional(true);
            else
            	item.setOptional(optional.booleanValue());
            val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+CTYPE_SUFFIX);
            if (val==EZEnvironmentImplem.UNKNOWNSTRING)
                throw new ItemException("CType is missing");
            item.setCtype(Item.getCType(val));
            val = EZEnvironmentImplem.getString(_bundle, ITEM_KEY+iKey+DTYPE_SUFFIX);
            if (val==EZEnvironmentImplem.UNKNOWNSTRING)
                throw new ItemException("DType is missing");
            item.setDtype(Item.getDType(val));
            if (item.getCtype()==Item.CTYPE_VALUE)
                setValue(iKey, item);
            else if (item.getCtype()==Item.CTYPE_CHOICE)
                setChoice(iKey, item);
            //special check for folder and file
            if (item.getDtype()==Item.DTYPE_FOLDER && item.getCtype()!=Item.CTYPE_VALUE)
            	throw new ItemException("folder data type is not atomic");
            if (item.getDtype()==Item.DTYPE_FILE && item.getCtype()!=Item.CTYPE_VALUE)
            	throw new ItemException("file data type is not atomic");
        }
        catch (Exception ex){
            throw new ItemException ("Error on item: "+iKey+": "+ex);
        }
        return item;
    }
    
    private FormItem createFormItem(String iKey){
        FormItem        item;
        String          val;
        String[]        itemKeys;
        int             i;
        HashSet<String> keys;
        
        item = new FormItem();
        try {
            val = EZEnvironmentImplem.getString(_bundle, FORM_KEY+iKey+NAME_SUFFIX);
            if (val==EZEnvironmentImplem.UNKNOWNSTRING)
                throw new ItemException("name is missing");
            item.setName(getMessage(val));
            val = EZEnvironmentImplem.getString(_bundle, FORM_KEY+iKey+KEY_SUFFIX);
            if (val==EZEnvironmentImplem.UNKNOWNSTRING)
                item.setKey(iKey);
            else
            	item.setKey(val);
            val = EZEnvironmentImplem.getString(_bundle, FORM_KEY+iKey+FORM_FOLDABLE_SUFFIX);
            if (val!=EZEnvironmentImplem.UNKNOWNSTRING)
	            item.setFoldable(val.equalsIgnoreCase("true")?true:false);
            val = EZEnvironmentImplem.getString(_bundle, FORM_KEY+iKey+FORM_FOLDED_SUFFIX);
            if (val!=EZEnvironmentImplem.UNKNOWNSTRING)
	            item.setFolded(val.equalsIgnoreCase("true")?true:false);
            
            
            val = EZEnvironmentImplem.getString(_bundle, FORM_KEY+iKey+FORM_ITEMS_KEY);
            itemKeys = EZEnvironmentImplem.tokenize(val);
            keys = new HashSet<String>();
            for (i = 0; i < itemKeys.length; i++) {
                if (_items.get(itemKeys[i])==null)
                    throw new ItemException("item '"+itemKeys[i]+"' does not exist" );
                if (keys.contains(itemKeys[i]))
                	throw new ItemException("item '"+itemKeys[i]+"'  cited more than once" );
                item.addItem(itemKeys[i]);
                keys.add(itemKeys[i]);
            }
            //System.out.println("Form: "+item.getStringRepr());
        }
        catch (Exception ex){
            throw new ItemException("Error on form item: "+iKey+": "+ex.getMessage());
        }

        return item;
    }

    private void initialize() throws ItemException{
        String      items;
        Item        meti;
        FormItem    fItem;
        String[]    itemsKeys;
        int         i;
        		
        //read items
        items = EZEnvironmentImplem.getString(_bundle, ITEMS_KEY);
        itemsKeys = EZEnvironmentImplem.tokenize(items);
        for (i = 0; i < itemsKeys.length; i++) {
            meti = createItem(itemsKeys[i]);
            if (meti != null){
                //System.out.println("Item "+(i+1)+":\n"+meti);
                if (_items.containsKey(itemsKeys[i]))
                	throw new ItemException("items list: item '"+itemsKeys[i]+"' cited more than once");
            	_items.put(itemsKeys[i], meti);
            }
        }
        //read forms
        items = EZEnvironmentImplem.getString(_bundle, FORMS_KEY);
        itemsKeys = EZEnvironmentImplem.tokenize(items);
        for (i = 0; i < itemsKeys.length; i++) {
            fItem = createFormItem(itemsKeys[i]);
            if (fItem!=null){
                _forms.add(fItem);
            }
        }
        //read name and description
        _name = getMessage(EZEnvironmentImplem.getString(_bundle, NAME_KEY));
        _description = getMessage(EZEnvironmentImplem.getString(_bundle, DESCRIPTION_KEY));
    }
    
    /**
     * Returns a name for this properties model.
     */
    public String getName(){
    	return _name;
    }
    
    /**
     * Returns a description for this properties model.
     */
    public String getDescription(){
    	return _description;
    }
    
    /**
     * Returns the set of items (aka Blast parameters). The returned set contains String
     * objects. Use those strings with the method getItem to retrieve the corresponding
     * items (parameters).
     * 
     */
    public Set<String> getItemsName(){
        return _items.keySet();   
    }
    /**
     * Returns an Item given its name, or null if not found.
     */
    public Item getItem(String name){
        return ((Item)_items.get(name));
    }

    /**
     * Returns an iterator over FormItem objects. It this iterator is empty, no forms
     * exist.
     */
    public Iterator<FormItem> getFormItems(){
        return _forms.iterator();   
    }

	private static final String UNKNOWN="?";
	
	public String getMessage(String val){
		int pos;
		
		pos = val.indexOf(RES_KEY);
		if (pos<0)
			return val;
		return (getString(val.substring(pos+RES_KEY.length())));
	}
	private String getString(String key) {
		if (_localizedMessages==null)
			return UNKNOWN;
		try {
			return _localizedMessages.getString(key);
		} catch (MissingResourceException e) {
			return UNKNOWN;
		}
	}
}
