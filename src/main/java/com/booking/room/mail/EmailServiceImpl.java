package com.booking.room.mail;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.booking.room.controller.RoomReservationController;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {

    private static final String NOREPLY_ADDRESS = "enrique.gutierrez.blancarte@gmail.com";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private AmazonS3 amazonS3Client;

    @Autowired
    private SimpleMailMessage template;

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);


    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to,
                                               String subject,
                                               String... templateModel) {
        String text = String.format(template.getText(), templateModel);
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            // pass 'true' to the constructor to create a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(NOREPLY_ADDRESS);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            S3Object s3object = amazonS3Client.getObject("s3roomreservation", "CartaMotivos.docx");
            S3ObjectInputStream inputStream = s3object.getObjectContent();

            File s3File = null;
            try {
                s3File = new File("."+File.separator+"CartaMotivos.docx");
                FileUtils.copyInputStreamToFile(inputStream, s3File);
            } catch (IOException e) {
                 LOG.error("Cannot get object from s3! "+ e.getMessage());
            }

            if(Objects.nonNull(s3File)) {
                LOG.info("Adding s3 object to mail!");
                FileSystemResource file = new FileSystemResource(s3File);
                helper.addAttachment("List of reservations.docx", file);
            }

            emailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}