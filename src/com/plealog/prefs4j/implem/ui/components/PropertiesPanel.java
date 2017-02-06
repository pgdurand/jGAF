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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.file.EZFileManager;
import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.prefs4j.api.DataConnector;
import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.api.PreferenceSection;
import com.plealog.prefs4j.api.PropertiesDataConnector;
import com.plealog.prefs4j.implem.ui.model.Choice;
import com.plealog.prefs4j.implem.ui.model.ChoiceEntry;
import com.plealog.prefs4j.implem.ui.model.DoubleRange;
import com.plealog.prefs4j.implem.ui.model.FormAction;
import com.plealog.prefs4j.implem.ui.model.FormItem;
import com.plealog.prefs4j.implem.ui.model.IntRange;
import com.plealog.prefs4j.implem.ui.model.Item;
import com.plealog.prefs4j.implem.ui.model.ItemGUIContainer;
import com.plealog.prefs4j.implem.ui.model.PropertiesModel;
import com.plealog.prefs4j.implem.ui.model.Range;
import com.plealog.prefs4j.implem.ui.tools.DesignGridLayout;
import com.plealog.prefs4j.implem.ui.tools.IRowCreator;
import com.plealog.prefs4j.implem.ui.tools.RowGroup;
import com.plealog.prefs4j.ui.ItemException;
import com.plealog.prefs4j.ui.PreferenceEditor;
import com.plealog.resources.Accessor;

/**
 * This class defines a PropertiesModel Editor.
 * 
 * @author Patrick G. Durand
 */
@SuppressWarnings("serial")
public class PropertiesPanel extends JPanel {
    private PreferenceSection _conf;
	private PropertiesModel     _pModel;
    private Hashtable<String, ItemGUIContainer>  _itemContainers;
    private Hashtable<String, String>           _itemKeys;
    private JTextArea           _helpArea;
    private JTextArea           _headerHelpArea;
    private Properties          _passedProperties = new Properties();
    private Properties          _defaultProperties = new Properties();
    private String              _name;
    private boolean             _configOk;
    private boolean             _canUpdateHelp=true;
    private boolean             _showRestoreBtn;
    private boolean             _showSaveAsDefBtn;
    private boolean             _showHeader;
    private boolean             _showHelp;
    
    private static final String END_PARAM = ":";
    
	private static final Dimension MINI_SIZE = new Dimension(1,1);

    private PropertiesPanel(){}

