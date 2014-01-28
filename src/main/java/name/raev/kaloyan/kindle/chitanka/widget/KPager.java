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
package name.raev.kaloyan.kindle.chitanka.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KColorName;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KFontFamilyName;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KFontStyle;

/**
 * A pager footer component that displays the index of the current page and the
 * total number of pages.
 * 
 * @author Kaloyan Raev
 */
public class KPager extends KLabel {

	private static final long serialVersionUID = -6021139255626746449L;

	private static final Color BLACK = KindletUIResources.getInstance()
			.getColor(KColorName.BLACK);
	private static final Color GRAY = KindletUIResources.getInstance()
			.getColor(KColorName.GRAY_01);

	private int totalPages;
	private int currentPage;

	/**
	 * Constructs a new pager with the given number of pages.
	 * 
	 * @param totalPages
	 *            the total number of pages
	 */
	public KPager(int totalPages) {
		this.totalPages = totalPages;
		this.currentPage = 1;

		setFont(KindletUIResources.getInstance().getFont(
				KFontFamilyName.SANS_SERIF, 20, KFontStyle.BOLD, true));

		updateText();
	}

	/**
	 * Sets the current page.
	 * 
	 * @param index
	 *            the index of the current page
	 */
	public void setPage(int index) {
		currentPage = index;
		updateText();
	}

	private void updateText() {
		if (totalPages > 1) {
			setText("Страница ".concat(Integer.toString(currentPage))
					.concat(" от ").concat(Integer.toString(totalPages)));
		} else {
			setText("");
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	/**
	 * {@inheritDoc }
	 */
	public Dimension getMinimumSize() {
		// Use KLabel's preferred size for its minimum size, to work around a
		// known bug in the KDK.
		Dimension d = super.getPreferredSize();
		return new Dimension(d.width, d.height + 14);
	}

	/**
	 * {@inheritDoc }
	 */
	public Dimension getSize() {
		Dimension d = super.getSize();
		return new Dimension(d.width, d.height + 14);
	}

	/**
	 * {@inheritDoc }
	 */
	public void paint(Graphics g) {
		Dimension d = getSize();

		g.setColor(GRAY);
		g.fillRect(0, d.height / 3, d.width, d.height);

		g.setColor(BLACK);
		g.drawLine(0, 0, d.width, 0);

		g.drawString(getText(), 32, 24);
	}

}
