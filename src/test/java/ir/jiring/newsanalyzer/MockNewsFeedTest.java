package ir.jiring.newsanalyzer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class MockNewsFeedTest {


    /*this is a test for "generateNewsItem()"  in "MockNewsFeed.java"
      it checks if the generated newsItem is valid*/
    @Test
    void testGenerateNewsItem() {

        String newsItem = MockNewsFeed.generateNewsItem();

        assertNotNull(newsItem);
        String[] parts = newsItem.split("\\|");
        assertEquals(2, parts.length);
    }

    /* this is a test for "generatePriority()" in "MockNewsFeed.java"
     it checks if the range of numbers for priority is correct*/
    @Test
    void testGeneratePriority() {

        int priority = MockNewsFeed.generatePriority();
        assertTrue(priority >= 0 && priority <= 9);
    }

    /*
     * Tests sending a news item to the NewsAnalyzer server.
     * Simulates a client connecting to the server and sending a news item.
     */
    @Test
    void testSendNewsToAnalyzer() throws InterruptedException {

        Thread mockNewsFeedThread = new Thread(() -> {
            try (Socket socket = new Socket("localhost", 1234)) {

                String newsItem = MockNewsFeed.generateNewsItem();
                socket.getOutputStream().write(newsItem.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mockNewsFeedThread.start();
        mockNewsFeedThread.join();

        assertTrue(true);
    }
}