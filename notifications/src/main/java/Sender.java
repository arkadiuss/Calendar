import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Sender {
    private final static Logger logger = LoggerFactory.getLogger(Sender.class);
    private final String apiKey;
    private final Email hostEmail;

    public Sender(String apiKey, Email hostEmail) {
        this.apiKey = apiKey;
        this.hostEmail = hostEmail;
    }

    void sendNotification(Email receiverEmail, String emailContent) {
        String subject = "Reminder";
        Content content = new Content("text/plain", emailContent);
        Mail mail = new Mail(hostEmail, subject, receiverEmail, content);

        SendGrid sg = new SendGrid(apiKey);

        try {
            Request request = createRequest(mail);
            sg.api(request);
            logger.info(String.format("Message: %s \n was sent to %s successfully", emailContent, receiverEmail.getEmail()));
        } catch (IOException e) {
            logger.error("Couldn't send notification to " + receiverEmail.getEmail(), e);
        }
    }

    private Request createRequest(Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.buildPretty());
        return request;
    }
}
