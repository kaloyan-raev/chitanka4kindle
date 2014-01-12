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

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class OpdsPage {

	private int itemsCount = Integer.MIN_VALUE;
	private String url;

	private SyndFeed firstPage;
	private int pagesCount;

	private SyndFeed currentPage;

	private List entries = new ArrayList();

	public OpdsPage(String url) {
		this.url = url;
	}

	public String getTitle() throws Exception {
		parseFirstPage();
		return firstPage.getTitle();
	}

	public int getItemsCount() throws Exception {
		if (itemsCount == Integer.MIN_VALUE) {
			parsePagesCount();
	
			int firstPageEntries = firstPage.getEntries().size();
			if (pagesCount == 1) {
				itemsCount = firstPageEntries;
			} else {
				SyndFeed lastPage = parsePage(pagesCount);
				itemsCount = firstPageEntries * (pagesCount - 1)
						+ lastPage.getEntries().size();
			}
		}
		return itemsCount;
	}

	private void parsePagesCount() throws Exception {
		parseFirstPage();

		String title = firstPage.getTitle();
		if (title == null) {
			pagesCount = 1;
		} else {
			int index = title.indexOf(" — страница 1 от ");
			if (index == -1) {
				pagesCount = 1;
			} else {
				int i2 = index + " — страница 1 от ".length();
				int i3 = title.indexOf(' ', i2);
				pagesCount = Integer.parseInt(title.substring(i2, i3));
			}
		}
	}

	private void parseFirstPage() throws Exception {
		if (firstPage == null) {
			URL feedUrl = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			firstPage = input.build(new XmlReader(feedUrl));
			entries.addAll(firstPage.getEntries());
			currentPage = firstPage;
		}
	}

	private void parseNextPage() throws Exception {
		SyndLink nextLink = getNextLink(currentPage);
		if (nextLink != null) {
			URL feedUrl = new URL("http://chitanka.info".concat(nextLink.getHref()));
			SyndFeedInput input = new SyndFeedInput();
			currentPage = input.build(new XmlReader(feedUrl));
			entries.addAll(currentPage.getEntries());
		}
	}

	private SyndLink getNextLink(SyndFeed feed) {
		List links = feed.getLinks();
		if (links == null)
			return null;

		Iterator iter = links.iterator();
		while (iter.hasNext()) {
			SyndLink link = (SyndLink) iter.next();
			if ("next".equals(link.getRel())) {
				return link;
			}
		}

		return null;
	}

	private SyndFeed parsePage(int pageNumber) throws Exception {
		String pageUrl = url.concat("/").concat(Integer.toString(pageNumber));
		URL feedUrl = new URL(pageUrl);
		SyndFeedInput input = new SyndFeedInput();
		return input.build(new XmlReader(feedUrl));
	}

	public static void main(String[] args) throws Exception {
		OpdsPage opds = new OpdsPage("http://chitanka.info/books/alpha/-.opds");
		System.out.println(opds.getItemsCount());
	}

	public OpdsItem[] getItems(int index, int length) throws Exception {
		if (entries.size() < index + length) {
			parseNextPage();
		}

		OpdsItem[] items = new OpdsItem[Math
				.min(length, entries.size() - index)];
		for (int i = 0; i < items.length; i++) {
			items[i] = new OpdsItem((SyndEntry) entries.get(index + i));
		}

		return items;
	}

	public OpdsItem getItem(int index) {
		if (index < entries.size()) {
			return new OpdsItem((SyndEntry) entries.get(index));
		}
		return null;
	}

}
