package org.sid.travelplanner;

import org.junit.jupiter.api.Test;
import org.sid.travelplanner.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
        // Arrange
        Context context = new Context();
        context.setVariable("userName", "Test User");
        context.setVariable("tripName", "Voyage à Paris");
        context.setVariable("activityName", "Visite de la Tour Eiffel");
        context.setVariable("activityDescription", "Visite guidée avec accès prioritaire");
        context.setVariable("activityDate", "2024-06-01");
        context.setVariable("link", "http://localhost:8080/trips/1/activities/1");

        // Act & Assert
        assertDoesNotThrow(() -> {
            emailService.sendNotificationEmail(
                    "your-test@email.com",
                    "Test Email - Nouvelle Activité",
                    "email/new-activity",
                    context
            );
        });
    }

    @Test
    void testSendTripInvitation() {
        // Arrange
        Context context = new Context();
        context.setVariable("userName", "John Doe");
        context.setVariable("inviterName", "Marie Dupont");
        context.setVariable("tripName", "Voyage à Rome");

        // Act & Assert
        assertDoesNotThrow(() -> {
            emailService.sendNotificationEmail(
                    "your-test@email.com",
                    "Test Email - Invitation Voyage",
                    "email/trip-invitation",
                    context
            );
        });
    }

    @Test
    void testSendExpenseNotification() {
        // Arrange
        Context context = new Context();
        context.setVariable("userName", "John Doe");
        context.setVariable("tripName", "Voyage à Rome");
        context.setVariable("expenseDescription", "Dîner au restaurant");
        context.setVariable("amount", "50.00 €");
        context.setVariable("paidBy", "Marie Dupont");
        context.setVariable("userShare", "25.00 €");

        // Act & Assert
        assertDoesNotThrow(() -> {
            emailService.sendNotificationEmail(
                    "your-test@email.com",
                    "Test Email - Nouvelle Dépense",
                    "email/new-expense",
                    context
            );
        });
    }
}
