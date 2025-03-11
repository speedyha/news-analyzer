package ir.jiring.newsanalyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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
    static final List<NewsItem> recentNews = new ArrayList<>();
    public static int positiveNewsCount = 0;


    /* Handles incoming client connection, reads news items from the client,
       and processes each news item line by line.*/
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processNewsItem(line);
            }
        } catch (IOException e) {
            log.error("Error handling client connection for socket: {}", clientSocket.getRemoteSocketAddress(), e);
        }
    }

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

    /* Displays the count of positive news in the last 10 seconds,
       logs the top 3 highest priority positive news, and resets the news data.*/
    public static void displaySummary() {
        log.info("Positive news in the last 10 seconds: {}", positiveNewsCount);

        recentNews.sort((a, b) -> Integer.compare(b.priority, a.priority));
        List<NewsItem> topNewsList = recentNews.subList(0, Math.min(3, recentNews.size()));

        log.info("Top 3 highest priority positive news:");
        for (int i = 0; i < 3; i++) {
            if (i < topNewsList.size()) {
                NewsItem news = topNewsList.get(i);
                log.info("- {} (Priority: {})", news.headline, news.priority);
            } else {
                log.info("- No more positive news");
            }
        }

        recentNews.clear();
        positiveNewsCount = 0;
    }


}
