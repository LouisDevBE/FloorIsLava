/*    */ package nl.corwindev.floorislava.Commands;
/*    */ 
/*    */ import net.kyori.adventure.text.Component;
/*    */ import nl.corwindev.floorislava.HandleGame;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public class Begin
/*    */   implements CommandExecutor {
/*    */   public Begin(Plugin plugin) {
/* 14 */     this.plugin = plugin;
/*    */   }
/*    */   private final Plugin plugin;
/*    */   
/*    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/* 19 */     if (sender instanceof Player) {
/* 20 */       Player player = (Player)sender;
/*    */       
/* 22 */       if (args.length == 1) {
/*    */         try {
/* 24 */           int radius = Integer.parseInt(args[0]);
/*    */           
/* 26 */           this.plugin.getServer().broadcast((Component)Component.text("The game has started!"));
/*    */           
/* 28 */           (new HandleGame()).beginGame(radius, player);
/* 29 */           return true;
/* 30 */         } catch (NumberFormatException e) {
/* 31 */           player.sendMessage("Radius must be a number!");
/* 32 */           return false;
/*    */         } 
/*    */       }
/* 35 */       player.sendMessage("Usage: /begin {radius}");
/* 36 */       return false;
/*    */     } 
/*    */     
/* 39 */     sender.sendMessage("You must be a player to execute this command!");
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\dhaen\OneDrive\Bureaublad\mappen\decomiler\floorislava-0.1.jar!\nl\corwindev\floorislava\Commands\Begin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */