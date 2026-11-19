package com.airzyy.augments.network;
import com.airzyy.augments.data.Augment;
import com.airzyy.augments.data.AugmentManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public final class AugmentNetwork {
 private static final Map<UUID,Set<String>> PENDING=new ConcurrentHashMap<>();
 private AugmentNetwork(){}
 public static void open(ServerPlayer player,List<Augment> choices){
  if(choices.isEmpty())return;
  PENDING.put(player.getUUID(),choices.stream().map(Augment::id).collect(java.util.stream.Collectors.toUnmodifiableSet()));
  List<OpenAugmentScreenPayload.Card> cards=choices.stream().map(a->new OpenAugmentScreenPayload.Card(a.id(),a.name(),a.rarity().name(),a.displayValue(),a.description(),a.asset())).toList();
  PacketDistributor.sendToPlayer(player,new OpenAugmentScreenPayload(cards));
 }
 public static void select(ServerPlayer player,String id){
  Set<String> pending=PENDING.remove(player.getUUID());
  if(pending==null||!pending.contains(id))return;
  Augment a=AugmentManager.get(id);if(a==null)return;
  AugmentManager.apply(player,a);
  if(a.type()==Augment.Type.COMMAND)player.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(),a.target());
 }
}