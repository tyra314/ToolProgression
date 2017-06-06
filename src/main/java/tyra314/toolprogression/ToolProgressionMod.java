package tyra314.toolprogression;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tyra314.toolprogression.command.ToolProgressionCommand;
import tyra314.toolprogression.proxy.CommonProxy;

@Mod(modid = ToolProgressionMod.MODID, version = ToolProgressionMod.VERSION, dependencies = "after:tconstruct")
public class ToolProgressionMod
{
    public static final String MODID = "toolprogression";
    @SuppressWarnings("WeakerAccess")
    public static final String VERSION = "1.0";

    @SuppressWarnings({"WeakerAccess", "unused"})
    @SidedProxy(clientSide = "tyra314.toolprogression.proxy.ClientProxy", serverSide = "tyra314.toolprogression.proxy.ServerProxy")
    public static CommonProxy proxy;

    @SuppressWarnings("unused")
    @Mod.Instance
    public static ToolProgressionMod instance;

    public static final Logger logger = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ToolProgressionCommand());
    }
}
