package com.hrelix.app.leave;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(LeaveRequest leave) {
        try {
            // Create a MimeMessage
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set email details
            helper.setFrom("blufindesign@gmail.com");
//            helper.setTo(leave.getEmployee().getEmail());
            helper.setTo("abhijeetbhardwaj53@gmail.com");
            helper.setSubject("New Leave Request Notification");

            // Load email template
            try (InputStream inputStream = Objects.requireNonNull(
                    LeaveRequestController.class.getResourceAsStream("/templates/email.html"),
                    "Email template not found!")) {

                String emailContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                // Determine header color based on leave status
                String headerColor = switch (leave.getStatus()) {
                    case PENDING -> "orange";
                    case REJECTED -> "red";
                    case APPROVED -> "green";
                };

                // Replace placeholders with actual values
                emailContent = emailContent
                        .replace("{{employee_name}}", leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                        .replace("{{leave_type}}", leave.getLeaveType().name())
                        .replace("{{leave_status}}", leave.getStatus().name())
                        .replace("{{leave_reason}}", leave.getReason())
                        .replace("{{header_color}}", headerColor)
                        .replace("{{startDate}}", leave.getStartDate().toString())
                        .replace("{{endDate}}", leave.getEndDate().toString())
                        .replace("{{comments}}", leave.getComments() == null ? "N/A" : leave.getComments());

                // Set the email content
                helper.setText(emailContent, true);
            } catch (IOException e) {
                throw new FileNotFoundException("Unable to load email template: " + e.getMessage());
            }

            // Send the email
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
