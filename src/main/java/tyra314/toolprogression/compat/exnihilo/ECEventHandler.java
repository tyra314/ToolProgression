package tyra314.toolprogression.compat.exnihilo;

import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.item.ItemCompressedCrook;
import net.blay09.mods.excompressum.item.ItemCompressedHammer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ECEventHandler
{
    private static List<Item> hammers_and_crooks = new ArrayList<>();

    public static boolean isHammerOrCrook(ItemStack tool)
    {
        if (tool.isEmpty())
        {
            return false;
        }

        return hammers_and_crooks.contains(tool.getItem()) ||
               tool.getItem() instanceof ItemCompressedHammer ||
               tool.getItem() instanceof ItemCompressedCrook;

    }

    public static boolean canHarvest(ItemStack tool,
                                     IBlockState state)
    {
        if (hammers_and_crooks.contains(tool.getItem()))
        {
            return tool.canHarvestBlock(state);
        }
        else if (tool.getItem() instanceof ItemCompressedHammer)
        {
            ItemCompressedHammer ch = (ItemCompressedHammer) tool.getItem();
            return ch.canHarvestBlock(state, tool);
        }
        else if (tool.getItem() instanceof ItemCompressedCrook)
        {
            ItemCompressedCrook cc = (ItemCompressedCrook) tool.getItem();
            return cc.canHarvestBlock(state);
        }

        throw new IllegalArgumentException("Function called with invalid tool");
    }

    public static void postInit()
    {
        ExNihiloProvider en = ExCompressumAPI.getExNihilo();

        for (ExNihiloProvider.NihiloItems item : new ExNihiloProvider.NihiloItems[]{
                ExNihiloProvider.NihiloItems.HAMMER_WOODEN,
                ExNihiloProvider.NihiloItems.HAMMER_STONE,
                ExNihiloProvider.NihiloItems.HAMMER_IRON,
                ExNihiloProvider.NihiloItems.HAMMER_GOLD,
                ExNihiloProvider.NihiloItems.HAMMER_DIAMOND})
        {
            hammers_and_crooks.add(en.getNihiloItem(item).getItem());
        }
    }
}
