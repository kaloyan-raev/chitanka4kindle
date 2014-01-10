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

import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndLink;

public class OpdsItem {

	private SyndEntry entry;

	OpdsItem(SyndEntry entry) {
		this.entry = entry;
	}

	public String getTitle() {
		return entry.getTitle();
	}

	public String getNavigationLink() {
		return entry.getLink();
	}

	public String getDownloadLinks() {
		List links = entry.getLinks();
		SyndLink txtLink = (SyndLink) links.get(2);
		return txtLink.getHref();
	}
}