package com.example;

import org.json.JSONML;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.core.Response;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shinichi extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        utils weeb = new utils();
        String pushName = update.getMessage().getFrom().getFirstName() + " "
                + update.getMessage().getFrom().getLastName();
        String text = update.getMessage().getText();
        String[] textArray = text.split(" ");
        // System.out.println(Arrays.toString(textArray));
        String command = text.startsWith("/") == true ? textArray[0] : "";
        boolean isCmd = command.startsWith("/") ? true : false;
        String args = isCmd ? text.replace(command, "").trim() : "";
        System.out.println(args);
        final String from = update.getMessage().getChatId().toString();
        final int fromId = update.getMessage().getMessageId();
        final boolean isUrl = weeb.isUrl(args);
        System.out.println("[Message]   " + pushName + "   " + text);

        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                if (command.equals("/start")) {
                    String helpText;
                    helpText = "👋 Hey " + pushName + ", <b>Shinichi Bot Here</b>\n";
                    helpText += "I am here to make your experience of using Telegram Bot\n\n";
                    helpText += "Creator : <a href=\"https://github.com/Edward876\">Shilly Joestar - Supratim Saha</a>\n\n";
                    helpText += "👉 <b>Commands :</b>\n";
                    helpText += "1. /help : <i>Show this help message</i>\n";
                    helpText += "2. /pinterest or /pin : <i>Gives image from Pinterest according to your query.</i>\n";
                    helpText += "3. /ytv : <i>Gives video from YouTube according to your query.</i>\n";
                    helpText += "4. /yta : <i>Gives audio from YouTube according to your query.</i>\n";
                    helpText += "5. /play : <i>Gives song according to your query.</i>\n";
                    helpText += "6. /udemy : <i>Gives available free courses from Udemy.</i>\n";
                    helpText += "7. /instagram : <i>Download Instagram reels or photos from provided Instagram link.</i>\n";
                    
                    // String NewhelpText = weeb.escapeMarkdownV2(helpText);
                    SendAnimation message = new SendAnimation();
                    message.setChatId(from);
                    message.setParseMode("HTML");
                    message.setAnimation(new InputFile("https://media.tenor.com/iMeKS77VlEgAAAAd/anime.gif"));
                    message.setCaption(helpText);
                    message.setReplyToMessageId(fromId);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (command.equals("/test")) {
                    // SendPhoto photo = new SendPhoto(from, new
                    // InputFile("https://wallpapers.com/images/hd/4k-anime-girl-a3rpqsihkx1m9r5l.webp"));
                    String url = weeb.YtSearch(args.replace(" ", "+"));
                    System.out.println(url);
                    // try {
                    // execute(photo);
                    // } catch (TelegramApiException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                }

                if (command.equals("/pinterest") || command.equals("/pin")) {
                    if(args.length() == 0) {
                        SendMessage message = new SendMessage(from, "Please provide a query to search for.\n\nExample:\n/pinterest Dragon Or /pin Dragon.");
                        message.setReplyToMessageId(fromId);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    
                    }
                    Random random = new Random();

                    try {
                        List<String> images = weeb.pinterest(args.replace(" ", "+"));
                        System.out.println(images);
                        int index = random.nextInt(images.size());
                        String randomElement = images.get(index);
                        SendPhoto photo = new SendPhoto();
                        photo.setChatId(from);
                        photo.setParseMode("MarkdownV2");
                        photo.setPhoto(new InputFile(randomElement));
                        photo.setReplyToMessageId(fromId);
                        photo.setCaption("*Query :* " + args);
                        try {
                            execute(photo);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (command.equals("/ytv")) {
                    if(isUrl == false){
                        SendMessage message = new SendMessage(from, "Please provide a valid Youtube url to download video.\n\nExample:\n/ytv https://youtu.be/lIvhg8mjaDk?si=vrrr0iLcYr3mAqjT.");
                        message.setReplyToMessageId(fromId);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    
                    }
                    String link = weeb.normalizeYouTubeURL(args);

                    String messageText = "Download started...";
                    SendMessage message = new SendMessage(from, messageText);

                    try {
                        Message response = execute(message);
                        int messageId = response.getMessageId();
                        String videoId = link.substring(link.lastIndexOf("=") + 1);
                        String outputDir = "./ytTemp/";
                        String outputVideo = outputDir + videoId + ".mp4";
                        String outputJson = outputDir + videoId + ".info.json";
                        // String outputThumbnail = outputDir + videoId + ".webp"; // This path might
                        // need adjustment based on yt-dlp's actual output
                        String outputThumbnailWebp = outputDir + videoId + ".webp"; // Original thumbnail file in webp
                                                                                    // format
                        String outputThumbnailJpg = outputDir + videoId + ".jpg"; // Target thumbnail file in jpg format

                        // Use yt-dlp to download video metadata (JSON) and thumbnail
                        ProcessBuilder metadataBuilder = new ProcessBuilder("yt-dlp", "--write-info-json",
                                "--write-thumbnail", "--skip-download", "-o", outputDir + videoId, link);
                        metadataBuilder.redirectErrorStream(true);
                        Process metadataProcess = metadataBuilder.start();
                        int metadataExitCode = metadataProcess.waitFor();
                        if (metadataExitCode != 0) {
                            throw new IOException("Failed to download metadata and thumbnail");
                        }
                        ProcessBuilder convertThumbnail = new ProcessBuilder("ffmpeg", "-i", outputThumbnailWebp,
                                outputThumbnailJpg);
                        Process convertProcess = convertThumbnail.start();
                        int convertExitCode = convertProcess.waitFor();
                        if (convertExitCode != 0) {
                            throw new IOException("Failed to convert thumbnail from webp to jpg");
                        }
                        // At this point, you should have videoId.info.json and videoId.jpg in the
                        // outputDir
                        // Implement JSON parsing to extract title and description from
                        // videoId.info.json
                        String content = new String(Files.readAllBytes(Paths.get(outputJson)));
                        JSONObject json = new JSONObject(content);
                        String title = json.getString("title");
                        String description = json.getString("description");
                        // Download video
                        ProcessBuilder videoBuilder = new ProcessBuilder("yt-dlp", "-f", "best[height<=1080]",
                                "--newline", "-o", outputVideo, link);
                        videoBuilder.redirectErrorStream(true);
                        Process videoProcess = videoBuilder.start();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(videoProcess.getInputStream()));
                        String line, progress = "";
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("%")) {
                                String newProgress = line.substring(line.indexOf("%") - 4, line.indexOf("%") + 1)
                                        .trim();
                                if (!newProgress.equals(progress)) {
                                    progress = newProgress;
                                    EditMessageText newMessage = new EditMessageText();
                                    newMessage.setChatId(from);
                                    newMessage.setMessageId(messageId);
                                    newMessage.setText("Downloading... " + progress);
                                    execute(newMessage);
                                }
                            }
                        }
                        int exitCode = videoProcess.waitFor();
                        if (exitCode == 0) {
                            DeleteMessage deleteMessage = new DeleteMessage(from, messageId);
                            execute(deleteMessage);

                            // Prepare and send the video with caption and thumbnail
                            String videoCaption = title + "\n\n" + description;
                            if (videoCaption.length() > 1024) {
                                videoCaption = videoCaption.substring(0, 1021) + "...";
                            }

                            SendVideo video = new SendVideo();
                            video.setChatId(from);
                            video.setVideo(new InputFile(new File(outputVideo)));
                            video.setCaption(videoCaption);
                            video.setReplyToMessageId(fromId);
                            video.setThumbnail(new InputFile(new File(outputThumbnailJpg)));
                            execute(video);

                            // Delete the downloaded video, thumbnail, and metadata
                            Files.deleteIfExists(Paths.get(outputVideo));
                            // Files.deleteIfExists(Paths.get(outputThumbnail));
                            Files.deleteIfExists(Paths.get(outputJson));
                            Files.deleteIfExists(Paths.get(outputThumbnailJpg));
                            Files.deleteIfExists(Paths.get(outputThumbnailWebp));
                        } else {
                            System.out.println("Download failed with exit code " + exitCode);
                        }
                    } catch (IOException | InterruptedException | TelegramApiException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                if (command.equals("/yta") || command.equals("/play")) {
                    if(args.length() == 0) {
                        SendMessage message = new SendMessage(from, "Please provide a query or valid Youtube url to search for.\n\nExample:\n/yta https://youtu.be/lIvhg8mjaDk?si=vrrr0iLcYr3mAqjT Or /play Fiction By Sumika.");
                        message.setReplyToMessageId(fromId);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    
                    } else {
                    
                    String input = args.trim();
                    String youtubeUrl;

                    try {
                        try {
                            new URL(input);
                            youtubeUrl = weeb.normalizeYouTubeURL(input); // Assuming input is a direct URL
                        } catch (MalformedURLException e) {
                            // Input is not a URL, treat it as a search query
                            youtubeUrl = weeb.YtSearch(input.replace(" ", "+"));
                        }
                        SendMessage message = new SendMessage(from, "Audio download started...");

                        Message response = execute(message);
                        int messageId = response.getMessageId();
                        String videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("=") + 1);
                        String outputDir = "./ytTemp/";
                        String outputJson = outputDir + videoId + ".info.json"; // Metadata output file
                        // String outputThumbnailWebp = outputDir + videoId + ".webp";

                        // Download metadata and thumbnail
                        ProcessBuilder metadataBuilder = new ProcessBuilder("yt-dlp", "--write-info-json",
                                "--write-thumbnail", "--skip-download", "-o", outputDir + videoId, youtubeUrl);
                        metadataBuilder.redirectErrorStream(true);
                        Process metadataProcess = metadataBuilder.start();
                        metadataProcess.waitFor();
                        int metadataExitCode = metadataProcess.exitValue();
                        if (metadataExitCode != 0) {
                            throw new IOException("Failed to download metadata and thumbnail");
                        }

                        // Extract title from the JSON metadata
                        String content = new String(Files.readAllBytes(Paths.get(outputJson)));
                        JSONObject json = new JSONObject(content);
                        String title = json.getString("title").replaceAll("[\\\\/:*?\"<>|]", "_"); // Sanitize filename
                        String outputAudio = outputDir + title + ".mp3"; // Use title as filename

                        // Download the audio
                        ProcessBuilder audioBuilder = new ProcessBuilder("yt-dlp", "-x", "--audio-format", "mp3",
                                "--newline", "-o", outputAudio, youtubeUrl);
                        audioBuilder.redirectErrorStream(true);
                        Process audioProcess = audioBuilder.start();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(audioProcess.getInputStream()));
                        String line, progress = "";
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("%")) {
                                String newProgress = line.substring(line.indexOf("%") - 4, line.indexOf("%") + 1)
                                        .trim();
                                if (!newProgress.equals(progress)) {
                                    progress = newProgress;
                                    EditMessageText newMessage = new EditMessageText();
                                    newMessage.setChatId(from);
                                    newMessage.setMessageId(messageId);
                                    newMessage.setText("Downloading... " + progress);
                                    execute(newMessage);
                                }
                            }
                        }
                        audioProcess.waitFor();
                        int exitCode = audioProcess.exitValue();
                        if (exitCode == 0) {
                            DeleteMessage deleteMessage = new DeleteMessage(from, messageId);
                            execute(deleteMessage);

                            // Prepare to send the audio with title and thumbnail
                            String outputThumbnailJpg = outputDir + videoId + ".jpg"; // Assuming thumbnail conversion
                                                                                      // was successful
                            SendAudio audio = new SendAudio();
                            audio.setChatId(from);
                            audio.setAudio(new InputFile(new File(outputAudio)));
                            audio.setTitle(title);
                            audio.setReplyToMessageId(fromId);
                            audio.setCaption(title);
                            if (new File(outputThumbnailJpg).exists()) {
                                audio.setThumbnail(new InputFile(outputThumbnailJpg));
                            }
                            execute(audio);

                            // Cleanup
                            Files.deleteIfExists(Paths.get(outputAudio));
                            Files.deleteIfExists(Paths.get(outputThumbnailJpg));
                            Files.deleteIfExists(Paths.get(outputJson));
                            Files.deleteIfExists(Paths.get(outputThumbnailJpg.replace(".jpg", ".webp")));
                        } else {
                            System.out.println("Download failed with exit code " + exitCode);
                        }
                    } catch (IOException | InterruptedException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                }

                if (command.equals("/udemy")) {
                    SendMessage initialMessage = new SendMessage();
                    initialMessage.setChatId(from);
                    initialMessage.setText("Wait sometime... server load \uD83D\uDEE0");
                    try {
                        Message response = execute(initialMessage);
                        final int messageId = response.getMessageId();

                        // Fetch data synchronously
                        String jsonData = weeb.fetchUdemyCoursesData();
                        if (jsonData != null) {
                            JsonArray coursesArray = JsonParser.parseString(jsonData).getAsJsonArray();
                            StringBuilder coursesMessageText = new StringBuilder("Here are your courses: \n");
                            String newText = "Here are your courses: \n";

                            for (JsonElement element : coursesArray) {
                                JsonObject course = element.getAsJsonObject();
                                String title = weeb.escapeMarkdownV2(course.get("title").getAsString());
                                String enroll = weeb.escapeMarkdownV2(course.get("enroll").getAsString());
                                coursesMessageText.append("*").append(title).append("*\n")
                                        .append("[Enroll Here](").append(enroll).append(")\n\n");

                            }

                            SendMessage coursesMessage = new SendMessage();
                            coursesMessage.setChatId(from);
                            coursesMessage.setText(coursesMessageText.toString());
                            coursesMessage.setReplyToMessageId(fromId);
                            coursesMessage.setParseMode("MarkdownV2");
                            execute(coursesMessage);
                        } else {
                            // Handle case where data could not be fetched
                            SendMessage errorMessage = new SendMessage();
                            errorMessage.setChatId(from);
                            errorMessage.setReplyToMessageId(fromId);
                            errorMessage.setText("Failed to fetch courses. Please try again later.");
                            execute(errorMessage);
                        }

                        // Delete the initial "server load" message
                        DeleteMessage deleteMessage = new DeleteMessage();
                        deleteMessage.setChatId(from);
                        deleteMessage.setMessageId(messageId);
                        execute(deleteMessage);

                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                if (command.equals("/insta") || command.equals("/instagram")) {
                    // URL url = new URL(args);

                    if (isUrl == false) {
                        SendMessage message = new SendMessage();
                        message.setChatId(from);
                        message.setText("Please provide a valid instagram link to download content.\n\nExample:\n/instagram https://instagram.com/ndxaad= Or /insta https://instagram.com/ndxaad=");
                        message.setReplyToMessageId(fromId);
                        try {
                            execute(message);
                            
                        } catch (TelegramApiException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    
                    
                    } else {
                    Map<String, Object> instagramData = weeb.fetchInstagramData(args);
                    // System.out.println(instagramData);
                    List<Map<String, String>> urlsList = (List<Map<String, String>>) instagramData.get("urls");
                    String caption = (String) instagramData.get("caption");
                    String username = (String) instagramData.get("username");
                    caption = weeb.escapeMarkdownV2(caption);
                    username = weeb.escapeMarkdownV2(username);
                    // Iterate over each URL in the list
                    for (Map<String, String> urlInfo : urlsList) {
                        String type = urlInfo.get("type");
                        String url = urlInfo.get("url");
                        // System.out.println("Type: " + type);
                        // System.out.println("URL: " + url);
                        // System.out.println();
                        if (type.equals("video")) {
                            SendVideo video = new SendVideo();
                            video.setChatId(from);
                            video.setVideo(new InputFile(url));
                            video.setCaption("👽 *username : " + username + "*\n🗒️ *caption : " + caption + "*");
                            video.setParseMode("MarkdownV2");
                            video.setReplyToMessageId(fromId);
                            try {
                                execute(video);
                            } catch (TelegramApiException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (type.equals("image")) {
                            SendPhoto photo = new SendPhoto();
                            photo.setChatId(from);
                            photo.setPhoto(new InputFile(url));
                            photo.setCaption("👽 *username : " + username + "*\n🗒️ *caption : " + caption + "*");
                            photo.setParseMode("MarkdownV2");
                            photo.setReplyToMessageId(fromId);
                            try {
                                execute(photo);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        }
                    }
                }




                
            }
        }

    }

    @Override
    public String getBotUsername() {
        // TODO Auto-generated method stub
        return "shounen_shinichi_bot";
    }

    @Override
    public String getBotToken() {
        return "6318754886:AAG3hV41yFPfVKa8xVi4gLQMLKDJKO8KYCQ";
    }

}
