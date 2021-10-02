package com.newwares.hypixelstats;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    public FMLLoadingPlugin() {
        unlockLwjgl();
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.hypixelstats.json");
        MixinEnvironment.getCurrentEnvironment().setObfuscationContext("searge");
        MixinEnvironment.getCurrentEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @SuppressWarnings("unchecked")
    private void unlockLwjgl() {
        boolean lwjglUnlock = false;
        try {
            // Unlock LWJGL, allowing for it to be transformed.
            Field transformerExceptions = LaunchClassLoader.class.getDeclaredField("classLoaderExceptions");
            transformerExceptions.setAccessible(true);
            Object o = transformerExceptions.get(Launch.classLoader);
            lwjglUnlock = ((Set<String>) o).remove("org.lwjgl.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (!lwjglUnlock) {
            System.out.println("Failed to unlock LWJGL, several fixes will not work.");
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
