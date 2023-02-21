package carpet_livemaps;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class LiveMaps implements ModInitializer, CarpetExtension {

    public static void noop() { }
    static {
        CarpetServer.manageExtension(new LiveMaps());
    }

    @Override
    public void onGameStarted() {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(LiveMapsSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        InputStream langFile = LiveMaps.class.getClassLoader().getResourceAsStream("assets/carpet_livemaps/lang/%s.json".formatted(lang));
        if (langFile == null) {
            return Collections.emptyMap();
        }
        String jsonData;
        try {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        return new GsonBuilder().create().fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
    }

    @Override
    public void onInitialize() {

        MapTick mapTick = new MapTick(20);
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            mapTick.tick();
        });

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.GLOW_ITEM_FRAME) {
                ItemFrameEntityList.removeItemFrameEntity((ItemFrameEntity) entity);
            }
        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.GLOW_ITEM_FRAME) {
                ItemFrameEntityList.addItemFrameEntity((ItemFrameEntity) entity);
            }
        });

    }

}
