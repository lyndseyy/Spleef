package cc.lynzie.spleef.game;

import cc.lynzie.minigame.player.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SpleefPlayer extends GamePlayer {

  private int blocksBroken;

  public SpleefPlayer(Player player) {
    super(player);
  }

  @Override
  public void onJoin() {
    getPlayer().setGameMode(GameMode.SURVIVAL);
    getPlayer().getInventory().clear();

    ItemStack itemStack = new ItemStack(Material.DIAMOND_SHOVEL);
    itemStack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
    itemStack.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
    getPlayer().getInventory().addItem(itemStack);

    sendMessage(Component.text("Welcome! Run around and have some fun before the game starts!")
        .color(TextColor.color(50, 255, 100)));
  }

  @Override
  public void onLeave() {

  }

  public int getBlocksBroken() {
    return blocksBroken;
  }

  public void setBlocksBroken(int blocksBroken) {
    this.blocksBroken = blocksBroken;
  }

  /**
   * Adds a snowball to the players inventory, if the player has more than 32 snowballs in their
   * inventory then there won't be any added.
   *
   * @return Whether the snowballs were added to the players inventory.
   */
  public boolean giveSnowballs() {
    PlayerInventory inventory = getPlayer().getInventory();

    // Loop through the players inventory and count up how many snowballs they have.
    int snowballCount = 0;
    for (ItemStack content : inventory.getContents()) {
      if (content != null && content.getType() == Material.SNOWBALL) {
        snowballCount += content.getAmount();
      }
    }

    if (snowballCount > 32) {
      return false;
    }
    inventory.addItem(new ItemStack(Material.SNOWBALL, 4));
    return true;
  }

  public void sendActionBar(String msg) {
    getPlayer().sendActionBar(ChatColor.translateAlternateColorCodes('&', msg)
        .replace("{block_cnt}", getBlocksBroken() + ""));
  }

}
