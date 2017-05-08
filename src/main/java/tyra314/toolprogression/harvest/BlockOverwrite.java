package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.config.ConfigHandler;

public class BlockOverwrite
{
    private String toolclass;
    private int level;

    public String getConfig()
    {
        return toolclass + "=" + String.valueOf(level);
    }

    public void addOverwrite(String toolClass, int level)
    {
        this.toolclass = toolClass;
        this.level = level;
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

    public void apply(IBlockState state)
    {
        state.getBlock().setHarvestLevel(toolclass, level, state);

        ToolProgressionMod.logger.log(Level.INFO, String.format("Applying overwrite to block %s: %s %d", BlockHelper.getKeyString(state), toolclass, level));
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
