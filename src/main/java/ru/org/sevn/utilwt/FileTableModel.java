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

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.table.AbstractTableModel;

public class FileTableModel extends AbstractTableModel {
	
	private final File defaultDir;
	private final int columnCount;
	
	private FilenameFilter filenameFilter;
	private File currentDir;
	private FileListItemContainer[] files;

	public FileTableModel(File dir, int col, FilenameFilter filter) {
		columnCount = col;
		defaultDir = dir;
		filenameFilter = filter;
		setCurrentDir(defaultDir);
	}
	
	public void setCurrentDir(File file) {
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("" + file.getAbsolutePath() + " isn't directory");
		}
		currentDir = file;
		File[]flst = sortOrderApply(currentDir.listFiles(filenameFilter));
		File parentFile = currentDir.getParentFile();
		int hasParent = 0;
		if (parentFile != null) {
			hasParent = 1;
			files = new FileListItemContainer[flst.length + hasParent];
			files[0] = new FileListItemContainer(parentFile, "..");
		} else {
			files = new FileListItemContainer[flst.length + hasParent];
		}
		for (int i = 0; i < flst.length; i++) {
			files[i + hasParent] = new FileListItemContainer(flst[i]);
		}
	}
	
	public static class FileNameComparator implements Comparator<File> {

		@Override
		public int compare(File o1, File o2) {
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}
			return o1.compareTo(o2);
		}
	}
	
	protected File[] sortOrderApply(File[] flst) {
		Arrays.sort(flst, new FileNameComparator());
		return flst;
	}
	
	@Override
	public int getRowCount() {
		int incr = 0;
		if (files.length % columnCount > 0) {
			incr = 1;
		}
		return files.length / columnCount + incr;
	}

	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int idx = rowIndex * columnCount + columnIndex;
		if (idx < files.length) {
			return files[idx];
		}
		return null;
	}
	
	public static class RowCol {
		public final int row;
		public final int col;
		public RowCol(int r, int c) {
			this.row = r;
			this.col = c;
		}
	}
	
	public RowCol getRowCol(int idx) {
		if (idx >=0) {
			return new RowCol(idx / columnCount , idx % columnCount);
		}
		return null;
	}
	
	public int findFile(File fl) {
		if (fl != null) {
			for (int i=0; i < files.length; i++) {
				if (fl.equals(files[i].getFile())) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public RowCol findFileRowCol(File fl) {
		return getRowCol(findFile(fl));
	}
}
