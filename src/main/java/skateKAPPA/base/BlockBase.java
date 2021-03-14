package skateKAPPA.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.eventhandler.Event;
import skateKAPPA.SkateKAPPA;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

public class BlockBase extends Block {

    private final Function<Block, Item> itemFactory;
    protected final String name;
    private Item itemBlock;
    private boolean fullCube = true;
    private boolean opaqueCube = true;

    private AxisAlignedBB boundingBox = null;
    private EnumPushReaction pistonMovable = null;

    private Function<Entity, Event.Result> entityDestroy = null;

    public BlockBase(Block parentMaterial, String name) {
        this(parentMaterial.getDefaultState().getMaterial(), name);
    }

    public BlockBase(Material material, String name) {
        super(material);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(SkateKAPPA.MODID, name));
        setCreativeTab(CreativeTabs.MISC);
        itemBlock = null;
        itemFactory = ItemBlock::new;
    }

    public BlockBase(Block parentMaterial, String name, Function<Block, Item> itemFactory) {
        this(parentMaterial.getDefaultState().getMaterial(), name, itemFactory);
    }

    public BlockBase(Material material, String name, Function<Block, Item> itemFactory) {
        super(material);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(SkateKAPPA.MODID, name));
        setCreativeTab(CreativeTabs.MISC);
        itemBlock = null;
        this.itemFactory = itemFactory;
    }

    public void registerItemModel() {
        if (itemBlock == null)
            createItemBlock();
        SkateKAPPA.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        if (itemBlock == null) {
            this.itemBlock = itemFactory.apply(this);
            this.itemBlock.setRegistryName(Objects.requireNonNull(getRegistryName()));
        }
        return itemBlock;
    }

    @Override
    @Nonnull
    public BlockBase setCreativeTab(@Nonnull CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    @Nonnull
    public BlockBase setSoundType(@Nonnull SoundType sound) {
        super.setSoundType(sound);
        return this;
    }

    @Override
    @Nonnull
    public BlockBase setLightOpacity(int opacity) {
        super.setLightOpacity(opacity);
        return this;
    }

    @Override
    @Nonnull
    public BlockBase setLightLevel(float value) {
        super.setLightLevel(value);
        return this;
    }

    @Override
    @Nonnull
    public BlockBase setResistance(float resistance) {
        super.setResistance(resistance);
        return this;
    }

    @Override
    @Nonnull
    public BlockBase setHardness(float hardness) {
        super.setHardness(hardness);
        return this;
    }

    @Override
    @Nonnull
    public BlockBase setBlockUnbreakable() {
        super.setBlockUnbreakable();
        return this;
    }

    @Override
    @Nonnull
    public BlockBase setTickRandomly(boolean shouldTick) {
        super.setTickRandomly(shouldTick);
        return this;
    }

    public BlockBase setEntityDestroyMode(@Nullable Function<Entity, Event.Result> mode) {
        this.entityDestroy = mode;
        return this;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (entityDestroy == null)
            return super.canEntityDestroy(state, world, pos, entity);
        Event.Result result = entityDestroy.apply(entity);
        switch (result) {
            case DENY:
                return false;
            case ALLOW:
                return true;
            case DEFAULT:
            default:
                return super.canEntityDestroy(state, world, pos, entity);
        }
    }

    public BlockBase setFullCube(boolean fullCube) {
        this.fullCube = fullCube;
        return this;
    }

    public BlockBase setOpaqueCube(boolean opaqueCube) {
        this.opaqueCube = opaqueCube;
        return this;
    }

    @Override
    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return fullCube;
    }

    @Override
    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return opaqueCube;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (boundingBox == null)
            return super.getBoundingBox(state, source, pos);
        return boundingBox;
    }

    @Override
    @SuppressWarnings("deprecation")
    @Deprecated
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        if (boundingBox == null)
            return super.getCollisionBoundingBox(blockState, worldIn, pos);
        return boundingBox;
    }

    public BlockBase setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
        return this;
    }

    public BlockBase setHarvest(String toolClass, int level) {
        super.setHarvestLevel(toolClass, level);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public BlockBase setHarvest(String toolClass, int level, IBlockState state) {
        super.setHarvestLevel(toolClass, level, state);
        return this;
    }

    public BlockBase setPiston(EnumPushReaction mobility) {
        this.pistonMovable = mobility;
        return this;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        if (hasTileEntity(state))
            return EnumPushReaction.BLOCK;
        if (pistonMovable == null)
            return super.getMobilityFlag(state);
        return pistonMovable;
    }
}
