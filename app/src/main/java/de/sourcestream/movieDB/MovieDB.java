package de.sourcestream.movieDB;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class MovieDB extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    public static final String url = "https://api.themoviedb.org/3/";
    public static final String key = "a3fa12d9b56fec28eabaa5959ac69e04";
    public static final String imageUrl = "https://image.tmdb.org/t/p/";

    public static final String trailerImageUrl = "http://i1.ytimg.com/vi/";
    public static final String youtube = "https://www.youtube.com/watch?v=";
    public static final String appId = "95a38b92c5cb4bbfd779c0e2fcaef5a6";
    public static final String analyticsKey = "yourGoogleAnalyticsKey";

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(analyticsKey);
    }

    public Tracker getTracker() {
        return tracker;
    }
}