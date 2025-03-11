package ir.jiring.newsanalyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// NewsItem represents a news headline and its associated priority.
class NewsItem {
    String headline;
    int priority;

    public NewsItem(String headline, int priority) {
        this.headline = headline;
        this.priority = priority;
    }
}


public class NewsAnalyzer {
    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList("up", "rise", "good", "success", "high"));
    private static final Logger log = LoggerFactory.getLogger(NewsAnalyzer.class);
    public static final List<NewsItem> recentNews = new ArrayList<>();
    public static int positiveNewsCount = 0;



    // Processes and validates a news item, adds it to recent news if positive.
    public static void processNewsItem(String newsItem) {

        String[] parts = newsItem.split("\\|");
        if (parts.length != 2) {
            log.warn("Invalid news format: {}", newsItem);
            return;
        }

        String headline = parts[0].trim();
        int priority;
        try {
            priority = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid priority value: {}", parts[1]);
            return;
        }

        if (isPositiveHeadline(headline)) {
            recentNews.add(new NewsItem(headline, priority));
            positiveNewsCount++;
        }
    }


    // Checks if a headline has more positive words than negative ones.
    public static boolean isPositiveHeadline(String headline) {
        String[] words = headline.split(" ");
        long positiveWordCount = Arrays.stream(words).filter(POSITIVE_WORDS::contains).count();
        return positiveWordCount > words.length / 2;
    }
}
