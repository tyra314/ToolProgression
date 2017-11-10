package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.config.ConfigHandler;

@SuppressWarnings("WeakerAccess")
public class BlockOverwrite
{
    public String toolclass;
    public int level;
    public boolean toolRequired;

    public BlockOverwrite(String toolclass, int level, boolean toolRequired)
    {
        this.toolclass = toolclass;
        this.level = level;
        this.toolRequired = toolRequired;
    }

    public static void applyToAllStates(Block block)
    {
        for (IBlockState state : block.getBlockState().getValidStates())
        {
            applyToState(state);
        }
    }

    static void applyToState(IBlockState state)
    {
        String key = BlockHelper.getKeyString(state);

        BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(key);

        if (overwrite != null)
        {
            overwrite.apply(state);
        }
    }

    public static BlockOverwrite readFromConfig(String config)
    {
        String[] token = config.split("=");

        if (token.length == 2)
        {
            if (token[0].startsWith("?"))
            {
                return new BlockOverwrite(token[0].substring(1), Integer.parseInt(token[1]), false);
            }
            else
            {
                return new BlockOverwrite(token[0], Integer.parseInt(token[1]), true);
            }
        }

        return null;
    }

    public String getConfig()
    {
        return (toolRequired ? "" : "?") + toolclass + "=" + String.valueOf(level);
    }

    public void addOverwrite(String toolClass, int level, boolean toolRequired)
    {
        this.toolclass = toolClass;
        this.level = level;
        this.toolRequired = toolRequired;
    }

    public void apply(IBlockState state)
    {
        state.getBlock().setHarvestLevel(toolclass, level, state);

        ToolProgressionMod.logger.log(Level.INFO,
                String.format("Applying overwrite to block %s: %s %d",
                        BlockHelper.getKeyString(state),
                        toolclass,
                        level));
    }
}
