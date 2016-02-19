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
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.api.PreferenceModel;
import com.plealog.prefs4j.api.PreferenceSection;
import com.plealog.prefs4j.implem.core.PreferenceModelImplem;
import com.plealog.prefs4j.implem.core.event.ConfigurationFeatureEvent;
import com.plealog.prefs4j.ui.PreferenceComponent;
import com.plealog.prefs4j.ui.PreferenceEditor;
import com.plealog.prefs4j.ui.PreferenceEditorFactory;

public class PreferencePanel extends JPanel implements PreferenceComponent {

	private static final long serialVersionUID = -5485146002395151308L;
	private HashSet<PreferenceEditor.PROPERTY> _editorProps;
	private Map<String, PreferenceEditor>      _editors;
	private MyTreeSelectionListener            _myTreeSelectionListener;
	private PreferenceModel                    _confModel;
	private JTree                              _confTree;
	private JButton                            _restoreConfig;

	protected static final int DEFAULT_WIDTH = 640;
	protected static final int DEFAULT_HEIGHT = 480;

	public PreferencePanel(PreferenceModel model){
		this(model, DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
	}
	public PreferencePanel(PreferenceModel model, int width, int height,
			Set<PreferenceEditor.PROPERTY> props) throws PreferenceException{
    	if (model==null)
    		throw new PreferenceException("preference model is not defined");
		_confModel = model;
		_editors = new Hashtable<String, PreferenceEditor>();
    	//default properties
		_editorProps = new HashSet<PreferenceEditor.PROPERTY>();
		if (props==null){
			_editorProps.add(PreferenceEditor.PROPERTY.HEADER);
			_editorProps.add(PreferenceEditor.PROPERTY.HELP);
			//_editorProps.add(PreferenceEditor.PROPERTY.RESTORE_DEF_BTN);
			//_editorProps.add(PreferenceEditor.PROPERTY.SAVE_AS_DEF_BTN);
		}
		else{
			for(PreferenceEditor.PROPERTY prop : props){
				_editorProps.add(prop);
			}
		}
		buildGUI(width, height);
	}
	public void setPropertiesForEditor(HashSet<PreferenceEditor.PROPERTY> props){
		if (props==null)
			return;
		_editorProps = props;
	}
	@SuppressWarnings("unchecked")
	public HashSet<PreferenceEditor.PROPERTY> getPropertiesForEdiror(){
		//clone to avoid an external update of properties
		return (HashSet<PreferenceEditor.PROPERTY>)_editorProps.clone();
	}
	
	public JComponent getComponent(){
		return this;
	}
	/**
     * Initializes an editor for every ConfigurationObject contained in the
     * ConfigurationModel.
     */
    private void initEditors(JPanel cardPanel){
    	PreferenceEditor               cEditor;
    	JComponent                        compo;
    	Enumeration<PreferenceSection> nodes;
    	PreferenceSection              node;

    	nodes = _confModel.enumerator();
    	while(nodes.hasMoreElements()){
    		node = nodes.nextElement();
    		cEditor = PreferenceEditorFactory.getEditor(node, _editorProps);
    		if (cEditor==null)
    			throw new PreferenceException("unknown editor for section: "+node.getName());
    		compo = cEditor.getEditor();
    		if (compo==null){
    			throw new PreferenceException("unable to add ConfigEditor for section: "+node.getName()+": no GUI.");
    		}
    		_editors.put(node.getName(), cEditor);
    		cardPanel.add(compo, node.getName());
    	}
    }
	private void buildGUI(int width, int height){
		JPanel      propsPanel, configTreePanel;
		JSplitPane  split;
		JScrollPane scroll;
		
    	configTreePanel = new JPanel();
    	configTreePanel.setLayout(new BorderLayout());
    	propsPanel = new JPanel();
    	propsPanel.setLayout(new CardLayout());

    	//content panel
    	initEditors(propsPanel);

    	//create tree structure for configuration nodes
		_myTreeSelectionListener = new MyTreeSelectionListener(propsPanel);
		_confTree = new JTree(_confModel.getPreferenceSectionsHierarchy());
		_confTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		_confTree.setRootVisible(false);
		_confTree.setShowsRootHandles(true);
		_confTree.addTreeSelectionListener(_myTreeSelectionListener);
		_confTree.setCellRenderer(new CustomIconRenderer());
		_confTree.setBorder(null);
		scroll = new JScrollPane(_confTree);
		configTreePanel.add(scroll, BorderLayout.CENTER);
		
		//restore global
    	_restoreConfig = new JButton(EZEnvironment.getMessage("__EZConfDlg.restoreBtn"));
    	configTreePanel.add(_restoreConfig, BorderLayout.SOUTH);
    	_restoreConfig.setEnabled(false);

    	//not yet available
    	_restoreConfig.setVisible(false/*_editorProps.contains(ConfigurationEditor.PROPERTY.RESTORE_DEF_BTN)*/);
    	
    	split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, configTreePanel, propsPanel);
    	configTreePanel.setPreferredSize(new Dimension(width/3-20, height-100));
    	propsPanel.setPreferredSize(new Dimension(2*width/3-20, height-100));
    	split.setBorder(null);
		_confTree.setSelectionPath(_confTree.getPathForRow(0));

		this.setLayout(new BorderLayout());
		this.add(split, BorderLayout.CENTER);
	}
	//not yet available
    protected void setRestoreDefaultsAction(ActionListener al){
    	if (al==null)
    		return;
    	_restoreConfig.setEnabled(true);
    	_restoreConfig.addActionListener(al);
    }
    private void notifyListeners(PreferenceEditor editor){
    	PreferenceSection co;
    	
    	co = editor.getPreferenceSection();
    	if (co==null)
    		return;
    	//TODO: avoid cast using another mechanism
    	((PreferenceModelImplem)_confModel).fireConfigurationModelEvent(new ConfigurationFeatureEvent(this, co));
    }
    /*private void dumpData(String name, ConfigurationObject co, Properties data){
	String           str;
	FileOutputStream os;
	
	try {
		str = KResources.terminatePath(co.getPathToConfig());
		os = new FileOutputStream(str+co.getConfigName());
		KResources.saveProperties(data, os, name);
		os.flush();
		os.close();
	}
	catch (Exception e) {
		_logger.warn(
				KMessages.getString("ConfDlf.err.saveData")+": "+
				name+": "+
				co.getConfigName()+", "+
				co.getPathToConfig()+": "+e);
	}
    }*/

