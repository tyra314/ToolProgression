package tyra314.toolprogression.compat.top;

import net.minecraftforge.fml.common.FMLModContainer;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.api.OverwrittenContent;
import tyra314.toolprogression.harvest.HarvestLevelName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TOPHelper
{
    public static final String HTI_NAME = "mcjty.theoneprobe.apiimpl.providers.HarvestInfoTools";
    private static Class<?> harvest_info = null;

    static
    {
        try
        {
            harvest_info = Class.forName(HTI_NAME);
        }
        catch (ClassNotFoundException ignored)
        {
            ToolProgressionMod.logger.info("TheOneProbe isn't loaded.");
        }
    }

    public static boolean isLoaded()
    {
        return harvest_info != null;
    }

    public static void overwriteHarvestLevels()
    {
        List<String> list = new ArrayList<String>();

        for (HarvestLevelName level : OverwrittenContent.mining_level.values())
        {
            list.add(level.getFormatted());
        }

        String[] new_harvest_levels = list.toArray(new String[0]);

        try
        {
            Field f1 = harvest_info.getDeclaredField("harvestLevels");
            f1.setAccessible(true);
            f1.set(null, new_harvest_levels);

            ToolProgressionMod.logger.log(Level.INFO, "Applied compat for TheOneProbe mining levels");
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Couldn't apply compat for TheOneProbe mining levels");
        }
    }
}
