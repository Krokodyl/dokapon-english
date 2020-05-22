# Dokapon Gen

This project is a Java program that generates a translation file (.bps) for the Super Nintendo video game **Dokapon 3-2-1: Arashi o Yobu Yuujou** (ドカポン3・2・1 〜嵐を呼ぶ友情〜)

# The patch file

The patch file is available : https://github.com/Krokodyl/dokapon-english/blob/master/roms/Dokapon%203-2-1%20-%20Arashi%20wo%20Yobu%20Yujo%20(J).smc

## Requirements

* git
* Java 7 or above
* Maven
* The original japanese ROM of the game (not provided here)

File: Dokapon 3-2-1 - Arashi o Yobu Yuujou (Japan)
No-Intro: Super Nintendo Entertainment System (v. 20180813-062835)
ROM SHA-1: 9343CA8D3161DEA847E0369A4A91CD7F8DC2D3AE
ROM CRC32: FC353400

## Compile & Execute

1. Edit src/main/resources/config.json
```json
"rom-input" : "../roms/Dokapon 3-2-1 - Arashi wo Yobu Yujo (J).smc",
"rom-output" : "../roms/Dokapon 3-2-1 - English.smc",
"bps-patch-output": "../roms/Dokapon 3-2-1 - English.bps"
```

2. Copy the ROM in the folder you defined in "rom-input"

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

# Translations
