package com.airzyy.augments.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
public record SelectAugmentPayload(String id) implements CustomPacketPayload {
 public static final Type<SelectAugmentPayload> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("augments","select_augment"));
 public static final StreamCodec<RegistryFriendlyByteBuf,SelectAugmentPayload> STREAM_CODEC=StreamCodec.of((buf,p)->buf.writeUtf(p.id,128),buf->new SelectAugmentPayload(buf.readUtf(128)));
 @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
}