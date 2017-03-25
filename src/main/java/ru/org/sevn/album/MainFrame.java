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
package ru.org.sevn.album;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

import ru.org.sevn.utilwt.FileListItemContainer;
import ru.org.sevn.utilwt.FileTableCellRenderer;
import ru.org.sevn.utilwt.FileTableModel;
import ru.org.sevn.utilwt.FileTableModel.RowCol;
import ru.org.sevn.utilwt.ImagePreview;
import ru.org.sevn.utilwt.ImageUtil;

public class MainFrame extends JFrame {
	private JTable table;
	private ImagePreview imagePreview;
	
	public static void runMain(final File imageMagickPath) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame("Album", imageMagickPath);
				frame.showFrame();
			}
		});
		
	}
	
	public MainFrame(String title, File imageMagickPath) {
		super(title);
		setBounds(100, 100, 780, 580);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        createFileTable(imageMagickPath);
        //main folder button
        //folder list
        //full screen
        //open in external editor
        //show image info
        //find by description and comments, year month between
	}
	public void showFrame() {
        setVisible(true);
        table.setColumnSelectionInterval(0, 0);
        table.setRowSelectionInterval(0, 0);
        
		int r = table.getSelectedRow();
		int c = table.getSelectedColumn();
		selectedTableItem((FileListItemContainer) table.getValueAt(r, c));
        
	}
	private void selectedTableItem(FileListItemContainer fval) {
		if (fval != null) {
			if (fval.getFile() != null && !fval.getFile().isDirectory()) {
	    		ImageIcon ii = new ImageIcon(fval.getFile().getPath());
	    		// TODO check last displayed
		    	if (ii.getImage() != null) {
		    		imagePreview.setImageIconRepaint(ii, fval.getFile());
		    	} else {
		    		imagePreview.setImageIconRepaint(null, fval.getFile());
		    	}
			} else {
	    		imagePreview.setImageIconRepaint(null, fval.getFile());
			}
		}
		
	}
	private void createFileTable(File imageMagickPath) {
		//https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
		imagePreview = new ImagePreview(imageMagickPath);
		
		final FileTableModel tableModel = new FileTableModel(new File("C:/pub"), 4, null); 
		final TableCellRenderer cellRenderer = new FileTableCellRenderer();
		table = new JTable(tableModel) {
			public TableCellRenderer getCellRenderer(int row, int column) {
				return cellRenderer;
			}
		};
		table.setRowHeight(64 + 32);
		table.setShowGrid(false);
		table.setTableHeader(null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
			
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int r = table.getSelectedRow();
				int c = table.getSelectedColumn();
				FileListItemContainer fval = (FileListItemContainer) table.getValueAt(r, c);
				if (e.getClickCount() == 2) {
					if (fval != null) {
						System.out.println("-----------?-"+fval.getToolTipText());
						if (fval.getFile() != null && fval.getFile().isDirectory()) {
							tableModel.setCurrentDir(fval.getFile());
							tableModel.fireTableDataChanged();
						}
					}
				} else {
					selectedTableItem(fval);
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
		JPanel mainPane = new JPanel(new BorderLayout());
		mainPane.add(scrollPane, BorderLayout.EAST);
		mainPane.add(imagePreview, BorderLayout.CENTER);
		setContentPane(mainPane);
		
		imagePreview.getViewButton().addActionListener(e -> {
			scrollPane.setVisible(!scrollPane.isVisible());
			if (scrollPane.isVisible()) {
				int r = table.getSelectedRow();
				int c = table.getSelectedColumn();
				FileListItemContainer fval = (FileListItemContainer) table.getValueAt(r, c);
				if (imagePreview.getFile() != null && fval != null) {
					if (!imagePreview.getFile().equals(fval.getFile())) {
						RowCol rc = tableModel.findFileRowCol(imagePreview.getFile());
						if (rc != null) {
							int ci = table.convertColumnIndexToView(rc.col);
							int ri = table.convertRowIndexToView(rc.row);
					        table.setColumnSelectionInterval(ci, ci);
					        table.setRowSelectionInterval(ri, ri);
						} else {
							table.clearSelection();
						}
					}
				}
			}
			mainPane.revalidate();
		});
	}
	public static final ImageIcon DEFAULT_ICON = 
			ImageUtil.createImageIcon("/drawable/picture.png", MainFrame.class);
	public static final ImageIcon NO_ICON = 
			ImageUtil.createImageIcon("/drawable/nopicture.png", MainFrame.class);
	
}
