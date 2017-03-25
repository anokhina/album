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

import javax.swing.Icon;

public class PlainListItemContainer implements ListItemContainer {
	private String text;
	private String toolTipText;
	private Icon icon;

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

}
