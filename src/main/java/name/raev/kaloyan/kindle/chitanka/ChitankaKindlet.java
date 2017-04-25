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
package name.raev.kaloyan.kindle.chitanka;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.event.KindleKeyCodes;

import name.raev.kaloyan.kindle.chitanka.screen.ScreenManager;
import name.raev.kaloyan.kindle.chitanka.screen.SplashScreen;

public class ChitankaKindlet extends AbstractKindlet {

	private static final int VK_KEYBOARD = 17;

	public void create(KindletContext context) {
		ContextManager.setContext(context);

		// handle the Back key to navigate back in the browsing history
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(KeyEvent key) {
						if (key.isConsumed()
								|| key.getID() == KeyEvent.KEY_RELEASED)
							return false;

						switch (key.getKeyCode()) {
						case KindleKeyCodes.VK_BACK:
							ConnectivityManager mgr = ConnectivityManager.getInstance();
							if (mgr.canGoBack()) {
								key.consume();
								mgr.goBack();
								return true;
							}
							break;

						case KindleKeyCodes.VK_LEFT_HAND_SIDE_TURN_PAGE:
						case KindleKeyCodes.VK_RIGHT_HAND_SIDE_TURN_PAGE:
							key.consume();
							ScreenManager.getCurrentScreen().nextPage();
							return true;

						case KindleKeyCodes.VK_TURN_PAGE_BACK:
							key.consume();
							ScreenManager.getCurrentScreen().previousPage();
							return true;

						case VK_KEYBOARD:
							ScreenManager.getCurrentScreen().focusOnSearch();
							return true;
						}

						return false;
					}
				});
	}

	public void start() {
		try {
			new SplashScreen().display();
		} catch (IOException e) {
			// nothing to do
		}
		ConnectivityManager.getInstance().navigateTo("/catalog.opds");
	}

}
