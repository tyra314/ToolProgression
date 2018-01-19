package tyra314.toolprogression.compat.exnihilo;

import net.minecraftforge.fml.common.Loader;

public class ECHelper
{
    public static boolean isLoaded()
    {
        return Loader.isModLoaded("excompressum");
    }


    public static void postInit()
    {
        if(isLoaded())
        {
            ECEventHandler.postInit();
        }
    }
}
