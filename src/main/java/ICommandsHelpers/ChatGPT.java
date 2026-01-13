package ICommandsHelpers;

import Authenticate.Authenticate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class ChatGPT {
    private final static String model = "gpt-3.5-turbo";
    private static URL gptURL;

    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11; // Marker for where the content starts.
        int endMarker = response.indexOf("\"      },", startMarker); // Marker for where the content ends.
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
            String body = "{\"model\": \"" + model + "\", " +
                    "\"messages\": " +
                    "[{\"role\":\"system\", \"content\":\"You are an assistant that answers questions about Dungeons and Dragons 5th Edition rules. Be brief, 100 words or less.\"}, "+
                    "{\"role\": \"user\", \"content\": \"" + question + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String answer = extractContentFromResponse(response.toString())
                    .replace("\\\"", "\"")
                    .replace("\\n", System.getProperty("line.separator"));

            answerEmbed.setDescription(answer);
            event.getHook().sendMessageEmbeds(answerEmbed.build()).queue();
        }catch(Exception e){
            answerEmbed.setDescription(e.getMessage());
            event.getHook().sendMessageEmbeds(answerEmbed.build()).queue();
        }
    }
}




