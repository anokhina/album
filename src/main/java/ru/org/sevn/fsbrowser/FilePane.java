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
package ru.org.sevn.fsbrowser;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import ru.org.sevn.common.mime.Mime;
import ru.org.sevn.utilwt.FileCommenter;
import ru.org.sevn.utilwt.FileListItemContainer;
import ru.org.sevn.utilwt.FileTableCellRenderer;
import ru.org.sevn.utilwt.FileTableModel;
import ru.org.sevn.utilwt.ImagePreview;
import ru.org.sevn.utilwt.ImageUtil;

public class FilePane extends JSplitPane {
    private JTable table;
    private final Point tableSelected = new Point();
    private final JLabel status = new JLabel();
    private ImagePreview imagePreview;

    public void onVisible() {
        setDividerLocation(.4);

        table.setColumnSelectionInterval(0, 0);
        table.setRowSelectionInterval(0, 0);        
    }
    private void selectedTableItem(FileListItemContainer fval) {
        if (fval != null) {
            if (fval.getFile() != null && !Files.isDirectory(fval.getFile())) {
                ImageIcon ii = null;
                String contentType = Mime.getMimeTypePath(fval.getFile().getFileName());
                if (contentType != null && contentType.startsWith("image")) {
                    try {
                        ii = new ImageIcon(Files.readAllBytes(fval.getFile()));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // TODO check last displayed
                if (ii != null && ii.getImage() != null) {
                    imagePreview.setImageIconRepaint(ii, fval.getFile());
                } else {
                    imagePreview.setImageIconRepaint(null, fval.getFile());
                }
            } else {
                imagePreview.setImageIconRepaint(null, fval.getFile());
            }
        }

    }
    
    private static int getLastSelected(ListSelectionEvent e) {
        ListSelectionModel lmodel = (ListSelectionModel)e.getSource();
        int idx = e.getFirstIndex();
        if (lmodel.isSelectedIndex(e.getLastIndex())) {
            idx = e.getLastIndex();
        }
        return idx;
    }
    private void onUpdateSelection() {
        FileListItemContainer fval = (FileListItemContainer) table.getValueAt(tableSelected.y, tableSelected.x);
        if (fval != null) {
            status.setText(fval.getToolTipText());
        } else {
            status.setText("");
        }
        selectedTableItem(fval);
    }
    private void onEnterSelection() {
        onEnterSelection((FileListItemContainer) table.getValueAt(tableSelected.y, tableSelected.x));
    }
    private void enterDir(Path fl) {
        if (fl == null || fl != null && Files.isDirectory(fl)) {
            tableSelected.x = 0;
            tableSelected.y = 0;            
            final FileTableModel tableModel = (FileTableModel)table.getModel();
            tableModel.setCurrentDir(fl);
            tableModel.fireTableDataChanged();
            table.setColumnSelectionInterval(0, 0);
            table.setRowSelectionInterval(0, 0);
        }
    }
    private void onEnterSelection(FileListItemContainer fval) {
        if (fval != null) {
            enterDir(fval.getFile());
        }
    }
    
    private void setSelection(FileTableModel.RowCol rc) {
        if (rc != null) {
            int ci = table.convertColumnIndexToView(rc.col);
            int ri = table.convertRowIndexToView(rc.row);
            table.setColumnSelectionInterval(ci, ci);
            table.setRowSelectionInterval(ri, ri);
        }        
    }
    public static final ImageIcon DEFAULT_ICON
            = ImageUtil.createImageIcon("/drawable/picture.png", MainFrame.class);
    public static final ImageIcon NO_ICON
            = ImageUtil.createImageIcon("/drawable/nopicture.png", MainFrame.class);
    
    public FilePane(FileCommenter commenter) {
        //https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
        imagePreview = new ImagePreview(commenter);

        final FileTableModel tableModel = new FileTableModel(new File(System.getProperty("user.home")).toPath(), 4, null);
        final TableCellRenderer cellRenderer = new FileTableCellRenderer();
        table = new JTable(tableModel) {
            public TableCellRenderer getCellRenderer(int row, int column) {
                return cellRenderer;
            }
        };
        table.setRowHeight(64 + 32);
        table.setShowGrid(false);
        table.setTableHeader(null);
        //table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tableSelected.x = getLastSelected(e);
                    onUpdateSelection();
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tableSelected.y = getLastSelected(e);
                    onUpdateSelection();
                }
            }
        });
        table.setCellSelectionEnabled(true);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onEnterSelection();
                    e.consume();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = table.rowAtPoint(e.getPoint());
                    int c = table.columnAtPoint(e.getPoint());
                    if (r >= 0 && c >= 0) {
                        onEnterSelection((FileListItemContainer) table.getValueAt(r, c));
                    }
                }
            }
        });
        /*
		 * resize only lasty column
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		for (int i = tableModel.getColumnCount() - 1; i >= 0; i--) {
			table.getColumnModel().getColumn(i).setMinWidth(128);
			table.getColumnModel().getColumn(i).setMaxWidth(128);
		}
		table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setMaxWidth(Integer.MAX_VALUE);
         */
        JPanel filePanel = new JPanel(new BorderLayout());
        JPanel roots = new JPanel();
        JButton btndel = new JButton("Del");
        btndel.addActionListener(e -> {
            deleteSelected();
        });
        roots.add(btndel);
        JButton btncopy = new JButton("Copy");
        btncopy.addActionListener(e -> {
            copySelected();
        });
        roots.add(btncopy);
        for (File f : File.listRoots()) {//TODO refresh
            final File fl = f;
            final JButton btn = new JButton(fl.getAbsolutePath());
            btn.addActionListener(e -> {enterDir(fl.toPath());});
            roots.add(btn);
        }
        filePanel.add(roots, BorderLayout.NORTH);
        filePanel.add(scrollPane, BorderLayout.CENTER);
        filePanel.add(status, BorderLayout.SOUTH);
        final JSplitPane mainPane = this;
        mainPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        mainPane.setLeftComponent(imagePreview);
        mainPane.setRightComponent(filePanel);
        mainPane.setOneTouchExpandable(true);

        imagePreview.getViewButton().addActionListener(e -> {
            int dl = mainPane.getDividerLocation();
            int right = mainPane.getSize().width
                             - mainPane.getInsets().right
                             - mainPane.getDividerSize();

            if (Math.abs(right - dl) < 10) {
                Path fl = imagePreview.getFile();
                if (fl != null) {
                    table.clearSelection();
                    FileTableModel.RowCol rc = tableModel.findFileRowCol(fl);
                    setSelection(rc);
                }
                mainPane.setDividerLocation(.4);
            } else { 
                mainPane.setDividerLocation(1.0);
            }
        });
    }

    private ArrayList<FileListItemContainer> getSelectedFiles() {
        FileTableModel tableModel = (FileTableModel)table.getModel();
        Path curDir = tableModel.getCurrentDir();
        Path parDir = curDir.getParent().toAbsolutePath();
        ArrayList<FileListItemContainer> files = new ArrayList<>();
        Arrays.stream(table.getSelectedRows()).forEach(r -> {
            Arrays.stream(table.getSelectedColumns()).forEach(c -> {
                // TODO dont add parent
                FileListItemContainer fval = (FileListItemContainer) table.getValueAt(r, c);
                if (fval.getFile() != null && !parDir.equals(fval.getFile().toAbsolutePath())) {
                    files.add(fval);
                }
            });
        });
        return files;        
    }
    private void deleteSelected() {
        //ask
        FileTableModel tableModel = (FileTableModel)table.getModel();
        ArrayList<FileListItemContainer> files = getSelectedFiles();
        
        int n = JOptionPane.showConfirmDialog(
            this,
            "Delete " + files.size() + " selected files?",
            "Delete",
            JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            //delete
            //refresh
            enterDir(tableModel.getCurrentDir());
        }

    }
    private FilePane sibling;
    public void setSibling(FilePane fp) {
        sibling = fp;
    }
    private void copySelected() {
        //ask
        if (sibling != null) {
            FileTableModel tableModel = (FileTableModel)table.getModel();
            ArrayList<FileListItemContainer> files = getSelectedFiles();
            FileTableModel stableModel = (FileTableModel)sibling.table.getModel();
            Path toPath = stableModel.getCurrentDir();
            if (toPath != null) {
                String toName = toPath.toUri().toString();
                int n = JOptionPane.showConfirmDialog(
                    this,
                        toName + "\n" +
                    "Copy " + files.size() + " selected files?",
                    "Copy to ",
                    JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    //copy
                    //check not the same
                    //refresh
                    sibling.enterDir(tableModel.getCurrentDir());
                }
            }
        }
    }
}
