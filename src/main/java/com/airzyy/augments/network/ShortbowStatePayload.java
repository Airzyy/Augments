package com.airzyy.augments.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
public record ShortbowStatePayload(boolean enabled) implements CustomPacketPayload {
 public static final Type<ShortbowStatePayload> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("augments","shortbow_state"));
 public static final StreamCodec<RegistryFriendlyByteBuf,ShortbowStatePayload> STREAM_CODEC=StreamCodec.of((buf,p)->buf.writeBoolean(p.enabled),buf->new ShortbowStatePayload(buf.readBoolean()));
 @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
}
