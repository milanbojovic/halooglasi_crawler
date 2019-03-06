package com.halooglasi.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Smtp2GoEmailClient {

	String username;
	String password;
	Properties props;
	Session session;

	public Smtp2GoEmailClient(String username, String password) {
		this.username = username;
		this.password = password;

		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "mail.smtp2go.com");
		props.put("mail.smtp.port", "2525");

		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("milan.bojovic@braintribe.com", "9okUD5F1oKSL");
			}
		});
	}

	void sendEmail(List<Advertisement> advertisements) {
		String content = null;
		try {
			for (Advertisement advertisement : advertisements) {
				StringBuilder sb = new StringBuilder();
				sb.append(advertisement.toString());
				sb.append("\n");getClass();
				content = sb.toString();
			}
			if (content == null) return;
			
			Message message = new MimeMessage(session);
			Multipart mp = new MimeMultipart("alternative");
			BodyPart textmessage = new MimeBodyPart();
			textmessage.setText("Novi oglasi na za vas kriterijum: ");
			BodyPart htmlmessage = new MimeBodyPart();
			htmlmessage.setContent("Novi oglasi pronadjeni za vas kriterijum: " + content, "text/html");
			mp.addBodyPart(textmessage);
			mp.addBodyPart(htmlmessage);
			message.setFrom(new InternetAddress("digitalni-radnik@mbsoftware-solutions.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("milanbojovic@gmail.com"));
			message.setSubject("Novi oglasi na za vas kriterijum");
			message.setContent(mp);
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}