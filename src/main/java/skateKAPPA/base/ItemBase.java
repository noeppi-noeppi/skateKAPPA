package skateKAPPA.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import skateKAPPA.SkateKAPPA;

import javax.annotation.Nonnull;

public class ItemBase extends Item {

    protected final String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(SkateKAPPA.MODID, name));
        setCreativeTab(CreativeTabs.MISC);
    }

    public void registerItemModel() {
        SkateKAPPA.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    @Nonnull
    public ItemBase setCreativeTab(@Nonnull CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    @Nonnull
    public ItemBase setMaxStackSize(int maxStackSize) {
        super.setMaxStackSize(maxStackSize);
        return this;
    }

    @Override
    @Nonnull
    public ItemBase setHasSubtypes(boolean hasSubtypes) {
        super.setHasSubtypes(hasSubtypes);
        return this;
    }

    @Override
    @Nonnull
    public ItemBase setMaxDamage(int maxDamageIn) {
        super.setMaxDamage(maxDamageIn);
        return this;
    }

    @Override
    @Nonnull
    public ItemBase setFull3D() {
        super.setFull3D();
        return this;
    }

    @Override
    @Nonnull
    public ItemBase setContainerItem(@Nonnull Item containerItem) {
        super.setContainerItem(containerItem);
        return this;
    }

    @Override
    @Nonnull
    public ItemBase setNoRepair() {
        super.setNoRepair();
        return this;
    }
}
