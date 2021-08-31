package cc.lynzie.spleef.game;

import cc.lynzie.minigame.arena.GameArena;
import cc.lynzie.minigame.arena.state.GameState;
import cc.lynzie.minigame.player.GamePlayer;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class StatePlaying extends GameState {

  private final Map<Block, Material> blocksBroken = new HashMap<>();
  private final List<Material> allowedMaterials = List.of(Material.SNOW_BLOCK, Material.SAND,
      Material.GRAVEL);

  public StatePlaying(JavaPlugin javaPlugin,
      GameArena gameArena) {
    super(javaPlugin, gameArena, Duration.of(30, ChronoUnit.SECONDS), "Playing");
  }

  @Override
  public void stateStart() {
    getArena().sendMessage(Component.text("The game's on! Have fun, and don't die!")
        .color(TextColor.color(50, 200, 50)));
  }

  @Override
  public void stateUpdate() {
    // Tell the player how many blocks they've broken!
    for (GamePlayer player : getArena().getPlayers()) {
      ((SpleefPlayer) player).sendActionBar("&dYou've broken &a{block_cnt} &dblocks!");
    }
  }

  @Override
  public void stateEnd() {
    blocksBroken.forEach(Block::setType);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    // Make sure the player is breaking an allowed block.
    if (!allowedMaterials.contains(event.getBlock().getType())) {
      event.setCancelled(true);
      return;
    }

    SpleefPlayer player = (SpleefPlayer) getArena().getGameManager().getPlayer(event.getPlayer());
    player.setBlocksBroken(player.getBlocksBroken() + 1);

    try {
      // Try to give the player snowballs in their inventory, if they already have
      // 32 snowballs then 1-4 snowballs will be dropped on the ground instead.
      boolean dropped = player.giveSnowballs();
      if (!dropped) {
        event.getBlock().getLocation().getWorld()
            .dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.SNOWBALL,
                ThreadLocalRandom.current().nextInt(4)));
      }
    } catch (Exception ignored) {
    }

    // Cancel the default drop and add the block, so it will get regenerated later.
    event.setDropItems(false);
    blocksBroken.put(event.getBlock(), event.getBlock().getType());
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    // Check to see if the player fell out of bounds of the
    // arena, and they're not flying around in spectator.
    if (event.getPlayer().getLocation().getY() < 105
        && event.getPlayer().getGameMode() != GameMode.SPECTATOR) {
      // Remove the player from the activePlayers array, "marking" them as dead.
      SpleefPlayer player = (SpleefPlayer) getArena().getGameManager().getPlayer(event.getPlayer());
      getArena().removeActivePlayer(player);

      // Tell the player how many blocks they broke, set
      // their GameMode to Spectator and teleport them back up.
      player.sendMessage(Component.text("During the game you broke ").color(NamedTextColor.YELLOW)
          .append(Component.text(player.getBlocksBroken()).color(NamedTextColor.AQUA))
          .append(Component.text(" blocks! Good job!").color(NamedTextColor.YELLOW)));
      event.getPlayer().setGameMode(GameMode.SPECTATOR);
      event.getPlayer().teleport(getArena().getPlayerSpawns().get(0));

      // If there's at least two players left, send the death message.
      if (getArena().getActivePlayers().size() >= 1) {
        getArena().sendMessage(event.getPlayer().displayName().append(Component.text(
            String.format(" has been eliminated! %d players remain.",
                getArena().getActivePlayers().size())
        ).color(TextColor.color(255, 100, 100))));
      }

    }
  }

  @Override
  public boolean isAbleToEnd() {
    return getRemaining().isZero() || getArena().getActivePlayers().size() == 1;
  }

}