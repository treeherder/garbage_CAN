
### WINDOWS
=====
cd "c:\Riot Games\League of Legends\RADS\solutions\lol_game_client_sln\releases\" & for /d %F in (*) do cd %F & start "" /D "deploy" "League of Legends.exe" "8394" "LoLLauncher.exe" "" "spectator spectator.na.lol.riotgames.com:80 <encryptionKey> <gameID> NA1"

### OSX
=====
cd "/Applications/League of Legends.app/Contents/LoL/RADS/solutions/lol_game_client_sln/releases/"*"/deploy/LeagueOfLegends.app/Contents/MacOS"
riot_launched=true ./LeagueOfLegends 8394 LoLLauncher "" "spectator spectator.na.lol.riotgames.com:80 <encryptionKey> <gameID> NA1"
