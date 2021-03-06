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
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;

import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KPanel;

import name.raev.kaloyan.kindle.chitanka.model.Item;
import name.raev.kaloyan.kindle.chitanka.model.Page;
import name.raev.kaloyan.kindle.chitanka.widget.KActionLabel;

public class LongListScreen extends Screen {

	LongListScreen(Page page) {
		super(page);
	}

	protected int getPageSize() {
		return 13;
	}

	protected void createContent(Container container) throws IOException {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.insets = new Insets(8, 32, 8, 16);
		c.anchor = GridBagConstraints.WEST;

		// add title
		KLabel title = new KLabel(getPageTitle());
		title.setFont(FONT_PAGE_TITLE);
		container.add(title, c);

		// add subtitle
		String subtitle = page.getSubtitle();
		if (subtitle != null) {
			KLabel total = new KLabel(subtitle);
			total.setFont(FONT_PAGE_SUBTITLE);
			container.add(total, c);
		}

		// add item links
		Item[] items = page.getItems(pageIndex, getPageSize());
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];

			KActionLabel label = new KActionLabel(item.getTitle());
			label.setFont(FONT_LINK);
			container.add(label, c);

			label.addActionListener(new LinkActionListener(this, i));
		}
		
		// add empty filler
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0; // request any extra vertical space
		KPanel filler = new KPanel();
		filler.setFocusable(false);
		container.add(filler, c);
	}

	protected void updateContent(Container container) throws IOException {
		Item[] items = page.getItems(pageIndex, getPageSize());

		Component[] components = container.getComponents();
		int startIndex = (page.getSubtitle() == null) ? 1 : 2;
		for (int i = startIndex; i < components.length - 1; i++) {
			KActionLabel titleLabel = (KActionLabel) components[i];
			if (i - startIndex < items.length) {
				Item item = items[i - startIndex];
				titleLabel.setText(item.getTitle());
				titleLabel.setFocusable(true);
			} else {
				titleLabel.setText("");
				titleLabel.setFocusable(false);
			}
		}
	}

}
