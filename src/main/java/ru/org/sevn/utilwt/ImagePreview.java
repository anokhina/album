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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import ru.org.sevn.utilwin.FileUtil;

public class ImagePreview extends JPanel {
	private File file;
	private ImageIcon previewImage = null;
	private ImageIcon imageIcon = null;
	private int previewWidth = PREVIEW_WIDTH;
	private int previewHeight = PREVIEW_HEIGHT;
	public static int PREVIEW_WIDTH = 200;
	public static int PREVIEW_HEIGHT = 200;
	private JScrollPane scrollPane;
	private ImageJComponent imageJComponent;
	private JPanel controls;
	private JPanel controlsL;
	private JTextArea label;
	// button refresh ???
	private JButton viewButton;
	private JButton fitWin;
	private JButton zoomIn;
	private JButton zoomOut;
	private JButton openIn;
	private JButton open;
	private JButton prev;
	private JButton next;
	private double scale = 1;
	public static final double SCALE_MAX = 5;
	public static final double SCALE_DELTA = 0.1;
	public static final double SCALE_MIN = SCALE_DELTA;
	public static final int SCALE_SLIDER = (int)(SCALE_MAX / SCALE_DELTA);
	private FileUtil tempFile;
	private ImagemagickUtil imagemagickUtil;
	
