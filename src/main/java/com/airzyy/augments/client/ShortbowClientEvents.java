package com.airzyy.augments.client;
import com.airzyy.augments.Augments;
import com.airzyy.augments.network.ShortbowPayload;
import com.airzyy.augments.network.ShortbowStatePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.ItemTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
@EventBusSubscriber(modid=Augments.MOD_ID,value=Dist.CLIENT)
public final class ShortbowClientEvents {
 private static boolean enabled;
 private static final TagKey<Item> AUGMENT_BOWS=TagKey.create(Registries.ITEM,ResourceLocation.fromNamespaceAndPath("augments","bows"));
 private static final TagKey<Item> COMMON_BOWS=TagKey.create(Registries.ITEM,ResourceLocation.fromNamespaceAndPath("c","tools/bows"));
 private static final TagKey<Item> FORGE_BOWS=TagKey.create(Registries.ITEM,ResourceLocation.fromNamespaceAndPath("forge","tools/bows"));
 private static boolean isBow(net.minecraft.world.item.ItemStack stack){
  return stack.is(AUGMENT_BOWS)||stack.is(ItemTags.BOW_ENCHANTABLE)||stack.is(COMMON_BOWS)||stack.is(FORGE_BOWS);
 }
 private ShortbowClientEvents(){}
 @SubscribeEvent
 public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event){
  if(!enabled||!event.isAttack())return;
  Minecraft mc=Minecraft.getInstance();
  if(mc.player==null||mc.screen!=null||!isBow(mc.player.getMainHandItem()))return;
  event.setCanceled(true);
  event.setSwingHand(true);
  PacketDistributor.sendToServer(new ShortbowPayload());
 }
 public static void setEnabled(boolean value){enabled=value;}
}
