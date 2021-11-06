package com.newwares.hypixelstats.mixins;

import com.newwares.hypixelstats.handlers.GameEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(NetHandlerPlayClient.class)
public abstract class PlayerInfoMapGrabber {
    @SuppressWarnings("rawtypes")
    @Inject(method = "handlePlayerListItem(Lnet/minecraft/network/play/server/S38PacketPlayerListItem;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getPlayerInfo(S38PacketPlayerListItem p_handlePlayerListItem_1_, CallbackInfo ci, Iterator var2, S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata, NetworkPlayerInfo networkplayerinfo) {
        GameEvent.playerinfos.add(networkplayerinfo);
    }

    @SuppressWarnings("rawtypes")
    @Inject(method = "handlePlayerListItem(Lnet/minecraft/network/play/server/S38PacketPlayerListItem;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    private void removePlayerInfo(S38PacketPlayerListItem p_handlePlayerListItem_1_, CallbackInfo ci, Iterator var2, S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata) {
        GameEvent.playerinfos.removeIf(info -> info.getGameProfile().equals(s38packetplayerlistitem$addplayerdata.getProfile()));
    }


    @SuppressWarnings("rawtypes")
    @Inject(method = "handlePlayerListItem(Lnet/minecraft/network/play/server/S38PacketPlayerListItem;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getPlayerInfo1(S38PacketPlayerListItem p_handlePlayerListItem_1_, CallbackInfo ci, Iterator var2, S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata, NetworkPlayerInfo networkplayerinfo) {
        if (Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.endsWith(".hypixel.net")) {
            if (networkplayerinfo.getResponseTime() == 1) {
                GameEvent.someoneHas1Ping = true;
            }
        }
    }
}
