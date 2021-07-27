package skateKAPPA.trigger;

import net.minecraft.advancements.CriteriaTriggers;

public class ModTriggers {

    public static final DernikSheepTrigger DERNIK_SHEEP = new DernikSheepTrigger();
    public static final LogiSnakeTrigger LOGISNAKE = new LogiSnakeTrigger();
    public static final Nether66Trigger NETHER66 = new Nether66Trigger();
    
    public static void register() {
        CriteriaTriggers.register(DERNIK_SHEEP);
        CriteriaTriggers.register(LOGISNAKE);
        CriteriaTriggers.register(NETHER66);
    }
}
