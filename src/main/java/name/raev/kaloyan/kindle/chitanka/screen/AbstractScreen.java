/**
 * Copyright 2014 Kaloyan Raev
 * 
 * This file is part of chitanka4kindle.
 * 
 * chitanka4kindle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * chitanka4kindle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with chitanka4kindle.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.raev.kaloyan.kindle.chitanka.screen;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import name.raev.kaloyan.kindle.chitanka.ContextManager;
import name.raev.kaloyan.kindle.chitanka.OpdsPage;
import name.raev.kaloyan.kindle.chitanka.widget.KPager;

import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.KProgress;
import com.amazon.kindle.kindlet.ui.KTextArea;

public abstract class AbstractScreen {

	protected OpdsPage opdsPage;

	protected int pageIndex;

	private KPager pager;

	AbstractScreen(String opdsUrl) {
		opdsPage = new OpdsPage(opdsUrl);
	}

	protected abstract void createContent(Container container);

	protected abstract void updateContent(Container container);

	protected abstract int getPageSize();

	public void display() {
		Container container = ContextManager.getContext().getRootContainer();

		KProgress progress = ContextManager.getContext().getProgressIndicator();
		progress.setIndeterminate(true);

		// clear the content of the container
		container.removeAll();

		container.setLayout(new GridBagLayout());

		try {
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.gridx = 0;
			c.gridy = GridBagConstraints.RELATIVE;

			KPanel content = new KPanel(new GridBagLayout());
			container.add(content, c);

			// call the subclass to create the main content of the page
			createContent(content);

			// set the focus on the first focusable widget in the created
			// content
			setFocusOnFirst(content);

			createPager(container);
		} catch (Throwable t) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			container.removeAll();
			container.add(new KTextArea(sw.toString()));
		}

		progress.setIndeterminate(false);
		container.repaint();
	}

	private void createPager(Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		pager = new KPager(getTotalPages());
		container.add(pager, c);
	}

	private int getCurrentPageIndex() {
		return pageIndex / getPageSize() + 1;
	}

	private int getTotalPages() {
		return (opdsPage.getItemsCount() - 1) / getPageSize() + 1;
	}

	public void nextPage() {
		int count = opdsPage.getItemsCount();
		if (count - pageIndex > getPageSize()) {
			KProgress progress = ContextManager.getContext()
					.getProgressIndicator();
			progress.setIndeterminate(true);

			pageIndex += getPageSize();
			updateScreen();

			progress.setIndeterminate(false);
		}
	}

	public void previousPage() {
		if (pageIndex > 0) {
			pageIndex -= getPageSize();
			updateScreen();
		}
	}

	private void updateScreen() {
		try {
			KPanel content = (KPanel) ContextManager.getContext()
					.getRootContainer().getComponent(0);

			updateContent(content);

			pager.setPage(getCurrentPageIndex());
		} catch (Throwable t) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			Container container = ContextManager.getContext()
					.getRootContainer();
			container.removeAll();
			container.add(new KTextArea(sw.toString()));
		}

		ContextManager.getContext().getRootContainer().repaint();
	}

	public String getUrl() {
		return opdsPage.getUrl();
	}

	protected void setFocusOnFirst(Container container) {
		Component[] components = container.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];
			if (component.isFocusable()) {
				component.requestFocus();
				return;
			}
		}
	}

}
