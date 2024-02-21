package com.example;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.MalformedURLException;
// import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;


import java.io.BufferedReader;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
public class utils {

    public static List<String> pinterest(String query) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://id.pinterest.com/search/pins/?autologin=true&q=" + query))
                .header("cookie","_auth=1; _b=\"AVna7S1p7l1C5I9u0+nR3YzijpvXOPc6d09SyCzO+DcwpersQH36SmGiYfymBKhZcGg=\"; _pinterest_sess=TWc9PSZHamJOZ0JobUFiSEpSN3Z4a2NsMk9wZ3gxL1NSc2k2NkFLaUw5bVY5cXR5alZHR0gxY2h2MVZDZlNQalNpUUJFRVR5L3NlYy9JZkthekp3bHo5bXFuaFZzVHJFMnkrR3lTbm56U3YvQXBBTW96VUgzVUhuK1Z4VURGKzczUi9hNHdDeTJ5Y2pBTmxhc2owZ2hkSGlDemtUSnYvVXh5dDNkaDN3TjZCTk8ycTdHRHVsOFg2b2NQWCtpOWxqeDNjNkk3cS85MkhhSklSb0hwTnZvZVFyZmJEUllwbG9UVnpCYVNTRzZxOXNJcmduOVc4aURtM3NtRFo3STlmWjJvSjlWTU5ITzg0VUg1NGhOTEZzME9SNFNhVWJRWjRJK3pGMFA4Q3UvcHBnWHdaYXZpa2FUNkx6Z3RNQjEzTFJEOHZoaHRvazc1c1UrYlRuUmdKcDg3ZEY4cjNtZlBLRTRBZjNYK0lPTXZJTzQ5dU8ybDdVS015bWJKT0tjTWYyRlBzclpiamdsNmtpeUZnRjlwVGJXUmdOMXdTUkFHRWloVjBMR0JlTE5YcmhxVHdoNzFHbDZ0YmFHZ1VLQXU1QnpkM1FqUTNMTnhYb3VKeDVGbnhNSkdkNXFSMXQybjRGL3pyZXRLR0ZTc0xHZ0JvbTJCNnAzQzE0cW1WTndIK0trY05HV1gxS09NRktadnFCSDR2YzBoWmRiUGZiWXFQNjcwWmZhaDZQRm1UbzNxc21pV1p5WDlabm1UWGQzanc1SGlrZXB1bDVDWXQvUis3elN2SVFDbm1DSVE5Z0d4YW1sa2hsSkZJb1h0MTFpck5BdDR0d0lZOW1Pa2RDVzNySWpXWmUwOUFhQmFSVUpaOFQ3WlhOQldNMkExeDIvMjZHeXdnNjdMYWdiQUhUSEFBUlhUVTdBMThRRmh1ekJMYWZ2YTJkNlg0cmFCdnU2WEpwcXlPOVZYcGNhNkZDd051S3lGZmo0eHV0ZE42NW8xRm5aRWpoQnNKNnNlSGFad1MzOHNkdWtER0xQTFN5Z3lmRERsZnZWWE5CZEJneVRlMDd2VmNPMjloK0g5eCswZUVJTS9CRkFweHc5RUh6K1JocGN6clc1JmZtL3JhRE1sc0NMTFlpMVErRGtPcllvTGdldz0=; _ir=0")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Document doc = Jsoup.parse(response.body());
        Elements images = doc.select("div > a img");
        List<String> result = new ArrayList<>();

        for (Element img : images) {
            String src = img.attr("src");
            if (src != null && !src.isEmpty()) {
                result.add(src.replace("/236x/", "/736x/"));
            }
        }

        return result;
    }

    public static String normalizeYouTubeURL(String url) {
        String videoId = null;

        // Check if URL is the short youtu.be format
        if (url.contains("youtu.be")) {
            videoId = url.substring(url.lastIndexOf("/") + 1);
        }
        // Check for the standard YouTube watch URL
        else if (url.contains("youtube.com/watch?v=")) {
            videoId = url.substring(url.indexOf("v=") + 2);
            int ampersandPosition = videoId.indexOf("&");
            if (ampersandPosition != -1) {
                videoId = videoId.substring(0, ampersandPosition);
            }
        }
        // Handle YouTube Shorts URL
        else if (url.contains("youtube.com/shorts/")) {
            videoId = url.substring(url.lastIndexOf("/") + 1);
        }
        // Add more conditions here if needed to handle other types of YouTube URLs

        // Return the normalized URL, or null if no video ID was found
        return videoId != null ? "https://www.youtube.com/watch?v=" + videoId : null;
    }
    /**
     * Searches YouTube for videos matching the query and returns the first video link.
     * 
     * @param query The search query.
     * @return The YouTube URL of the first search result.
     */
    public static String YtSearch(String query){
        String youtubeUrl = "https://www.youtube.com/watch?v=";
        String searchCommand = "yt-dlp ytsearch1:" + query + " --get-id --no-warnings";

        try {
            Process process = Runtime.getRuntime().exec(searchCommand);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> videoIds = new ArrayList<>();
            
            while ((line = reader.readLine()) != null) {
                videoIds.add(line.trim());
            }
            
            process.waitFor();
            if (!videoIds.isEmpty()) {
                return youtubeUrl + videoIds.get(0);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "No results found for: " + query;
    } 


    public String fetchUdemyCoursesData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://free-edu.onrender.com/api/coupons/json"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null; // Return null in case of any error
        }
    }

    public static boolean isUrl(String urlString) {
        try {
            new URL(urlString); // Attempt to create a URL object
            return true; // If successful, return true
        } catch (MalformedURLException e) {
            return false; // If a MalformedURLException is thrown, return false
        }
    }




    public static String escapeMarkdownV2(String text) {
        // Escapes special characters for MarkdownV2 formatting
        return text.replaceAll("([_\\*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }

    public static Map<String, Object> fetchInstagramData(String instagramUrl) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> urlsList = new ArrayList<>();
        String apiUrl = "https://weeb-api.vercel.app/insta?url=" + instagramUrl;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());

            // Extracting required fields
            String username = jsonObject.getString("username");
            String name = jsonObject.getString("name");
            String caption = jsonObject.getString("caption");
            JSONArray urls = jsonObject.getJSONArray("urls");

            for (int i = 0; i < urls.length(); i++) {
                JSONObject urlObject = urls.getJSONObject(i);
                String type = urlObject.getString("type");
                String url = urlObject.getString("url");
                Map<String, String> urlInfo = new HashMap<>();
                urlInfo.put("type", type);
                urlInfo.put("url", url);
                urlsList.add(urlInfo);
            }

            // Putting extracted data into the result map
            result.put("username", username);
            result.put("name", name);
            result.put("caption", caption);
            result.put("urls", urlsList);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


}
