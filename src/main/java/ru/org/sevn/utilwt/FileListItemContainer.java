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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import ru.org.sevn.common.mime.Mime;

public class FileListItemContainer implements ListItemContainer {

    private Path file;
    private Icon icon;
    private String text;

    public FileListItemContainer(Path fl) {
        this(fl, null);
    }

    public FileListItemContainer(Path fl, String text) {
        file = fl;
        this.text = text;
    }

    @Override
    public String getText() {
        String ret = text;
        if (ret == null && file != null) {
            ret = Util.getFileName(file.getFileName());
            if (ret == null || ret.length() == 0) {
                ret = file.toAbsolutePath().toString();
            }
        }
        if (ret == null) {
            ret = "";
        }
        return ret;
    }

    @Override
    public String getToolTipText() {
        if (file != null) return file.toAbsolutePath().toString();
        return "";
    }

    @Override
    public Icon getIcon() {
        if (icon == null) {
            icon = makeImageIcon();
        }
        return icon;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
        icon = null;
    }

    private Icon makeImageIcon() {
        return getScaledIcon(file, text, 64, 64);
    }

    public static Icon getScaledIcon(Path f, String fileName, int w, int h) {
        return ImageUtil.getScaledIcon(getIcon(f, fileName), w, h, false);
    }

    public static Icon getIcon(Path f, String fileName) {
        Icon ret = null;
        if (f != null) {
            String contentType = null;
            contentType = Mime.getMimeTypePath(f.getFileName());
            if (!Files.isDirectory(f) && contentType != null && contentType.startsWith("image")) {
                ImageIcon r;
                try {
                    r = new ImageIcon(Files.readAllBytes(f));
                    if (r.getImage() != null) {
                        ret = r;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FileListItemContainer.class.getName()).log(Level.SEVERE, null, ex);
                }
                //d1 = Util.checkTime(System.err, "--------"+f.getName()+"--------", d1);
            }
            
            if (Files.isDirectory(f)) {
                if (f.getParent() == null) {
                    ret = FOLDER_ROOT;
                } else {
                    if ("..".equals(fileName)) {
                        ret = FOLDER_UP;
                    } else {
                        ret = FOLDER;
                    }
                }
            }
            File ff = null;
            try {
                ff = f.toFile();
            } catch (Exception e) {}
            if (ret == null && FileSystemView.getFileSystemView() != null && ff != null) {
                ret = FileSystemView.getFileSystemView().getSystemIcon(ff);
                try {
                    Image im = sun.awt.shell.ShellFolder.getShellFolder(ff).getIcon(true);
                    if (im != null) {
                        ret = new ImageIcon(im);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //d1 = Util.checkTime(System.err, "========"+f.getName()+"========", d1);
            }
            if (ret == null) {
                ret = NO_ICON;
            }
        } else {
            ret = FOLDER_UP;
        }
        return ret;
    }

    public static final ImageIcon FOLDER
            = ImageUtil.createImageIcon("/drawable/folder.png", FileListItemContainer.class);
    public static final ImageIcon FOLDER_UP
            = ImageUtil.createImageIcon("/drawable/folder-up.png", FileListItemContainer.class);
    public static final ImageIcon FOLDER_ROOT
            = ImageUtil.createImageIcon("/drawable/folder-root.png", FileListItemContainer.class);
    public static final ImageIcon NO_ICON
            = ImageUtil.createImageIcon("/drawable/nopicture.png", FileListItemContainer.class);

}
