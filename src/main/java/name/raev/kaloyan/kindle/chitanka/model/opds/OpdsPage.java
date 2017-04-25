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
package name.raev.kaloyan.kindle.chitanka.model.opds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import name.raev.kaloyan.kindle.chitanka.model.Item;
import name.raev.kaloyan.kindle.chitanka.model.Page;
import name.raev.kaloyan.kindle.chitanka.utils.Network;

public class OpdsPage implements Page {

	private int itemsCount = Integer.MIN_VALUE;
	private String url;

	private SyndFeed firstPage;
	private int pagesCount;

	private SyndFeed currentPage;

	private List entries = new ArrayList();

	public OpdsPage(String url) {
		this.url = url;
	}

	public String getTitle() throws IOException {
		parseFirstPage();
		return firstPage.getTitle();
	}

	public String getSubtitle() throws IOException {
		return null;
	}

	public int getItemsCount() throws IOException {
		if (itemsCount == Integer.MIN_VALUE) {
			if (url == null) {
				itemsCount = 1;
			} else {
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
		}
		return itemsCount;
	}

	private void parsePagesCount() throws IOException {
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

	private void parseFirstPage() throws IOException {
		if (firstPage == null) {
			firstPage = parse(url);
			entries.addAll(firstPage.getEntries());
			currentPage = firstPage;
		}
	}

	private boolean parseNextPage() throws IOException {
		SyndLink nextLink = getNextLink(currentPage);
		if (nextLink != null) {
			currentPage = parse(Network.getUrlFromLinkAsString(nextLink.getHref()));
			entries.addAll(currentPage.getEntries());
			return true;
		}
		return false;
	}
	
	private SyndFeed parsePage(int pageNumber) throws IOException {
		String pageUrl = url.concat("/").concat(Integer.toString(pageNumber));
		return parse(pageUrl);
	}

	private SyndFeed parse(String address) throws IOException {
		try {
			SyndFeedInput input = new SyndFeedInput();
			return input.build(new XmlReader(Network.getInputStream(address)));
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

	public Item[] getItems(int index, int length) throws IOException {
		while (entries.size() < index + length && parseNextPage());

		OpdsItem[] items = new OpdsItem[Math
				.min(length, entries.size() - index)];
		for (int i = 0; i < items.length; i++) {
			items[i] = new OpdsItem((SyndEntry) entries.get(index + i));
		}

		return items;
	}

	public Item getItem(int index) {
		if (index < entries.size()) {
			return new OpdsItem((SyndEntry) entries.get(index));
		}
		return null;
	}

	public Item[] getItems() throws IOException {
		return getItems(0, getItemsCount());
	}

	public String getUrl() {
		return url;
	}
	
}
