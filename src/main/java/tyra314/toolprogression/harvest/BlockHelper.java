package tyra314.toolprogression.harvest;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import javax.annotation.Nonnull;

public class BlockHelper
{
    private static ItemStack[] tools = null;

    static
    {
        tools = new ItemStack[]{
                new ItemStack(Items.WOODEN_PICKAXE),
                new ItemStack(Items.WOODEN_SHOVEL),
                new ItemStack(Items.WOODEN_AXE)};
    }


    static private boolean guessTool(ItemStack item, IBlockState state)
    {
        ItemTool tool = (ItemTool) item.getItem();

        return item.getDestroySpeed(state) >= tool.toolMaterial.getEfficiency();
    }

    static String getConfigFromState(IBlockState state)
    {
        Block block = state.getBlock();

        String toolClass = block.getHarvestTool(state);

        // Vanilla sucks, some Blocks actually need a tool, but don't reveal which one...
        if (toolClass == null && !state.getMaterial().isToolNotRequired() &&
            state.getBlock() != Blocks.BEDROCK &&
            state.getBlock() != Blocks.END_PORTAL_FRAME)
        {
            // so we have to fix that :(
            for (int i = 0; i < tools.length; i++)
            {
                if (guessTool(tools[i], state))
                {
                    String
                            toolclass =
                            tools[i].getItem().getToolClasses(tools[i]).iterator().next();

                    // let's do everyone a favor, do we?
                    // This is EVIL, but ... you know... ¯\_(ツ)_/¯
                    state.getBlock().setHarvestLevel(toolclass, 0, state);
                    return String.format("%s=%d", toolclass, 0);
                }
            }
        }

        if (toolClass == null)
        {
            return "null=-1";
        }

        int level = block.getHarvestLevel(state);

        return (state.getMaterial().isToolNotRequired() ? "?" : "") +
               String.format("%s=%d", toolClass, level);
    }

    public static String getKeyString(@Nonnull IBlockState state)
    {
        Block block = state.getBlock();

        //noinspection ConstantConditions
        String key = block.getRegistryName().toString();

        int meta = block.getMetaFromState(state);

        return String.format("%s:%d", key, meta);
    }
}
