package com.hotel.controller;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
public static void main(String ContactReceiver, String ContactTitle, String ContactContent) {
	Properties props = new Properties();
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.port", "587");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	
	Session session = Session.getInstance(props, new Authenticator() {
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("eenql0529@gmail.com", "imik bqcu lipc sdtm");
		}
	});
	
	String receiver = ContactReceiver; // 메일 받을 주소
	String title = ContactTitle;
	String content = ContactContent;
	Message message = new MimeMessage(session);
	try {
		message.setFrom(new InternetAddress("eenql0529@gmail.com", "관리자", "utf-8"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
		message.setSubject(title);
		message.setContent(content, "text/html; charset=utf-8");

		Transport.send(message);
		System.out.println("메일보내기 성공!");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}