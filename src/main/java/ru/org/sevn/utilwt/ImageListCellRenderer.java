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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class ImageListCellRenderer extends DefaultListCellRenderer {
    private JLabel label = new JLabel("");
    private Color textSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = Color.CYAN;
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;
    
    public ImageListCellRenderer() {
    	label.setOpaque(true);
    }
    
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {

    	ListItemContainer bi = (ListItemContainer)value;
    	label.setIcon(null);
    	label.setToolTipText(null);
    	label.setText(null);
    	
    	try {
    		int wh = 64;
    		label.setMinimumSize(new Dimension(wh, wh));
	    	label.setText(bi.getText());
	    	label.setToolTipText(bi.getToolTipText());
    		label.setIcon(ImageUtil.getStretchedImageIcon(getAnyIcon((ImageIcon)bi.getIcon()), wh, wh, true));
    	} catch (Exception e) {
    		e.printStackTrace(System.err);
    	}
    	
        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }
    private ImageIcon getAnyIcon(ImageIcon ii) {
    	if (ii == null) {
    		return DEFAULT_ICON;
    	}
    	return ii;
    }
	public static final ImageIcon DEFAULT_ICON = 
			ImageUtil.createImageIcon("/drawable/picture.png", ImageListCellRenderer.class);
    
}