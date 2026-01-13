# MythicMate
A D&amp;D Discord bot that facilitates dice rolling and rules referencing. Implements a web scraper to update local database files on the machine I run it on.

## 🚀 Quick Links

- **[Docker Setup](DOCKER.md)** - Run MythicMate in Docker
- **[CI/CD Setup](QUICKSTART-CICD.md)** - Quick start for automated builds
- **[CI/CD Documentation](CICD.md)** - Full CI/CD guide

## Overview
My friends and I play D&amp;D and due to distance we do it remotely via discord. There are some really good discord bots out there but they all felt like they were lacking something. The dice input was really specific in its syntax, the information was locked behind paywalls and account linking, we wanted something quick, easy, and low maintenance. Enter MythicMate.

## Dice Rolling
When you are in the middle of a game, you don't want to have to remember the complex syntax of commands, or worry about a misspelling ruining your flow, you want fast and effective. For the dice rolling commands, MythicMate accepts any length of dice, and displays what each one rolled for dramatic effect. It sorts through your input, splitting it by plus signs, and removing any spaces that might cause issue. If the bot for some reason does not recognize a symbol, it will still roll the dice it does recognize and simply alert you of the error! No more pausing to ask "How do I roll again?"

Currently MythicMate supports rolling flat, rolling with advantadge and disavantadge, rolling for great weapon fighting (rerolling 1's and 2's), and sorcerer empowering (rerolling any dice below a specified number).

## Web Scraping
A lot of D&amp;D content is locked behind a paywall, but there are user ran sites like the DND5eWikiDot, that you can gather the important things from like rules, and spell details. When I run the password locked "/update" command, it searches through the links on the homepage of this wiki, and pulls information from them, saving them as formatted text files on my computer. These files are formatted in Discord's Markdown format. When a user wants to look up something, it references the available text files, and offers autocomplete suggestions. The files are then simply passed into a discord message with no further formatting needed, because it is all taken care of when scraping the data. This results in lightning fast searches.

## AI Integration
While a lot of information about D&D is locked behind a paywall, even more of it is scattered across random iterations of rule books, and source material. Utilizing ChatGPT's interface, and precise preformatted instruction, I have created the "/ask" command. The user simply types "/ask" and then their question, and my bot accesses the API and runs a formatted request. The answer is then quickly returned in a message for them.

## Multithreaded Implementation
A Discord bot with AI integration is great until you realize the processing delays involved with AI, specifically ChatGPT's API. When one user had a hanging request for an AI generated rules check, no other user was capable of running commands. To solve this I implemented multithreading, allowing each command to create and run on a new thread as soon as it is called, keeping the main bot free to process incoming commands.

## Project Structure
The project is managed by Maven, each of the included directories are their own packages - the main program is located in the Bot package, DiscordBot.java. The code is written entirely in Java, and the work was 100% done by me.

Included dependencies:
- JSoup: an open source Java HTML parsing library
- JDA: Java Discord API, a clean and full wrapping of the Discord REST API
- Apache Commons Lang: This is actually only used for their stop-watch, to ensure the webscraping isn't happening too quickly.

## Future Plans
I would like for MythicMate to feel like an actual D&amp;D companion, someone who is a part of your server, helping you play the game that you love to play! I am researching conversational AI, and hope to soon be able to add light banter to rolling commands, and present information in a more conversational format to the users.


