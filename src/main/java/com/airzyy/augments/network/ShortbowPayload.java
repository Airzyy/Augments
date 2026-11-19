package com.airzyy.augments.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
public record ShortbowPayload() implements CustomPacketPayload {
 public static final Type<ShortbowPayload> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("augments","shortbow_fire"));
 public static final StreamCodec<RegistryFriendlyByteBuf,ShortbowPayload> STREAM_CODEC=StreamCodec.of((buf,p)->{},buf->new ShortbowPayload());
 @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
}
