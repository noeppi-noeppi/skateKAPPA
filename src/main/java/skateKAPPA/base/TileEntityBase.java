package skateKAPPA.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class TileEntityBase extends TileEntity {

    private final Map<Capability<?>, Object> capabilities = new HashMap<>();

    protected final <T> void addCapability(Capability<T> capability, T instance) {
        capabilities.put(capability, instance);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capabilities.containsKey(capability))
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capabilities.containsKey(capability)) {
            @SuppressWarnings("unchecked")
            T t = (T) capabilities.get(capability);
            return t;
        }
        return super.getCapability(capability, facing);
    }

    protected ITextComponent[] getInfoText() {
        return null;
    }

    protected void breakBlock() {

    }

    protected ActivationType getActivationType() {
        return ActivationType.NONE;
    }

    protected boolean handleActivition(EntityPlayer player, EnumHand hand) {
        return false;
    }

    public enum ActivationType {
        INFO_TEXT,
        TE_HANDLER,
        NONE
    }
}
