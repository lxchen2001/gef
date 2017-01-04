/*******************************************************************************
 * Copyright (c) 2017 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.gef.mvc.fx.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.mvc.fx.operations.ITransactionalOperation;
import org.eclipse.gef.mvc.fx.operations.SelectOperation;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.ui.actions.ActionFactory;

import javafx.scene.Node;

/**
 * The {@link SelectAllAction}
 *
 * @author mwienand
 *
 */
public class SelectAllAction extends AbstractViewerAction {

	/**
	 * Creates a new {@link SelectAllAction}.
	 */
	public SelectAllAction() {
		super("Select All");
		setId(ActionFactory.SELECT_ALL.getId());
		setEnabled(true);
	}

	@Override
	protected ITransactionalOperation createOperation() {
		return new SelectOperation(getViewer(), getSelectableContentParts());
	}

	/**
	 * Returns a list containing the {@link IContentPart}s that should be
	 * selected by this action handler at the point of time this method is
	 * called.
	 * <p>
	 * Per default, all active and selectable parts within the content-part-map
	 * of the current viewer are returned.
	 *
	 * @return A list containing the {@link IContentPart}s that should be
	 *         selected by this action handler at the point of time this method
	 *         is called.
	 */
	protected List<? extends IContentPart<? extends Node>> getSelectableContentParts() {
		if (getViewer() == null) {
			return Collections.emptyList();
		}
		ArrayList<IContentPart<? extends Node>> parts = new ArrayList<>(
				getViewer().getContentPartMap().values());
		parts.removeIf(p -> !p.isSelectable());
		return parts;
	}
}
