package cc.lynzie.spleef.game;

import cc.lynzie.minigame.arena.GameArena;
import cc.lynzie.minigame.arena.state.GameState;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StateLobby extends GameState {

  private int lastTip = 0;
  private int waitTime = 0;
  private boolean waitReduced = false;
  private List<String> gameTips = List.of("You can run around and break blocks right now!",
      "Keep moving throughout the game! Standing still makes it easier for you to get knocked out!",
      "Don't walk backwards, you can't see holes behind you!",
      "Don't stay in one place for too long, move around!");

  public StateLobby(JavaPlugin javaPlugin, GameArena gameArena) {
    super(javaPlugin, gameArena, Duration.ofHours(1), "Lobby");
  }

  @Override
  public void stateStart() {

  }

  @Override
  public void stateUpdate() {
    if (getArena().getMaxPlayers() - 2 <= getArena().getPlayers().size()) {
      if (waitReduced || getArena().getPlayers().size() == 1) {
        return;
      }

      setStateDuration(Duration.of(waitTime + 30, ChronoUnit.SECONDS));

      getArena().sendMessage(
          Component.text("Wait reduced! Allowing time for more players to join!")
              .color(TextColor.color(100, 200, 100)));
      waitReduced = true;
    }

    // Every 30 seconds send a tip.
    if (lastTip == 30) {
      lastTip = -1;
      int tipIndex = ThreadLocalRandom.current().nextInt(gameTips.size());
      getArena().sendMessage(Component.text("TIP! ").color(TextColor.color(50, 255, 100))
          .append(Component.text(gameTips.get(tipIndex)).color(TextColor.color(
              241, 241, 59))));
    }

    lastTip++;
    waitTime++;
  }

  @Override
  public void stateEnd() {
    // Replace all broken blocks in the lobby and teleport players to their spawns.
    for (Block brokenBlock : brokenBlocks) {
      brokenBlock.setType(Material.SNOW_BLOCK);
    }

    for (int i = 0; i < getArena().getPlayers().size(); i++) {
      getArena().getPlayers().get(i).teleportPlayer(getArena().getPlayerSpawns().get(i));
    }
  }

  private List<Block> brokenBlocks = new ArrayList<>();

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    brokenBlocks.add(event.getBlock());

    SpleefPlayer player = (SpleefPlayer) getArena().getGameManager().getPlayer(event.getPlayer());
    player.setBlocksBroken(player.getBlocksBroken() + 1);
  }

}