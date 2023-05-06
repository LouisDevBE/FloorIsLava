/*    */ package nl.corwindev.floorislava;
/*    */ 
/*    */ import nl.corwindev.floorislava.Commands.Begin;
/*    */ import nl.corwindev.floorislava.Commands.End;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ public final class Main
/*    */   extends JavaPlugin
/*    */ {
/*    */   public static Main plugin;
/*    */   
/*    */   public void onEnable() {
/* 15 */     plugin = this;
/*    */     
/* 17 */     saveDefaultConfig();
/*    */     
/* 19 */     getCommand("begin").setExecutor((CommandExecutor)new Begin((Plugin)this));
/* 20 */     getCommand("end").setExecutor((CommandExecutor)new End((Plugin)this));
/*    */     
/* 22 */     getServer().getPluginManager().registerEvents(new Events(this), (Plugin)this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 28 */     (new HandleGame()).endGame();
/*    */   }
/*    */ }


/* Location:              C:\Users\dhaen\OneDrive\Bureaublad\mappen\decomiler\floorislava-0.1.jar!\nl\corwindev\floorislava\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */