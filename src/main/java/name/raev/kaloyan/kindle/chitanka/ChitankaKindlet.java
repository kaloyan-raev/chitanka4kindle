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

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;
import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.KProgress;
import com.amazon.kindle.kindlet.ui.KTextArea;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class ChitankaKindlet extends AbstractKindlet {

	private KindletContext ctx;

	public void create(KindletContext context) {
		this.ctx = context;
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
						
						final KProgress progress = ctx
								.getProgressIndicator();
						progress.setIndeterminate(true);

						try {
							URL feedUrl = new URL(opdsUrl);
							SyndFeedInput input = new SyndFeedInput();
							SyndFeed feed = input.build(new XmlReader(feedUrl));

							ctx.setSubTitle(feed.getTitle());

							List entries = feed.getEntries();
							if (entries != null) {
								KPanel panel = new KPanel(new GridLayout(
										30, entries.size() / 30));
								root.add(panel);

								Iterator iterator = entries.iterator();
								while (iterator.hasNext()) {
									final SyndEntry entry = (SyndEntry) iterator
											.next();
									KButton button = new KButton(entry.getTitle());
									panel.add(button);
									button.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent e) {
											String link = entry.getLink();
											if (link != null) {
												displayPage(link);
											} else {
												List links = entry.getLinks();
												SyndLink txtLink = (SyndLink) links.get(2);
												downloadBook(txtLink.getHref());
											}
										}
									});
								}

								panel.getComponent(0).requestFocus();
							}
						} catch (Throwable t) {
							StringWriter sw = new StringWriter();
							t.printStackTrace(new PrintWriter(sw));
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

	public static void main(String[] args) throws Exception {
		String book = 
				"http://chitanka.info/book/3046-gotvarska-kniga-za-myzhe.txt.zip";
		Utils.downloadZippedBook(book);
	}

}
