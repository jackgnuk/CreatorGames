# CreatorGames
Dependency: PartyGames Plugin.

### Commands:

#### /top
Use: Displays the leaderboard for every team within the event. Displaying team and Total points using API Hook to PartyGames. 

Alias: /test, /t

## Known Issues

/top only works when all players are online.
Fix by taking the data from the file or SQL. Need to add the coins to SQL and inspect file sructure then create code.

#### OnBatKillEvent
When the game ends you can kill bats and gain points after the event.

If a bat is killed by a non-player issues a error.