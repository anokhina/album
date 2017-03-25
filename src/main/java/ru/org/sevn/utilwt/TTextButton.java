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
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

public class TTextButton extends JButton {
	public TTextButton(String ttext, Icon i) {
		this(null, i, ttext);
	}
	public TTextButton(String text, Icon i, String ttext) {
		super(i);
		setBackground(Color.WHITE);
		setMargin(new Insets(2, 3, 2, 3));
		if (ttext != null) {
			setToolTipText(ttext);
		}
		if (text != null) {
			setText(text);
		}
		if (i == null && text == null) {
			setText(ttext);
		}
	}
}