package carpet_livemaps;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multisets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.map.MapState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class MapUpdater {

    public static void updateColorsWithoutPlayer(World world, MapState state) {
        if (world.getRegistryKey() != state.dimension) {
            return;
        }
        int i = 1 << state.scale;
        int j = state.centerX;
        int k = state.centerZ;
        int n = 128 / i;
        if (world.getDimension().hasCeiling()) {
            n /= 2;
        }
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        boolean bl = false;
        for (int o = 0; o < 128; o++) {
            double d = 0.0;
            for (int p = 0; p < 128; p++) {
                double f;
                if (o < 0 || p < -1 || o >= 128 || p >= 128) continue;
                int q = MathHelper.square(o - 64) + MathHelper.square(p - 64);
                boolean bl2 = q > (n - 2) * (n - 2);
                int r = (j / i + o - 64) * i;
                int s = (k / i + p - 64) * i;
                LinkedHashMultiset<MapColor> multiset = LinkedHashMultiset.create();
                WorldChunk worldChunk = world.getChunk(ChunkSectionPos.getSectionCoord(r), ChunkSectionPos.getSectionCoord(s));
                if (worldChunk.isEmpty()) continue;
                int t = 0;
                double e = 0.0;
                if (world.getDimension().hasCeiling()) {
                    int u = r + s * 231871;
                    if (((u = u * u * 31287121 + u * 11) >> 20 & 1) == 0) {
                        multiset.add(Blocks.DIRT.getDefaultState().getMapColor(world, BlockPos.ORIGIN), 10);
                    } else {
                        multiset.add(Blocks.STONE.getDefaultState().getMapColor(world, BlockPos.ORIGIN), 100);
                    }
                    e = 100.0;
                } else {
                    for (int u = 0; u < i; ++u) {
                        for (int v = 0; v < i; ++v) {
                            BlockState blockState;
                            mutable.set(r + u, 0, s + v);
                            int w = worldChunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, mutable.getX(), mutable.getZ()) + 1;
                            if (w > world.getBottomY() + 1) {
                                do {
                                    mutable.setY(--w);
                                } while ((blockState = worldChunk.getBlockState(mutable)).getMapColor(world, mutable) == MapColor.CLEAR && w > world.getBottomY());
                                if (w > world.getBottomY() && !blockState.getFluidState().isEmpty()) {
                                    BlockState blockState2;
                                    int x = w - 1;
                                    mutable.set(r + u, x, s + v);
                                    do {
                                        mutable.setY(--x);
                                        blockState2 = worldChunk.getBlockState(mutable);
                                        ++t;
                                    } while (x > world.getBottomY() && !blockState2.getFluidState().isEmpty());
                                    blockState = getFluidStateIfVisible(world, blockState, mutable);
                                }
                            } else {
                                blockState = Blocks.BEDROCK.getDefaultState();
                            }
                            state.removeBanner(world, mutable.getX(), mutable.getZ());
                            e += (double)w / (double)(i * i);
                            multiset.add(blockState.getMapColor(world, mutable));
                        }
                    }
                }
                MapColor mapColor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.CLEAR);
                MapColor.Brightness brightness = mapColor == MapColor.WATER_BLUE ? ((f = (double)(t /= i * i) * 0.1 + (double)(o + p & 1) * 0.2) < 0.5 ? MapColor.Brightness.HIGH : (f > 0.9 ? MapColor.Brightness.LOW : MapColor.Brightness.NORMAL)) : ((f = (e - d) * 4.0 / (double)(i + 4) + ((double)(o + p & 1) - 0.5) * 0.4) > 0.6 ? MapColor.Brightness.HIGH : (f < -0.6 ? MapColor.Brightness.LOW : MapColor.Brightness.NORMAL));
                d = e;
                if (p < 0 || q >= n * n || bl2 && (o + p & 1) == 0) continue;
                bl |= state.putColor(o, p, mapColor.getRenderColorByte(brightness));
            }
        }
    }

    private static BlockState getFluidStateIfVisible(World world, BlockState state, BlockPos pos) {
        FluidState fluidState = state.getFluidState();
        if (!fluidState.isEmpty() && !state.isSideSolidFullSquare(world, pos, Direction.UP)) {
            return fluidState.getBlockState();
        }
        return state;
    }

}
