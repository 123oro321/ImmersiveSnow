package straywave.minecraft.immersivesnow;

import net.minecraft.world.level.ChunkPos;

import java.time.Instant;
import java.util.HashMap;

public class Memory {
    private static final HashMap<ChunkPos, Long> data = new HashMap<>();

    public static void remember(ChunkPos chunkPos) {
        data.put(chunkPos, Instant.now().getEpochSecond());
    }

    public static boolean hasForgotten(ChunkPos chunkPos) {
        long rememberedTime = data.getOrDefault(chunkPos, 0L);
        long currentTime = Instant.now().getEpochSecond();
        long difference = currentTime - rememberedTime;
        return difference >= Configuration.data.memoryDuration;
    }

    public static void erase() {
        data.clear();
    }
}
