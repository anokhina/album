/*******************************************************************************
 * Copyright 2017 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ru.org.sevn.utilwt;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class FileTableCellRenderer extends DefaultTableCellRenderer {
	
	public FileTableCellRenderer() {
		setHorizontalTextPosition(CENTER);
		setVerticalTextPosition(BOTTOM);		
    	setHorizontalAlignment(SwingConstants.CENTER);
	}
    protected void setValue(Object value) {
    	FileListItemContainer obj = (FileListItemContainer)value;
    	if (obj != null) {
	    	setText(obj.getText());
	    	setToolTipText(obj.getToolTipText());
	    	setIcon(obj.getIcon());
    	} else {
	    	setText(null);
	    	setToolTipText(null);
	    	setIcon(null);
    	}
    }
}
