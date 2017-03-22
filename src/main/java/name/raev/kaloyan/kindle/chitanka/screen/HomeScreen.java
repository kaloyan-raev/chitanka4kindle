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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KImage;
import com.amazon.kindle.kindlet.ui.KTextField;

import name.raev.kaloyan.kindle.chitanka.OpdsItem;
import name.raev.kaloyan.kindle.chitanka.Utils;
import name.raev.kaloyan.kindle.chitanka.widget.KActionLabel;
import name.raev.kaloyan.kindle.chitanka.widget.KBorderedPanel;

public class HomeScreen extends Screen {

	HomeScreen(String opdsUrl) {
		super(opdsUrl);
	}

	protected int getPageSize() {
		return 8;
	}

	protected void createContent(Container container) throws IOException {
		addLogo(container);
		addLinks(container);
		addSearch(container);
	}

	protected void updateContent(Container container) {
		// nothing to update
	}

	private void addLogo(Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		Image img = Utils.loadBuiltinImage("logo.jpg");
		KImage image = new KImage(img, 688 / 2, 720 / 2);
		container.add(image, c);
	}

	private void addLinks(Container container) throws IOException {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		OpdsItem[] items = opdsPage.getItems();
		for (int i = 0; i < items.length; i++) {
			final OpdsItem item = items[i];

			KActionLabel label = new KActionLabel(item.getTitle());
			label.setFont(FONT_LINK);

			c.gridx = i % 2;
			container.add(label, c);

			label.addActionListener(new LinkActionListener(this, i));
		}
	}

	private void addSearch(Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		KBorderedPanel panel = new KBorderedPanel();
		container.add(panel, c);

		KTextField text = new KTextField(16);
		text.setHint("Въведете поне 3 символа");
		text.setMargin(new Insets(0, 20, 0, 20));
		panel.add(text);

		KButton button = new KButton(" в библиотеката");
		panel.add(button);
	}

}
