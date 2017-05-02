package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;

import java.util.HashMap;
import java.util.Map;

public class BlockOverwrite
{

    private final String toolclass;
    private final int level;

    public final static Map<String, BlockOverwrite> overwrites = new HashMap<>();

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

        if (overwrites.containsKey(key))
        {
            overwrites.get(key).apply(state);
        }
    }

    private void apply(IBlockState state)
    {
        state.getBlock().setHarvestLevel(toolclass, level, state);

        ToolProgressionMod.logger.log(Level.INFO, String.format("Applying overwrite to block %s: %s %d", BlockHelper.getKeyString(state), toolclass, level));
    }

    private BlockOverwrite(String toolclass, int level)
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
