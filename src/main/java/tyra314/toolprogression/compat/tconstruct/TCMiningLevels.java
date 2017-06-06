package tyra314.toolprogression.compat.tconstruct;

import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.config.ConfigHandler;

import java.util.Map;

public class TCMiningLevels
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

}
