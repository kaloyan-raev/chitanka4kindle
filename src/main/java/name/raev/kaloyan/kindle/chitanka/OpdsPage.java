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

	public String getTitle() {
		parseFirstPage();
		return firstPage.getTitle();
	}

	public int getItemsCount() {
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

	private void parsePagesCount() {
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

	private void parseFirstPage() {
		if (firstPage == null) {
			firstPage = parse(url);
			entries.addAll(firstPage.getEntries());
			currentPage = firstPage;
		}
	}

	private void parseNextPage() {
		SyndLink nextLink = getNextLink(currentPage);
		if (nextLink != null) {
			currentPage = parse("http://chitanka.info".concat(nextLink.getHref()));
			entries.addAll(currentPage.getEntries());
		}
	}
	
	private SyndFeed parsePage(int pageNumber) {
		String pageUrl = url.concat("/").concat(Integer.toString(pageNumber));
		return parse(pageUrl);
	}

	private SyndFeed parse(String address) {
		try {
			URL feedUrl = new URL(address);
			SyndFeedInput input = new SyndFeedInput();
			return input.build(new XmlReader(feedUrl));
		} catch (Exception e) {
			throw new RuntimeException("Failed parsing: " + address, e);
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

	public OpdsItem[] getItems(int index, int length) {
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

	public OpdsItem[] getItems() {
		return getItems(0, getItemsCount());
	}

	public String getUrl() {
		return url;
	}
	
}