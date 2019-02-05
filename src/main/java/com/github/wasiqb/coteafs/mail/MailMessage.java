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
package com.github.wasiqb.coteafs.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author wasiqb
 * @since Feb 2, 2019
 */
public class MailMessage {
	private static final Logger log = LogManager.getLogger (MailMessage.class);

	private Object			content;
	private Date			receivedDate;
	private List <String>	recipients;
	private List <String>	senders;
	private String			subject;

	/**
	 * @author wasiqb
	 * @param message
	 * @since Feb 2, 2019
	 */
	public MailMessage (final Message message) {
		try {
			this.recipients = new ArrayList <> ();
			this.senders = new ArrayList <> ();
			setContent (message.getContent ());
			setReceivedDate (message.getReceivedDate ());
			setRecipients (message.getRecipients (RecipientType.TO));
			setRecipients (message.getRecipients (RecipientType.CC));
			setRecipients (message.getRecipients (RecipientType.BCC));
			setSenders (message.getFrom ());
			setSubject (message.getSubject ());
		}
		catch (IOException | MessagingException e) {
			log.error ("FAILED while reading Mail message...");
			log.catching (e);
		}
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the content
	 */
	public Object getContent () {
		return this.content;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the receivedDate
	 */
	public Date getReceivedDate () {
		return this.receivedDate;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the recipients
	 */
	public List <String> getRecipients () {
		return this.recipients;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the senders
	 */
	public List <String> getSenders () {
		return this.senders;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @return the subject
	 */
	public String getSubject () {
		return this.subject;
	}

	private void setContent (final Object content) {
		this.content = content;
	}

	private void setReceivedDate (final Date date) {
		this.receivedDate = date;
	}

	private void setRecipients (final Address [] addresses) {
		if (addresses != null) {
			for (final Address address : addresses) {
				this.recipients.add (address.toString ());
			}
		}
	}

	private void setSenders (final Address [] addresses) {
		if (addresses != null) {
			for (final Address address : addresses) {
				this.senders.add (address.toString ());
			}
		}
	}

	private void setSubject (final String subject) {
		this.subject = subject;
	}
}