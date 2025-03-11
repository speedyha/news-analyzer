package ir.jiring.newsanalyzer;

import org.junit.jupiter.api.Test;

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
}