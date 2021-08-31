package cc.lynzie.spleef;

import cc.lynzie.minigame.GameManager;
import cc.lynzie.minigame.arena.GameArena;
import cc.lynzie.spleef.game.SpleefScoreboard;
import cc.lynzie.spleef.game.StateEnding;
import cc.lynzie.spleef.game.StateLobby;
import cc.lynzie.spleef.game.StatePlaying;
import cc.lynzie.spleef.game.StateWaiting;
import cc.lynzie.spleef.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spleef extends JavaPlugin {

  private GameManager gameManager;

  @Override
  public void onEnable() {
    this.gameManager = new GameManager(this);

    getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
  }

  public GameArena getArena() {
    GameArena arena = this.gameManager.getOpenArena();

    // Add the GameStates if the arena is missing them.
    if (arena.getGameStates().isEmpty()) {
      arena.setScoreboard(new SpleefScoreboard());

      arena.addGameState(new StateLobby(this, arena));
      arena.addGameState(new StateWaiting(this, arena));
      arena.addGameState(new StatePlaying(this, arena));
      arena.addGameState(new StateEnding(this, arena));
    }

    return arena;
  }

  public GameManager getGameManager() {
    return gameManager;
  }
}
