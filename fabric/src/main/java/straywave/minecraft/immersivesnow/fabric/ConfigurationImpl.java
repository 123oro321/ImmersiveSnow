package straywave.minecraft.immersivesnow.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ConfigurationImpl {
    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
