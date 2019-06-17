# ASPECT
## Discord Bot built using [D4J](https://github.com/Discord4J/Discord4J)

Revamped. V2.0

To add Aspect to your discord server, shoot me a dm on Discord **@Requiem#8148**, and I'll provide an OAuth2 URL

All commands are prefixed by "$", unless manually changed by the discord server admin.
All commands are formatted as: "$\[command name\] param1, param2, ....". Note the space between the command name and the first param


## All available commands:
[Source for all commands](https://github.com/PhaseRush/Aspect/blob/e3016e4ee0c16d4a734a17a87fb7b4306eab27d2/src/main/java/main/CommandManager.java#L44)
- examples with params in round brackets indicate optional params. Square brackets indicate a placeholder. Do not use the round or square bracket characters if you choose to use that parameter


#### Music

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| stop/lvoice | 0 | Stops current song, leaves voice | `$stop |
| play | 1 | Adds param to Queue | `$play [YT song/plist]` |
| nowplaying/current/np| 0 | Displays info about current song | `$np` |
| skip | 0, 1 | skips 1 song, or \[param\] songs | `$skip 13` |
| purge | 1 | deletes all songs after x in queue | `$purge 12` |
| queue/q | 0 | lists the first 15 songs in queue | `$queue` |
| loop | 0, 1 | loops indefinetly, or \[param\] times | `$loop 3` |
| shuffle | 0 | shuffles current queue | `$shuffle` |
| sfx | 1 | plays \[param\] sound effect | `$sfx purple` | 
| listsfx | 0 | lists all available sound effects | `$listsfx` |
| qdel/songdel | 1 | removes song in position x from queue | `$qdel 4` |
| listmusic | 0 | lists all preconfigured playlists | `$listmusic` |
| pq | 0 | lists past queue (past songs) | `$pq` |
| seek | 1 | goes to that timestamp in the current song | `$seek 4:20` |
| eq | 1.. | applies an equalizer filter | `$eq [curr, bass, treb]` |


#### Misc.

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| summarize | 1 | summarizes webpage article | `$summarize https://goo.gl/oei2cu` |
| img | 1 | image recognition | `$img https://goo.gl/5dx7VS` |
| greet | 0 | greets everyone in current Voice channel | `$greet` |

#### Utility (non-meta)

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| dic/def | 1 | urban dictionary query | `$dic english` |
| wiki | 1 | wikipedia query | `$wiki apple` |
| roll | 1 | rolls dice. Use dice notation | `$roll 6d14` |
| render | 1 | renders an image with text param | `$render Hello!` |
| freq | 1 | analyses word frequency for tagged user | `$freq @Requeim` |
| bob | 1 | Spongebob-ify text parameter | `$bob haha so funny` |
| haste | 1 | dumps ATTACHED file into hastebin | `$haste [attached file]` |

#### General Commands

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| bulkdelete | 1 | deletes all msg in channel up to x mins | `$bulkdelete 3` |
| timev | 0 | displays time in Vancouver, Canada | `$time` |
| deletemsg | 2 | deletes msg by \[x] in past \[y] mins | `$deletemsg 264213620026638336, 10` |

#### Meta

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| setprefix | 1 | sets prefix for disc server | `$setprefix ! (next time: !setprefix $)`|
| ping | 0 | pretty useless | `$ping` |
| info | 1 | shows info about a person. Ask for details | `$info @Requiem` |
| help | 0, 1 | sends pm for help, or specific help a cmd | `$help (poll)` |
| membercount | 0 | displays number of members in current server | `$membercount` |
| poll | 2..* | constructs poll | `$poll Question, option1, op2,...` |
| cpuload | 0 | displays system info | `$cpuload` |

#### Meta Utility
| command | #Params | Description | Example|
| --- | --- | --- | --- |
| time | 1.. | times execution duration of (nested) command | `$time ping` |

#### League of Legends

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| lolign | 1 | shows availability of summoner name | `$lolign AKatHelicopter` |
| lollevel | 1 | shows level of summoner | `$lollevel SchrödingersKat` |
| lolsum | 1 | shows info of summoner | `$lolsum SchrödingersKat` |
| lolregions | 0 | shows available regions | `$lolregions` | 
| lolitem | 0, 1| shows info about item, or \[x] items | `$lolitem (3)` |
| lolrecent | 1 | WIP - shows recent match | `$lolrecent SchrödingersKat` |
| allskins | 1 | lists all skins for \[champion] | `$allskins katarina` |
| skin | 2 | shows picture of skin | `$skin katarina, [4 or Kitty Cat Katarina]` |
| lolquote | 0, 1 | shows a random quote (from specified champ) | `$lolquote zed` |

#### Warframe

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| wfdaily | 0 | shows daily deals | `$wfdaily` |
| wfcetus | 0 | shows time in cetus | `$wfcetus` |
| wfalerts | 0 | shows all alerts | `$wfalert` |
| wfvoid | 0,1 | shows all void fissures, or filters accordingly | `$wfvoid (meso)` |
| wfinfo | 1 | shows basic info about item (has spellcheck) | `$wfinfo abkroncoe prwime blurewpint` |
| wfaco | 0 | shows info about all currentl acolytes | `$wfaco` |
| wfdropinfo | 1 | shows drop locations of materials | `$wfdropinfo polymer bundle` |
| wfvoidtrader | 0 | shows info about current or upcomming void trader | `$wfvoidtrader` |
| wfmarket | 1,2 | shows current listings for item | `$wfmarket flow, all` |

#### NASA

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| apod | 0 | NASA's Astronomy Picture of the Day | `$apod` |
| bluemarble | 0, 1 | NASA's image of the earth, updated frequently | `$bluemarble (natural)` |

#### Wolfram Alpha

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
|wolfram/solve/_ | 1..* | Basic WA query | `$_ tell me a comp sci joke` |
| __ | 1..* | Full WA query | `$__ centripetal force 15kg 600m 13rad/sec` |

#### Humour // Don't dead open inside

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| cute | 1 | displays a cute image | `$cute baka` |
| ship | 2 | Ships two people | `$ship @Aspect, @Requiem` |
| insult | 0 | says an insult (slight nsfw) | `$insult` |
| ascii | 1,2 | Ascii-fy an image. Can specify font size| `$ascii https://goo.gl/5dx7VS, 4` |
| ascii2 | 2 | Ascii-fy, old. Specify font size | `$ascii2 https://goo.gl/5dx7VS, 10` |
| count | 1, 2 | Counts # of occurences of word/regex in channel, or entire server | `$count word, all` |

#### Stats
| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| ow | 2 | displays overwatch stats| `$ow cats, 11481` |
| fn | 1 | displays fortnite stats | `$fn ninja` |

#### Realm of the Mad God - Temporarily Deprecated

| Command | #Params | Description | Example |
| --- | --- | --- | --- |
| katinv | 0 | Lists kat's inventory | `$katinv` |
| rguild | 1 | Rates guild | `$rguild Black Bullet` |
| rpet | 1 | Rates player's pet | `$rpet SexySelfie` |
| rrate | 1 | Rates player | `$rrate SexySelfie` | |
| rdesc | 1 | Shows player's realmeye desc | `$rdesc SexySelfie` |
| rrecentchar | 1 | Shows player's most recent char | `$rrecentchar SexySelfie`|
| rscore | 1 | Scores player | `$rscore SexySelfie` |
| setinv | - | For use by dev only | `$setinv \[x]` |


#### Other Commands
dictfilter :: filter the entire english dictionary by some regex or substring
wyr :: would you rather
face :: demographic recognition given image of face
uwu :: spams uwu (or other text) over target's pfp
upscale :: upscales image using Waifu2x
sweep :: minesweeper game


#### WIP Commands:
- [ ] General: fetch: command that searches google and compiles keywords and top image
- [ ] Google image search
- [ ] Imgur upload/integration
- [ ] Microsoft Azure/Cloud integration for image recognition
- [ ] Remind me bot (timezones op?)

#### WIP Features:
- [ ] Dump MasterState json with command
- [ ] use [FFT](https://en.wikipedia.org/wiki/Fast_Fourier_transform) to detect "beat drops" etc.
- [ ] Passively listen for hashtag. Excl channels, search via twitter api.
- [ ] Word and Frequency Counter - regex to english translator for the layman

#### Other Features:
- Warframe :: Automatically updates alerts and posts to channel every 30 minutes
- Warframe :: Has automatic spell correction. "abkroncoe prwime blurewpint" -> "akbronco prime blueprint"
- Warframe :: Status is constantly updating to display current time and solar cycle on Cetus
- Music :: Calling $play without valid url triggers Youtube search
- Music :: `$play` supports various [keywords](src/main/java/main/utility/music/MusicUtils.java#L28)
- Music :: keeps track of song history with `$pq`
- Count :: Supports regular expression matching. Begin Regex pattern with backslash: `$count \[regex]`
- Pokemon Identification :: Due to request from server admin, the pokedex only works when `$identify` is called. This is to not ruin the fun of guessing pokemon
- Privacy :: Can give users a role to access private channels if a custom password is pm'd to the bot. Uses SHA-256 for encryption.
- Summarize :: If a summary exceeds a certain character count, Aspect will upload the summarized text to [Gist](https://gist.github.com/Aspection/)
- Frequency :: Can count word frequencies and detect spelling mistakes. Provides message analytics for users.
- Can dump an uploaded text file into hastebin.
- ... and much, much more.

#### Notes
- music player uses [LavaPlayer](https://github.com/sedmelluq/lavaplayer). Supports YouTube, SoundCloud, Bandcamp, Vimeo, and Twitch.
- $bulkdelete (and certain other commands) restricted to people with a certain role/privileges
- League of Legends - due to riot's policy on api keys, the private key needs to be updated daily. Message me to update it.


#### Special thanks: 

[decyg](https://github.com/decyg) for help with [basically everything](src/main/java/main/commands/dontopendeadinside/Imaging.java).

[Drew Cornfield](https://github.com/Resoona) for the private channel/role/password idea.

[Mike Winkelmann](https://www.beeple-crap.com/about) for permission to use their art as icons.


Aspect isn’t endorsed by Riot Games and doesn’t reflect the views or opinions of Riot Games or anyone officially involved in producing or managing League of Legends. League of Legends and Riot Games are trademarks or registered trademarks of Riot Games, Inc. League of Legends © Riot Games, Inc.
