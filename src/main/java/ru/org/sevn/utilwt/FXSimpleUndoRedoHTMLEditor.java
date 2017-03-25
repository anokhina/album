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

import java.io.InputStream;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.web.HTMLEditor;

public class FXSimpleUndoRedoHTMLEditor extends HTMLEditor {
	private int maxUndoStack;
	private final ArrayList<String> stack = new ArrayList<>(20);
	private final ArrayList<String> stackRedo = new ArrayList<>(20);
	private javafx.scene.control.Button undoButton;
	private javafx.scene.control.Button redoButton;
	private ToolBar toolBar;
	
	private final int iconSize;
	
    public ImageView createFXImageView(InputStream is) {
	    ImageView graphic = new ImageView(
    	        new Image(is, iconSize, iconSize, true, true));
    	    graphic.setEffect(new DropShadow());
	    return graphic;
    }
    
    public void setNewHtmlText(String txt) {
    	setHtmlText(txt);
    	clearRedo();
    	clearUndo();
    }
    
	public FXSimpleUndoRedoHTMLEditor(InputStream undoPicture, InputStream redoPicture) {
		this(0, 16, undoPicture, redoPicture);
	}
	
	private void addRedoUndo(String state, ArrayList<String> stack, javafx.scene.control.Button stackButton) {
		stack.add(state);
		setEnabled(stack, stackButton);
	}
	private String removeRedoUndo(int i, ArrayList<String> stack, javafx.scene.control.Button stackButton) {
		String ret = stack.remove(i);
		setEnabled(stack, stackButton);
		return ret;
	}
	private void clearUndo() {
    	stack.clear();
		setEnabled(stack, undoButton);
	}
	private void clearRedo() {
    	stackRedo.clear();
		setEnabled(stackRedo, redoButton);
	}
	private void saveState(String state) {
		addRedoUndo(state, stack, undoButton);
    	clearRedo();
	}
	private void performRedoUndo(String state, ArrayList<String> stack, ArrayList<String> stackRedo, javafx.scene.control.Button stackButton, javafx.scene.control.Button stackRedoButton) {
    	if (stack.size() > 0) {
    		String old = removeRedoUndo(stack.size() - 1, stack, undoButton);
    		addRedoUndo(state, stackRedo, stackRedoButton);
    		setHtmlText(old);
    	}
	}
	private void setEnabled(ArrayList<String> stack, javafx.scene.control.Button stackButton) {
		stackButton.setDisable(!(stack.size() > 0));
		if (stackButton.getTooltip() == null) {
			stackButton.setTooltip(new Tooltip());
		}
		stackButton.getTooltip().setText(""+stack.size());
	}
	
	
	
	public javafx.scene.control.Button getUndoButton() {
		return undoButton;
	}

	public javafx.scene.control.Button getRedoButton() {
		return redoButton;
	}
	
	public ToolBar getToolBar() {
		return toolBar;
	}

	public FXSimpleUndoRedoHTMLEditor(int maxUndoStack, int iconSize, InputStream undoPicture, InputStream redoPicture) {
		this.maxUndoStack = maxUndoStack;
    	final HTMLEditor htmlEditor = this;
    	this.iconSize = iconSize;
    	htmlEditor.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {

            @Override
            public void handle(InputEvent event) {
            	String state = htmlEditor.getHtmlText();
            	if (stack.size() == 0) {
            		saveState(state);
            	} else {
            		if (!state.equals(stack.get(stack.size() - 1))) {
                		saveState(state);
            		}
            	}
            	if (maxUndoStack > 0 && stack.size() > maxUndoStack) {
            		removeRedoUndo(0, stack, undoButton);
            	}
            }
        });
    	
    	Node node = lookup(".top-toolbar");
    	if (node instanceof ToolBar) {
    		toolBar = (ToolBar) node;
    	    undoButton = new javafx.scene.control.Button("", createFXImageView(undoPicture));
    	    redoButton = new javafx.scene.control.Button("", createFXImageView(redoPicture));
    	    toolBar.getItems().add(undoButton);
    	    toolBar.getItems().add(redoButton);
    	    undoButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
    	        @Override public void handle(javafx.event.ActionEvent arg0) {
    	        	performRedoUndo(htmlEditor.getHtmlText(), stack, stackRedo, undoButton, redoButton);
    	        }
    	    });
    	    redoButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
    	        @Override public void handle(javafx.event.ActionEvent arg0) {
    	        	performRedoUndo(htmlEditor.getHtmlText(), stackRedo, stack, redoButton, undoButton);
    	        }
    	    });
    	}    		
	}
}