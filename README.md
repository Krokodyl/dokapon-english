### Table of content
1. Introduction<br/>
2. The English patch file<br/>
Translations<br/>
Special characters<br/>
Variables<br/>
Menus
3. Know bugs / Improvements<br/>
Fixes<br/>
Missing translations<br/>
Screenshots<br/>
BPS Patcher
4. Tips and Hints<br/>
Scenario mode<br/>
Locations<br/>
Special chests<br/>
Dokapon Ring<br/>
Sky Palace<br/>
Quizz

# Dokapon Gen

This project is a Java program that generates a translation file (.bps) for the Super Famicom video game\
**Dokapon 3-2-1: Arashi o Yobu Yuujou** (ドカポン3・2・1 〜嵐を呼ぶ友情〜) released in 1994.

**This is a work in progress**\
**Feel free to fork it, share it, improve it.**\
**You're welcome to report any bugs or issues**

Dokapon 3-2-1 is the second Dokapon game for the Super Famicom. Like its predecessor, it follows a group of ragtag RPG heroes across a board game-like map as they attempt to outmaneuver each other and be the first to save the kingdom. Characters move around determinant on dice rolls and can acquire items and treasure depending on where they land. They can also get into fights with random monsters to gain experience points and increase their stats, though being defeated will cause them to lose a turn.

A remake for the Wii and PS2 was released in 2008.

## The English patch file

The latest patch file is available : [Dokapon 3-2-1 English.zip](/patch/Dokapon%203-2-1%20-%20English.zip)

It applies to the following ROM :\
File: Dokapon 3-2-1 - Arashi o Yobu Yuujou (Japan)\
No-Intro: Super Nintendo Entertainment System (v. 20180813-062835)\
ROM SHA-1: 9343CA8D3161DEA847E0369A4A91CD7F8DC2D3AE\
ROM CRC32: FC353400

## How to generate the patch yourself

### Requirements
* git
* Java 7 or above
* Maven
* The original japanese ROM of the game (not provided here)

### Compile & Execute

