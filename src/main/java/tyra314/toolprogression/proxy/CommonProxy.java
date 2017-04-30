package tyra314.toolprogression.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.handlers.CommonEventHandler;

import java.io.File;


public class CommonProxy
{

    // ConfigHandler instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e)
    {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "inca.cfg"));
        ConfigHandler.readConfig();
    }

    public void init(FMLInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }

    public void postInit(FMLPostInitializationEvent e)
    {
    }
}

