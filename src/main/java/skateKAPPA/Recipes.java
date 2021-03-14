package skateKAPPA;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class Recipes {

    private static final IForgeRegistry<IRecipe> recipes = GameRegistry.findRegistry(IRecipe.class);
    private static int next_id = 0;

    private Recipes() {
        
    }

    public static void register() {
        addShapeless(ModItems.banPowder, Items.GUNPOWDER, Items.MELON);
    }

    private static ResourceLocation newRL() {
        return new ResourceLocation(SkateKAPPA.MODID, "recipe" + (next_id++));
    }

    private static void addShaped(ItemStack output, Object... inputs) {
        recipes.register(new ShapedOreRecipe(newRL(), output, inputs).setRegistryName(newRL()));
    }

    private static void addShaped(Item output, Object... inputs) {
        addShaped(new ItemStack(output), inputs);
    }

    private static void addShaped(Block output, Object... inputs) {
        addShaped(new ItemStack(output), inputs);
    }

    private static void addShapeless(ItemStack output, Object... inputs) {
        recipes.register(new ShapelessOreRecipe(newRL(), output, inputs).setRegistryName(newRL()));
    }

    @SuppressWarnings("SameParameterValue")
    private static void addShapeless(Item output, Object... inputs) {
        addShapeless(new ItemStack(output), inputs);
    }

    private static void addShapeless(Block output, Object... inputs) {
        addShapeless(new ItemStack(output), inputs);
    }

    @SuppressWarnings("unused")
    private static void addFurnace(Object input, Object output) {
        addFurnace(input, output, 0);
    }

    @SuppressWarnings("SameParameterValue")
    private static void addFurnace(Object input, Object output, int xp) {
        ItemStack[] in = getStacks(input);
        ItemStack[] out = getStacks(output);
        for (ItemStack s1 : in) {
            for (ItemStack s2 : out) {
                GameRegistry.addSmelting(s1, s2, xp);
            }
        }
    }

    private static ItemStack[] getStacks(Object object) {
        if (object instanceof ItemStack)
            return new ItemStack[]{(ItemStack) object};
        else if (object instanceof Item)
            return new ItemStack[]{new ItemStack((Item) object)};
        else if (object instanceof Block)
            return new ItemStack[]{new ItemStack((Block) object)};
        else if (object instanceof String)
            return OreDictionary.getOres((String) object).toArray(new ItemStack[]{});
        else
            return new ItemStack[]{};
    }
}
