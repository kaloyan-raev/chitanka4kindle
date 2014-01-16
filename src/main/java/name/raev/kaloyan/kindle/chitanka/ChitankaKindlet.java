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
package name.raev.kaloyan.kindle.chitanka;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import name.raev.kaloyan.kindle.chitanka.widget.KActionLabel;
import name.raev.kaloyan.kindle.chitanka.widget.KPager;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.event.KindleKeyCodes;
import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;
import com.amazon.kindle.kindlet.ui.KImage;
import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KLabelMultiline;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.KProgress;
import com.amazon.kindle.kindlet.ui.KTextArea;

public class ChitankaKindlet extends AbstractKindlet {

	private final static int PAGE_SIZE = 6;

	private KindletContext ctx;

	private Stack pageHistory = new Stack();

	private OpdsPage currentPage;
	private int pageIndex;

	Image noCoverImage;

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

		try {
			noCoverImage = Toolkit
					.getDefaultToolkit()
					.createImage(
							new URL(
									"http://assets.chitanka.info/thumb/book-cover/00/0.65.png"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
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

							final KPanel content = new KPanel(
									new GridBagLayout());

							GridBagConstraints cIndex = new GridBagConstraints();
							cIndex.gridx = 0;
							cIndex.gridy = GridBagConstraints.RELATIVE;
							cIndex.weightx = 0.0;
							cIndex.weighty = 1.0;
							cIndex.anchor = GridBagConstraints.EAST;
							GridBagConstraints cImage = new GridBagConstraints();
							cImage.gridx = 1;
							cImage.gridy = GridBagConstraints.RELATIVE;
							cImage.weightx = 0.0;
							cImage.weighty = 1.0;
							cImage.insets = new Insets(0, 20, 0, 20);
							GridBagConstraints cTitle = new GridBagConstraints();
							cTitle.gridx = 2;
							cTitle.gridy = GridBagConstraints.RELATIVE;
							cTitle.weightx = 1.0;
							cTitle.weighty = 1.0;
							cTitle.anchor = GridBagConstraints.WEST;

							for (int i = 0; i < PAGE_SIZE; i++) {
								KLabel indexLabel = new KLabel();
								content.add(indexLabel, cIndex);

								KImage image = new KImage(null, 65, 86);
								content.add(image, cImage);

								KActionLabel titleLabel = new KActionLabel();
								content.add(titleLabel, cTitle);

								if (i < opdsItems.length) {
									OpdsItem opdsItem = opdsItems[i];

									indexLabel.setText(Integer.toString(
											pageIndex + i + 1).concat("."));
									image.setImage(opdsItem.getImage(), false);

									titleLabel.setText(opdsItem.getTitle());
									titleLabel.setFocusable(true);

									final int index = i;
									titleLabel
											.addActionListener(new ActionListener() {
												public void actionPerformed(
														ActionEvent e) {
													OpdsItem opdsItem = currentPage
															.getItem(pageIndex
																	+ index);
													String link = opdsItem
															.getNavigationLink();
													if (link != null) {
														// push the current page
														// to
														// history
														pageHistory
																.push(opdsUrl);
														// navigate to the
														// selected page
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

							content.getComponent(2).requestFocus();

							KPager pager = new KPager(getTotalPages());
							c.insets = new Insets(0, 0, 0, 0);
							root.add(pager, c);
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
			Utils.downloadMobiFromEpubUrl(href);
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
			pageIndex += PAGE_SIZE;
			updatePage();
		}

		progress.setIndeterminate(false);
	}

	private void previousPage() throws Exception {
		if (pageIndex > 0) {
			pageIndex -= PAGE_SIZE;
			updatePage();
		}
	}

	private void updatePage() throws Exception {
		OpdsItem[] opdsItems = currentPage.getItems(pageIndex, PAGE_SIZE);

		Container  root = ctx.getRootContainer();
		KPanel content = (KPanel) root.getComponent(2);
		Component[] components = content.getComponents();
		for (int i = 0; i < components.length; i += 3) {
			KLabel indexLabel = (KLabel) components[i];
			KImage image = (KImage) components[i + 1];
			KActionLabel titleLabel = (KActionLabel) components[i + 2];
			if (i / 3 < opdsItems.length) {
				OpdsItem opdsItem = opdsItems[i / 3];

				indexLabel.setText(Integer.toString(pageIndex + (i / 3) + 1)
						.concat("."));
				image.setImage(opdsItem.getImage(), false);

				titleLabel.setText(opdsItem.getTitle());
				titleLabel.setFocusable(true);
			} else {
				indexLabel.setText("");
				image.setImage(null, false);
				titleLabel.setText("");
				titleLabel.setFocusable(false);
			}
		}

		components[2].requestFocus();

		KPager pager = (KPager) root.getComponent(3);
		pager.setPage(getCurrentPageIndex());

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
