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
package name.raev.kaloyan.kindle.chitanka.utils;

import java.io.IOException;

public class KindleModel {

	/**
	 * The serial number prefix that determines the model.
	 */
	private String snPrefix;

	private KindleModel(String snPrefix) {
		this.snPrefix = snPrefix;
	}

	public static KindleModel getModel() throws IOException {
		String serialNumber = SerialNumber.getSerialNumber();
		int prefixLength = serialNumber.startsWith("G0") ? 6 : 4;
		String snPrefix = serialNumber.substring(0, prefixLength);
		return new KindleModel(snPrefix);
	}

	/**
	 * Returns the serial number prefix that determines the model.
	 * 
	 * @return the serial number prefix
	 */
	public String getSerialNumberPrefix() {
		return snPrefix;
	}

	/**
	 * Returns if the model has unrestricted 3G network support.
	 * 
	 * <p>
	 * This should be the models Kindle Keyboard WiFi + 3G (k3g and k3gb).
	 * </p>
	 * 
	 * @return <code>true</code> if the model has unrestricted 3G network
	 *         support, <code>false</code> otherwise
	 */
	public boolean hasUnrestricted3G() {
		return "B006".equals(snPrefix) || "B00A".equals(snPrefix);
	}

}
