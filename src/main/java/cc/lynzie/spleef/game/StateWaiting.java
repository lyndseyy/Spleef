package cc.lynzie.spleef.game;

import cc.lynzie.minigame.arena.GameArena;
import cc.lynzie.minigame.arena.state.GameState;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StateWaiting extends GameState {

  private int lastSecond = 5;

  public StateWaiting(JavaPlugin javaPlugin, GameArena gameArena) {
    super(javaPlugin, gameArena, Duration.of(5, ChronoUnit.SECONDS), "Game Start");
  }

  @Override
  public void stateUpdate() {
    // Don't count if it goes into the negatives.
    if (lastSecond < 0) {
      return;
    }

    // Tell everyone when the game will start.
    getArena().sendMessage(Component.text("The game will be starting in ")
        .color(TextColor.color(TextColor.color(59, 241, 241))).append(
            Component.text(lastSecond).color(getColourForTime())
                .append(Component.text(" seconds!").color(TextColor.color(59, 241, 241)))));

    lastSecond--;
  }

  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    // Don't let the player change their position, only look around.
    Location previous = event.getFrom();
    Location current = event.getTo();

    if (previous.getX() != current.getX() || previous.getZ() != current.getZ()) {
      event.getPlayer().teleport(previous);
    }
  }

  private TextColor getColourForTime() {
    // If there's 5 seconds left, return green
    // If there's 2-4 seconds left, return yellow
    // If there's 0-1 seconds left, return red
    if (lastSecond == 5) {
      return TextColor.color(50, 255, 100);
    }
    if (lastSecond == 1 || lastSecond == 0) {
      return TextColor.color(255, 100, 100);
    } else {
      return TextColor.color(241, 241, 59);
    }
  }

  @Override
  public void stateStart() {

  }

  @Override
  public void stateEnd() {

  }

}