    public PropertiesPanel(PropertiesModel pm, PreferenceSection conf){
    	this(pm, conf, new HashSet<PreferenceEditor.PROPERTY>());
    }
    public PropertiesPanel(PropertiesModel pm, PreferenceSection conf, HashSet<PreferenceEditor.PROPERTY> props){
    	this();
    	EZEnvironmentImplem.addSearchClass(Accessor.class);
    	_conf = conf;
    	_showRestoreBtn = props.contains(PreferenceEditor.PROPERTY.RESTORE_DEF_BTN);
    	_showSaveAsDefBtn = props.contains(PreferenceEditor.PROPERTY.SAVE_AS_DEF_BTN);
    	_showHeader = props.contains(PreferenceEditor.PROPERTY.HEADER);
    	_showHelp = props.contains(PreferenceEditor.PROPERTY.HELP);
    	if (_showRestoreBtn){
    		if (conf.getDefaultConfigurationLocator()==null ||
    				conf.getDataConnector().canRead(conf.getDefaultConfigurationLocator()) == false){
    			_showRestoreBtn = false;
    		}
    	}
    	if (_showSaveAsDefBtn){
    		if (conf.getDefaultConfigurationLocator()==null ||
    			conf.getDataConnector().canWrite(conf.getDefaultConfigurationLocator()) == false){
    			_showSaveAsDefBtn = false;
    		}
    	}
    	setPropertiesModel(pm);
    	_name = conf.getName();
    	initializeGUI();
    }
    /**
     * Initializes the GUI.
     */
    private void initializeGUI(){
        JPanel        mainPanel, headerPanel, editorPanel, btnPanel;
        JScrollPane   scroller;
        JButton       restoreBtn, saveAsBtn;
        
        this.setLayout(new BorderLayout());
        try{
    		if (_pModel==null){
    			throw new PreferenceException (EZEnvironment.getMessage("__EZPropertiesPanel.err.noModel"));
    		}
            _itemContainers = new Hashtable<String, ItemGUIContainer>();
            _itemKeys = new Hashtable<String, String>();
			_helpArea = new JTextArea();
			_helpArea.setRows(3);
            _helpArea.setLineWrap(true);
            _helpArea.setWrapStyleWord(true);
			_helpArea.setEditable(false);
			_helpArea.setOpaque(false);
			_helpArea.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			_headerHelpArea = new JTextArea();
			_headerHelpArea.setRows(2);
			_headerHelpArea.setLineWrap(true);
			_headerHelpArea.setWrapStyleWord(true);
			_headerHelpArea.setEditable(false);
			_headerHelpArea.setOpaque(false);
			_headerHelpArea.setText(_pModel.getDescription());
			mainPanel = new JPanel(new BorderLayout());
			headerPanel = new JPanel(new BorderLayout());
			if (_showHeader)
				headerPanel.add(EZEnvironmentImplem.getTitlePanel(_name), BorderLayout.NORTH);
			if (_showHelp)
				headerPanel.add(_headerHelpArea, BorderLayout.SOUTH);
			if (_showHeader || _showHelp)
			mainPanel.add(headerPanel, BorderLayout.NORTH);
			scroller = new JScrollPane(createPropertiesForm(), 
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			editorPanel = new JPanel(new BorderLayout());
			btnPanel = new JPanel(new BorderLayout());
			btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			restoreBtn = new JButton(EZEnvironment.getMessage("__EZPropertiesPanel.btn.restore"));
			restoreBtn.addActionListener(new RestoreDefaultsAction());
			saveAsBtn = new JButton(EZEnvironment.getMessage("__EZPropertiesPanel.btn.savedef"));
			saveAsBtn.addActionListener(new SaveAsDefaultsAction());
			if (_showSaveAsDefBtn)
				btnPanel.add(saveAsBtn, BorderLayout.WEST);
			if(_showRestoreBtn)
				btnPanel.add(restoreBtn, BorderLayout.EAST);
			
			editorPanel.add(scroller, BorderLayout.CENTER);
			editorPanel.add(btnPanel, BorderLayout.SOUTH);
			mainPanel.add(editorPanel, BorderLayout.CENTER);
			mainPanel.add(_helpArea, BorderLayout.SOUTH);
			_helpArea.setText("");
            this.add(mainPanel, BorderLayout.CENTER);
			_configOk = true;
        }
        catch (Exception ex){
        	throw new PreferenceException ("Unable to create client GUI: "+ex);
        }
    }
    
    public Dimension getMaximumSize(){
    	return this.getPreferredSize();
    }
    
    public Dimension getMinimumSize(){
    	return MINI_SIZE;
    }
	
	private void setPropertiesModel(PropertiesModel manager){
		_pModel = manager;
	}

    private JTextField createTextField(){
        JTextField tf;
        
        tf = new JTextField();

        return tf;
    }
    private JCheckBox createCheckBox(){
    	JCheckBox tf;
        
        tf = new JCheckBox();

        return tf;
    }
	/**
	 * Helper method to create a JSpinner.
	 * 
	 * @param range the range of the spinner
	 * @param dtype data type. One of the DTYPE_XXX constants defined in
	 * com.korilog.kform.core.Item.
	 */
    private JSpinner createSpinner(Range range, int dtype){
        JSpinner     spin;
        SpinnerModel model;
        
        if (dtype==Item.DTYPE_INT){
            model = new SpinnerNumberModel(
                    ((IntRange)range).getRangeDef(),
                    ((IntRange)range).getRangeFrom(),
                    ((IntRange)range).getRangeTo(),
                    1);
        }
        else{
            model = new SpinnerNumberModel(
                    ((DoubleRange)range).getRangeDef(),
                    ((DoubleRange)range).getRangeFrom(),
                    ((DoubleRange)range).getRangeTo(),
                    1);
        }
        spin= new JSpinner(model);
        
        return spin;
    }
	/**
	 * Helper method to create a JComboBox.
	 */
    private JComboBox<ChoiceEntry> createCombo(){
        JComboBox<ChoiceEntry> cbx;
        
        cbx =  new JComboBox<>();
        return cbx;
    }
	/**
     * Helper method to fill in a JComboBox correctly with data contained in item.
     */
    private void fillCombo(JComboBox<ChoiceEntry> combo, Item item, String itemName){
        Choice             choice;
        Object[]           sortedKeys;
        int                i;
        
        choice = item.getChoice();
        sortedKeys = choice.getEntriesName().toArray();
        Arrays.sort(sortedKeys);
        for(i=0;i<sortedKeys.length;i++){
            combo.addItem(choice.getEntry(sortedKeys[i].toString()));
        }
    }
    /**
     * Helper method to create a JLabel from an Item object.
     */
    private JLabel createLabel(Item item){
        JLabel lbl;
        
        if (item.getOptional()){
            lbl = new JLabel(item.getName()+END_PARAM);
        }
        else{
            lbl = new JLabel("<HTML><B>"+item.getName()+END_PARAM+"</B></HTML>");
        }
        
        lbl.setToolTipText(item.getDescription());
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        return lbl;
    }
    /**
     * Helper method to add the content of a FormItem within a container. Note that
     * this container is the parameter builder.
     */
    
    private void addFields(DesignGridLayout layout, RowGroup group, FormItem fItem){
        Iterator<String>   iter;
        Item               item;
        String             itemName;
        Range              range;
        ItemGUIContainer   guiC;
        JTextField         txtField;
        JComboBox<ChoiceEntry> combo;
        JCheckBox          check;
        JSpinner           spinner;
        IRowCreator        row;
        
        
        iter = fItem.getItems();
        while(iter.hasNext()){
            row = layout.row().group(group);
        	itemName = iter.next().toString();
            item = _pModel.getItem(itemName);
            guiC = new ItemGUIContainer(item);
            if (item.getCtype()==Item.CTYPE_VALUE){
                range = item.getRange();
                if (range!=null){
                    spinner = createSpinner(range, item.getDtype());
                    ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addFocusListener(new GuiCompoFocusListener(item));
                    guiC.setType(ItemGUIContainer.SPINNER_TYPE);
                    guiC.setSpinner(spinner);
                }
                else{
                    if(item.getDtype()==Item.DTYPE_BOOLEAN){
                    	check = createCheckBox();
                    	check.addFocusListener(new GuiCompoFocusListener(item));
                    	guiC.setType(ItemGUIContainer.CHECKBOX_TYPE);
                        guiC.setCheckBox(check);
                    }
                    else{
                    	txtField = createTextField();
                        txtField.addFocusListener(new GuiCompoFocusListener(item));
                        if (item.getDefault()!=null){
                            txtField.setText(item.getDefault().toString());
                        }
                        guiC.setType(ItemGUIContainer.TXT_FIELD_TYPE);
                        guiC.setTxtField(txtField);
                    }
                }
                if(item.getDtype()==Item.DTYPE_FOLDER || item.getDtype()==Item.DTYPE_FILE
                    || item.getDtype()==Item.DTYPE_FILES){
                    JButton btn = new JButton(EZEnvironment.getImageIcon("folder.png"));
                    if(item.getDtype()==Item.DTYPE_FOLDER)
                    	btn.addActionListener(new ChooseFolderAction(guiC.getTxtField()));
                    else
                    	btn.addActionListener(new ChooseFileAction(guiC.getTxtField(), item.getDtype()==Item.DTYPE_FILES));
                    	
                    JPanel selectDirectoryPanel = new JPanel();
                    new DesignGridLayout(selectDirectoryPanel).margins(0).row().right().add(guiC.getComponent()).add(btn).fill();
                    //row.grid(createLabel(item)).add(guiC.getComponent()).add(selectDirectoryPanel);
                    row.grid(createLabel(item)).add(selectDirectoryPanel);
                }
                else{
                	row.grid(createLabel(item)).add(guiC.getComponent());
                }
            }
            else{
                guiC.setType(ItemGUIContainer.COMBO_TYPE);
                combo = createCombo();
                combo.addFocusListener(new GuiCompoFocusListener(item));
                combo.addActionListener(new ComboSelectionActionListener(item));
                fillCombo(combo, item, itemName);
                guiC.setCombo(combo);
                //builder.append(createLabel(item), combo);
                row.grid(createLabel(item)).add(combo);
            }
            _itemContainers.put(itemName, guiC);
            _itemKeys.put(item.getKey(), itemName);
            //if (iter.hasNext())
            //    builder.nextLine();
        }
        
    }
    private void addGroup(DesignGridLayout layout, String name, RowGroup group)
      	{
    		if (name==null || name.length()==0){
    			layout.row().group(group);
    		}
    		else{
        		JFoldedButton groupBox = new JFoldedButton(name);
          		groupBox.setName(name);
          		//groupBox.setForeground(Color.BLUE);
          		groupBox.setSelected(true);
          		groupBox.addItemListener(new ShowHideAction(group));
          		layout.emptyRow();
          		layout.row().left().add(groupBox, new JSeparator()).fill();
    		}
   	}
    private class ShowHideAction implements ItemListener
      	{
      		public ShowHideAction(RowGroup group)
      		{
      			_group = group;
      		}
      		
      		@Override public void itemStateChanged(ItemEvent event)
      		{
      			if (event.getStateChange() == ItemEvent.SELECTED)
      			{
      				_group.show();
      			}
     			else
      			{
      				_group.hide();
      			}
      			//frame().pack();
      		}
      		
      		final private RowGroup _group;
     	}
    /**
     * This method creates the form.
     */
    private Component createPropertiesForm(){
        ItemGUIContainer   guiC;
        FormItem           fItem;
        DesignGridLayout   layout;
        Iterator<FormItem> iter;
        Iterator<ItemGUIContainer> iterUI;
        Choice             choice;
        ChoiceEntry        ce;
        JPanel             formsPanel, mainPanel, fPanel;
        
        formsPanel = new JPanel();
        formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));

