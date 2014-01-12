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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
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

	public Image getImage() {
		List links = entry.getLinks();
		if (links != null && links.size() > 5) {
			try {
				SyndLink imageLink = (SyndLink) links.get(5);
				String href = imageLink.getHref();
				href = replace(href, ".200.", ".65.");
				URL url = new URL(href);
				return Toolkit.getDefaultToolkit().createImage(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private String replace(String str, String text, String replacement) {
		int i = str.indexOf(text);
		if (i != -1) {
			return str.substring(0, i).concat(replacement).concat(str.substring(i + 5));
		}
		return str;
	}
}
