package carpet_livemaps.mixins;

import carpet_livemaps.ItemFrameEntityList;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.decoration.ItemFrameEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

    private static final List<ItemFrameEntity> itemFrameEntities = new ArrayList<>();


    @Inject(method = "setHeldItemStack", at = @At("TAIL"))
    public void setItem(ItemStack value, CallbackInfo ci) {
        ItemFrameEntityList.addItemFrameEntity((ItemFrameEntity) (Object) this);
    }

    @Inject(method = "removeFromFrame", at = @At("HEAD"))
    public void removeItem(ItemStack itemStack, CallbackInfo ci) {
        ItemFrameEntityList.removeItemFrameEntity((ItemFrameEntity) (Object) this);
    }
    
}

