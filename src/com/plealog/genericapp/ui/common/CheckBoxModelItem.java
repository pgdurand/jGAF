/* Copyright (C) 2019 Patrick G. Durand
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

/**
 * Data model to use with CheckBoxChooserDialog component.
 */
public class CheckBoxModelItem {
  private String _code;
  private String _label;
  private boolean _selected;
  
  /**
   * Constructor.
   * 
   * @param label name of the item
   * @param selected status of the item
   */
  public CheckBoxModelItem(String code, String label, boolean selected) {
    setSelected(selected);
    setLabel(label);
    setCode(code);
  }
  /**
   * Set code name. A code can be viewed as an internal stuff to
   * uniquely identify items. Not used for display purpose.
   * 
   * @param code code of the item
   * */
  public void setCode(String code) {
    _code = code;
  }

  /**
   * returns code of the item.
   * 
   * @return code
   */
  public String getCode() {
    return _code;
  }
  /**
   * Set label name. Used for display purpose: this is the string
   * showed in JCheckBox.
   * 
   * @param label name of the item
   * */
  public void setLabel(String label) {
    _label = label;
  }

  /**
   * returns name of the item.
   * 
   * @return name
   */
  public String getLabel() {
    return _label;
  }
  
  /**
   * Set label status.
   * 
   * @param selected status of the item
   * */
  public void setSelected(boolean selected) {
    _selected = selected;
  }
  
  /**
   * returns status of the item.
   * 
   * @return status
   */
  public boolean isSelected() {
    return _selected;
  }
}
