# hypixel-stats

`denicker is only available if you have my personal mod (i'm currently not releasing it)`

Your average hypixel queue dodger mod

this is probably the only minecraft mod i will ever make which actually uses objects so i tried to stick a factory pattern in

![image](https://user-images.githubusercontent.com/45801973/136713738-0835e4e3-5cae-4510-beb0-c59c79636723.png)

![image](https://user-images.githubusercontent.com/45801973/136708050-2afb3d42-d7af-4694-ac8c-010686478465.png)

![image](https://user-images.githubusercontent.com/45801973/136709135-eb59ede0-d0ea-45c5-a3a4-5beff0197009.png)


![image](https://user-images.githubusercontent.com/45801973/136708123-560f187e-5eb7-47b6-b128-98601c929330.png)



run /api new to set your api key and join a game

supported modes: speeduhc, ranked sw, insane sw, normal sw, bedwars

known issue: it will print out the stats of the bots that briefly appear at the start of a game (this bug is fixed with [this mod](https://github.com/waresnew/remove-fake-players))

caches api results to avoid rate limits

config is stored in the `HypixelStats` folder in the minecraft directory

go check out [DuelSniffer](https://github.com/exejar/DuelSniffer) if you want a duels queue dodger

### Commands
- /hypixelstats <nsw/rsw/isw/bw/uhc> \<username\>

  manually prints out the stats for a player depending on the gamemode
  
  alias = `hs`
  
- /hypixelstats nicks <nick>
  
  views the usernames associated with the nick and the date they were found