    public void saveData() throws PreferenceException{
    	Enumeration<PreferenceEditor> editors;
        PreferenceEditor              editor;
    	
    	editors = enumerator();
        while(editors.hasMoreElements()){
        	editor = editors.nextElement();
        	if (!editor.isEdited())
        		continue;
			try {
				editor.saveData();
			} catch (PreferenceException e) {
				//selectEditor(editor.getName());
				throw e;
			}
        	notifyListeners(editor);
        }
    }
    public void selectEditor(String editorName){
    	DefaultMutableTreeNode node, current, root;
    	PreferenceSection   conf;
    	
    	if (_editors.containsKey(editorName) == false)
    		return;
    	
    	conf = _editors.get(editorName).getPreferenceSection();
    	if (conf==null)
    		return;
    	String confId;
    	
    	confId = conf.getId();
    	root = (DefaultMutableTreeNode)_confTree.getModel().getRoot();
    	node = null;
    	if (root != null)
    	    for (Enumeration<?> e = root.breadthFirstEnumeration(); e.hasMoreElements();){
    	        current = (DefaultMutableTreeNode)e.nextElement();
     	        if (((PreferenceSection) current.getUserObject()).getId().equals(confId)){
    	            node = current;
    	            break;
    	        }
    	    }

    	if (node != null){
    		_confTree.setSelectionPath(new TreePath(node.getPath()));
    	}
    }
    public Enumeration<PreferenceEditor> enumerator(){
		return new Enumeration<PreferenceEditor>() {
            Iterator<String> keys;
            boolean          bFirst = true;
            
            private void initialize(){
                keys = _editors.keySet().iterator();
                bFirst = false;
            }
            
            public boolean hasMoreElements() {
                if (bFirst){
                    initialize();
                 }
                return (keys.hasNext());
            }
            
            public PreferenceEditor nextElement() {
                if (bFirst){
                    initialize();
                }
                return (_editors.get(keys.next()));
            }
        };
	}
	private class MyTreeSelectionListener implements TreeSelectionListener{
		private JPanel cardPanel;
		
		private MyTreeSelectionListener(JPanel cardPanel){
			this.cardPanel = cardPanel;
		}
		@Override
		public void valueChanged(TreeSelectionEvent event) {
			TreePath             path = event.getPath();
			Object               obj;
			PreferenceSection node;
			
			obj = path.getLastPathComponent();
			if (obj==null)
				return;
			node = (PreferenceSection) ((DefaultMutableTreeNode)obj).getUserObject();
			if (node==null)
				return;
			//System.out.println(node.getName());
			((CardLayout)cardPanel.getLayout()).show(cardPanel, node.getName());
			_editors.get(node.getName()).editorShowed();
		}
	}
	
	@SuppressWarnings("serial")
	private class CustomIconRenderer extends DefaultTreeCellRenderer {
		//from: http://www.daniweb.com/software-development/java/threads/117886
		public CustomIconRenderer() {
		}

		public Component getTreeCellRendererComponent(JTree tree,
				Object value,boolean sel,boolean expanded,boolean leaf,
				int row,boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, 
					expanded, leaf, row, hasFocus);

			PreferenceSection nodeObj = (PreferenceSection) ((DefaultMutableTreeNode)value).getUserObject();
			if (nodeObj.getIcon()!=null) {
				setIcon(nodeObj.getIcon());
			} 
			return this;
		}
	}
}
