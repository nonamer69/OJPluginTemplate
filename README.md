My first ever plugin and finished project in java! Kinda scuffed I know. I don't have much to say copy it and use it however you want lol. Add things, delete things, etc. Okay see ya hf :3

--[TUTORIAL]--
Hello! This tutorial should teach you how to use the plugin.

1. checkpoints
   - the blaze rod in your inventory teleports you to a set checkpoint, while the echo shard sets that checkpoint. another way a checkpoint can be set is via checkpoint plates. refer to 2.3 for more info
   
2. /oj
   - this command is separated into multiple subcommands, i'll list what each one does.
     
     2.1 /oj create difficulty0-12(-/+) name
     PERMISSIONS: rank.set, belongs to admin+ only
     
       - this command essentially creates the jumps. the jump warp gets created at the location you were standing on while executing the command. the difficulty must be between 0 and 12 and you may add         a + or - to the end to further specify the difficulty. created jumps are automatically added to the menu(to access it, right click the nether star in your inventory)
         
     2.2 /oj delete jumpNumber
     PERMISSIONS: rank.set, belongs to admin+ only
     
       - pretty self explanatory, deletes the jump specified. removes all one jump points, completions and completion plates related to it
    
     2.3 /oj checkpoint
       PERMISSIONS: rank.set, belongs to admin+ only
     
       - this creates an iron pressure plate at your location. shift right click it to edit it. click the compass item in the opened menu to set the coordinates of the jump via chat (format: X Y Z Yaw             Pitch, it will not accept any other format). same goes for the jump strategy which can be edited by clicking the book and typing the strategy in chat. the red wool deletes the plate.
         NOTE: if you encounter a bug where a checkpoint does not set your checkpoint's destination coordinates, simply create another checkpoint next to it, step on it, delete it and then step back on             the checkpoint you were just on. that should fix it.

     2.4 /oj finish jumpNumber
       PERMISSIONS: rank.set, belongs to admin+ only

       - creates a finish plate tied to the jump specified at the location you were standing on while executing the command. once the countdown begins it is advised to step away from that block as to           not get an auto completion. if such thing does occur, refer to the next subcommand:
    
         (tip: if you shift right click the finish plate, it removes it without removing the entire jump)
    
     2.5 /oj uncomplete player jumpNumber
       PERMISSIONS: rank.set, belongs to admin+ only

       - self explanatory, removes the completion of the player specified. automatically removes their ojp.
    
     2.6 /oj complete player jumpNumber
       PERMISSIONS: rank.set, belongs to admin+ only

       - does the complete opposite of the previous subcommand, it gives a completion to the player specified. ojp are automatically added to the player's total ojp
    
     2.7 /oj reload
       PERMISSIONS: rank.set, belongs to admin+ only

       - recommended to use this command only if ojp aren't being automatically updated by the completion system. recalculates the total ojp of every player
    
     2.8 /oj warp jumpNumber
       PERMISSIONS: all members can use this command

       - teleports the player to the jump specified

3. /rank player rank
   PERMISSIONS: rank.set, belongs to admin+ only

   - sets the specified player's rank. Rank list: member, jrmod, mod, admin, owner

4. /mute player duration(s/m/h/d/w/mo) reason
   PERMISSIONS: mute.use, belongs to jrmod+ only

   - mutes the player for a certain amount of time
  
5. /unmute player
   PERMISSIONS: unmute.use, belongs to jrmod+ only

   - unmutes the player specified
  
6. /kick player reason
   PERMISSIONS: kick.use, belongs to jrmod+ only

   - kicks the player specified for the reason provided
  
7. /ban player duration(s/m/h/d/w/mo) reason
   PERMISSIONS: ban.use, belongs to mod+ only

   - bans the player specified for the provided duration and reason
  
8. /unban player
   PERMISSIONS: unban.use, belongs to mod+ only

   - unbans the player specified
  
-------------------------------------------------------------------------------


that was all! thank you for reading and i hope you guys enjoy!
