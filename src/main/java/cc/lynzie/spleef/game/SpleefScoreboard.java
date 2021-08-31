package cc.lynzie.spleef.game;

import cc.lynzie.minigame.arena.state.GameState;
import cc.lynzie.minigame.player.GamePlayer;
import cc.lynzie.minigame.player.GameScoreboardLines;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SpleefScoreboard implements GameScoreboardLines {

  @Override
  public Component getDisplayName() {
    return Component.text("Spleef")
        .color(TextColor.color(100, 241, 241))
        .decorate(TextDecoration.BOLD);
  }

  @Override
  public List<Component> getLines(GamePlayer gamePlayer) {
    SpleefPlayer spleefPlayer = (SpleefPlayer) gamePlayer;
    String upcomingState = "Game End";

    // Check to see if there's another state/event coming up in the arena, if
    // there's not one then we'll display "Game End" on the scoreboard.
    if (gamePlayer.getArena().getGameStates().size() > 1) {
      // Get the state and the name of it.
      GameState upcoming = gamePlayer.getArena().getGameStates().get(1);
      upcomingState = upcoming.getFriendlyName();
      // Get the remaining time and add it to the string.
      int[] remainingTime = gamePlayer.getArena().getCurrentGameState().getRemainingTime();
      upcomingState += " in %02d:%02d".formatted(remainingTime[0], remainingTime[1]);
    }

    // Create our list, and set our colours to use.
    List<Component> lines = new ArrayList<>();
    TextColor headerColour = TextColor.color(52, 188, 240);
    TextColor infoColour = TextColor.color(245, 245, 245);
    TextColor frozenColour = TextColor.color(132, 189, 190);

    // Display the next event/state, and if it's frozen (meaning the countdown will still
    // work, but not complete if time goes low enough), then display it as frozenColour.
    lines.add(Component.text("Next Event").color(headerColour));
    lines.add(Component.text(upcomingState).color(
        (spleefPlayer.getArena().getCurrentGameState().isFrozen() ? frozenColour : infoColour)));
    lines.add(Component.empty());

    lines.add(Component.text("Blocks Broken").color(headerColour));
    lines.add(Component.text(spleefPlayer.getBlocksBroken()).color(infoColour));
    lines.add(Component.empty());

    lines.add(Component.text("Players Alive").color(headerColour));
    lines.add(Component.text(gamePlayer.getArena().getActivePlayers().size()).color(infoColour));

    return lines;
  }

}