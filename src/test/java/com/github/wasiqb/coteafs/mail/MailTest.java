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

import java.util.List;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.wasiqb.coteafs.mail.enums.MailFlag;

/**
 * @author wasiqb
 * @since Feb 4, 2019
 */
public class MailTest {
	/**
	 * @author wasiqb
	 * @since Feb 4, 2019
	 */
	@Test
	public void testMail () {
		final Mailer mail = new Mailer ();
		final List <MailMessage> message = mail.withSubject ("You have a bill due")
			.forRecipientTo ("xasysttest2@gmail.com")
			.forSender ("patientpay@patientpay.com")
			.fromDate (new DateTime (2019, 1, 23, 12, 14))
			.withFlag (MailFlag.SEEN, true)
			.search ();
		Assert.assertEquals (message.size (), 1);
		Assert.assertEquals (message.get (0)
			.getSubject (), "You have a bill due");
	}
}