package carpet_livemaps;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static carpet_livemaps.LiveMapsSettings.liveMapsFunction;

public class MapTick {

    private int tickCount = 0;
    private final int intervalTicks;

    public MapTick(int intervalTicks) {
        this.intervalTicks = intervalTicks;
    }

    public void tick() {
        switch (liveMapsFunction) {
            case "Ticks":
                tickCount++;
                if (tickCount >= intervalTicks) {
                    List<ItemFrameEntity> ItemFrameEntities = ItemFrameEntityList.getItemFrameEntities();
                    for (ItemFrameEntity entity : ItemFrameEntities) {
                        ItemStack itemStack = entity.getHeldItemStack();
                        MapState mapState = FilledMapItem.getOrCreateMapState(itemStack, entity.world);
                        if (mapState != null) MapUpdater.updateColorsWithoutPlayer(entity.world, mapState);
                    }
                    tickCount = 0;
                }
                break;
            case "RedstonePower":
                List<ItemFrameEntity> ItemFrameEntities = ItemFrameEntityList.getItemFrameEntities();
                for (ItemFrameEntity entity : ItemFrameEntities) {
                    if(entity.world.getReceivedRedstonePower(entity.getBlockPos()) > 1) {
                        ItemStack itemStack = entity.getHeldItemStack();
                        MapState mapState = FilledMapItem.getOrCreateMapState(itemStack, entity.world);
                        if (mapState != null) MapUpdater.updateColorsWithoutPlayer(entity.world, mapState);
                    }
                }
                break;
            default:
                break;
        }
    }

}
