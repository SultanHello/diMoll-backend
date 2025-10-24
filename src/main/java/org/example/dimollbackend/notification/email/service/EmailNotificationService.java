package org.example.dimollbackend.notification.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNewTrackEmail(String to, String artistName, String trackTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("🎶 Новый трек от " + artistName);
        message.setText("Музыкант " + artistName + " выложил новый трек: \"" + trackTitle + "\"\n\nПослушать можно на сайте 🎧");
        mailSender.send(message);
    }
}
//constanse
//universal