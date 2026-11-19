package com.airzyy.augments.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;
public record OpenAugmentScreenPayload(List<Card> cards) implements CustomPacketPayload {
 public static final Type<OpenAugmentScreenPayload> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("augments","open_selection"));
 public static final StreamCodec<RegistryFriendlyByteBuf,OpenAugmentScreenPayload> STREAM_CODEC=StreamCodec.of((buf,p)->{
  buf.writeVarInt(p.cards.size());
  for(Card c:p.cards){buf.writeUtf(c.id);buf.writeUtf(c.name);buf.writeUtf(c.rarity);buf.writeUtf(c.value);buf.writeUtf(c.description);buf.writeUtf(c.asset);}
 },buf->{int n=buf.readVarInt();List<Card> cards=new ArrayList<>();for(int i=0;i<n;i++)cards.add(new Card(buf.readUtf(128),buf.readUtf(256),buf.readUtf(32),buf.readUtf(256),buf.readUtf(1024),buf.readUtf(256)));return new OpenAugmentScreenPayload(cards);});
 @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
 public record Card(String id,String name,String rarity,String value,String description,String asset){}
}