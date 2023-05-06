/*    */ package nl.corwindev.floorislava;
/*    */ 
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ 
/*    */ public class Events implements Listener {
/*    */   public static Main plugin;
/*    */   
/*    */   public Events(Main plugin) {
/* 11 */     this; Events.plugin = plugin;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerDeath(PlayerDeathEvent event) {
/* 18 */     if (!HandleGame.stopped)
/*    */     {
/* 20 */       if (HandleGame.alivePlayers.contains(event.getEntity())) {
/*    */         
/* 22 */         HandleGame.alivePlayers.remove(event.getEntity());
/*    */         
/* 24 */         if (HandleGame.alivePlayers.size() == 0 || HandleGame.alivePlayers.size() == 1) {
/*    */           
/* 26 */           if (HandleGame.alivePlayers.size() == 0) {
/* 27 */             HandleGame.alivePlayers.add(event.getEntity());
/*    */           }
/*    */           
/* 30 */           (new HandleGame()).endGame();
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\dhaen\OneDrive\Bureaublad\mappen\decomiler\floorislava-0.1.jar!\nl\corwindev\floorislava\Events.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */