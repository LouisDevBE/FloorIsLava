/*    */ package nl.corwindev.floorislava.Commands;
/*    */ 
/*    */ import nl.corwindev.floorislava.HandleGame;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class End implements CommandExecutor {
/*    */   private final Plugin plugin;
/*    */   
/*    */   public End(Plugin plugin) {
/* 14 */     this.plugin = plugin;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
/* 21 */     if (!HandleGame.stopped) {
/*    */       
/* 23 */       (new HandleGame()).endGame();
/* 24 */       return true;
/*    */     } 
/* 26 */     sender.sendMessage("The game is not running!");
/* 27 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\dhaen\OneDrive\Bureaublad\mappen\decomiler\floorislava-0.1.jar!\nl\corwindev\floorislava\Commands\End.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */