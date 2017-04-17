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

import java.io.IOException;
import java.util.Stack;

import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;

import name.raev.kaloyan.kindle.chitanka.screen.Screen;
import name.raev.kaloyan.kindle.chitanka.screen.ScreenManager;
import name.raev.kaloyan.kindle.chitanka.utils.ProgressIndicator;

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
		Connectivity connectivity = ContextManager.getContext().getConnectivity();
		connectivity.submitSingleAttemptConnectivityRequest(new ConnectivityHandler() {
			public void disabled(NetworkDisabledDetails details) throws InterruptedException {
				handleNetworkError();
				display(url);
			}

			public void connected() throws InterruptedException {
				Screen screen = ScreenManager.createScreen(url);
				try {
					screen.display();
				} catch (IOException e) {
					handleNetworkError();
					display(url);
				}

				ScreenManager.setCurrentScreen(screen);
			}
		}, true);
	}

	public void downloadBook(final String href) {
		ProgressIndicator.start("Книгата се изтегля");
		Connectivity connectivity = ContextManager.getContext().getConnectivity();
		connectivity.submitSingleAttemptConnectivityRequest(new ConnectivityHandler() {
			public void disabled(NetworkDisabledDetails details) throws InterruptedException {
				handleNetworkError();
				downloadBook(href);
			}

			public void connected() throws InterruptedException {
				try {
					Utils.downloadMobiFromEpubUrl(href);
					Utils.rescanDocuments();
				} catch (IOException e) {
					handleNetworkError();
					downloadBook(href);
					return;
				} finally {
					ProgressIndicator.stop();
				}

				// show info message
				String title = "Книгата е изтеглена";
				String message = "Ще намерите книгата в началото на главния екран на Kindle. Натиснете бутона Home, за да преминете към главния екран.";
				DialogManager.displayDialog(message, title);
			}
		}, true);

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
	
	public void handleNetworkError() throws InterruptedException {
		ProgressIndicator.stop();
		String title = "Неуспешно свързване";
		String message = "Приложението не може да се свърже с мрежата. Уверете се, че сте в обхвата на безжична мрежа.\n\nЗатворете това съобщение, за да опитате отново.";
		DialogManager.displayDialog(message, title);
	}

}
