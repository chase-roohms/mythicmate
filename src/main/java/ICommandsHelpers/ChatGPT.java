package ICommandsHelpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

import Authenticate.Authenticate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ChatGPT {
    private final static String MODEL = "gpt-3.5-turbo";
    private static URL gptURL;

    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11; // Marker for where the content starts.
        if (startMarker < 11) { // indexOf returned -1, so startMarker would be 10
            return "Error: Unable to parse response - content field not found";
        }
        
        int endMarker = response.indexOf("\"      },", startMarker); // Marker for where the content ends.
        if (endMarker == -1) { // endMarker not found
            // Try alternative end markers
            endMarker = response.indexOf("\",", startMarker);
            if (endMarker == -1) {
                endMarker = response.indexOf("\"}", startMarker);
            }
            if (endMarker == -1) {
                return "Error: Unable to parse response - end marker not found";
            }
        }
        
        return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
    }



    public static void askChat (SlashCommandInteractionEvent event) {
        String question = Objects.requireNonNull(event.getOption("question")).getAsString();

        EmbedBuilder answerEmbed = new EmbedBuilder();
        answerEmbed.setAuthor(question, "https://chat.openai.com/",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/ChatGPT_logo.svg/768px-ChatGPT_logo.svg.png");
        try{
            //Connect
            gptURL = new URI("https://api.openai.com/v1/chat/completions").toURL();
            HttpURLConnection connection = (HttpURLConnection)gptURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + Authenticate.getGPTKey());
            connection.setRequestProperty("Content-Type", "application/json");

            // Build the request body
            String body = "{\"model\": \"" + MODEL + "\", " +
                    "\"messages\": " +
                    "[{\"role\":\"system\", \"content\":\"You are an assistant that answers questions about Dungeons and Dragons 5th Edition rules. Be brief, 100 words or less.\"}, "+
                    "{\"role\": \"user\", \"content\": \"" + question + "\"}]}";
            connection.setDoOutput(true);
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(body);
                writer.flush();
            }

            // Get the response
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            String answer = extractContentFromResponse(response.toString())
                    .replace("\\\"", "\"")
                    .replace("\\n", System.getProperty("line.separator"));

            answerEmbed.setDescription(answer);
            event.getHook().sendMessageEmbeds(answerEmbed.build()).queue();
        } catch (java.io.IOException | java.net.URISyntaxException e) {
            answerEmbed.setDescription(e.getMessage());
            event.getHook().sendMessageEmbeds(answerEmbed.build()).queue();
        }
    }
}




