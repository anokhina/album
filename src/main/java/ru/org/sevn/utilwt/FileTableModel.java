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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

public class FileTableModel extends AbstractTableModel {

    private final Path defaultDir;
    private final int columnCount;

    private FilenameFilter filenameFilter;
    private Path currentDir;
    private FileListItemContainer[] files;
    
    public static interface FilenameFilter {
        boolean accept(Path dir, String name);
    }

    public FileTableModel(Path dir, int col, FilenameFilter filter) {
        columnCount = col;
        defaultDir = dir;
        filenameFilter = filter;
        setCurrentDir(defaultDir);
    }

    private Path[] getDirFiles(Path currentDir) {
        if (currentDir != null) {
            try {
                return Files.list(currentDir).filter(e -> {
                    if (filenameFilter != null) {
                        return filenameFilter.accept(e.getParent(), Util.getFileName(e.getFileName()));
                    }
                    return true;
                }).toArray(Path[]::new);
            } catch (Exception ex) {
                Logger.getLogger(FileTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new Path[] {};//File.listRoots();
    }
    private Path getParentFile(Path f) {
        if (f != null) {
            return f.getParent();
        }
        return null;
    }
    public void setCurrentDir(Path file) {
        if (file != null && !Files.isDirectory(file)) {
            throw new IllegalArgumentException("" + file.toAbsolutePath() + " isn't directory");
        }
        currentDir = file;
        Path[] flst = sortOrderApply(getDirFiles(currentDir));
        Path parentFile = getParentFile(currentDir);
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

    public static class FileNameComparator implements Comparator<Path> {

        @Override
        public int compare(Path o1, Path o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.compareTo(o2);
        }
    }

    protected Path[] sortOrderApply(Path[] flst) {
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
        if (idx >= 0) {
            return new RowCol(idx / columnCount, idx % columnCount);
        }
        return null;
    }

    public int findFile(Path fl) {
        if (fl != null) {
            for (int i = 0; i < files.length; i++) {
                if (fl.equals(files[i].getFile())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public RowCol findFileRowCol(Path fl) {
        return getRowCol(findFile(fl));
    }
}
