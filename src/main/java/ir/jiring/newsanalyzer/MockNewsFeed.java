package ir.jiring.newsanalyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/* MockNewsFeed generates random news items and sends them to a server at a specified host and port.
 Each news item consists of 3 to 5 random words from a predefined list and a priority level (0-9),
 with higher priority values being less likely to be generated.
 The news items are sent at intervals determined by the 'newsFrequency' system property.*/
public class MockNewsFeed {
    private static final List<String> WORDS = Arrays.asList("up", "down", "rise", "fall", "good",
            "bad", "success", "failure", "high", "low");
    private static final Random RANDOM = new Random();
    private static final int PORT = 1234;
    private static final String HOST = "localhost";
    private static final Logger log = LoggerFactory.getLogger(MockNewsFeed.class);


    public static void main(String[] args) {
        int newsFrequency = Integer.parseInt(System.getProperty("newsFrequency", "2000"));

        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            while (true) {
                String newsItem = generateNewsItem();
                writer.println(newsItem);
                log.info("Sent: {}", newsItem);
                Thread.sleep(newsFrequency);
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error occurred", e);
        }
    }

    //    this method generates random news from 3 to 5 specific words also each news has a random priority from 0 to 9
    public static String generateNewsItem() {
        int numWords = RANDOM.nextInt(3) + 3;
        List<String> headlineWords = new ArrayList<>();
        for (int i = 0; i < numWords; i++) {
            headlineWords.add(WORDS.get(RANDOM.nextInt(WORDS.size())));
        }
        String headLine = String.join(" ", headlineWords);

        int priority = generatePriority();
        return headLine + "|" + priority;
    }


    /*this method generates the priority of the news from 1 to 9 . but higher priority got lower chance to generate.
      priority : 0 to 2 (70% generation chance)
      priority : 3 to 5 (20% generation chance)
      priority : 6 to 9 (10% generation chance)*/
    public static int generatePriority() {
        int randomValue = RANDOM.nextInt(10);

        if (randomValue < 7) {
            return RANDOM.nextInt(3);
        } else if (randomValue < 9) {
            return RANDOM.nextInt(3) + 3;
        } else {
            return RANDOM.nextInt(4) + 6;
        }
    }
}
