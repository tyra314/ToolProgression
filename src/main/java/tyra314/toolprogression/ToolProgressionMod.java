package tyra314.toolprogression;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tyra314.toolprogression.command.ToolProgressionCommand;
import tyra314.toolprogression.proxy.CommonProxy;

@Mod(modid = ToolProgressionMod.MODID,
        certificateFingerprint = ToolProgressionMod.SHA_HASH,
        version = ToolProgressionMod.VERSION,
        dependencies = "before:tconstruct;after:gamestages;after:orestages;before:metallurgy;before:aoa3;before:twilightforest;before:brandoncore;before:codechickenlib",
        acceptedMinecraftVersions = "[1.12,1.13)")
public class ToolProgressionMod
{
    public static final String MODID = "toolprogression";

    @SuppressWarnings("WeakerAccess")
    public static final String VERSION = "@VERSION@";

    @SuppressWarnings("WeakerAccess")
    public static final String SHA_HASH = "@FINGERPRINT@";

    @SuppressWarnings({"WeakerAccess", "unused"})
    @SidedProxy(clientSide = "tyra314.toolprogression.proxy.ClientProxy", serverSide = "tyra314.toolprogression.proxy.ServerProxy")
    public static CommonProxy proxy;

    @SuppressWarnings("unused")
    @Mod.Instance
    public static ToolProgressionMod INSTANCE;

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

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        logger.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}
