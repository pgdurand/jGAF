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
package com.plealog.prefs4j.implem.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.plealog.genericapp.api.EZEnvironment;
import com.plealog.genericapp.api.file.EZFileUtils;
import com.plealog.genericapp.ui.starter.EZEnvironmentImplem;
import com.plealog.prefs4j.api.DataConnectorFactory;
import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.api.PreferenceSection;
import com.plealog.prefs4j.implem.core.event.ConfigurationModelListenerSupport;

public class PreferenceModelImplem extends ConfigurationModelListenerSupport {

	private Map<String, PreferenceSectionImplem>    _dataNodes;
	private String[]                                _nodesOrder;
	private String                                  _masterConfLocator;
	private String                                  _userConfLocator;
	private String                                  _resourceLocator;
	private ResourceBundle                          _localizedMessages;

	private static final String RES_KEY      = "res:";
	private static final String UNKNOWN      ="?";
	private static final String NODE_LOCATOR = "locator.root";
	private static final String NODE_IDS   = "sections";
	private static final String NODE_RESS  = "resources";
	private static final String NODE_PFX   = "section.";
	private static final String NODE_NAME  = ".name";
	private static final String NODE_DESC  = ".descriptor";
	private static final String NODE_CONF  = ".config";
	private static final String NODE_DCONF = ".defconfig";
	private static final String NODE_TYPE  = ".type";
	private static final String NODE_EDT   = ".editor";
	private static final String NODE_PARE  = ".parent";
	private static final String NODE_ICON  = ".icon";
	
	private PreferenceModelImplem(){super();}

	public PreferenceModelImplem(String masterConfigLocator) throws PreferenceException{
		this();
		initialize(masterConfigLocator, null);
	}
	//1.masterConfigLocator : pointe toujours sur un fichier (descripteur des nodes)
	//2. si la cle ressource est definie, c'est forcement un fhcier situe dans le meme rep que de descripteur des noeuds
	//3. l'acces aux elements des noeuds (desc + conf+ defConf) est donne par la cle "locator.root" du descripteur des noeuds
	//   si non defini, on supose qu'ils sont dans le meme rep que le masterConfFile
	//   sinon, on utilise le chemin donne par cette cle
	//4. si userConfLocator est null, alors tout est dans le meme rep du master conf file
	//   sinon la conf de l'utilisateur est specifie par cette valeur
	// note : tous ces locators sont associes aux DataConnectors qui sont capables de les utiliser.
	//        par defaut, les implems sont des fichiers    
	public PreferenceModelImplem(String masterConfigLocator, String userConfLocator) throws PreferenceException{
		this();
		initialize(masterConfigLocator, userConfLocator);
	}
	
