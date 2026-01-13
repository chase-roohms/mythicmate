package Scrapers;

import Bot.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WikiScraper {
    //Known class names
    private final static Set<String> classes = new HashSet<>(Arrays.asList("/artificer", "/barbarian", "/bard",
            "/blood-hunter", "/cleric", "/druid", "/fighter", "/monk", "/paladin", "/ranger", "/rogue", "/sorcerer",
            "/warlock", "/wizard"));
    private final static String spellListPath = DiscordBot.ROOTDIR + "/database/SpellList.txt";
    private final static String featListPath = DiscordBot.ROOTDIR + "/database/FeatList.txt";
    private final static String lineageListPath = DiscordBot.ROOTDIR + "/database/LineageList.txt";
    private final static String classListPath = DiscordBot.ROOTDIR + "/database/ClassList.txt";
    private final static String subclassListPath = DiscordBot.ROOTDIR + "/database/SubclassList.txt";

    public static void scrape(SlashCommandInteractionEvent event) throws IOException {
        int count = 0;

        String baseURL = "http://dnd5e.wikidot.com";
        String linkURL = "http://dnd5e.wikidot.com";
        String databaseType = Objects.requireNonNull(
                event.getOption("database")).getAsString();

        String currentPath = "";
        FileWriter myWriter;

        if(databaseType.equalsIgnoreCase("spell")){
            new File(spellListPath);
            linkURL += "/spells";
            currentPath = spellListPath;
        }else if(databaseType.equalsIgnoreCase("lineage")) {
            new File(lineageListPath);
            linkURL += "/lineage:";
            currentPath = lineageListPath;
        }else if(databaseType.equalsIgnoreCase("feat")) {
            new File(featListPath);
            currentPath = featListPath;
        }else if(databaseType.equalsIgnoreCase("class")) {
            new File(classListPath);
            currentPath = classListPath;
        }else if(databaseType.equalsIgnoreCase("subclass")) {
            new File(subclassListPath);
            currentPath = subclassListPath;
        }

        try{
            myWriter = new FileWriter(currentPath);
        }catch (IOException e){
            event.reply(e.getMessage() + " - Cancelling operation.").queue();
            return;
        }


        Document doc = Jsoup.connect(linkURL).get();
        Set<String> newestVersion = new HashSet<>();

        StopWatch timer = new StopWatch();
        timer.start();

        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href = link.attr("href");
            boolean scrape = false;
            String scrapeURL = "";
            String check = href.replace("/", "")
                    .replace("-ua","")
                    .replace("-revised", "");

            if(databaseType.equalsIgnoreCase("spell")){
                if(href.startsWith("/spell") && !href.startsWith("/spells")) {
                    if(!newestVersion.contains(check)){
                        newestVersion.add(check);
                        scrapeURL = baseURL + href;
                        scrape = true;
                    }
                }
            }else if(databaseType.equalsIgnoreCase("lineage")) {
                if(href.startsWith("/lineage:")) {
                    if(!newestVersion.contains(check)) {
                        newestVersion.add(check);
                        scrapeURL = baseURL + href;
                        scrape = true;
                    }
                }
            }else if(databaseType.equalsIgnoreCase("feat")) {
                if(href.startsWith("/feat:")) {
                    if(!newestVersion.contains(check)) {
                        newestVersion.add(check);
                        scrapeURL = baseURL + href;
                        scrape = true;
                    }
                }
            }else if(databaseType.equalsIgnoreCase("class")) {
                if(classes.contains(href)) {
                    if(!newestVersion.contains(check)) {
                        newestVersion.add(check);
                        scrapeURL = baseURL + href;
                        scrape = true;
                    }
                }
            }else if(databaseType.equalsIgnoreCase("subclass")) {
                if (href.contains(":")) {
                    if (classes.contains(href.substring(0, href.indexOf(":")))) {
                        if(!newestVersion.contains(check)) {
                            newestVersion.add(check);
                            scrapeURL = baseURL + href;
                            scrape = true;
                        }
                    }
                }
            }
            if(scrape){
                count++;
                subDocScrape(scrapeURL, databaseType, myWriter);
            }
        }
        myWriter.close();
        //Notify when done
        String databaseNameResponse = databaseType.toUpperCase().charAt(0) +
                                      databaseType.substring(1);

        timer.stop();
        EmbedBuilder updateEmbed = new EmbedBuilder();
        updateEmbed.setTitle("Update Complete");
        updateEmbed.setDescription("Be wary of running this command too often, it can create a heavy load on the servers");
        updateEmbed.addField("Time to Update", "➥" + timer.getTime(TimeUnit.SECONDS) + " sec.", true);
        updateEmbed.addField("Files Updated", "➥" + count + " files.", true);
        updateEmbed.setFooter(databaseNameResponse + " database updated!");

        event.getHook().sendMessageEmbeds(updateEmbed.build()).queue();
    }

    public static void subDocScrape(String URL, String databaseType, FileWriter myWriter) throws IOException {
        Document subDoc = Jsoup.connect(URL).get();//Save HTML
        //Save name of item
        String itemName = subDoc.getElementsByClass("page-title page-header").text();
        myWriter.write(itemName + "\n");
        //Format name to a filename
        String fileName = itemName.replace(" ", "").replace("'", "")
                .replace(":", "").replace("-", "")
                .replace("/", "").toLowerCase();

        //Set database path
        String path = "database/" + databaseType + "/"
                + fileName + ".txt";

        //Scrape away
        WebPageScraper.scrape(path, itemName, subDoc);
    }

}
