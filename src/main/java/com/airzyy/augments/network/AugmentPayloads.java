package com.airzyy.augments.network;
import com.airzyy.augments.client.AugmentClient;
import com.airzyy.augments.client.ShortbowClientEvents;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
public final class AugmentPayloads {
 private AugmentPayloads(){}
 public static void register(RegisterPayloadHandlersEvent event){
  PayloadRegistrar registrar=event.registrar("1");
  registrar.playToClient(OpenAugmentScreenPayload.TYPE,OpenAugmentScreenPayload.STREAM_CODEC,(payload,context)->{
   if(net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT) context.enqueueWork(()->AugmentClient.open(payload));
  });
  registrar.playToClient(ShortbowStatePayload.TYPE,ShortbowStatePayload.STREAM_CODEC,(payload,context)->context.enqueueWork(()->ShortbowClientEvents.setEnabled(payload.enabled())));
  registrar.playToServer(ShortbowPayload.TYPE,ShortbowPayload.STREAM_CODEC,(payload,context)->context.enqueueWork(()->{
   if(context.player() instanceof ServerPlayer player)AugmentNetwork.fireShortbow(player);
  }));
  registrar.playToServer(SelectAugmentPayload.TYPE,SelectAugmentPayload.STREAM_CODEC,(payload,context)->context.enqueueWork(()->{
   if(context.player() instanceof ServerPlayer player)AugmentNetwork.select(player,payload.id());
  }));
 }
}