/**
 * Copyright 2017 Kaloyan Raev
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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KOptionPane;
import com.amazon.kindle.kindlet.ui.KOptionPane.MessageDialogListener;
import com.amazon.kindle.kindlet.ui.KTextField;

import name.raev.kaloyan.kindle.chitanka.ConnectivityManager;

/**
 * A search field widget.
 * 
 * <p>
 * It is a composite of a bordered panel with a text field and a button.
 * </p>
 * 
 * <p>
 * When the button is clicked, the Search API of Chitanka is queried.
 * </p>
 */
public class KSearchField extends KBorderedPanel {

	private static final long serialVersionUID = 5995745417624143763L;

	private static final String SEARCH_LINK_PREFIX = "/search.json?q=";

	private KTextField text;
	private KButton button;

	public KSearchField() {
		this(null);
	}

	public KSearchField(String initialText) {
		text = new KTextField(initialText, 16);
		text.setHint("Въведете поне 3 символа");
		text.setMargin(new Insets(0, 20, 0, 20));
		add(text);
		text.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					doSearch();
				}
			}
		});

		button = new KButton(" в библиотеката");
		add(button);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearch();
			}
		});
	}

	public void requestFocus() {
		text.requestFocus();
	}

	private void doSearch() {
		if (text.getText().trim().length() >= 3) {
			String url = SEARCH_LINK_PREFIX + text.getText().trim();
			ConnectivityManager.getInstance().navigateTo(url);
		} else {
			KOptionPane.showMessageDialog(null, "Въведете поне 3 символа.", "Търсене", new MessageDialogListener() {
				public void onClose() {
					text.requestFocus();
				}
			});
		}
	}

}
