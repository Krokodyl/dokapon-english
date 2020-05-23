# Dokapon Gen

This project is a Java program that generates a translation file (.bps) for the Super Famicom video game\
**Dokapon 3-2-1: Arashi o Yobu Yuujou** (ドカポン3・2・1 〜嵐を呼ぶ友情〜) released in 1994.
If you only care about the translation patch, see below.

**This is a work in progress**
**Feel free to fork it, share it, improve it.**
**You're welcome to report any bugs or issues**

Dokapon 3-2-1 is the second Dokapon game for the Super Famicom as well as the second overall. Like it predecessor, it follows a group of ragtag RPG heroes across a board game-like map as they attempt to outmaneuver each other and be the first to save the kingdom. Characters move around determinant on dice rolls and can acquire items and treasure depending on where they land. They can also get into fights with random monsters which raises their levels, though being defeated will cause them to lose a turn.

A remake for the Wii and PS2 was released in 2008.

## The English patch file

The latest patch file is available : [Dokapon 3-2-1 English.bps](/roms/Dokapon%203-2-1%20-%20English.bps)

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
- [ ] Bug on the ranking table
- [ ] Bug on the Kill Race starting level
- [ ] Menu after a player kill is not wide enough
- [ ] Bug on menu after a player escapes another player
- [ ] Bug on the rename screen (after a player kill)
- [ ] Bug on sell gear (Steal x10) the x is shown as DC-OW
- [ ] Move the AI menu to the left to show full words instead of Wea, Nor, Har
- [ ] In the menus, differentiate Towns and Castles
- [ ] The main menu guy has no background (could be an emulator issue)
- [ ] Bad translation: "Who will play together"
- [ ] Typo: "Knifes"

## Missing translations

ITEMS
Item8
Item10
Item12

FIELD MAGIC
FMAG1
FMAG2
FMAG3
FMAG5   

MONSTER MAGIC
MOSK1
MOSK2
MOSK5
MOSK6
MOSK8
MOSK11

NEGATIVE EFFECTS
-oruhaou
MOSK1

Some quest related names (last part of Table 6) have to match with same names in other translations.

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

I could not find an open-source Java BPS patch generator so I made one.
It's under /dokapon-gen/src/main/java/dokapon/bps
It's still experimental and is not very well optimized but it seems to work.
