package com.medianet.spamDetection.utils;

import jakarta.mail.*;
import jakarta.mail.search.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailReader {

    @Value("${mail.imap.host}")
    private String host;

    @Value("${mail.imap.username}")
    private String username;

    @Value("${mail.imap.password}")
    private String password;

    @Value("${mail.imap.port}")
    private String port;

    public List<String> checkInbox( )  {
        try{
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getInstance(props);
        Store store = session.getStore();
        store.connect(host, username, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);


        Message[] messages = inbox.search(getSearchTerm());
        List<String> emailContents = new ArrayList<>();
        for (Message message : messages) {
            String subject = message.getSubject();
            String content = getTextFromMessage(message);
            emailContents.add(content);
            // Call your spam detection API here with 'content'
            System.out.println("New Email: " + subject);
            System.out.println("Content: " + content);

        }
        inbox.close(false);
        store.close();

        return emailContents;
        }
        catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static SearchTerm getSearchTerm() {

        //get only unread messages since midnight
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayMidnight = cal.getTime();

        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GE, todayMidnight);

        // Combine with unread flag search
        SearchTerm unread = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

        SearchTerm andTerm = new AndTerm(newerThan, unread);
        return andTerm;
    }

    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            // Optionally strip HTML tags here or return raw HTML
            String html = (String) message.getContent();
            return org.jsoup.Jsoup.parse(html).text(); // convert HTML to plain text using jsoup
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            String text = null;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    return part.getContent().toString();
                } else if (part.isMimeType("text/html") && text == null) {
                    String html = (String) part.getContent();
                    text = org.jsoup.Jsoup.parse(html).text();
                }
            }
            return text != null ? text : "";
        }
        return "";
    }

}
