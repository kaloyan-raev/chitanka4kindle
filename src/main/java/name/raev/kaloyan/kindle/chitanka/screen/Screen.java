/**
 * Copyright 2014-2017 Kaloyan Raev
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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.KTextArea;
import com.amazon.kindle.kindlet.ui.KindletUIResources;

import name.raev.kaloyan.kindle.chitanka.ConnectivityManager;
import name.raev.kaloyan.kindle.chitanka.ContextManager;
import name.raev.kaloyan.kindle.chitanka.DialogManager;
import name.raev.kaloyan.kindle.chitanka.model.NullPage;
import name.raev.kaloyan.kindle.chitanka.model.Page;
import name.raev.kaloyan.kindle.chitanka.utils.ProgressIndicator;
import name.raev.kaloyan.kindle.chitanka.widget.KPager;
import name.raev.kaloyan.kindle.chitanka.widget.KSearchField;

public abstract class Screen {
	
	protected static final Font FONT_PAGE_TITLE = KindletUIResources.getInstance().getFont(
			KindletUIResources.KFontFamilyName.SANS_SERIF, 18,
			KindletUIResources.KFontStyle.BOLD_ITALIC, true);
	
	protected static final Font FONT_PAGE_SUBTITLE = KindletUIResources.getInstance().getFont(
			KindletUIResources.KFontFamilyName.SANS_SERIF, 16,
			KindletUIResources.KFontStyle.PLAIN, true);

	protected static final Font FONT_LINK = KindletUIResources.getInstance().getFont(
			KindletUIResources.KFontFamilyName.SANS_SERIF, 22,
			KindletUIResources.KFontStyle.BOLD, true);
	
	protected static final Font FONT_ERROR = KindletUIResources.getInstance().getFont(
			KindletUIResources.KFontFamilyName.SANS_SERIF, 10,
			KindletUIResources.KFontStyle.PLAIN, true);

	protected Page page;

	protected int pageIndex;

	protected KSearchField search;
	private KPager pager;

	Screen(Page page) {
		this.page = page;
	}

	protected abstract void createContent(Container container) throws IOException;

	protected abstract void updateContent(Container container) throws IOException;

	protected abstract int getPageSize();

	public void display() throws IOException {
		ProgressIndicator.start();
		
		Container container = ContextManager.getContext().getRootContainer();

		container.setLayout(new GridBagLayout());

		try {
			KPanel content = createContentPanel();

			pageIndex = 0;

			// call the subclass to create the main content of the page
			createContent(content);

			// clear the content of the container
			container.removeAll();

			// add the content panel
			addContentPanel(container, content);

			// add the search field, but not on the splash screen
			createSearch(container);

			// add the pager
			createPager(container);

			// reset keyboard focus
			resetFocus(container);
		} catch (IOException e) {
			throw e;
		} catch (Throwable t) {
			displayError(t);
		} finally {
			ProgressIndicator.stop();
		}
		container.repaint();
	}

	private KPanel createContentPanel() {
		return new KPanel(new GridBagLayout());
	}

	private void addContentPanel(Container container, KPanel content) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		container.add(content, c);
	}

	private void createSearch(Container container) {
		if (!(page instanceof NullPage)) {
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 0;
			c.gridy = GridBagConstraints.RELATIVE;
			c.insets = new Insets(20, 8, 10, 8);
			search = new KSearchField();
			container.add(search, c);
		}
	}

	private void createPager(Container container) throws IOException {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		pager = new KPager(getTotalPages());
		container.add(pager, c);
	}

	private int getCurrentPage() {
		return pageIndex / getPageSize() + 1;
	}

	protected int getTotalPages() throws IOException {
		return (page.getItemsCount() - 1) / getPageSize() + 1;
	}

	public void nextPage() {
		if (DialogManager.isDialogDisplayed()) {
			return;
		}

		ProgressIndicator.start();
		try {
			int count = page.getItemsCount();
			if (count - pageIndex > getPageSize()) {
				pageIndex += getPageSize();
				updateScreen();
			}
		} catch (IOException e) {
			updatePage();
		} finally {
			ProgressIndicator.stop();
		}
	}

	public void previousPage() {
		if (DialogManager.isDialogDisplayed()) {
			return;
		}

		try {
			if (pageIndex > 0) {
				pageIndex -= getPageSize();
				updateScreen();
			}
		} catch (IOException e) {
			updatePage();
		}
	}

	public void focusOnSearch() {
		if (search != null) {
			search.requestFocus();
		}
	}

	private void updatePage() {
		ProgressIndicator.start();
		Connectivity connectivity = ContextManager.getContext().getConnectivity();
		connectivity.submitSingleAttemptConnectivityRequest(new ConnectivityHandler() {
			public void disabled(NetworkDisabledDetails details) throws InterruptedException {
				ConnectivityManager.getInstance().handleNetworkError();
				updatePage();
			}

			public void connected() throws InterruptedException {
				try {
					updateScreen();
				} catch (IOException e) {
					ConnectivityManager.getInstance().handleNetworkError();
					updatePage();
				}
			}
		}, true);
	}

	private void updateScreen() throws IOException {
		try {
			Container container = ContextManager.getContext().getRootContainer();
			Component firstComponent = container.getComponent(0);
			KPanel content;
			if (firstComponent instanceof KPanel) {
				content = (KPanel) firstComponent;
				updateContent(content);
			} else {
				// perhaps an error was displayed - recreate the screen
				content = createContentPanel();
				createContent(content);
				container.removeAll();
				addContentPanel(container, content);
				createSearch(container);
				createPager(container);
			}

			resetFocus(content);

			pager.setPage(getCurrentPage());
		} catch (IOException e) {
			throw e;
		} catch (Throwable t) {
			displayError(t);
		}

		ContextManager.getContext().getRootContainer().repaint();
	}

	/**
	 * The default implementation sets the focus to the first focusable widget.
	 */
	protected boolean resetFocus(Container container) {
		Component[] components = container.getComponents();

		for (int i = 0; i < components.length; i++) {
			Component component = components[i];

			// check children recursively
			if (component instanceof KPanel) {
				KPanel c = (KPanel) component;
				if (resetFocus(c))
					return true;
			} else if (component.isFocusable() && component.isEnabled()) {
				component.requestFocus();
				return true;
			}
		}
		
		return false;
	}
	
	protected String getPageTitle() throws IOException {
		String title = page.getTitle();
		
		int index = title.indexOf(" — страница");
		if (index != -1) {
			title = title.substring(0, index);
		}
		
		index = title.indexOf(" — Моята библиотека");
		if (index != -1) {
			title = title.substring(0, index);
		}
		
		return title;
	}
	
	public static void displayError(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		
		Container container = ContextManager.getContext().getRootContainer();
		container.removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		KTextArea text = new KTextArea(sw.toString());
		text.setFont(FONT_ERROR);
		text.setRows(70);
		container.add(text, c);
		
		container.repaint();
	}

}
