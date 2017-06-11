package tyra314.toolprogression.compat.waila;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public class WailaPlugin
{
    public static void preInit()
    {
        FMLInterModComms.sendMessage("waila",
                "register",
                "tyra314.toolprogression.compat.waila.WailaTooltipProvider.register");
    }
}
