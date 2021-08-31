package cc.lynzie.spleef.listeners;

import cc.lynzie.minigame.player.GamePlayer;
import cc.lynzie.spleef.Spleef;
import cc.lynzie.spleef.game.SpleefPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

  private final Spleef spleef;

  public PlayerListeners(Spleef spleef) {
    this.spleef = spleef;
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    // Disable the default join message.
    event.joinMessage(null);

    Player player = event.getPlayer();
    this.spleef.getArena().addPlayer(new SpleefPlayer(player));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    // Disable the default quit message.
    event.quitMessage(null);

    Player player = event.getPlayer();
    GamePlayer gamePlayer = this.spleef.getGameManager().getPlayer(player);

    gamePlayer.getArena().removePlayer(gamePlayer);
  }

  @EventHandler
  public void onHungerChange(FoodLevelChangeEvent event) {
    event.setFoodLevel(20);
  }

  @EventHandler
  public void onHealthChange(EntityDamageEvent event) {
    if (event.getEntityType() == EntityType.PLAYER) ((Player) event.getEntity()).setHealth(20);
  }

}