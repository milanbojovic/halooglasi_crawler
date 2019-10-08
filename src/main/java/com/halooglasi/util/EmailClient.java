package com.halooglasi.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class EmailClient {

    String from;
    String pass;
    String host;

    Properties props;

    public EmailClient(final String username, final String password) {
        this.from = username;
        this.pass = password;

        String host = "smtp.gmail.com";

        props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    public void sendEmail(String[] to, String subject, List<Advertisement> advertisements) {
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        String content = null;

        if(advertisements.size() == 0) return;

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            StringBuilder sb = new StringBuilder();
            for (Advertisement advertisement : advertisements) {
                sb.append(advertisement.toString());
                sb.append("\n");
            }
            content = sb.toString();

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(content);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}