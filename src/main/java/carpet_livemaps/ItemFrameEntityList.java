package carpet_livemaps;

import net.minecraft.entity.decoration.ItemFrameEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemFrameEntityList {
    private static final List<ItemFrameEntity> itemFrameEntities = new ArrayList<>();

    public static void addItemFrameEntity(ItemFrameEntity entity) {
        if(entity.containsMap()) itemFrameEntities.add(entity);
    }

    public static void removeItemFrameEntity(ItemFrameEntity entity) {
        itemFrameEntities.remove(entity);
    }

    public static List<ItemFrameEntity> getItemFrameEntities() {
        return itemFrameEntities;
    }

}
