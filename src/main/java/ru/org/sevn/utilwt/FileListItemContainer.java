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

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

public class FileListItemContainer implements ListItemContainer {
	
	private File file;
	private Icon icon;
	private String text;
	
	public FileListItemContainer(File fl) {
		this(fl, null);
	}
	public FileListItemContainer(File fl, String text) {
		file = fl;
		this.text = text;
	}

	@Override
	public String getText() {
		if (text != null) {
			return text;
		}
		return file.getName();
	}

	@Override
	public String getToolTipText() {
		return file.getAbsolutePath();
	}

	@Override
	public Icon getIcon() {
		if (icon == null) {
			icon = makeImageIcon();
		}
		return icon;
	}
	
	
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		icon = null;
	}

	private Icon makeImageIcon() {
		return getScaledIcon(file, text, 64, 64);
	}

    public static Icon getScaledIcon(File f, String fileName, int w, int h) {
    	return ImageUtil.getScaledIcon(getIcon(f, fileName), w, h, false);
    }
    public static Icon getIcon(File f, String fileName) {
    	Icon ret = null;
    	String contentType = null;
    	try {
			contentType = Files.probeContentType(Paths.get(f.getPath()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//Date d1 = new Date();
    	if (!f.isDirectory() && contentType != null && contentType.startsWith("image")) {
    		ImageIcon r = new ImageIcon(f.getPath());
    		//d1 = Util.checkTime(System.err, "--------"+f.getName()+"--------", d1);
	    	if (r.getImage() != null) {
	    		ret = r;
	    	}
    	} 
    	if (f.isDirectory()) {
    		if (f.getParentFile() == null) {
    			ret = FOLDER_ROOT;
    		} else {
    			if ("..".equals(fileName)) {
    				ret = FOLDER_UP;
    			} else {
    				ret = FOLDER;
    			}
    		}
    	}
    	if (ret == null && FileSystemView.getFileSystemView() != null) {
    		ret = FileSystemView.getFileSystemView().getSystemIcon(f);
    		try {
				Image im = sun.awt.shell.ShellFolder.getShellFolder(f).getIcon( true );
				if (im != null) {
					ret = new ImageIcon(im);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//d1 = Util.checkTime(System.err, "========"+f.getName()+"========", d1);
    	}
    	if (ret == null) {
    		ret = NO_ICON;
    	}
    	return ret;
    }
    
	public static final ImageIcon FOLDER = 
			ImageUtil.createImageIcon("/drawable/folder.png", FileListItemContainer.class);
	public static final ImageIcon FOLDER_UP = 
			ImageUtil.createImageIcon("/drawable/folder-up.png", FileListItemContainer.class);
	public static final ImageIcon FOLDER_ROOT = 
			ImageUtil.createImageIcon("/drawable/folder-root.png", FileListItemContainer.class);
	public static final ImageIcon NO_ICON = 
			ImageUtil.createImageIcon("/drawable/nopicture.png", FileListItemContainer.class);
    
}
