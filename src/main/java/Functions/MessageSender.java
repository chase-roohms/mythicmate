package Functions;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.util.Scanner;

public class MessageSender {
    public static void sendMessage(MessageChannelUnion chan, StringBuilder toSend, Scanner fileDetails){
        boolean inTable = false;

        //Pull line by line while the source is not empty
        while(fileDetails.hasNextLine()){
            String newLine = fileDetails.nextLine();

            //Line is the beginning or end of Markdown table
            if(newLine.contains("```")){
                if(!inTable){       //Beginning of table
                    chan.sendMessage(toSend).queue();
                    toSend.delete(0, toSend.length());
                    toSend.append(newLine);
                    toSend.append(System.getProperty("line.separator"));
                    inTable = true;
                }
                else{               //End of table
                    toSend.append(newLine);
                    toSend.append(System.getProperty("line.separator"));
                    chan.sendMessage(toSend).queue();
                    toSend.delete(0, toSend.length());
                    inTable = false;
                }
            } else{ //Line is not beginning or end of Markdown table

                //If message is going to exceed discord message cap
                if((toSend.length() + newLine.length()) > 1990){
                    if(inTable){    //In table, need to break
                        toSend.append(System.getProperty("line.separator"));
                        toSend.append("```");               //End table
                        chan.sendMessage(toSend).queue();   //Send message
                        toSend.delete(0, toSend.length());  //Clear string
                        toSend.append("```");               //Start table
                        toSend.append(newLine);             //Add newline
                        toSend.append(System.getProperty("line.separator"));
                    }
                    else{           //Not in table
                        chan.sendMessage(toSend).queue();   //Send message
                        toSend.delete(0, toSend.length());  //Clear string
                        toSend.append(newLine);             //Add newline
                        toSend.append(System.getProperty("line.separator"));
                    }
                } else{ //No risk of exceeding message cap
                    toSend.append(newLine);                 //Add newline
                    toSend.append(System.getProperty("line.separator"));
                }
            }
        }
        if(!toSend.isEmpty()){  //If not everything has been sent
            chan.sendMessage(toSend).queue();
        }
    }
}