package tyra314.toolprogression.plugin;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;

@mcp.mobius.waila.api.WailaPlugin
public class WailaPlugin implements IWailaPlugin
{
    public static boolean enabled = true;

    @Override
    public void register(IWailaRegistrar registrar)
    {
        if (enabled)
        {
            registrar.registerTailProvider(new WailaTooltipProvider(), Block.class);
        }
    }
}
