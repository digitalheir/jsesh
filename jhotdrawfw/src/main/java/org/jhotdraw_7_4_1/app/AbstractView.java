/*
 * @(#)AbstractView.java
 *
 * Copyright (c) 1996-2010 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw_7_4_1.app;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.URI;
import java.util.*;
import javax.swing.*;
import java.util.concurrent.*;
import java.util.prefs.*;

import org.jhotdraw_7_4_1.beans.Disposable;
import org.jhotdraw_7_4_1.gui.JFileURIChooser;
import org.jhotdraw_7_4_1.gui.URIChooser;
import org.jhotdraw_7_4_1.util.prefs.PreferencesUtil;

/**
 * This abstract class can be extended to implement a {@link View}.
 * 
 * @author Werner Randelshofer
 * @version $Id: AbstractView.java 604 2010-01-09 12:00:29Z rawcoder $
 */
public abstract class AbstractView extends JPanel implements View {

	private Application application;
	/**
	 * The executor used to perform background tasks for the View in a
	 * controlled manner. This executor ensures that all background tasks are
	 * executed sequentually.
	 */
	protected ExecutorService executor;
	/**
	 * This is set to true, if the view has unsaved changes.
	 */
	private boolean hasUnsavedChanges;
	/**
	 * The preferences of the view.
	 */
	protected Preferences preferences;
	/**
	 * This id is used to make multiple open views of the same URI identifiable.
	 */
	private int multipleOpenId = 1;
	/**
	 * This is set to true, if the view is showing.
	 */
	private boolean isShowing;
	/**
	 * The title of the view.
	 */
	private String title;
	/** List of objects that need to be disposed when this view is disposed. */
	private LinkedList<Disposable> disposables;
	/**
	 * The URI of the view. Has a null value, if the view has not been loaded
	 * from a URI or has not been saved yet.
	 */
	protected URI uri;

	/**
	 * Creates a new instance.
	 */
	public AbstractView() {
		preferences = PreferencesUtil.userNodeForPackage(getClass());
	}

	/**
	 * Initializes the view. This method does nothing, subclasses don't neet to
	 * call super.
	 */
	public void init() {
	}

	/**
	 * Starts the view. This method does nothing, subclasses don't neet to call
	 * super.
	 */
	public void start() {
	}

	/**
	 * Activates the view. This method does nothing, subclasses don't neet to
	 * call super.
	 */
	public void activate() {
	}

	/**
	 * Deactivates the view. This method does nothing, subclasses don't neet to
	 * call super.
	 */
	public void deactivate() {
	}

	/**
	 * Stops the view. This method does nothing, subclasses don't neet to call
	 * super.
	 */
	public void stop() {
	}

	/**
	 * Gets rid of all the resources of the view. No other methods should be
	 * invoked on the view afterwards.
	 */
	@SuppressWarnings("unchecked")
	public void dispose() {
		if (executor != null) {
			executor.shutdown();
			executor = null;
		}

		if (disposables != null) {
			for (Disposable d : (LinkedList<Disposable>) disposables.clone()) {
				d.dispose();
			}
			disposables = null;
		}

		removeAll();
	}

	public boolean canSaveTo(URI uri) {
		return true;
	}

	public URI getURI() {
		return uri;
	}

	
	public void setURI(URI newValue) {
		URI oldValue = uri;
		uri = newValue;
		if (preferences != null && newValue != null) {
			preferences.put("projectFile", newValue.toString());
		}
		firePropertyChange(URI_PROPERTY, oldValue, newValue);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		setLayout(new java.awt.BorderLayout());
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables
	public void setApplication(Application newValue) {
		Application oldValue = application;
		application = newValue;
		firePropertyChange("application", oldValue, newValue);
	}

	public Application getApplication() {
		return application;
	}

	public JComponent getComponent() {
		return this;
	}

	/**
	 * Returns true, if the view has unsaved changes. This is a bound property.
	 */
	public boolean hasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	protected void setHasUnsavedChanges(boolean newValue) {
		boolean oldValue = hasUnsavedChanges;
		hasUnsavedChanges = newValue;
		firePropertyChange(HAS_UNSAVED_CHANGES_PROPERTY, oldValue, newValue);
	}

	/**
	 * Executes the specified runnable on the worker thread of the view.
	 * Execution is perfomred sequentially in the same sequence as the runnables
	 * have been passed to this method.
	 */
	public void execute(Runnable worker) {
		if (executor == null) {
			executor = Executors.newSingleThreadExecutor();
		}
		executor.execute(worker);
	}

	public void setMultipleOpenId(int newValue) {
		int oldValue = multipleOpenId;
		multipleOpenId = newValue;
		firePropertyChange(MULTIPLE_OPEN_ID_PROPERTY, oldValue, newValue);
	}

	public int getMultipleOpenId() {
		return multipleOpenId;
	}

	public void setShowing(boolean newValue) {
		boolean oldValue = isShowing;
		isShowing = newValue;
		firePropertyChange(SHOWING_PROPERTY, oldValue, newValue);
	}

	public boolean isShowing() {
		return isShowing;
	}

	public void markChangesAsSaved() {
		setHasUnsavedChanges(false);
	}

	public void setTitle(String newValue) {
		String oldValue = title;
		title = newValue;
		firePropertyChange(TITLE_PROPERTY, oldValue, newValue);
	}

	public String getTitle() {
		return title;
	}

	/**
	 * Adds a disposable object, which will be disposed when the specified view
	 * is disposed.
	 * 
	 * @param disposable
	 */
	public void addDisposable(Disposable disposable) {
		if (disposables == null) {
			disposables = new LinkedList<Disposable>();
		}
		disposables.add(disposable);
	}

	/**
	 * Removes a disposable object, which was previously added.
	 * 
	 * @param disposable
	 */
	public void removeDisposable(Disposable disposable) {
		if (disposables != null) {
			disposables.remove(disposable);
			if (disposables.isEmpty()) {
				disposables = null;
			}
		}
	}
}
