/**
 * Copyright (c) 2017-2020, Wasiq Bhamla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wasiqb.coteafs.mail.config;

import static org.apache.commons.text.StringSubstitutor.replaceSystemProperties;

/**
 * @author wasiqb
 * @since Feb 2, 2019
 */
public class MailSetting {
	private String	folder;
	private String	host;
	private String	passcode;
	private int		port;
	private String	protocol;
	private String	userId;

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the folder
	 */
	public String getFolder () {
		return this.folder;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the host
	 */
	public String getHost () {
		return this.host;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the passcode
	 */
	public String getPasscode () {
		return this.passcode.startsWith ("$")	? replaceSystemProperties (this.passcode)
												: this.passcode;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the port
	 */
	public int getPort () {
		return this.port;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the protocol
	 */
	public String getProtocol () {
		return this.protocol;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the userId
	 */
	public String getUserId () {
		return this.userId.startsWith ("$") ? replaceSystemProperties (this.userId) : this.userId;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param folder
	 *            the folder to set
	 */
	public void setFolder (final String folder) {
		this.folder = folder;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param host
	 *            the host to set
	 */
	public void setHost (final String host) {
		this.host = host;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param passcode
	 *            the passcode to set
	 */
	public void setPasscode (final String passcode) {
		this.passcode = passcode;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param port
	 *            the port to set
	 */
	public void setPort (final int port) {
		this.port = port;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol (final String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId (final String userId) {
		this.userId = userId;
	}
}