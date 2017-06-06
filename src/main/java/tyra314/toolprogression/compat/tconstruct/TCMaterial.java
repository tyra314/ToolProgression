package tyra314.toolprogression.compat.tconstruct;

import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.MaterialOverwrite;

public class TCMaterial
{
    public static boolean hasOverwrite(String identifier)
    {
        MaterialOverwrite mat = ConfigHandler.matOverwrites.get(identifier);

        return mat != null;
    }

    public static int getOverwrite(String identifier)
    {
        return ConfigHandler.matOverwrites.get(identifier).getHarvestLevel();

    }
}
