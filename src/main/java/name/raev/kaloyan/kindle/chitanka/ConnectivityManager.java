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

import java.util.Stack;

import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;
import com.amazon.kindle.kindlet.ui.KOptionPane;
import com.amazon.kindle.kindlet.ui.KOptionPane.MessageDialogListener;

import name.raev.kaloyan.kindle.chitanka.screen.Screen;
import name.raev.kaloyan.kindle.chitanka.screen.ScreenManager;

public class ConnectivityManager {
	
	public static final String BASE_URL = "http://chitanka.info";

	private static ConnectivityManager instance;

	private Stack history = new Stack();

	public static ConnectivityManager getInstance() {
		if (instance == null) {
			instance = new ConnectivityManager();
		}
		return instance;
	}

	public void display(final String url) {
		Connectivity connectivity = ContextManager.getContext()
				.getConnectivity();
		connectivity.submitSingleAttemptConnectivityRequest(
				new ConnectivityHandler() {
					public void disabled(NetworkDisabledDetails details)
							throws InterruptedException {
						// TODO show error message
					}

					public void connected() throws InterruptedException {
						Screen screen = ScreenManager.createScreen(url);
						screen.display();

						ScreenManager.setCurrentScreen(screen);
					}
				}, true);
	}

	public void downloadBook(String href) throws IllegalStateException,
			IllegalArgumentException {
		Utils.startProgressIndicator();

		try {
			Utils.downloadMobiFromEpubUrl(href);
			Utils.rescanDocuments();
		} catch (Throwable t) {
			Screen.displayError(t);
		} finally {
			Utils.stopProgressIndicator();
		}

		// show info message
		String title = "Книгата е свалена";
		String message = "Ще намерите книгата в началото на главния екран на Kindle. Натиснете бутона Home, за да преминете към главния екран.";
		KOptionPane.showMessageDialog(null, message, title,
				new MessageDialogListener() {
					public void onClose() {
						// do nothing
					}
				});
	}

	public boolean canGoBack() {
		return !history.isEmpty();
	}

	public void goBack() {
		// display the previous page from history
		display((String) history.pop());
	}

	public void navigateTo(String link) {
		// push the current page to history
		history.push(ScreenManager.getCurrentScreen().getUrl());
		// navigate to the selected page
		display(Utils.getUrlFromLinkAsString(link));
	}
	
}
