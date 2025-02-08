package com.pharmacie.Email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import java.security.SecureRandom;

import com.pharmacie.models.User;

public class EmailService {

    
    public static void sendEmail(User user, String password) {
        final String senderEmail = "amouzoufabio@gmail.com";
        final String senderPassword = "dxjc djdn pbhn anis ";

        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Pour Gmail
        props.put("mail.smtp.port", "587");

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Construction du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Recupetation de mot de passe pharmacie");
            message.setText("votre nouveau mot de passe est: "+password);

            // Envoi du message
            Transport.send(message);
            System.out.println("Email envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public static String generatePassword(int length) {
        // Ensemble de caractères possibles pour le mot de passe
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Génération aléatoire des caractères
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}
