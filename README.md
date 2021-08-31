# Spleef

## What is Spleef?

Spleef is a simple Minecraft minigame where you dig out the ground from other players around you,
and try to be the last one standing!

## Why another version of Spleef?

This version of Spleef is a version showing off what my other project
[GameManager](https://github.com/lyndseyy/GameManager) can do! This shows off how to use GameStates,
GamePlayers, GameArenas and GameScoreboards (for now! This will continue to be the example project
for the framework so any new features will be shown off here :])

## How do I use this on my server?

This is more-so meant to be an example, but if you want to use it go right ahead, you'll just have
to build the plugin yourself, I'd recommend IntelliJ IDEA for this, just clone the project there and
select "Build" in the Gradle menu on the right. You'll then have to configure arena.yml to match the
spec of your arena!

### Note though! There are some values that are hardcoded into the plugin!

This means that the fall bounds for the arena will always be Y 105, or the game will always end
after 5 minutes at most! This *really* isn't meant to be used on a server without modification!
You've been warned!