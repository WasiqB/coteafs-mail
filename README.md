# coteafs-mail

<h1 align="center">A small mail utility to search Emails from any Mail providers using IMAP protocol.</h1>

[![BCH compliance](https://bettercodehub.com/edge/badge/WasiqB/coteafs-mail?branch=master)](https://bettercodehub.com/results/WasiqB/coteafs-mail)

## Usage

Add following dependency to your `pom.xml` in order to use this library.

```xml
<dependency>
  <groupId>com.github.wasiqb.coteafs</groupId>
  <artifactId>appium</artifactId>
  <version>3.0.0</version>
</dependency>
```

## Mail Settings

Following is an example of mail setting file,

> `src/test/resources/mail-config.yaml`

```yaml
folder: INBOX
protocol: imaps
host: imap.gmail.com
port: 993
user_id: ${user}
passcode: ${pass}
```