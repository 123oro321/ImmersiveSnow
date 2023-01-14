package straywave.minecraft.immersivesnow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ImmersiveSnow {
    public static Logger LOGGER = LogManager.getLogger("immersiveatmosphere");

    public static final ArrayList<ChunkPos> queue = new ArrayList<>();

    public static void init() {
        Configuration.load();
    }

    public static void recalculateBlock(ServerLevel level, BlockPos freePos) {
        // Heightmap gives us the top block that is available, so the actual block is the one immediately below

        BlockPos topPos = freePos.below();
        BlockState topState = level.getBlockState(topPos);
        Block topBlock = topState.getBlock();

        BlockPos bottomPos = topPos.below();
        BlockState bottomState = level.getBlockState(bottomPos);
        Block bottomBlock = bottomState.getBlock();

        #if MC_1_16_5
        Biome biome = level.getBiome(topPos);
        #else
        Biome biome = level.getBiome(topPos).value();
        #endif

        boolean shouldSnow = biome.shouldSnow(level, freePos) || (topBlock instanceof SnowLayerBlock && Utils.coldAndDark(level, biome, bottomPos));
        boolean shouldFreeze = biome.shouldFreeze(level, topPos, false) || (topBlock instanceof IceBlock && Utils.coldAndDark(level, biome, bottomPos));

        // Here is the actual bread & butter of the mod

        if (shouldSnow && !(topBlock == Blocks.SNOW)) {
            Utils.setBlock(level, freePos, Blocks.SNOW);
        } else if (!shouldSnow && topBlock == Blocks.SNOW) {
            Utils.setBlock(level, topPos, Blocks.AIR);
        }

        if (shouldFreeze && !(topBlock == Blocks.WATER)) {
            Utils.setBlock(level, topPos, Blocks.ICE);
        } else if (!shouldFreeze && topBlock == Blocks.ICE) {
            Utils.setBlock(level, topPos, Blocks.WATER);
        }
    }
}