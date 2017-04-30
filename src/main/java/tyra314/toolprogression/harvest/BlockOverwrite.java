package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BlockOverwrite
{
    public final static Map<ResourceLocation, BlockOverwrite> overwrites = new HashMap();

    public static String getConfig(Block block)
    {
        String toolClass = block.getHarvestTool(block.getDefaultState());
        int level = block.getHarvestLevel(block.getDefaultState());

        return String.format("%s=%d", toolClass, level);
    }

    public static void applyTo(Block block)
    {

    }
}
