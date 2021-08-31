package cc.lynzie.spleef.game;

import cc.lynzie.minigame.arena.GameArena;
import cc.lynzie.minigame.arena.state.GameState;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StateEnding extends GameState {

  String[] placeNumber = new String[]{"1st", "2nd", "3rd"};

  public StateEnding(JavaPlugin javaPlugin, GameArena gameArena) {
    super(javaPlugin, gameArena, Duration.of(5, ChronoUnit.SECONDS), "Ending");
  }

  @Override
  public void stateStart() {
    // Sort the players based on how many blocks they've broken.
    getArena().getPlayers()
        .sort(Comparator.comparingInt(o -> ((SpleefPlayer) o).getBlocksBroken()).reversed());

    // Set the default of 3 players unless there's fewer players, then
    // lower the amount of players displayed in the end.
    int maxPlayers = 3;
    if (getArena().getPlayers().size() < 3) {
      maxPlayers = getArena().getPlayers().size();
    }

    // Loop through all of our players, and say how many blocks they've broken and
    // if they were the last one standing or not.
    for (int i = 0; i < maxPlayers; i++) {
      SpleefPlayer player = (SpleefPlayer) getArena().getPlayers().get(i);
      boolean winner = getArena().getActivePlayers().contains(player);

      TextComponent message = Component.text(placeNumber[i] + " place! ")
          .color(TextColor.color(200, 200, 200))
          .append(Component.text(
              MessageFormat.format(
                  "{0} - {1} blocks broken" + (winner ? ", and last standing!" : "!"),
                  player.getDisplayName(),
                  player.getBlocksBroken())).color(TextColor.color(100, 200, 100)));
      getArena().sendMessage(message);
    }
  }

  @Override
  public void stateUpdate() {
  }

  @Override
  public void stateEnd() {
    // Loop through all players and reset their blocks broken to 0.
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      SpleefPlayer gamePlayer = (SpleefPlayer) getArena().getGameManager().getPlayer(player);
      gamePlayer.setBlocksBroken(0);
    }
  }

  @Override
  public Duration getStateDuration() {
    return Duration.of(5, ChronoUnit.SECONDS);
  }
}
