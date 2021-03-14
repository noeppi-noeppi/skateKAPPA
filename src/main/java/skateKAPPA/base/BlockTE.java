package skateKAPPA.base;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class BlockTE<T extends TileEntity> extends BlockBase implements ITileEntityProvider {

    protected final Class<? extends T> teClass;
    private final Constructor<? extends T> constructor;

    public BlockTE(Block parentMaterial, String name, Class<? extends T> teClass) {
        this(parentMaterial.getDefaultState().getMaterial(), name, teClass);
    }

    public BlockTE(Material material, String name, Class<? extends T> teClass) {
        super(material, name);
        this.teClass = teClass;
        Constructor<? extends T> c = null;
        try {
            c = teClass.getConstructor(World.class);
        } catch (NoSuchMethodException e) {
            //
        }
        constructor = c;
    }

    public BlockTE(Block parentMaterial, String name, Class<T> teClass, Function<Block, Item> itemFactory) {
        this(parentMaterial.getDefaultState().getMaterial(), name, teClass, itemFactory);
    }

    public BlockTE(Material material, String name, Class<T> teClass, Function<Block, Item> itemFactory) {
        super(material, name, itemFactory);
        this.teClass = teClass;
        Constructor<T> c = null;
        try {
            c = teClass.getConstructor(World.class);
        } catch (NoSuchMethodException e) {
            //
        }
        constructor = c;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.OFF_HAND)
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        if (TileEntityBase.class.isAssignableFrom(teClass)) {
            TileEntityBase te = (TileEntityBase) getTile(worldIn, pos);
            if (te != null) {
                switch (te.getActivationType()) {
                    case INFO_TEXT:
                        if (worldIn.isRemote) {
                            return true;
                        } else {
                            ITextComponent[] text = te.getInfoText();
                            if (text != null) {
                                for (ITextComponent t : text) {
                                    playerIn.sendMessage(t);
                                }
                                return true;
                            }
                            return false;
                        }
                    case TE_HANDLER:
                        if (worldIn.isRemote) {
                            return true;
                        } else {
                            return te.handleActivition(playerIn, hand);
                        }
                    case NONE:
                        return false;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        if (constructor == null) {
            try {
                return teClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        } else {
            try {
                return constructor.newInstance(worldIn);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public final boolean hasTileEntity() {
        return true;
    }

    @Override
    public final boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public final TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return createNewTileEntity(world, getMetaFromState(state));
    }

    public final T getTile(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && teClass.isAssignableFrom(tile.getClass()))
            //noinspection unchecked
            return (T) tile;
        return null;
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!worldIn.isRemote) {
            T t = getTile(worldIn, pos);
            if (t instanceof TileEntityBase)
                ((TileEntityBase) t).breakBlock();
            worldIn.removeTileEntity(pos);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @Nonnull
    public BlockTE<T> setCreativeTab(@Nonnull CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    @Nonnull
    public BlockTE<T> setSoundType(@Nonnull SoundType sound) {
        super.setSoundType(sound);
        return this;
    }

    @Override
    @Nonnull
    public BlockTE<T> setLightOpacity(int opacity) {
        super.setLightOpacity(opacity);
        return this;
    }

    @Override
    @Nonnull
    public BlockTE<T> setLightLevel(float value) {
        super.setLightLevel(value);
        return this;
    }

    @Override
    @Nonnull
    public BlockTE<T> setResistance(float resistance) {
        super.setResistance(resistance);
        return this;
    }

    @Override
    @Nonnull
    public BlockTE<T> setHardness(float hardness) {
        super.setHardness(hardness);
        return this;
    }

    @Override
    @Nonnull
    public BlockTE<T> setBlockUnbreakable() {
        super.setBlockUnbreakable();
        return this;
    }

    @Override
    @Nonnull
    public BlockTE<T> setTickRandomly(boolean shouldTick) {
        super.setTickRandomly(shouldTick);
        return this;
    }

    public BlockTE<T> setEntityDestroyMode(@Nullable Function<Entity, Event.Result> mode) {
        super.setEntityDestroyMode(mode);
        return this;
    }

    @Override
    public BlockTE<T> setFullCube(boolean fullCube) {
        super.setFullCube(fullCube);
        return this;
    }

    @Override
    public BlockTE<T> setOpaqueCube(boolean opaqueCube) {
        super.setOpaqueCube(opaqueCube);
        return this;
    }

    @Override
    public BlockTE<T> setBoundingBox(AxisAlignedBB boundingBox) {
        super.setBoundingBox(boundingBox);
        return this;
    }

    @Override
    public BlockTE<T> setHarvest(String toolClass, int level) {
        super.setHarvest(toolClass, level);
        return this;
    }

    @Override
    public BlockTE<T> setHarvest(String toolClass, int level, IBlockState state) {
        super.setHarvest(toolClass, level, state);
        return this;
    }
}
