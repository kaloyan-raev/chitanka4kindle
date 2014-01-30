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
package name.raev.kaloyan.kindle.chitanka.screen;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import name.raev.kaloyan.kindle.chitanka.OpdsItem;
import name.raev.kaloyan.kindle.chitanka.widget.KActionLabel;

import com.amazon.kindle.kindlet.ui.KImage;
import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KLabelMultiline;
import com.amazon.kindle.kindlet.ui.KPanel;

public class BookListScreen extends AbstractScreen {

	BookListScreen(String opdsUrl) {
		super(opdsUrl);
	}

	protected int getPageSize() {
		return 6;
	}

	protected void createContent(Container container) {
		container.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.insets = new Insets(8, 32, 8, 16);

		container.add(new KLabelMultiline(opdsPage.getTitle()), c);
		container.add(
				new KLabel("Общо ".concat(Integer.toString(
						opdsPage.getItemsCount()).concat(" заглавия"))), c);

		OpdsItem[] opdsItems = opdsPage.getItems(pageIndex, getPageSize());

		final KPanel content = new KPanel(new GridBagLayout());

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

		for (int i = 0; i < getPageSize(); i++) {
			KLabel indexLabel = new KLabel();
			content.add(indexLabel, cIndex);

			KImage image = new KImage(null, 65, 86);
			content.add(image, cImage);

			KActionLabel titleLabel = new KActionLabel();
			content.add(titleLabel, cTitle);

			if (i < opdsItems.length) {
				OpdsItem opdsItem = opdsItems[i];

				indexLabel.setText(Integer.toString(pageIndex + i + 1).concat(
						"."));
				image.setImage(opdsItem.getImage(), false);

				titleLabel.setText(opdsItem.getTitle());
				titleLabel.setFocusable(true);

				titleLabel.addActionListener(new LinkActionListener(this, i));
			} else {
				titleLabel.setFocusable(false);
			}
		}

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0; // request any extra vertical space
		container.add(content, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.0;

		content.getComponent(2).requestFocus();
	}

	protected void updateContent(Container container) {
		OpdsItem[] opdsItems = opdsPage.getItems(pageIndex, getPageSize());

		KPanel content = (KPanel) container.getComponent(2);
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
	}

}
