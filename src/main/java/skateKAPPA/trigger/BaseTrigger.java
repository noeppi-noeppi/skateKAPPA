package skateKAPPA.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

public abstract class BaseTrigger<T extends ICriterionInstance & Predicate<EntityPlayerMP>> implements ICriterionTrigger<T> {

    private final Map<PlayerAdvancements, Listeners> listeners = new HashMap<>();

    @Nonnull
    @Override
    public abstract ResourceLocation getId();

    @Override
    public void addListener(@Nonnull PlayerAdvancements advancements, @Nonnull ICriterionTrigger.Listener<T> listener) {
        this.listeners.computeIfAbsent(advancements, Listeners::new).add(listener);
    }

    @Override
    public void removeListener(@Nonnull PlayerAdvancements advancements, @Nonnull ICriterionTrigger.Listener<T> listener) {
        if (this.listeners.containsKey(advancements)) {
            Listeners listeners = this.listeners.get(advancements);
            listeners.remove(listener);
            if (listeners.isEmpty()) this.listeners.remove(advancements);
        }
    }

    @Override
    public void removeAllListeners(@Nonnull PlayerAdvancements advancements) {
        this.listeners.remove(advancements);
    }

    @Nonnull
    @Override
    public abstract T deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context);

    public void trigger(EntityPlayerMP player) {
        if (this.listeners.containsKey(player.getAdvancements())) {
            this.listeners.get(player.getAdvancements()).trigger(player);
        }
    }

    private class Listeners {
        
        private final PlayerAdvancements advancements;
        private final Set<Listener<T>> listeners;

        public Listeners(PlayerAdvancements advancements) {
            this.advancements = advancements;
            this.listeners = new HashSet<>();
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<T> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<T> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(EntityPlayerMP player) {
            List<Listener<T>> list = new ArrayList<>();
            for (ICriterionTrigger.Listener<T> listener : this.listeners) {
                if ((listener.getCriterionInstance()).test(player)) {
                    list.add(listener);
                }
            }

            if (!list.isEmpty()) {
                for (ICriterionTrigger.Listener<T> listener : list) {
                    listener.grantCriterion(this.advancements);
                }
            }
        }
    }
}
