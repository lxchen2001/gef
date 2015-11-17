/*******************************************************************************
 * Copyright (c) 2014 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.gef4.mvc.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef4.mvc.parts.IContentPart;

import com.google.common.collect.SetMultimap;

/**
 * The {@link AttachToContentAnchorageOperation} uses the {@link IContentPart}
 * API to attach an anchored to the given anchorage.
 *
 * @author mwienand
 *
 * @param <VR>
 *            The visual root node of the UI toolkit, e.g. javafx.scene.Node in
 *            case of JavaFX.
 */
public class AttachToContentAnchorageOperation<VR> extends AbstractOperation
		implements ITransactionalOperation {

	private final IContentPart<VR, ? extends VR> anchored;
	private final Object contentAnchorage;
	private final String role;

	// initial content anchorages (for no-op test)
	private SetMultimap<? extends Object, String> initialContentAnchorages;

	/**
	 * Creates a new {@link AttachToContentAnchorageOperation} to attach the
	 * given <i>anchored</i> {@link IContentPart} to the given
	 * <i>contentAnchorage</i> under the specified <i>role</i>, so that it will
	 * be returned by subsequent calls to
	 * {@link IContentPart#getContentAnchorages()}.
	 *
	 * @param anchored
	 *            The {@link IContentPart} which is to be attached to the given
	 *            <i>contentAnchorage</i>.
	 * @param contentAnchorage
	 *            The content object to which the given <i>anchored</i> is to be
	 *            attached.
	 * @param role
	 *            The role under which the <i>contentAnchorage</i> is anchored.
	 */
	public AttachToContentAnchorageOperation(
			IContentPart<VR, ? extends VR> anchored, Object contentAnchorage,
			String role) {
		super("Attach To Content Anchorage");
		this.anchored = anchored;
		this.contentAnchorage = contentAnchorage;
		this.initialContentAnchorages = anchored.getContentAnchorages();
		this.role = role;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		// System.out.println("EXEC attach " + anchored + " to content "
		// + contentAnchorage + " with role " + role + ".");
		if (anchored.getContent() != null && !anchored.getContentAnchorages()
				.containsEntry(contentAnchorage, role)) {
			anchored.attachToContentAnchorage(contentAnchorage, role);
		}
		return Status.OK_STATUS;
	}

	@Override
	public boolean isNoOp() {
		return initialContentAnchorages.containsEntry(contentAnchorage, role);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		// System.out.println("UNDO attach " + anchored + " to content "
		// + contentAnchorage + " with role " + role + ".");
		if (anchored.getContent() != null && anchored.getContentAnchorages()
				.containsEntry(contentAnchorage, role)) {
			anchored.detachFromContentAnchorage(contentAnchorage, role);
		}
		return Status.OK_STATUS;
	}

}