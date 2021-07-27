package skateKAPPA.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import skateKAPPA.SkateKAPPA;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class Nether66Trigger extends BaseTrigger<Nether66Trigger.Instance> {
    
    public static final ResourceLocation ID = new ResourceLocation(SkateKAPPA.MODID, "nether66");

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Nonnull
    @Override
    public Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
        return new Instance();
    }

    public static class Instance extends AbstractCriterionInstance implements Predicate<EntityPlayerMP> {

        public Instance() {
            super(ID);
        }

        @Override
        public boolean test(EntityPlayerMP entityPlayerMP) {
            return true;
        }
    }
}
