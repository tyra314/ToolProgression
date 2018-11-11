package tyra314.toolprogression.harvest;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import tyra314.toolprogression.api.OverwrittenContent;
import tyra314.toolprogression.config.ConfigHandler;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockHelper
{
    private static final String REGEX =
            "^(?<effective>\\?)?(?<toolclass>[^\\?=]+)=(?<level>-?\\d+)(@(?<hardness>.+))?$";

    private static final Pattern pattern;

    static
    {
        pattern = Pattern.compile(REGEX);
    }

    private static ItemStack[] tools = null;

    static
    {
        tools = new ItemStack[]{
                new ItemStack(Items.WOODEN_PICKAXE),
                new ItemStack(Items.WOODEN_SHOVEL),
                new ItemStack(Items.WOODEN_AXE)};
    }


    static private boolean isToolEffective(ItemStack item, IBlockState state)
    {
        ItemTool tool = (ItemTool) item.getItem();

        return item.getDestroySpeed(state) >= tool.toolMaterial.getEfficiency();
    }

    static String getConfigString(IBlockState state)
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
                if (isToolEffective(tools[i], state))
                {
                    String toolclass =
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

    public static void applyToAll(Block block)
    {
        for (IBlockState state : block.getBlockState().getValidStates())
        {
            applyTo(state);
        }
    }

    static void applyTo(IBlockState state)
    {
        String key = getKeyString(state);

        BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(key);

        if (overwrite != null)
        {
            overwrite.apply(state);
        }
        OverwrittenContent.blocks.put(state.getBlock().getUnlocalizedName(), overwrite);
    }

    public static BlockOverwrite createFromConfigString(String config,
                                                        BlockOverwrite.OverwriteSource source, String key)
    {
        Matcher matcher = pattern.matcher(config);

        if (!matcher.find())
        {
            return null;
        }

        String toolClass = matcher.group("toolclass");
        int level = Integer.parseInt(matcher.group("level"));
        boolean toolRequired = matcher.group("effective") == null;

        BlockOverwrite b = new BlockOverwrite(toolClass, level, toolRequired, source, key);

        String hardness = matcher.group("hardness");
        if (hardness != null)
        {
            b.hardness =  Float.parseFloat(hardness);
        }

        return b;
    }

    public static String getKeyFromItemStack(ItemStack stack)
    {
        Item item = stack.getItem();
        ItemBlock iblock = (ItemBlock) item;
        Block block = iblock.getBlock();

        // I don't know, why we shouldn't use that here. Enlighten me, if you do. Thanks.
        @SuppressWarnings("deprecation")
        IBlockState state = block.getStateFromMeta(item.getDamage(stack));

        return getKeyString(state);
    }
}
