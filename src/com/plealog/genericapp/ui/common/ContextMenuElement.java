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
package com.plealog.genericapp.ui.common;

import java.util.List;

import javax.swing.Action;

/**
 * This class defines an element to be used with a ContextMenuManager.
 * 
 * @author Patrick G. Durand
 */
public class ContextMenuElement {
  private Action       act;
  private List<ContextMenuElement> subActions;

  /**
   * Constructor.
   */
  public ContextMenuElement(){}

  /**
   * Constructor.
   * 
   * @param act action to wrap into a menu element
   */
  public ContextMenuElement(Action act) {
    super();
    this.act = act;
  }

  /**
   * Constructor.
   * 
   * @param act action to wrap into a menu element
   * @param subActions list of sub-actions of this menu element
   */
  public ContextMenuElement(Action act, List<ContextMenuElement> subActions) {
    super();
    this.act = act;
    this.subActions = subActions;
  }
  /**
   * Get the action that will be used to create the menu item.
   * 
   * @return the action wrapped in this menu element
   */
  public Action getAct() {
    return act;
  }
  /**
   * Set the action that will be used to create the menu item.
   * 
   * @param act the action wrapped in this menu element
   */
  public void setAct(Action act) {
    this.act = act;
  }
  /**
   * Get the list of sub-items. Please note that this release of
   * ContextMenuManager only handles one level of sub-items.
   * 
   * @return a list of sub-items or null if none are available
   */
  public List<ContextMenuElement> getSubActions() {
    return subActions;
  }
  /**
   * Set the list of sub-items. Please note that this release of
   * ContextMenuManager only handles one level of sub-items.
   * 
   * @param subActions a list of sub-items
   */
  public void setSubActions(List<ContextMenuElement> subActions) {
    this.subActions = subActions;
  }


}