	private String getValue(Properties props, String key){
		String value;
		
		value = props.getProperty(key);
		if (value==null)
			return null;
		value=value.trim();
		if (value.length()>0)
			return value;
		else
			return null;
	}
	private PreferenceSectionImplem readDataNode(
			Map<String, PreferenceSectionImplem> curNodes, 
			String confLocator, 
			String userConfLocator, 
			String nodeId, 
			Properties props)
			throws PreferenceException{
		PreferenceSectionImplem node;
		String              value;
		ImageIcon           icon;
		
		if (curNodes.containsKey(nodeId)){
			throw new PreferenceException("nodes list: node '"+nodeId+"' cited more than once");
		}
		node = new PreferenceSectionImplem(nodeId);
		node.setName(getMessage(props.getProperty(NODE_PFX+nodeId+NODE_NAME)));
		value = props.getProperty(NODE_PFX+nodeId+NODE_TYPE);
		node.setDataConnector(DataConnectorFactory.getDataConnector(value));
		if (node.getDataConnector()==null){
			throw new PreferenceException("undefined data type: "+value+": for node '"+nodeId+"'");
		}
		node.setConfType(props.getProperty(NODE_PFX+nodeId+NODE_EDT));
		value = getValue(props, NODE_PFX+nodeId+NODE_PARE);
		if (value==null)
			node.setParent(PreferenceSectionImplem.ROOT_NODE);
		else
			node.setParent(value);

		value = getValue(props, NODE_PFX+nodeId+NODE_DESC);
		if (value!=null){
			node.setDescriptorLocator(confLocator+value);
		}
		value = getValue(props, NODE_PFX+nodeId+NODE_CONF);
		if (value!=null){
			if (userConfLocator==null)
				node.setConfigurationLocator(confLocator+value);
			else
				node.setConfigurationLocator(userConfLocator+value);
		}
		value = getValue(props, NODE_PFX+nodeId+NODE_DCONF);
		if (value!=null){
			node.setDefaultConfigurationLocator(confLocator+value);
		}
		if (_resourceLocator!=null){
			node.setResourceLocator(_resourceLocator);
		}
		value = getValue(props, NODE_PFX+nodeId+NODE_ICON);
		if (value!=null){
			icon = EZEnvironment.getImageIcon(value);
			node.setIcon(icon);
		}
		
		node.check();
		if (nodeId.equals(node.getParentId())){
			throw new PreferenceException("node '"+nodeId+"' refers to itself as parent node");
		}
		curNodes.put(nodeId, node);
		return node;
	}
	private Map<String, PreferenceSectionImplem> getNodeDataObjects(
			String[] ids, String confLocator, String userConfLocator, Properties props) throws PreferenceException{
		Hashtable<String, PreferenceSectionImplem> dataNodes;
		
		dataNodes = new Hashtable<String, PreferenceSectionImplem>();
		for(String nodeId:ids){
			readDataNode(dataNodes, confLocator, userConfLocator, nodeId, props);
		}
		if(dataNodes.isEmpty()){
			StringBuffer buf = new StringBuffer();
			for(String id : ids){
				buf.append(id); buf.append(",");
			}
			String str = buf.toString();
			throw new PreferenceException("unable to find a description for any of nodes: "+str.substring(0, str.length()-1));
		}
		return dataNodes;
	}
	private String[] getNodeIds(Properties props)throws PreferenceException{
		String[] ids;
		
		ids = EZEnvironmentImplem.tokenize(props.getProperty(NODE_IDS));
		if (ids.length==0)
			throw new PreferenceException("configuration does not contain any nodes.");
		return ids;			
	}
	private void initialize(String confDir, String userConfLocator, InputStream is) throws PreferenceException{
		Properties props;
		String     value, confLocator;
		
		props = new Properties();
		try {
			props = EZEnvironmentImplem.loadProperties(is);
		} catch (IOException e) {
			throw new PreferenceException("unable to read configuration: "+e.toString());
		}
		EZEnvironmentImplem.addSearchPath(confDir);
		value = getValue(props, NODE_RESS);
		if (value==null){
			_resourceLocator = null;
		}
		else{
			//cannot use ResourceBundle.getBundle() because it works only on java packages
			_resourceLocator = confDir+value+"_"+Locale.getDefault().getCountry().toLowerCase()+".properties";
			if (new File(_resourceLocator).exists() == false){
				_resourceLocator = confDir+value+".properties";
				if (new File(_resourceLocator).exists() == false){
					throw new PreferenceException("unable to read configuration resources: file not found: "+_resourceLocator);
				}
			}
			
			try {
				FileInputStream fis = new FileInputStream(_resourceLocator);
				_localizedMessages = new PropertyResourceBundle(fis);
				fis.close();
			} catch (IOException e) {
				throw new PreferenceException("unable to read resource locator file: "+_resourceLocator+": "+e.toString());
			}
		}
		value = getValue(props, NODE_LOCATOR);
		if (value!=null){
			confLocator = value;
		}
		else{
			confLocator = confDir;
		}
		_nodesOrder = getNodeIds(props);
		_dataNodes = getNodeDataObjects(_nodesOrder, confLocator, userConfLocator, props);
		
	}
	private void initialize(String masterConfLocator, String userConfLocator) throws PreferenceException{
		FileInputStream fis = null;
		File            f;
		
		_masterConfLocator = masterConfLocator;
		_userConfLocator = userConfLocator;
		f = new File(masterConfLocator);
		try {
			fis = new FileInputStream(masterConfLocator);
			initialize(
					EZFileUtils.terminatePath(f.getParent()), 
					userConfLocator!=null?EZFileUtils.terminatePath(userConfLocator):null, 
					fis);
		} catch (IOException e) {
			throw new PreferenceException("unable to read configuration: "+masterConfLocator+": "+e.toString());
		}
		finally{
			try{if (fis!=null){fis.close();}}catch(Exception ex){}
		}
	}
	public String getResourceLocator(){
		return _resourceLocator;
	}
	public String getPreferenceModelLocator(){
		return _masterConfLocator;
	}
	public String getUserPreferenceLocator(){
		return _userConfLocator;
	}
	public Enumeration<PreferenceSection> enumerator(){
		return new Enumeration<PreferenceSection>() {
            Iterator<String> keys;
            boolean          bFirst = true;
            
            private void initialize(){
                keys = _dataNodes.keySet().iterator();
                bFirst = false;
            }
            
            public boolean hasMoreElements() {
                if (bFirst){
                    initialize();
                 }
                return (keys.hasNext());
            }
            
            public PreferenceSectionImplem nextElement() {
                if (bFirst){
                    initialize();
                }
                return (_dataNodes.get(keys.next()));
            }
        };
	}
	public MutableTreeNode getPreferenceSectionsHierarchy(){
		DefaultMutableTreeNode                    root;
		Hashtable<String, DefaultMutableTreeNode> treeNodes;
		PreferenceSectionImplem                node;
		Set<String>                               nodeIds;
		
		root = new DefaultMutableTreeNode(new PreferenceSectionImplem(PreferenceSectionImplem.ROOT_NODE));
		treeNodes = new Hashtable<String, DefaultMutableTreeNode>();
		treeNodes.put(PreferenceSectionImplem.ROOT_NODE, root);
		
		for(String nodeId : _nodesOrder){
			node = _dataNodes.get(nodeId);
			addParentNode(node, treeNodes);
		}
		
		//check tree structure: for each node in _dataNodes, find it from Root.
		//if not found, connect to root.
		//this is to avoid loosing ConfNode wrongly defined
		nodeIds = _dataNodes.keySet();
		traverseTreeNodes(root, nodeIds);
		if (nodeIds.isEmpty() == false){
			for(String id : nodeIds){
				root.add(new DefaultMutableTreeNode(_dataNodes.get(id)));
			}
		}
		return root;
	}
	//nodes are added, and tree structure, are created by scanning parent nodes from curNode
	//given a curNode, get its parent (if any) and branch them together. Then from that
	//parent, call again this method.
	private void addParentNode(PreferenceSectionImplem curNode, Hashtable<String, DefaultMutableTreeNode> treeNodes){
		DefaultMutableTreeNode parent, tnode;
		PreferenceSectionImplem    parentNode;
		String                 parentId;
		
		//reach root: end!
		if(curNode==null)
			return;
		
		//get parent data (id and node object)
		parentId = curNode.getParentId();
		parentNode = _dataNodes.get(parentId);
		
		//parent already seen ?
		parent = treeNodes.get(parentId);
		if (parent==null){//do not create node if it exists
			parent = new DefaultMutableTreeNode(parentNode);
			treeNodes.put(parentId, parent);
		}
		
		//curNode already seen ?
		tnode = treeNodes.get(curNode.getId());
		if (tnode==null){//do not create node if it exists
			tnode = new DefaultMutableTreeNode(curNode);
			treeNodes.put(curNode.getId(), tnode);
		}
		
		//link parent and curNode
		if (linkNodes(parent, tnode)){//do not create link parent/node if it exists
			parent.add(tnode);
		}
		
		//go up by scanning from parent
		addParentNode(parentNode, treeNodes);
	}
	private boolean linkNodes(DefaultMutableTreeNode parent, DefaultMutableTreeNode tnode){
		DefaultMutableTreeNode node;
		
		for (int i = 0; i < parent.getChildCount(); i++){
			node =  (DefaultMutableTreeNode) parent.getChildAt(i);
			//compare by reference because we use same objects
			if (node == tnode)
				return false;
		}
		return true;
	}
	private void traverseTreeNodes(TreeNode r, Set<String> nodeIds){
		DefaultMutableTreeNode node;
		String                 id;
		
		for (int i = 0; i < r.getChildCount(); i++){
			node =  (DefaultMutableTreeNode) r.getChildAt(i);
			id = ((PreferenceSectionImplem)node.getUserObject()).getId();
			if (nodeIds.contains(id)){
				nodeIds.remove(id);
			}
			if (!r.getChildAt(i).isLeaf()){
				traverseTreeNodes(r.getChildAt(i), nodeIds);
			}
		}	
	}
	
	private String getMessage(String val){
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