0. git clone [dokapon-gen](https://github.com/Krokodyl/dokapon-english/tree/master/dokapon-gen)

1. Edit src/main/resources/config.json with the location of the ROM
```json
"config": {
    "rom-input" : "../roms/Dokapon 3-2-1 - Arashi wo Yobu Yujo (J).smc",
    "rom-output" : "../roms/Dokapon 3-2-1 - English.smc",
    "bps-patch-output": "../roms/Dokapon 3-2-1 - English.bps",
    "file.jap" : "tables/table-complete.txt",
    "file.latin" : "tables/table-latin.txt",
    "file.names" : "tables/table-names.txt"
  }
```

2. Copy the ROM in the folder you defined in "rom-input" (by default ../roms)

3. Compile
```console
dokapon-gen git:(master) ✗ mvn clean install
```

4. Execute
```console
dokapon-gen git:(master) ✗ java -jar ./target/Dokapon-1.0-jar-with-dependencies.jar
Loading config
rom-input=../roms/Dokapon 3-2-1 - Arashi wo Yobu Yujo (J).smc
rom-output=../roms/Dokapon 3-2-1 - English.smc
bps-patch-output=../roms/Dokapon 3-2-1 - English.bps
Saving rom-output...
Saving bps-patch-output...
Process complete
```

## Translations

Many translations are inspired by the Wii remake from 2008.
Such as the character names, the items and magic names, the chapter titles, etc.

The town names, unlike in the Wii version, are actual city names.
But because the city names in Japanese fit on 4 characters, I added two-letter characters in order to be able to write 8-letter city names.
The city names that were longer than 8 letters are replaced with shorter names from other actual cities of the same area.

The two-letter characters are also used for some of the equipment names but due to the way the characters are encoded in the game, there's only so many that can be used.

Japanese | English
------------ | -------------
![image 012](/screenshots/japanese/012.png) | ![image 000](/screenshots/english/012.png)
|![image 000](/screenshots/english/900.png)

### How to help improve the translations

The translations are split into 6 files (matching the 6 data banks of the rom).
For practical reasons, there's no table 3.

File | Translations | Comment
------------ | ------------- | -------------
[Table 1](/dokapon-gen/src/main/resources/translations/Table%201.txt) | 131 |
[Table 2](/dokapon-gen/src/main/resources/translations/Table%202.txt) | 139 | Contains menus
[Table 4](/dokapon-gen/src/main/resources/translations/Table%204.txt) | 256 |
[Table 5](/dokapon-gen/src/main/resources/translations/Table%205.txt) | 208 |
[Table 6](/dokapon-gen/src/main/resources/translations/Table%206.txt) | 20 | Contains all the names (items, magic, towns, monsters etc.)
[Table 7](/dokapon-gen/src/main/resources/translations/Table%207.txt) | 418 |

#### Example of a translation entry
```text
OFFSET=15066
VALUE=f590(90f5)
OFFSETDATA=17590
DATA=8000 4c00 4f00 9600 0700 0200 1e00 2c00 0200 2200 1400 b900 0030 2400 1c00 4400 4a00 7401 1400 0600 4e01 4b00 2200 8101 1000 0200 b400 ffff
JAP=ファイルがいっぱいです!{NL}どちらを消すか選んで下さい.{EL}
ENG=No empty save file!{NL}Please choose one to erase.{EL}
```

#### Special characters
- The characters between **{}** are not printed in game, unless they match a special code.
- A translation must always end with **{EL}** (EL for End of Line)
- A translation must be segmented by line breaks with **{NL}** (NL for New Line).
- A segment cannot be longer than **30** printed characters.
- Table 6 doesn't use {EL} because the **names have a fixed size** (8 for the items, 8 for the armor/weapons/shields including the icon, 6 for the castles, 9 for the continents, etc.)

Special code | In game
------------ | -------------
{NL} | Line break
{EL} | End of line
{CW} | Set text color to white (default)
{CR} | Set text color to red
{CY} | Set text color to yellow
{CG} | Set text color to green
{CB} | Set text color to blue
{SHIELD} | Shield icon
{WEAP} | Weapon icon
{ARMOR} | Weapon icon
{BUT} | Button icon

#### Variables
Many translations contains variables (player name, gold amount, health points etc) which are usually represented by two codes (e.g. **{8014 5985}** is current player name)
These are 2-character long in the data but can go from 2 (monster level) to 9 (gold amount) printed characters.
These codes are listed (not all) under "special" in config.json and used to calculate the length of a given translation to make sure they don't go over 30 printed characters.
This list is not exhaustive.
It can be shown when calling DataReader.collectSpecialChars from the main class.

#### Menus

Table 2 contains menus. Each entry has an extra field **MENUDATA**
```text
MENUDATA=1107 0d05
ENG=Resume{NL}Scenario mode{NL}Normal mode{NL}Battle Royale{NL}Quizz{EL}
```

MENUDATA represents the coordinates (x,y) and the dimensions (width,height) of the menu.
1107 0d05 means the menu is at 16,7 with width 13 and height 5
The top left coordinates 0,0.
The width unit is a character width. (Screen width is 30)
The height unit is half a character height. (Screen height is 25)

I wrote a function DataReader.checkMenuData to check if a menu is out of bound but it needs testing.

## Know bugs / Improvements

- [ ] Yes/No questions are sometimes hidden under the Yes/No menu
- [ ] Move the AI menu to the left to show full words instead of Wea, Nor, Har
- [ ] When using Look on a square with 2 or more characters, only shows 1st char.

## Fixes
v1.2 (February 2022)
- [X] Fixed the ranking table for 3 and 4 players
- [X] In the menus, differentiate Towns and Castles
- [X] Fixed menu size when talking with Risque
- [X] Bug on menu after a player escapes another player
- [X] Text too long when using a revival after a loss
- [X] Text correction in the casino.
- [X] Translation for MOSK8: Sap (Gremlin skill that reduces the target's magic count)
- [X] Translation for MOSK2: Steal (Gnome skill that takes 25% of the target's gold)
- [X] Translation for Item8: Doka.Orb
- [X] Translation for MOSK11: Twister (かまいたち Kamaitachi, a wind yokai)

v1.1 (April 2021)
- [X] Bad translation: "Who will play together"
- [X] Typo: "Knifes"
- [X] Bug on the Kill Race starting level
- [X] Menu after a player kill is not wide enough
- [X] Bug on the rename screen (after a player kill)

## Missing translations

The whole game is translated.

## Screenshots
Japanese | English
------------ | -------------
![image 000](/screenshots/japanese/000.png) | ![image 000](/screenshots/english/000.png)
![image 000](/screenshots/japanese/001.png) | ![image 001](/screenshots/english/001.png)
![image 000](/screenshots/japanese/002.png) | ![image 002](/screenshots/english/002.png)
![image 000](/screenshots/japanese/003.png) | ![image 003](/screenshots/english/003.png)
![image 000](/screenshots/japanese/004.png) | ![image 004](/screenshots/english/004.png)
![image 000](/screenshots/japanese/005.png) | ![image 005](/screenshots/english/005.png)
![image 000](/screenshots/japanese/006.png) | ![image 006](/screenshots/english/006.png)
![image 000](/screenshots/japanese/010.png) | ![image 010](/screenshots/english/010.png)
![image 000](/screenshots/japanese/011.png) | ![image 011](/screenshots/english/011.png)

## BPS Patcher

I could not find an open-source Java BPS patch generator so I made one. \
It's under /dokapon-gen/src/main/java/dokapon/bps \
It's still experimental and is not very well optimized but it seems to work.

## Tips and Hints

### Scenario mode

The scenario mode starts in a training area. There, you have two weeks to collect basic equipments from chest. There are no monsters in this area.

The first players to reach the castle will get bonus points to put in their stats.

Once every player is done with the training area, the story starts. It's divided into chapters.

Each chapter follows the same structure. Walk around fighting monsters, freeing towns, leveling up, gearing up in order to kill bosses (Represented by a green dragon on the map).
After a certain number of bosses is killed, the special quest for the chapter will be triggered. Completing the quest will unlock the next continent and the next chapter.

At the end of the last chapter, the player with the highest worth (town value + gold) is the winner.

### Locations

Special location | Access
------------ | -------------
Spring Cave | Asia
Casino Cave | Europe
Lava Cave | North America
Lost Forest | North America<br/>South America
Freaky Mountain | South America
Sea Temple | South America
Ruins | Africa
Underground Passage | Africa<br/>Australia
Rabble Tower | Australia
Sky Palace | Freaky Mountain<br/>(requires Wings)
Hell<br/>Devil's Castle | Last chapter in scenario mode

### Special chests

Location | Content | Description
------------ | ------------- | -------------
Spring Cave (top left)			|Portal|[Item] Teleports you to Dokapon Castle or a town you own
Spring Cave (top right)			|Convoke|[Field Magic] Brings all the players on this map to your space.
Spring Cave (bottom right)		|Gold Bug|[Item] A bug that can double your gold or take it all. Sells for a good value.
Casino Cave (bottom left)		|*Spinner|[Item] Gives 1 to 5 spinners to move. (Random each day) Lasts a week.
Casino Cave (top right)			|Revival|[Item] Automatically revives you with half of your max HP when you die.
Casino Cave (center)			|Tonic|[Item] Doubles your attack, defense and speed for a week. Sells for half of your current gold.
Lava Cave 2F (top side)			|Spyglass| [Item] Reveals the cards before a battle. Single use.
Lava Cave 2F (bottom side)		|Mix-up|[Field Magic] See effect below
Lava Cave 3F (right) 			|Elven Mantle | [Armor]
Lava Cave 3F (top left) 		|Fire Sword	| [Weapon] with 136 attack
Lava Cave 3F (bottom left) 		|Athena Shield | [Shield] with 103 defense
Lost Wood (near shop)			|Gold Bug|[Item] A bug that can double your gold or take it }all. Sells for a good value.
Lost Wood (near stump)			|Invasion|[Field Magic] Summons monsters in all the towns of the continent.
Freaky Mountain 2F (right)		|Jade Ring | [Item] Ring that gives +25% speed
Freaky Mountain 2F (left)		|Fireball|[Field Magic] Deals damage to every player and boss on the same map.
Sky Palace						|Divine Fist | [Weapon] with 179 attack
Sea Temple 2B					|Ruby Ring | [Item] Ring that gives +25% attack
Ruins 2F						|Onyx Ring | [Item] Ring that gives +25% defense
Underground	(top left)				|Bribe|[Item] Allows you to take over the town of another player.
Underground	(bottom left)			|Puppet| See effect below
Underground (left of item shop)		|Bait| [Item] Spawns a boss in the target town. The current boss is removed.
Underground (right of item shop)	|Mirror| [Item] Protects from any field magic once.
Rabble Tower 1F (right)			|Frost|[Field Magic] Deals ice damage to everyone on the target space.
Rabble Tower 1F (left)			|Mix-up|[Field Magic] Random effect among those:<br/>- Gives +2 to all stats<br/>- Spawns traps all over the continent<br/>- Swaps everyone's gear randomly<br/>- Swaps everyone's items and magic randomly<br/>- Swaps everyone's gold randomly
Rabble Tower 2F (left)			|Onyx Ring | [Item] Ring that gives +25% defense
Rabble Tower 2F (right)			|Ruby Ring | [Item] Ring that gives +25% attack
Rabble Tower 2F (middle)		|Jade Ring | [Item] Ring that gives +25% speed
Rabble Tower 3F (left)			|Dokapon Orb | [Item] Sells for 490 000 gold in a shop or for 90 000 to the king
Rabble Tower 3F (middle left)	|Wings | [Item] Used to access the Sky Palace
Rabble Tower 3F (middle right)	|Puppet| [Field Magic] Takes control of the target player for one turn.

### Dokapon Ring

Taking the three rings (Onyx Ring, Ruby Ring and Jade Ring) to the castle will merge them into one ring (Dokapon Ring).
AFAIK, the Dokapon Ring only has the combine effects of the three rings but it only uses one inventory slot. 

### Sky Palace

Beating Rico Jr. (random spawn in some dungeons, or fixed spawn in scenario mode) will give you the item [Wings].
Going to the top of the Freaky Mountain (in South America), with the Wings will take you to the Sky Palace.
The Sky Palace is a place with many blue, yellow and white chests and one special chest.

### Quizz

The quizz is a series of 8 random questions. According to your results, you are given a recommendation of the AI to play against (weak, normal or hard).
The last question (times played) is always the same and seems to have no purpose.

Question | Answer
------------ | -------------
In Look mode, what do the buttons L and R do? | Move fast
Which of the following Field Magic deals damage to everyone on a targeted square? | Thunder+
Which button do you use to examine a square? | X Button
Which item protects once from a Field Magic? | Mirror
Where do you find this item :Ruby Ring? | Sea Temple
There is a place in Dokapon Kingdom where powerful weapons lie. Where is it? | Lava Cave
What color is the weapon treasure chest? | Red
What is the coin-shaped square on the map? | Tax Office
What does the shop with a red roof sell ? | Weapons
What button allows to scroll faster when in examine mode? | Y button
What is the effect of the Weak status? | Move 1
What is the name of the Devil's weapon? | Union Sword
Which of these weapons is the most expensive? | Halberd
What are the base damage of Thunder+? | 30
What item allows the wearer to pass through the toxic swamp safely? | Boots
Which cursed weapon can attack its owner? | Draco Blade
What is the selling price of an item compared to the purchasing price? | 50%
Which battle skill can sometimes one-shot an enemy, regardless of the HP? | Slash
What is the magic defense bonus of Deflect? | +3
There is a Warp Space fountain. Where is it? | Spring Cave
What is the name of the princess of Dokapon Kingdom? | Poring
Which starting job is missing: Fighter, Warrior, Knight, Thief? | Mage
How much is the Dokapon Orb, the ultimate treasure, worth? | 490000
A Contract is used for a deal with the Devil. For how much can you sell it to a shop? | 1
Which item is the most expensive? | 3-Spinner
There is a martial arts expert in Dokapon Kingdom. What's their name? | Chen Li
Which town has the highest value? | Sidney
How many churches are there? | 6
How many floors has the dungeon Rabble Tower? | 3
Which small monster has the highest level? | Tiamat
In order to become the Devil, you must give away all your gold, items, magic and what? | Towns