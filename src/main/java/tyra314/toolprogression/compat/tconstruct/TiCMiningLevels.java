package tyra314.toolprogression.compat.tconstruct;

import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.HarvestLevel;

import java.util.List;
import java.util.Map;

public class TiCMiningLevels
{
    public static Map<Integer, String> getMiningLevels()
    {
        Map<Integer, String> miningLevels = null;

        try
        {
            Class clazz = Class.forName("slimeknights.tconstruct.library.utils.HarvestLevels");
            if (clazz != null && ConfigHandler.tconstruct_overwrite)
            {
                miningLevels = (Map<Integer, String>) clazz.getField("harvestLevelNames")
                        .get(null);
            }
        }
        catch (Exception e)
        {
            ToolProgressionMod.logger.info("Tinkers Construct not found.");
        }

        return miningLevels;
    }

    public static void overwriteMiningLevels()
    {
        Map<Integer, String> tcmininglevels = TiCMiningLevels.getMiningLevels();

        tcmininglevels.clear();

        for (HarvestLevel level : HarvestLevel.levels.values())
        {
            tcmininglevels.put(level.getLevel(), level.getFormatted());
        }

        ToolProgressionMod.logger.log(Level.INFO, "Applied compat for TiC mining levels");

    }

}
