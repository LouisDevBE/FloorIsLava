/*     */ package nl.corwindev.floorislava;
/*     */ import com.sk89q.worldedit.EditSession;
/*     */ import com.sk89q.worldedit.MaxChangedBlocksException;
/*     */ import com.sk89q.worldedit.bukkit.BukkitAdapter;
/*     */ import com.sk89q.worldedit.bukkit.WorldEditPlugin;
/*     */ import com.sk89q.worldedit.math.BlockVector3;
/*     */ import com.sk89q.worldedit.regions.CuboidRegion;
/*     */ import com.sk89q.worldedit.world.World;
/*     */ import com.sk89q.worldedit.world.block.BaseBlock;
/*     */ import com.sk89q.worldedit.world.block.BlockStateHolder;
/*     */ import com.sk89q.worldedit.world.block.BlockTypes;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.bukkit.scoreboard.Criteria;
/*     */ import org.bukkit.scoreboard.DisplaySlot;
/*     */ import org.bukkit.scoreboard.Objective;
/*     */ import org.bukkit.scoreboard.Scoreboard;
/*     */ import org.bukkit.scoreboard.ScoreboardManager;
/*     */ 
/*     */ public class HandleGame {
/*     */   public static EditSession editSession;
/*     */   public static boolean stopped = false;
/*  29 */   public static Set<Player> deadPlayers = new HashSet<>();
/*  30 */   public static Set<Player> alivePlayers = new HashSet<>();
/*     */   private final Plugin plugin;
/*  32 */   ScoreboardManager manager = Bukkit.getScoreboardManager();
/*     */   private int radius;
/*     */   private World worldBorder;
/*     */   
/*     */   public HandleGame() {
/*  37 */     this.plugin = (Plugin)Main.plugin;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginGame(int radiuss, Player player) {
/*  42 */     stopped = false;
/*  43 */     for (Player p : this.plugin.getServer().getOnlinePlayers()) {
/*  44 */       alivePlayers.add(p);
/*     */     }
/*  46 */     int x = player.getLocation().getBlockX();
/*  47 */     int z = player.getLocation().getBlockZ();
/*  48 */     World world = BukkitAdapter.adapt(player.getLocation().getWorld());
/*  49 */     player.getWorld().getWorldBorder().setCenter(player.getLocation());
/*  50 */     player.getWorld().getWorldBorder().setSize(radiuss);
/*     */     
/*  52 */     for (Player p : this.plugin.getServer().getOnlinePlayers()) {
/*  53 */       p.teleport(new Location(p.getWorld(), x, player.getLocation().getY(), z));
/*  54 */       p.setHealth(20.0D);
/*  55 */       p.setFoodLevel(20);
/*  56 */       Scoreboard scoreboard = this.manager.getNewScoreboard();
/*  57 */       Objective objective = scoreboard.registerNewObjective("countdown", Criteria.create("dummy"), (Component)Component.text(this.plugin.getConfig().getString("countdownTitle")));
/*  58 */       objective.setDisplaySlot(DisplaySlot.SIDEBAR);
/*  59 */       objective.getScore(this.plugin.getConfig().getString("timeleft")).setScore(this.plugin.getConfig().getInt("pvpandlavastartsafter"));
/*  60 */       p.setScoreboard(scoreboard);
/*     */     } 
/*     */     
/*  63 */     this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> { for (Player p : this.plugin.getServer().getOnlinePlayers()) { if (p.getScoreboard().getObjective("countdown").getScore(this.plugin.getConfig().getString("timeleft")).getScore() == 0) { p.getScoreboard().getObjective("countdown").unregister(); this.plugin.getServer().getScheduler().cancelTask(((BukkitTask)this.plugin.getServer().getScheduler().getPendingTasks().get(0)).getTaskId()); continue; }  p.getScoreboard().getObjective("countdown").getScore(this.plugin.getConfig().getString("timeleft")).setScore(p.getScoreboard().getObjective("countdown").getScore(this.plugin.getConfig().getString("timeleft")).getScore() - 1); }  }0L, 20L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.worldBorder = player.getWorld();
/*  75 */     this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> { this.radius = radiuss; setLavaLevel(this.plugin.getConfig().getInt("startheight"), x, z, world); shrinkBorder(player.getLocation()); }(this.plugin
/*     */ 
/*     */ 
/*     */         
/*  79 */         .getConfig().getInt("pvpandlavastartsafter") * 20));
/*     */   }
/*     */   
/*     */   private void shrinkBorder(Location location) {
/*  83 */     if (stopped)
/*  84 */       return;  if (location.getWorld().getWorldBorder().getSize() > 1.0D) {
/*  85 */       location.getWorld().getWorldBorder().setSize(location.getWorld().getWorldBorder().getSize() - 1.0D);
/*  86 */       this.radius--;
/*     */     } else {
/*  88 */       location.getWorld().getWorldBorder().setSize(1.0D);
/*     */     } 
/*  90 */     this.plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)Main.plugin, () -> shrinkBorder(location), (this.plugin
/*     */         
/*  92 */         .getConfig().getInt("borderspeed") * 20));
/*     */   }
/*     */   
/*     */   private void setLavaLevel(int level, int x, int z, World world) {
/*  96 */     if (stopped)
/*  97 */       return;  WorldEditPlugin worldEdit = (WorldEditPlugin)this.plugin.getServer().getPluginManager().getPlugin("WorldEdit");
/*     */ 
/*     */ 
/*     */     
/* 101 */     CuboidRegion cuboidRegion = new CuboidRegion(world, BlockVector3.at(x - this.radius / 2, level, z + this.radius / 2), BlockVector3.at(x + this.radius / 2, level, z - this.radius / 2));
/*     */     
/* 103 */     editSession = worldEdit.getWorldEdit().newEditSessionBuilder().world(world).build(); 
/* 104 */     try { EditSession editSessions = editSession; 
/* 105 */       try { Set<BaseBlock> baseBlock = new HashSet<>();
/* 106 */         baseBlock.add(BlockTypes.AIR.getDefaultState().toBaseBlock());
/* 107 */         editSession.replaceBlocks((Region)cuboidRegion, baseBlock, (BlockStateHolder)BlockTypes.LAVA.getDefaultState().toBaseBlock());
/* 108 */         if (editSessions != null) editSessions.close();  } catch (Throwable throwable) { if (editSessions != null) try { editSessions.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (MaxChangedBlocksException e)
/* 109 */     { e.printStackTrace(); }
/*     */     
/* 111 */     handleScoreboard(level, this.plugin.getConfig().getInt("roundTimer"));
/*     */     
/* 113 */     this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> { editSession.undo(editSession); setLavaLevel(level + this.plugin.getConfig().getInt("increaseamount"), x, z, world); }(this.plugin
/*     */ 
/*     */ 
/*     */         
/* 117 */         .getConfig().getInt("roundTimer") * 20));
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleScoreboard(int lavaLevel, int roundTime) {
/* 122 */     if (roundTime == 0 || stopped)
/* 123 */       return;  Scoreboard board = this.manager.getNewScoreboard();
/* 124 */     Objective objective = board.registerNewObjective("lol", Criteria.create("lava"), (Component)Component.text(this.plugin.getConfig().getString("sidebarTitle")));
/* 125 */     this.plugin.getServer().getOnlinePlayers().forEach(player -> {
/*     */           objective.getScore(this.plugin.getConfig().getString("players") + alivePlayers.size() + "/" + deadPlayers.size()).setScore(1);
/*     */           objective.getScore(" ").setScore(2);
/*     */           objective.getScore(this.plugin.getConfig().getString("lavalevel") + lavaLevel + "/319").setScore(3);
/*     */           objective.getScore(this.plugin.getConfig().getString("border") + this.radius + "x" + this.radius).setScore(4);
/*     */           objective.getScore(" ").setScore(5);
/*     */           objective.getScore(this.plugin.getConfig().getString("nextlavaraise") + roundTime + "s").setScore(6);
/*     */           objective.setDisplaySlot(DisplaySlot.SIDEBAR);
/*     */           player.setScoreboard(board);
/*     */           this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, (), 20L);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endGame() {
/* 145 */     stopped = true;
/*     */     
/* 147 */     if (editSession != null) {
/* 148 */       editSession.undo(editSession);
/*     */     }
/*     */     
/* 151 */     this.plugin.getServer().broadcast((Component)Component.text("The game has ended!"));
/* 152 */     if (alivePlayers.size() == 1) {
/* 153 */       this.plugin.getServer().broadcast((Component)Component.text(((Player)alivePlayers.iterator().next()).getName() + " has won the game!"));
/*     */     } else {
/* 155 */       this.plugin.getServer().broadcast((Component)Component.text("Nobody has won the game!"));
/*     */     } 
/*     */     
/* 158 */     if (this.worldBorder != null) this.worldBorder.getWorldBorder().setSize(1000000.0D);
/*     */ 
/*     */     
/* 161 */     for (Player player : this.plugin.getServer().getOnlinePlayers())
/* 162 */       player.setScoreboard(this.manager.getNewScoreboard()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\dhaen\OneDrive\Bureaublad\mappen\decomiler\floorislava-0.1.jar!\nl\corwindev\floorislava\HandleGame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */