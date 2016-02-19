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
package com.plealog.prefs4j.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.plealog.prefs4j.api.DataConnector;
import com.plealog.prefs4j.api.DataConnectorFactory;
import com.plealog.prefs4j.api.PreferenceException;
import com.plealog.prefs4j.implem.core.PreferenceSectionImplem;
import com.plealog.prefs4j.ui.PreferenceEditor;
import com.plealog.prefs4j.ui.PreferenceEditorFactory;


public class EZFormTest {
	private static final String cfgPath = "./src/com/ezlog/ezform/test/";
	
    public static void locateOnOpticalScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (int) ((screenSize.height - paneSize.height) *0.45));
    }
    public static PreferenceEditor getEditor(
    		String confPath, String descFile, String confFile)
    		throws Exception{
    	PreferenceSectionImplem co;
    	
    	co = new PreferenceSectionImplem("a");
    	co.setDescriptorLocator(confPath+descFile);
    	co.setResourceLocator(confPath+"testMessages.properties");
		co.setConfigurationLocator(confPath+confFile);
		co.setDefaultConfigurationLocator(confPath+confFile);
    	co.setConfType(PreferenceEditor.TYPE.kvp.toString());
    	co.setDataConnector(DataConnectorFactory.getDataConnector(DataConnector.TYPE.props.toString()));
    	return PreferenceEditorFactory.getEditor(co, null);
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//ConfigurationEditor getEditor;
		
		String confPath = cfgPath;
		String descFile = "test.desc";
		String confFile = "test.config";
		
		try {
			JPanel  mainpanel;
			JButton btn;
			PreferenceEditor editor;
			
	        JFrame frame = new JFrame();
	        frame.setTitle(EZFormTest.class.getName());
	        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        mainpanel = new JPanel(new BorderLayout());
	        btn = new JButton("validate!");
	        
	        editor = EZFormTest.getEditor(confPath, descFile, confFile);
	        btn.addActionListener(new ValidateAction(editor));
	        mainpanel.add(editor.getEditor(),BorderLayout.CENTER);
	        mainpanel.add(btn,BorderLayout.SOUTH);
	        frame.getContentPane().add(mainpanel);
	        frame.pack();
	        EZFormTest.locateOnOpticalScreenCenter(frame);
	        frame.setVisible(true);
		}
		catch (Exception e) {
			System.err.println("Error: "+e);
		}
	}
	private static class ValidateAction implements ActionListener{
		private PreferenceEditor editor;
		private ValidateAction(PreferenceEditor editor){
			this.editor = editor;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				editor.saveData();
			} catch (PreferenceException e) {
				System.err.println("Save action answer: "+e.getMessage());
			}
			
		}
		
	}
	/*private static class ValidateAction implements ActionListener{
		PropertiesPanel _p;
		public ValidateAction(PropertiesPanel p){
			_p = p;
		}
		private void test1(){
			try {
				_p.getValues();
				System.out.println("Validation ok!");
				System.out.println("Is edited: "+_p.isEdited());
			} catch (ItemException e1) {
				System.err.println("Validation error: "+e1);
			}
		}
		private void test2(){
			ConfigurationDialog dlg;
			ConfigurationObject co;
			ConfigurationModel  model;
			
			String confPath = "h:\\devel\\korilog\\kform\\src\\com\\korilog\\kform\\test\\";
			URL    url;
			
			co = new ConfigurationObject();
			co.setConfigName("test.config");
			co.setDescriptorName("testProps.config");
			try {
				url = new File(confPath).toURL();
			} catch (MalformedURLException e1) {
				System.err.println("Error: "+e1);
				return;
			}
			co.setPathToConfig(confPath);
			co.setPathToDescriptor(url);
			
			model = new ConfigurationModel();
			model.addConfObject(co);
			
			co = new ConfigurationObject();
			co.setConfigName("BlasterScheduler.config");
			co.setDescriptorName("BlasterSchedulerProps.config");
			try {
				url = new File(confPath).toURL();
			} catch (MalformedURLException e1) {
				System.err.println("Error: "+e1);
				return;
			}
			co.setPathToConfig(confPath);
			co.setPathToDescriptor(url);
			
			model.addConfObject(co);
			
			dlg= new ConfigurationDialog(null, null, model);
			dlg.showDlg();
		}
		public void actionPerformed(ActionEvent e){
			test2();
		}
	}*/

}
