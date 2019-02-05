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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.DateTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.github.wasiqb.coteafs.config.loader.ConfigLoader;
import com.github.wasiqb.coteafs.mail.config.MailSetting;
import com.github.wasiqb.coteafs.mail.enums.MailFlag;

/**
 * @author wasiqb
 * @since Feb 2, 2019
 */
public class Mailer {
	private static final Logger log = LogManager.getLogger (Mailer.class);

	private boolean					connected;
	private final List <SearchTerm>	filters;
	private Folder					folder;
	private Session					session;
	private final MailSetting		setting;
	private Store					store;

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 */
	public Mailer () {
		this.connected = false;
		this.filters = new ArrayList <> ();
		this.setting = ConfigLoader.settings ()
			.withKey ("coteafs.mail.config")
			.withDefault ("/mail-config.yaml")
			.load (MailSetting.class);
		withFlag (null, true);
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param recipient
	 * @return mailer
	 */
	public Mailer forRecipientBCC (final String recipient) {
		return forRecipient (recipient, RecipientType.BCC);
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param recipient
	 * @return mailer
	 */
	public Mailer forRecipientCC (final String recipient) {
		return forRecipient (recipient, RecipientType.CC);
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param recipient
	 * @return mailer
	 */
	public Mailer forRecipientTo (final String recipient) {
		return forRecipient (recipient, RecipientType.TO);
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param sender
	 * @return mailer
	 */
	public Mailer forSender (final String sender) {
		if (!StringUtils.isEmpty (sender)) {
			final FromStringTerm fromTerm = new FromStringTerm (sender);
			this.filters.add (fromTerm);
		}
		return this;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param date
	 * @return mailer
	 */
	public Mailer fromDate (final DateTime date) {
		if (date != null) {
			final DateTerm dateTerm = new ReceivedDateTerm (ComparisonTerm.EQ, date.toDate ());
			this.filters.add (dateTerm);
		}
		return this;
	}

	/**
	 * Searches the inbox for unseen messages returns results filtered by supplied parameters.
	 * Please note that the start date doesn't seem to be as reliable as expected. It is STRONGLY
	 * advised to do a check against the actual sent date of the message.
	 *
	 * @return an array of messages
	 * @author wasiqb
	 * @since Feb 2, 2019
	 */
	public List <MailMessage> search () {
		if (!this.connected) {
			login ();
		}
		final SearchTerm [] terms = new SearchTerm [this.filters.size ()];
		this.filters.toArray (terms);
		try {
			final Message [] mails = this.folder.search (new AndTerm (terms));
			if (mails.length == 0) {
				log.warn ("No mails found for the selected criteria...");
				return null;
			}
			return Arrays.asList (mails)
				.stream ()
				.map (m -> new MailMessage (m))
				.collect (Collectors.toList ());
		}
		catch (final MessagingException e) {
			log.error ("FAILED while searching Mail server...");
			log.catching (e);
			return null;
		}
		finally {
			logout ();
		}
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param flag
	 * @param value
	 * @return mailer
	 */
	public Mailer withFlag (final MailFlag flag, final boolean value) {
		if (flag != null) {
			this.filters.add (new FlagTerm (new Flags (flag.getFlag ()), value));
		}
		else {
			this.filters.add (new FlagTerm (new Flags (Flags.Flag.SEEN), false));
		}
		return this;
	}

	/**
	 * @author wasiqb
	 * @since Feb 2, 2019
	 * @param subject
	 * @return mailer
	 */
	public Mailer withSubject (final String subject) {
		if (!StringUtils.isEmpty (subject)) {
			final SubjectTerm subjectTerm = new SubjectTerm (subject);
			this.filters.add (subjectTerm);
		}
		return this;
	}

	private Mailer forRecipient (final String recipient, final RecipientType type) {
		if (!StringUtils.isEmpty (recipient)) {
			final RecipientStringTerm toTerm = new RecipientStringTerm (type, recipient);
			this.filters.add (toTerm);
		}
		return this;
	}

	private void login () {
		final URLName url = new URLName (this.setting.getProtocol (), this.setting.getHost (),
			this.setting.getPort (), this.setting.getFolder (), this.setting.getUserId (),
			this.setting.getPasscode ());
		final Properties prop = new Properties ();
		prop.setProperty ("mail.imap.partialfetch", "false");
		prop.setProperty ("mail.store.protocol", "imaps");
		prop.setProperty ("mail.imap.ssl.enable", "true");
		this.session = Session.getInstance (prop, null);
		try {
			this.store = this.session.getStore (url);
			this.store.connect (this.setting.getHost (), this.setting.getPort (),
				this.setting.getUserId (), this.setting.getPasscode ());
			this.folder = this.store.getFolder (url);
			this.folder.open (Folder.READ_WRITE);
		}
		catch (final MessagingException e) {
			log.error ("FAILED to connect to Mail server...");
			log.catching (e);
			this.connected = false;
			return;
		}
		this.connected = this.store.isConnected ();
	}

	private void logout () {
		try {
			if (this.connected) {
				this.filters.clear ();
				this.folder.close ();
				this.store.close ();
				this.store = null;
				this.session = null;
				this.connected = false;
			}
		}
		catch (final MessagingException e) {
			log.error ("FAILED to logout to Mail server...");
			log.catching (e);
		}
	}
}