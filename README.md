# CheckMeOut
This is an island submission addon. This addon enables players to submit their island for consideration by admins. In this way, Admins can set up site-wide challenges or competitions that players can do and then submit their island for consideration. Admins get a GUI that lists submissions and they can teleport to the islands from there. Once an island is reviewed by admins it can be deleted, or when the whole activity is over, all submissions can be cleared.

## Commands

### Player Command

The only player command is `checkmeout` for example `/is checkmeout`. It can be changed in config.yml if you like.
This command will submit the island to admins for review.

### Admin Commands

Admins have the following commands (use with the regular admin command, e.g., /bsbadmin):

* `cmo seesubs` - this will open a GUI showing all the submitted islands. You can click on an island icon or pick the random button!
* `cmo check <player name>` - this will teleport you to the player's island without opening the GUI. You can use Tab complete to cycle through submissions. You will teleport to the player's home position when they made the submission. Teleports are checked for safety and if unsafe you will be teleported close to the spot.
* `cmo delete <player name>` - use this to delete submissions once they have been checked out. You will need to confirm this command.
* `cmo clearall` - use this to clear all submissions. You will need to confirm this command.

The default admin command is `cmo` and it can be changed in config.yml.

## Permissions

```
permissions:
  bskyblock.checkmeout:
    description: Player can use the checkmeout command
    default: true
  bskyblock.checkmeout.admin.check:
    description: Admin can teleport to a player's island
    default: op
  bskyblock.checkmeout.admin.delete:
    description: Admin can delete a submission
    default: op
  bskyblock.checkmeout.admin.clearsubmissions:
    description: Admin can clear submissions
    default: op
  bskyblock.checkmeout.admin.seesubs:
    description: Admin can open the submissions GUI
    default: op
    
  acidisland.checkmeout:
    description: Player can use the checkmeout command
    default: true
  acidisland.checkmeout.admin.check:
    description: Admin can teleport to a player's island
    default: op
  acidisland.checkmeout.admin.delete:
    description: Admin can delete a submission
    default: op
  acidisland.checkmeout.admin.clearsubmissions:
    description: Admin can clear submissions
    default: op
  acidisland.checkmeout.admin.seesubs:
    description: Admin can open the submissions GUI
    default: op
    
  caveblock.checkmeout:
    description: Player can use the checkmeout command
    default: true
  caveblock.checkmeout.admin.check:
    description: Admin can teleport to a player's island
    default: op
  caveblock.checkmeout.admin.delete:
    description: Admin can delete a submission
    default: op
  caveblock.checkmeout.admin.clearsubmissions:
    description: Admin can clear submissions
    default: op
  caveblock.checkmeout.admin.seesubs:
    description: Admin can open the submissions GUI
    default: op
 ```
 
 ## Config.yml
 
 ```
# Icon that will be displayed in CheckMeOut list.
# It uses native Minecraft material strings, but using string 'PLAYER_HEAD', it is possible to
# use player heads instead. Beware that Mojang API rate limiting may prevent heads from loading.
icon: 'GRASS_BLOCK'
#
# This list stores GameModes in which CheckMeOut will not apply.
# To disable addon it is necessary to write its name in new line that starts with -. Example:
# disabled-gamemodes:
#  - BSkyBlock
disabled-gamemodes: []
#
# CheckMeOut panel name formatting.
# Example: &c will make names red, &f is white
name-format: "&f"
#
# Allow random checking - adds a button to the panel that goes to a random island
random-allowed: true
#
#
# User and admin commands. You can change them if they clash with other addons or plugins.
user-command: checkmeout
admin-command: cmo
 ```
 