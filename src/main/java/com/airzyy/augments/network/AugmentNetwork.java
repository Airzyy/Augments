package com.airzyy.augments.network;
import com.airzyy.augments.data.Augment;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
public final class AugmentNetwork {
 private AugmentNetwork(){}
 public static void open(ServerPlayer player,List<Augment> choices){
  player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Augment selection: "+String.join(" | ",choices.stream().map(Augment::name).toList())));
 }
}