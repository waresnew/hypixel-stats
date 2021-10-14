package com.newwares.hypixelstats.mixins.pseudo;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(targets = "com.newwares.helper.players.Denicker")
public interface DenickerInvoker {

    @Invoker(value = "denick", remap = false)
    static String[] denick(GameProfile gameProfile) {
        throw new IllegalStateException("Mod not loaded");
    }
}
