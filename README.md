# ASPECT
## Discord Bot built using [D4J](https://github.com/Discord4J/Discord4J)

Revamped. V2.0

To add Aspect to your discord server, shoot me a dm on Discord **@Requiem#8148**, and I'll provide an OAuth2 URL

All commands are prefixed by "$", unless manually changed by the discord server admin.
All commands are formatted as: "$\[command name\] param1, param2, ....". Note the space between the command name and the first param


## All available commands:
- examples with params in round brackets indicate optional params. Square brackets indicate a placeholder. Do not use the round or square brackets if you choose to use that parameter

#### Humour

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| cute | 1 | displays a cute image | $cute baka |
| ship | 2 | Ships two people | $ship @Aspect, @Requiem |
| insult | 0 | says an insult (slight nsfw) | $insult |

#### Music

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| stop/lvoice | 0 | Stops current song, leaves voice | $stop, $lvoice |
| play | 1 | Adds param to Queue | $play \[YT song/plist] |
| nowplaying/currentsong| 0 | Displays info about current song | $nowplaying |
| skip | 0, 1 | skips 1 song, or \[param\] songs | $skip (15) |
| queue | 0 | lists the first 15 songs in queue | $queue |
| loop | 0, 1 | loops indefinetly, or \[param\] times | $loop (3) |
| shuffle | 0 | shuffles current queue | $shuffle |
| sfx | 1 | plays \[param\] sound effect | $sfx purple | 
| listsfx | 0 | lists all available sound effects | $listsfx |

#### General Commands

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| bulkdelete | 1 | deletes all msg in channel up to x mins | $bulkdelete 3 |
| time | 0 | displays time in Vancouver, Canada | $time |
| deletemsg | 2 | deletes msg by \[x] in past \[y] mins | $deletemsg 264213620026638336, 10 |

#### Meta

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| setprefix | 1 | sets prefix for disc server | $setprefix ! (next time use !setprefix $)|
| ping | 0 | pretty useless | $ping |
| dev | 0 | also pretty useless :) | $dev |
| help | 0, 1 | sends pm for help, or specific help for 1 command | $help (poll) |
| membercount | 0 | displays number of members in current server | $membercount |
| poll | 2..* | constructs poll | $poll Question, option1, op2,... |

#### League of Legends

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| lollevel | 1 | shows level of summoner | $lollevel SchrödingersKat |
| lolsum | 1 | shows info of summoner | $lolsum SchrödingersKat |
| lolregions | 0 | shows available regions | $lolregions | 
| lolitem | 0, 1| shows info about item, or \[x] items | $lolitem (3) |
| lolrecent | 1 | WIP - shows recent match | $lolrecent SchrödingersKat |
| allskins | 1 | lists all skins for \[champion] | $allskins katarina |
| skin | 2 | shows picture of skin | $skin katarina, \[4 or Kitty Cat Katarina\] |

#### Warframe

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| wfdaily | 0 | shows daily deals | $wfdaily |
| wfcetus | 0 | shows time in cetus | $wfcetus |
| wfalerts | 0 | shows all alerts | $wfalert |
| wfvoid | 0,1 | shows all void fissures, or filters accordingly | $wfvoid (meso) |
| wfinfo | 1 | shows basic info about item (has spellcheck) | $wfinfo abkroncoe prwime blurewpint |
| wfaco | 0 | shows info about all currentl acolytes | $wfaco |
| wfvoidtrader | 0 | shows info about current or upcomming void trader | $wfvoidtrader |
| wfmarket | 1,2 | shows current listings for item | $wfmarket flow, all |

#### NASA

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| apod | 0 | NASA's Astronomy Picture of the Day | $apod |
| bluemarble | 0, 1 | NASA's image of the earth, updated frequently | $bluemarble (natural) |

#### Wolfram Alpha

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
|wolfram/solve/(space literal) | 1..* | Querys WA | $ tell me a comp sci joke |

#### Misc.

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| summarize | 1 | summarizes webpage article | $summarize https://goo.gl/oei2cu |
| img | 1 | image recognition | $img https://goo.gl/5dx7VS |
| fn -DEPRECATED- | 2 | displays fortnite stats | $fn AmperianLoop, \[one of: all, squad, solo, duo\] |

#### Realm of the Mad God

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| katinv | 0 | Lists kat's inventory | $katinv |
| rguild | 1 | Rates guild | $rguild Black Bullet |
| rpet | 1 | Rates player's pet | $rpet SexySelfie |
| rrate | 1 | Rates player | $rrate SexySelfie |
| rdesc | 1 | Shows player's realmeye desc | $rdesc SexySelfie |
| rrecentchar | 1 | Shows player's most recent char | $rrecentchar SexySelfie|
| rscore | 1 | Scores player | $rscore SexySelfie |
| setinv | - | For use by dev only | $setinv \[x] |


#### WIP Commands:
- [x] Fortnite shop - DEPRECATED
- [ ] League: full match analysis
- [ ] League: "suggestions" :)
- [x] Warframe: detailed item information
- [ ] Warframe: relic drop locations
- [ ] General: fetch: command that searches google and compiles keywords and top image


#### non-command features
- Warframe :: Automatically updates alerts and posts to channel every 30 minutes.
- Warframe :: Has automatic spell correction. "abkroncoe prwime blurewpint" -> "akbronco prime blueprint"

#### Notes
- time command displays wrong time (not time in Vancouver). This is due to the bot using the time of the server its being hosted on.
- command prefix cannot be changed as of 8/25
- music player uses [LavaPlayer](https://github.com/sedmelluq/lavaplayer). Supports YouTube, SoundCloud, Bandcamp, Vimeo, and Twitch.
- Icons used curtesy of [Mike Winkelmann](https://www.beeple-crap.com/about). 




Special thanks to [decyg](https://github.com/decyg) for help with [basically everything](/src/main/java/main/commands/Imaging.java).


Aspect isn’t endorsed by Riot Games and doesn’t reflect the views or opinions of Riot Games or anyone officially involved in producing or managing League of Legends. League of Legends and Riot Games are trademarks or registered trademarks of Riot Games, Inc. League of Legends © Riot Games, Inc.
