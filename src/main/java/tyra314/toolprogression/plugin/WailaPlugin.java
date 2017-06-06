package tyra314.toolprogression.plugin;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import tyra314.toolprogression.config.ConfigHandler;

@mcp.mobius.waila.api.WailaPlugin
public class WailaPlugin implements IWailaPlugin
{
    @Override
    public void register(IWailaRegistrar registrar)
    {
        if (ConfigHandler.waila_enabled)
        {
            registrar.registerTailProvider(new WailaTooltipProvider(), Block.class);
        }
    }
}