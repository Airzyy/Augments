package com.airzyy.augments.client;
import com.airzyy.augments.Augments;
import com.airzyy.augments.network.ShortbowPayload;
import com.airzyy.augments.network.ShortbowStatePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BowItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
@EventBusSubscriber(modid=Augments.MOD_ID,value=Dist.CLIENT)
public final class ShortbowClientEvents {
 private static boolean enabled;
 private ShortbowClientEvents(){}
 @SubscribeEvent
 public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event){
  if(!enabled||!event.isAttack())return;
  Minecraft mc=Minecraft.getInstance();
  if(mc.player==null||mc.screen!=null||!(mc.player.getMainHandItem().getItem() instanceof BowItem))return;
  event.setCanceled(true);
  event.setSwingHand(true);
  PacketDistributor.sendToServer(new ShortbowPayload());
 }
 public static void setEnabled(boolean value){enabled=value;}
}
