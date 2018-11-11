package tyra314.toolprogression.harvest;

import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;

@SuppressWarnings("WeakerAccess")
public class BlockOverwrite
{
    public String toolclass;
    public int level;
    public boolean toolRequired;
    public OverwriteSource source;
    public String sourceKey;
    public float hardness;
    public BlockOverwrite(String toolclass,
                          int level,
                          boolean toolRequired,
                          OverwriteSource source,
                          String sourceKey)
    {
        this.toolclass = toolclass;
        this.level = level;
        this.toolRequired = toolRequired;
        this.source = source;
        this.sourceKey = sourceKey;
        this.hardness = -1F;
    }

    public String getConfig()
    {
        StringBuilder str = new StringBuilder();

        if (!toolRequired)
        {
            str.append("?");
        }

        str.append(toolclass).append("=").append(level);

        if (hardness >= 0F)
        {
            str.append("@").append(hardness);
        }

        return str.toString();
    }

    public void addOverwrite(String toolClass, int level, boolean toolRequired, String key)
    {
        this.toolclass = toolClass;
        this.level = level;
        this.toolRequired = toolRequired;

        this.source = OverwriteSource.Single;
        this.sourceKey = key;
    }

    public void apply(IBlockState state)
    {
        if (hardness >= 0F)
        {
            state.getBlock().setHardness(hardness);
        }

        state.getBlock().setHarvestLevel(toolclass, level, state);

        ToolProgressionMod.logger.log(Level.INFO,
                String.format("Applying overwrite to block %s: %s %d",
                        BlockHelper.getKeyString(state),
                        toolclass,
                        level));
    }

    public enum OverwriteSource
    {
        OreDict(0),
        Wildcard(1),
        Single(2);

        private final int priority;

        OverwriteSource(int priority)
        {
            this.priority = priority;
        }
        
        public boolean hasLessPriority(OverwriteSource source)
        {
            return this.priority <= source.priority;
        }
    }
}
