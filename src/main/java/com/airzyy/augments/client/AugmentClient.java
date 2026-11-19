package com.airzyy.augments.client;
import com.airzyy.augments.network.OpenAugmentScreenPayload;
import net.minecraft.client.Minecraft;
public final class AugmentClient {
 private AugmentClient(){}
 public static void open(OpenAugmentScreenPayload payload){Minecraft.getInstance().execute(()->Minecraft.getInstance().setScreen(new AugmentScreen(payload.cards())));}
}