    private JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, SCALE_SLIDER, (int)(1 / SCALE_DELTA));

	public ImagePreview(File imageMagickPath) {
		this(imageMagickPath, PREVIEW_WIDTH, PREVIEW_HEIGHT);
	}
	private String getFileName(String name) {
		if(name.contains(" ")) {
			return "\""+name+"\"";
		}
		return name;
	}
	
	public ImagePreview(File imPath, int w, int h) {
		super(new BorderLayout());
		try {
			tempFile = new FileUtil();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		try {
			imagemagickUtil = new ImagemagickUtil(imPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		previewWidth = w;
		previewHeight = h;
		imageJComponent = new ImageJComponent(); 
		viewButton = new TTextButton("View", createImageIcon("/drawable/view.png"));
		fitWin = new TTextButton("Fit win", createImageIcon("/drawable/perspective.png"));
		zoomIn = new TTextButton("Zoom In", createImageIcon("/drawable/zoom-in.png"));
		zoomOut = new TTextButton("Zoom Out", createImageIcon("/drawable/zoom-out.png"));
		openIn = new TTextButton("Open In", createImageIcon("/drawable/app.png", "/drawable/folder-11.png"));
		open = new TTextButton("Open", createImageIcon("/drawable/folder-11.png"));
		prev = new TTextButton("Prev", createImageIcon("/drawable/prev.png"));
		next = new TTextButton("Next", createImageIcon("/drawable/next.png"));
		controls = new JPanel();
		controlsL = new JPanel();
		controlsL.setLayout(new BoxLayout(controlsL, BoxLayout.Y_AXIS));
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		controlsL.add(viewButton);
		controlsL.add(fitWin);
		controlsL.add(zoomOut);
		controls.add(slider);
		controlsL.add(zoomIn);
		controlsL.add(openIn);
		controlsL.add(open);
		controlsL.add(prev);
		controlsL.add(next);
		fitWin.addActionListener(e -> {fitWin();} );
		zoomIn.addActionListener(e -> {zoomIn();} );
		zoomOut.addActionListener(e -> {zoomOut();} );
		openIn.addActionListener(e -> {tempFile.runTemp(file);});
		open.addActionListener(e -> {FileUtil.openDefaultEditor(file);});
		prev.addActionListener(e -> {showPrev();} );
		next.addActionListener(e -> {showNext();} );
        slider.addChangeListener(new ChangeListener() {
            private int tmpVal = -1;
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider.getValueIsAdjusting()) {
					tmpVal = slider.getValue();
				} else {
					if (tmpVal >= 0) {
						int setVal = tmpVal;
						tmpVal = -1;
						setScale(setVal * SCALE_DELTA);
					}
				}
			}
		});
		
		
		scrollPane = new JScrollPane(imageJComponent);
		
	    label = makeJTextArea(0, 0); //1, 20
	    
	    JPanel imagePanel = new JPanel(new BorderLayout());
	    imagePanel.add(scrollPane, BorderLayout.CENTER);
	    imagePanel.add(controls, BorderLayout.SOUTH);
	    add(controlsL, BorderLayout.WEST);
	    
	    JTabbedPane tabPane = new JTabbedPane();
	    tabPane.addTab("Image", null, imagePanel, "Image");
	    final JFXPanel fxPanel = new JFXPanel();	    
	    tabPane.addTab("Comment", fxPanel);
	    
	    
	    add(label, BorderLayout.NORTH);
	    add(tabPane, BorderLayout.CENTER);
	    
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
		        fxPanel.setScene(createHtmlEditor());
			}
		});    
	}
	
    private FXSimpleUndoRedoHTMLEditor htmlEditor;
    
    private Scene createHtmlEditor() {
    	htmlEditor = new FXSimpleUndoRedoHTMLEditor(200, 16, this.getClass().getResourceAsStream("/drawable/prev.png"), this.getClass().getResourceAsStream("/drawable/next.png"));
    	javafx.scene.control.Button saveButton = new javafx.scene.control.Button("", htmlEditor.createFXImageView(this.getClass().getResourceAsStream("/drawable/save.png")));
    	saveButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
	        @Override public void handle(javafx.event.ActionEvent arg0) {
	        	//System.err.println(htmlEditor.getHtmlText());
	        	if (imagemagickUtil.setComment(file, htmlEditor.getHtmlText())) {updateComment();}
	        }
	    });
    	
    	htmlEditor.getToolBar().getItems().add(0, saveButton);
    	Scene scene = new Scene(htmlEditor);
    	
    	return scene;
    }	
	private JTextArea makeJTextArea(int w, int h) {
	    JTextArea textArea = new JTextArea(w, h);
	    textArea.setText("");
	    textArea.setWrapStyleWord(true);
	    textArea.setLineWrap(true);
	    textArea.setOpaque(false);
	    textArea.setEditable(false);
	    textArea.setFocusable(false);
	    textArea.setBackground(UIManager.getColor("Label.background"));
	    textArea.setFont(UIManager.getFont("Label.font"));
	    textArea.setBorder(UIManager.getBorder("Label.border"));
	    return textArea;
	}
	
	public void fitWin() {
		setScale(1);
	}
	public void zoomIn() {
		if (scale < SCALE_MAX) {
			setScale(scale + SCALE_DELTA);
		}
	}
	public void zoomOut() {
		if (scale > SCALE_MIN ) { 
			setScale(scale - SCALE_DELTA);
		} 
	}
	public void setScale(double v) {
		if (v >= SCALE_MIN && v <= SCALE_MAX) {
			scale = v;
			reloadImage();
			scrollPane.invalidate();
			repaint();
			updateSlider();
		}
	}
	
	private void updateSlider() {
		int sliderVal = (int)(scale / SCALE_DELTA);
		if (sliderVal != slider.getValue()) {
			slider.setValue(sliderVal);
		}
	}
	
	public ImageIcon getImageIcon() {
		return imageIcon;
	}
	
	public void setImageIcon(ImageIcon i) {
		scale=1;
		imageIcon = i;
		reloadImage();
		updateSlider();
	}
	
	private File[] files2show;
	private int current;
	
	public void setImageIconRepaint(ImageIcon ii, File fl) {
		current = -1;
		files2show = null;
		if (fl != null) {
			File parentFile = fl.getParentFile();
			if (parentFile != null) {
				files2show = parentFile.listFiles();
				Arrays.sort(files2show, new FileTableModel.FileNameComparator());
			}
			if (files2show != null && fl != null) {
				for(int i = 0; i < files2show.length; i++) {
					if (fl.equals(files2show[i])) {
						current = i;
						break;
					}
				}
			}
		}
		displayImageIconRepaint(ii, fl);
	}
	
	private void showNext() {
		if (file != null && files2show != null) {
			if (current >= 0) {
				if (current < files2show.length - 1) {
					for(int cur = current + 1; cur < files2show.length; cur++ ) { 
						ImageIcon ii = showCurrent(cur);
						if (ii!= null) {
							current = cur;
							displayImageIconRepaint(ii, files2show[cur]);
							break;
						}
					}
				}
			}
		}
	}
	private void showPrev() {
		if (file != null && files2show != null) {
			if (current >= 1) {
				for(int cur = current-1; cur >= 0; cur--) {
					ImageIcon ii = showCurrent(cur);
					if (ii!= null) {
						current = cur;
						displayImageIconRepaint(ii, files2show[cur]);
						break;
					}
				}
			}
		}
	}
	private ImageIcon showCurrent(int cur) {
		try {
			String contentType = Files.probeContentType(Paths.get(files2show[cur].getPath()));
			if (contentType != null && contentType.startsWith("image")) {
				return new ImageIcon(files2show[cur].getPath());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void displayImageIconRepaint(ImageIcon i, File fl) {
		setImageIcon(i);
		file = fl;
		label.setText("");
		if (file != null) {
			label.setText(fl.getAbsolutePath());
		}
		updateComment();
		repaint();
	}

	private void setNewHtmlText(final String txt) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	    		htmlEditor.setNewHtmlText(txt);
	        }
	   });		
	}
	private void updateComment() {
		setNewHtmlText("");
		if (file != null && imageIcon != null) {
			String appTxt = null;
			if (imagemagickUtil != null) {
				appTxt = imagemagickUtil.identifyComment(file);
			}
			if (appTxt == null) {
				appTxt = "";
			} else {
				appTxt = appTxt.replace("\r\n\r\n", "\n");
			}
			
			if (appTxt.startsWith("<html")) {
				setNewHtmlText(appTxt);
			} else {
				setNewHtmlText("<html><pre>"+appTxt+"</pre></html>");
			}
		}
	}

	public void reloadImage() {
		previewImage = null;
		previewImage = scaleToView(getImageIcon());
		Rectangle bounds = scrollPane.getViewport().getViewRect();
		if (imageJComponent.alterSize(bounds.width, bounds.height)) {
	        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
	        horizontal.setValue(horizontal.getMaximum() / 2 - (horizontal.getMaximum() - bounds.width) / 2 );

	        JScrollBar vertical = scrollPane.getVerticalScrollBar();
	        vertical.setValue(vertical.getMaximum() / 2 - (vertical.getMaximum() - bounds.height) / 2 );
	        
	        //System.err.println("=============="+horizontal.getMaximum()+":"+vertical.getMaximum()+":"+bounds);
		}
	}
	
	private ImageIcon scaleToView(ImageIcon ii) {
		return ImageUtil.getScaledImageIcon(ii, (int)(scrollPane.getWidth()*scale), (int)(scrollPane.getHeight()*scale), false);
	}

	private class ImageJComponent extends JComponent {
		public ImageJComponent() {
			setPreferredSize(new Dimension(previewWidth, previewHeight));
		}
		
		public boolean alterSize(int w, int h) {
			ImageIcon thumbnail = getPreviewImage();
			if (thumbnail != null) {
				if (thumbnail.getIconWidth() > w || thumbnail.getIconHeight() > h ) {
					Dimension dim = new Dimension(Math.max(thumbnail.getIconWidth() , w), 
							Math.max(thumbnail.getIconHeight() , h));
					setSize(dim);
					//setMinimumSize(dim);
					setPreferredSize(dim);
					return true;
				} else if (getWidth() > w || getHeight() > h) {
					Dimension dim = new Dimension(Math.min(getWidth(), w), Math.min(getHeight(), h));
					setSize(dim);
					//setMinimumSize(dim);
					setPreferredSize(dim);
					return true;
				}
			}
			return false;
		}
		
		protected void paintComponent(Graphics g) {
			ImageIcon thumbnail = getPreviewImage();
			if (thumbnail != null) {
				int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
				int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;

				if (y < 0) {
					y = 0;
				}

				if (x < 5) {
					x = 5;
				}
				thumbnail.paintIcon(this, g, x, y);
			}
		}
	}

	public ImageIcon getPreviewImage() {
		return previewImage;
	}

	public JTextArea getLabel() {
		return label;
	}

	private ImageIcon createImageIcon(String ...paths) {
		ImageIcon[] ret = new ImageIcon[paths.length];
		for (int i = 0; i < paths.length; i++) {
			ret[i] = ImageUtil.getScaledImageIcon(ImageUtil.createImageIcon(paths[i], ImagePreview.class), 32, 32, true);
		}
		if (ret.length == 0) {
			return null;
		} else if (ret.length == 1) {
			return ret[0];
		} else {
			return ImageUtil.imageFlip(32, 32, ret);
		}
	}
	
	public JButton getViewButton() {
		return viewButton;
	}
	public File getFile() {
		return file;
	}
	
}