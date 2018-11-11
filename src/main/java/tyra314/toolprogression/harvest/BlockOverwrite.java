package tyra314.toolprogression.harvest;

import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;

import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public class BlockOverwrite
{

    public String toolclass;
    public int level;
    public boolean toolRequired;
    public float hardness;

    public BlockOverwrite(String toolclass, int level, boolean toolRequired)
    {
        this.toolclass = toolclass;
        this.level = level;
        this.toolRequired = toolRequired;
        this.hardness = -1F;
    }


    public String getConfig()
    {
        StringBuilder str = new StringBuilder();

        if(!toolRequired)
        {
            str.append("?");
        }

        str.append(toolclass).append("=").append(level);

        if(hardness >= 0F)
        {
            str.append("@").append(hardness);
        }

        return str.toString();
    }

    public void addOverwrite(String toolClass, int level, boolean toolRequired)
    {
        this.toolclass = toolClass;
        this.level = level;
        this.toolRequired = toolRequired;
    }

    public void apply(IBlockState state)
    {
        if(hardness >= 0F)
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
}
