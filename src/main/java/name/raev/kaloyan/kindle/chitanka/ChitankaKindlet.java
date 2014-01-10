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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.raev.kaloyan.kindle.chitanka;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;

import org.kwt.ui.KWTSelectableLabel;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.event.KindleKeyCodes;
import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;
import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KLabelMultiline;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.KProgress;
import com.amazon.kindle.kindlet.ui.KTextArea;
import com.amazon.kindle.kindlet.ui.border.KLineBorder;

public class ChitankaKindlet extends AbstractKindlet {

	private final static int PAGE_SIZE = 20;

	private KindletContext ctx;

	private Stack pageHistory = new Stack();

	private OpdsPage currentPage;
	private int pageIndex;

	public void create(KindletContext context) {
		this.ctx = context;

		// handle the Back key to navigate back in the browsing history
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(KeyEvent key) {
						if (key.isConsumed()
								|| key.getID() == KeyEvent.KEY_RELEASED)
							return false;

						switch (key.getKeyCode()) {
						case KindleKeyCodes.VK_BACK:
							if (!pageHistory.isEmpty()) {
								key.consume();
								// display the previous page from history
								displayPage((String) pageHistory.pop());
								return true;
							}
							break;

						case KindleKeyCodes.VK_LEFT_HAND_SIDE_TURN_PAGE:
						case KindleKeyCodes.VK_RIGHT_HAND_SIDE_TURN_PAGE:
							key.consume();
							try {
								nextPage();
							} catch (Exception e) {
								e.printStackTrace();
							}
							return true;

						case KindleKeyCodes.VK_TURN_PAGE_BACK:
							key.consume();
							try {
								previousPage();
							} catch (Exception e) {
								e.printStackTrace();
							}
							return true;
						}

						return false;
					}
				});
	}

	public void start() {
		displayPage("http://chitanka.info/catalog.opds");
	}

	private void displayPage(final String opdsUrl) {
		Connectivity connectivity = ctx.getConnectivity();
		connectivity.submitSingleAttemptConnectivityRequest(
				new ConnectivityHandler() {

					public void disabled(NetworkDisabledDetails details)
							throws InterruptedException {
						// TODO Auto-generated method stub

					}

					public void connected() throws InterruptedException {
						Container root = ctx.getRootContainer();
						root.removeAll();
						root.setLayout(new GridBagLayout());

						GridBagConstraints c = new GridBagConstraints();
						c.fill = GridBagConstraints.HORIZONTAL;
						c.weightx = 1.0;
						c.gridx = 0;
						c.gridy = GridBagConstraints.RELATIVE;
						c.insets = new Insets(8, 32, 8, 16);

						KProgress progress = ctx.getProgressIndicator();
						progress.setIndeterminate(true);

						try {
							currentPage = new OpdsPage(opdsUrl);

							root.add(
									new KLabelMultiline(currentPage.getTitle()),
									c);
							root.add(
									new KLabel("Общо ".concat(Integer.toString(
											currentPage.getItemsCount())
											.concat(" заглавия"))), c);

							pageIndex = 0;
							OpdsItem[] opdsItems = currentPage.getItems(
									pageIndex, PAGE_SIZE);

							final KPanel content = new KPanel(new GridBagLayout());

							GridBagConstraints cc0 = new GridBagConstraints();
							cc0.gridx = 0;
							cc0.gridy = GridBagConstraints.RELATIVE;
							cc0.weightx = 0.0;
							cc0.weighty = 1.0;
							cc0.anchor = GridBagConstraints.EAST;
							GridBagConstraints cc1 = new GridBagConstraints();
							cc1.gridx = 1;
							cc1.gridy = GridBagConstraints.RELATIVE;
							cc1.weightx = 1.0;
							cc1.weighty = 1.0;
							cc1.anchor = GridBagConstraints.WEST;
							cc1.insets = new Insets(0, 10, 0, 0);

							for (int i = 0; i < PAGE_SIZE; i++) {
								KLabel indexLabel = new KLabel();
								content.add(indexLabel, cc0);
								
								KWTSelectableLabel titleLabel = new KWTSelectableLabel();
								content.add(titleLabel, cc1);

								if (i < opdsItems.length) {
									indexLabel.setText(Integer.toString(pageIndex + i + 1).concat("."));
									
									OpdsItem opdsItem = opdsItems[i];
									titleLabel.setText(opdsItem.getTitle());
									titleLabel.setFocusable(true);

									final int index = i;
									titleLabel.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent e) {
											OpdsItem opdsItem = currentPage
													.getItem(pageIndex + index);
											String link = opdsItem
													.getNavigationLink();
											if (link != null) {
												// push the current page to
												// history
												pageHistory.push(opdsUrl);
												// navigate to the selected page
												displayPage(link);
											} else {
												// getDownloadLink
												link = opdsItem
														.getDownloadLinks();
												if (link != null) {
													downloadBook(link);
												}
											}
										}
									});
								} else {
									titleLabel.setFocusable(false);
								}
							}

							c.fill = GridBagConstraints.BOTH;
							c.weighty = 1.0; // request any extra vertical space
							root.add(content, c);
							c.fill = GridBagConstraints.HORIZONTAL;
							c.weighty = 0.0;

							content.getComponent(1).requestFocus();

							KLabel pageIndex = new KLabel(("Страница 1 от "
									.concat(Integer.toString(getTotalPages()))));
							pageIndex.setBorder(new KLineBorder(1));
							root.add(pageIndex, c);
						} catch (Throwable t) {
							StringWriter sw = new StringWriter();
							t.printStackTrace(new PrintWriter(sw));
							root.removeAll();
							root.add(new KTextArea(sw.toString()));
						}

						progress.setIndeterminate(false);
						root.repaint();
					}
				}, true);
	}

	private void downloadBook(String href) {
		KProgress progress = ctx.getProgressIndicator();
		progress.setIndeterminate(true);

		try {
			Utils.downloadZippedBook(href);
			Utils.rescanDocuments();
		} catch (Throwable t) {
			Container root = ctx.getRootContainer();
			root.removeAll();

			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));

			root.add(new KTextArea(sw.toString()));
			root.repaint();
		}

		progress.setIndeterminate(false);
		progress.setString("Книгата е свалена");
	}

	private void nextPage() throws Exception {
		KProgress progress = ctx.getProgressIndicator();
		progress.setIndeterminate(true);

		int count = currentPage.getItemsCount();
		if (count - pageIndex > PAGE_SIZE) {
			pageIndex += 20;
			updatePage();
		}

		progress.setIndeterminate(false);
	}

	private void previousPage() throws Exception {
		if (pageIndex > 0) {
			pageIndex -= 20;
			updatePage();
		}
	}

	private void updatePage() throws Exception {
		OpdsItem[] opdsItems = currentPage.getItems(pageIndex, PAGE_SIZE);

		Container root = ctx.getRootContainer();
		KPanel panel = (KPanel) root.getComponent(2);
		Component[] components = panel.getComponents();
		for (int i = 0; i < components.length; i += 2) {
			KLabel indexLabel = (KLabel) components[i];
			KWTSelectableLabel titleLabel = (KWTSelectableLabel) components[i + 1];
			if (i / 2 < opdsItems.length) {
				indexLabel.setText(Integer.toString(pageIndex + (i / 2) + 1).concat("."));
				
				final OpdsItem opdsItem = opdsItems[i / 2];
				titleLabel.setText(opdsItem.getTitle());
				titleLabel.setFocusable(true);
			} else {
				indexLabel.setText("");
				titleLabel.setText("");
				titleLabel.setFocusable(false);
			}
		}

		components[1].requestFocus();

		KLabel pageIndexLabel = (KLabel) root.getComponent(3);
		pageIndexLabel.setText("Страница ".concat(Integer.toString(
				getCurrentPageIndex()).concat(
				" от ".concat(Integer.toString(getTotalPages())))));

		root.repaint();
	}
	
	private int getCurrentPageIndex() {
		return pageIndex / PAGE_SIZE + 1;
	}

	private int getTotalPages() throws Exception {
		return (currentPage.getItemsCount() - 1) / PAGE_SIZE + 1;
	}

	public static void main(String[] args) throws Exception {
		String book = "http://chitanka.info/book/3046-gotvarska-kniga-za-myzhe.txt.zip";
		Utils.downloadZippedBook(book);
	}

}
