package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BlockOverwrite
{
    String toolclass;
    int level;

    public final static Map<ResourceLocation, BlockOverwrite> overwrites = new HashMap<>();

    public static String getConfig(Block block)
    {
        String toolClass = block.getHarvestTool(block.getDefaultState());
        int level = block.getHarvestLevel(block.getDefaultState());

        return String.format("%s=%d", toolClass, level);
    }

    public static void applyTo(Block block)
    {
        if (overwrites.containsKey(block.getRegistryName()))
        {
            overwrites.get(block.getRegistryName()).apply(block);
        }
    }

    public void apply(Block block)
    {
        for (IBlockState state : block.getBlockState().getValidStates())
        {
            block.setHarvestLevel(toolclass, level, state);
            if (block.getMaterial(state).isToolNotRequired())
            {

            }
        }
    }

    public BlockOverwrite(String toolclass, int level)
    {
        this.toolclass = toolclass;
        this.level = level;
    }

    public static BlockOverwrite readFromConfig(String config)
    {
        String[] token = config.split("=");

        if (token.length == 2)
        {
            return new BlockOverwrite(token[0], Integer.parseInt(token[1]));
        }

        return null;
    }
}
