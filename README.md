# TomShelbyBot
An official Telegram SDK's wrapper with custom features.

## Features
### Commands
There is a light framework to deal with commands.  
Create your commands in a second:
```java
@Command(
    label = "name", // Label of your command
    aliases = "aliases", // List of aliases for your command
    description = "description", // Description of your command
    explicitAccess = true // Should your command only be handled with /name@name_of_bot
)
public class YourBotCommand extends SimpleBotCommand {
  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    // Handle command with arguments, bot and update (message etc.)  
  }
} 
```
Then register them:
```java
bot.getCommandContainer()
  .attach(new YourBotCommand());
  .attach(new YourAnotherBotCommand());
  ...
```
### Packages
With TomShelbyBot you can easily embed custom packages into the bot:
```java
public class YourBotPackage extends JavaBotPackage {
    @Override
    public void onEnable() {
      ThomasBot bot = getBot();
      Logger logger = getLogger();
      // Do something on enable of your package.  
    }
   
    @Override
    public void onDisable() {
      // Do something on disable of your package.
    }
}
```
Provide information about your package, make botpackage.json in resources folder of your project:
```json
{
  "name": "mypackage",
  "author": "you",
  "version": "0.0.1",
  "description": "Your description",
  "main": "<path to the main class: YourBotPackage>"
}
```
Build your package, then drag jar file into the folder 'packs', restart the bot and there you go!
## TODO
- [ ] Complete README with other features
- [ ] Do something with ThomasBot class
- [ ] Permission API
- [ ] Clusterization