# Dokapon Gen

This project is a Java program that generates a translation file (.bps) for the Super Famicom video game\
**Dokapon 3-2-1: Arashi o Yobu Yuujou** (ドカポン3・2・1 〜嵐を呼ぶ友情〜) released in 1994.

This game is the second Dokapon game for the Super Famicom as well as the second overall. Like it predecessor, it follows a group of ragtag RPG heroes across a board game-like map as they attempt to outmaneuver each other and be the first to save the kingdom. Characters move around determinant on dice rolls and can acquire items and treasure depending on where they land. They can also get into fights with random monsters which raises their levels, though being defeated will cause them to lose a turn.

A remake for the Wii and PS2 was released in 2008.

## The English patch file

The latest patch file is available : [Dokapon 3-2-1 English.bps](https://github.com/Krokodyl/dokapon-english/blob/master/roms/Dokapon%203-2-1%20-%20English.bps)

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

The two-letter characters are also used for some of the equipment names but for due to the way the characters are encoded in the game, there's only so many that can be used.

Japanese | English
------------ | -------------
![image 012](/screenshots/japanese/012.png) | ![image 000](/screenshots/english/012.png)
![image 000](/screenshots/english/900.png)

### How to improve the translations

The translations are split into 6 files (matching the 6 data banks of the rom).
For practical reasons, there's no table 3.

File | Content
------------ | -------------
[Table 1](https://github.com/Krokodyl/dokapon-english/blob/master/dokapon-gen/src/main/resources/translations/Table 1.txt) | 131 lines
[Table 2](https://github.com/Krokodyl/dokapon-english/blob/master/dokapon-gen/src/main/resources/translations/Table 2.txt) | 139 lines<br/>Contains menus
[Table 4](https://github.com/Krokodyl/dokapon-english/blob/master/dokapon-gen/src/main/resources/translations/Table 4.txt) | 256 lines
[Table 5](https://github.com/Krokodyl/dokapon-english/blob/master/dokapon-gen/src/main/resources/translations/Table 5.txt) | 208 lines
[Table 6](https://github.com/Krokodyl/dokapon-english/blob/master/dokapon-gen/src/main/resources/translations/Table 6.txt) | 20 lines<br/>Contains all the names (items, magic, towns, monsters etc.)
[Table 7](https://github.com/Krokodyl/dokapon-english/blob/master/dokapon-gen/src/main/resources/translations/Table 7.txt) | 418 lines


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
