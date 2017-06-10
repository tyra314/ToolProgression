package tyra314.toolprogression.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import tyra314.toolprogression.config.ConfigHandler;

public class WailaPlugin
{
    public static void register(IWailaRegistrar registrar)
    {
        if (ConfigHandler.waila_enabled)
        {
            registrar.registerTailProvider(new WailaTooltipProvider(), Block.class);
        }
    }

    public static void preInit()
    {
        FMLInterModComms.sendMessage("waila",
                "register",
                "tyra314.toolprogression.compat.waila.WailaPlugin.register");
    }
}