        iter = _pModel.getFormItems();
        if (iter.hasNext()){
            while(iter.hasNext()){
                fItem = iter.next();
                fPanel = new JPanel();
                layout = new DesignGridLayout(fPanel);
                
                RowGroup group = new RowGroup();
                addGroup(layout, fItem.getName(), group);
                
                addFields(layout, group, fItem);
                formsPanel.add(fPanel);
                if (iter.hasNext())
                	formsPanel.add(Box.createRigidArea(new Dimension(0,8)));
            }
        }
        iterUI = _itemContainers.values().iterator();
        while(iterUI.hasNext()){
            guiC = iterUI.next();
            if (guiC.getType()==ItemGUIContainer.COMBO_TYPE){
                choice = guiC.getItem().getChoice();
                ce = choice.getEntry(choice.getDefault()); 
                if (ce!=null){
                    guiC.getCombo().setSelectedItem(ce);
                }
                else{
                   guiC.getCombo().setSelectedIndex(0);
                }
            }
        }
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formsPanel, BorderLayout.NORTH);
        return mainPanel;
    }

    private class GuiCompoFocusListener extends FocusAdapter{
    	private Item item;
    	
    	public GuiCompoFocusListener(Item it){
    		item = it;
    	}
    	public void focusGained(FocusEvent e){
    		if (_canUpdateHelp)
				_helpArea.setText(item.getName()+": "+item.getDescription());
    	}
    }
    /**
     * This class hanldes actions made by a user on a ComboBox item type.
     */
    private class ComboSelectionActionListener implements ActionListener {
        private Item _item;
        
        public ComboSelectionActionListener(Item item){
        	_item = item;	
        }
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent event){
            ChoiceEntry          entry;
            ItemGUIContainer     guiC;
            JComboBox<ChoiceEntry> combo;
            Iterator<FormAction> iter;
            FormAction           fa;
            
            combo = (JComboBox<ChoiceEntry>) event.getSource();
            entry = (ChoiceEntry) combo.getSelectedItem();
            if (entry==null)
                return;
			if (_canUpdateHelp)
				_helpArea.setText(_item.getName()+": "+entry.getName()+": "+entry.getDescription());
            //System.out.println(entry);
            if (entry.hasActions()==false)
                return;
            iter = entry.getActions();
			_canUpdateHelp=false;
            while(iter.hasNext()){
                fa = iter.next();
                guiC = _itemContainers.get(fa.getTargetItemName());
                fa.executeAction(guiC);
            }
			_canUpdateHelp=true;
        }
    }

    private String getValue(String key){
    	ItemGUIContainer guiC;
        Item             item;
        Object           value;
        
        guiC = (ItemGUIContainer) _itemContainers.get(key);
        if (guiC==null){//this is for hidden parameters
            item = _pModel.getItem(key);
            if (item==null)
                return null;
            value = item.getDefault();
            return (value!=null ? value.toString():null);
        }
        if (guiC.isEnabled()==false)
            return null;
        return (guiC.getValue());   
    }
    private Properties getValues(Set<String> items) throws ItemException{
    	ItemGUIContainer guiC;
    	Properties       values;
        Item             item;
        String           value;
        
        values = new Properties();
        for(String key : items){
            item = _pModel.getItem(key);
            guiC = (ItemGUIContainer) _itemContainers.get(key);
            if (guiC==null){//hidden element
            	value = _passedProperties.getProperty(item.getKey());
            	if (value!=null){
            		values.put(item.getKey(), value);
            	}
            }
            else{
	            value = getValue(key);
	            if (value!=null){
	                try {
						item.controlValue(value);
					} catch (ItemException e) {
						guiC.setBackgroundColor(Color.ORANGE);
						throw e;
					}
	                values.put(item.getKey(), value);
	            }
	            guiC.setBackgroundColor(Color.WHITE);
            }
        }
        return values;
    }
    /**
     * Returns the values that can be saved.
     */
    public Properties getValues() throws ItemException{
        if (!_configOk)
        	return null;
        else
            return (getValues(_pModel.getItemsName()));   
    }
    
    /**
     * Sets defaults values.
     */
    public void setDefaultValues(Properties values){
    	if (values!=null)
    		_defaultProperties = values;
    }
    private void setValues(Properties values, boolean forceEditedFlag){
    	ItemGUIContainer guiC;
    	Iterator<Object> iter;
    	String           key, name;
    	
    	if (values==null || values.isEmpty())
    		return;
    	_passedProperties = values;
    	iter = values.keySet().iterator();
    	while(iter.hasNext()){
    		key = (String) iter.next();
    		name = (String) _itemKeys.get(key);
    		if (name==null)
    			continue;
    		guiC = (ItemGUIContainer) _itemContainers.get(name);
    		if (guiC!=null){
    			guiC.setValue(values.get(key));
    			guiC.setEdited(forceEditedFlag);
    		}
    	}
    	_helpArea.setText("");
    }
    public void setHelpAreaText(String txt){
    	_helpArea.setText(txt);
    }
    /**
     * Sets some values to the properties displayed by this panel.
     */
    public void setValues(Properties values){
    	setValues(values, false);
    }
    /**
     * Figures out whether some properties have been edited.
     */
    public boolean isEdited(){
    	ItemGUIContainer guiC;
    	boolean          bRet = false;
    	Set<String>      ls;
    	
    	ls = _pModel.getItemsName();
    	for(String key : ls){
    		guiC = (ItemGUIContainer) _itemContainers.get(key);
    		if (guiC!=null && guiC.isEdited()){
    			bRet = true;
    			break;
    		}
    	}
    	
    	return bRet;
    }
    public String getName(){
    	return _name;
    }
    public void saveData(String path) throws PreferenceException{
    	Properties       data;
    	DataConnector    conn;
    	
    	if (path==null)
    		return;
		data = getValues();
		conn = _conf.getDataConnector();
		if (conn instanceof PropertiesDataConnector == false){
			throw new PreferenceException("invalid DataConnector: expected a PropertiesDataConnector");
		}
    	try{
    		((PropertiesDataConnector)conn).save(path, data);
    	}
    	catch(IOException ex){//invalid data
    		String msg = _conf.getName()+": \n"+EZEnvironment.getMessage("__EZCTextEditor.err.save")+":\n"+path+":\n"+ex.getMessage();
    		throw new PreferenceException(msg);
    	}
    }
    private class RestoreDefaultsAction implements ActionListener{
    	public void actionPerformed(ActionEvent event){
    		setValues(_defaultProperties, true);
    	}
    }
    private class SaveAsDefaultsAction implements ActionListener{
    	public void actionPerformed(ActionEvent event){
    		if (!EZEnvironment.confirmMessage(PropertiesPanel.this, EZEnvironment.getMessage("__EZConfDlg.saveDef.msg"))){
    			return;
    		}
    		try {
				saveData(_conf.getDefaultConfigurationLocator());
			} catch (PreferenceException e) {
				EZEnvironment.displayWarnMessage(PropertiesPanel.this, e.getMessage());
			}
    	}
	}
	private class ChooseFolderAction implements ActionListener{
    	private JTextField _field;
    	private ChooseFolderAction(JTextField field){
    		_field = field;
    	}
    	public void actionPerformed(ActionEvent event){
    		File f;
    		
    		f = EZFileManager.chooseDirectory(PropertiesPanel.this, null, null);
    		if (f!=null)
    			_field.setText(f.getAbsolutePath());
    	}
    }
	private class ChooseFileAction implements ActionListener{
    	private JTextField _field;
    	private boolean _enableMultipleSelection;
    	
    	private ChooseFileAction(JTextField field, boolean enableMultipleSelection){
    		_field = field;
    		_enableMultipleSelection = enableMultipleSelection;
    	}
    	public void actionPerformed(ActionEvent event){
    		if (_enableMultipleSelection){
    		  File[] fs = EZFileManager.chooseFilesForOpenAction(PropertiesPanel.this, null, null);
    		  if (fs!=null){
    		    StringBuffer buf = new StringBuffer();
    		    Iterator<File> flst = Arrays.asList(fs).iterator();
    		    while(flst.hasNext()){
    		      buf.append(flst.next());
    		      if (flst.hasNext()){
    		        buf.append(",");
    		      }
    		    }
    		    _field.setText(buf.toString());
    		  }
    		}
    		else{
          File f = EZFileManager.chooseFileForOpenAction(PropertiesPanel.this, null, null);
          if (f!=null)
            _field.setText(f.getAbsolutePath());
    		}
    	}
    }
}
