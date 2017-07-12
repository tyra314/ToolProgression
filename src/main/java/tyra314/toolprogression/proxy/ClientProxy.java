package tyra314.toolprogression.proxy;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tyra314.toolprogression.handlers.TooltipEventHandler;


@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);

		MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
	}
}
