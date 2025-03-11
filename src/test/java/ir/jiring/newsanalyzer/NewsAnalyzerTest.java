package ir.jiring.newsanalyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NewsAnalyzerTest {

    @BeforeEach
    public void setUp() {
        NewsAnalyzer.recentNews.clear();
        NewsAnalyzer.positiveNewsCount = 0;
    }

    // Tests if the headline is classified as positive or negative based on the count of positive words.
    @Test
    public void testIsPositiveHeadline_withPositiveWords() {
        String positiveHeadline = "rise down good low high";
        assertTrue(NewsAnalyzer.isPositiveHeadline(positiveHeadline));
    }
    @Test
    public void testIsPositiveHeadline_withNegativeWords() {
        String negativeHeadline = "down up failure good low";
        assertFalse(NewsAnalyzer.isPositiveHeadline(negativeHeadline));
    }


    // Tests if an invalid news format or priority value is handled correctly, ensuring no news is added.
    @Test
    public void testProcessNewsItem_invalidFormat() {
        String invalidNewsItem = "Invalid news format";
        NewsAnalyzer.processNewsItem(invalidNewsItem);

        assertEquals(0, NewsAnalyzer.positiveNewsCount);
        assertTrue(NewsAnalyzer.recentNews.isEmpty());
    }
    @Test
    public void testProcessNewsItem_invalidPriority() {
        String invalidPriorityNewsItem = " rise  good bad | invalidPriority";
        NewsAnalyzer.processNewsItem(invalidPriorityNewsItem);

        assertEquals(0, NewsAnalyzer.positiveNewsCount);
        assertTrue(NewsAnalyzer.recentNews.isEmpty());
    }

// Tests the displaySummary method to ensure it correctly handles displaying and resetting the summary after processing news items.
    @Test
    public void testDisplaySummary() {
        String newsItem1 = "rise good failure  | 5";
        String newsItem2 = "bad down up | 3";
        NewsAnalyzer.processNewsItem(newsItem1);
        NewsAnalyzer.processNewsItem(newsItem2);

        NewsAnalyzer.displaySummary();

        assertEquals(0, NewsAnalyzer.positiveNewsCount);
        assertTrue(NewsAnalyzer.recentNews.isEmpty());
    }

}
