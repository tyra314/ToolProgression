package tyra314.toolprogression.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = ToolProgressionCoreMod.MODID,
        certificateFingerprint = tyra314.toolprogression.ToolProgressionMod.SHA_HASH,
        version = tyra314.toolprogression.ToolProgressionMod.VERSION,
        dependencies = "before:toolprogression",
        acceptedMinecraftVersions = "[1.12,1.13)"
)
public class ToolProgressionCoreMod
{
    public static final String MODID = "toolprogression-core";
    public static final Logger logger = LogManager.getLogger(MODID);
    @SuppressWarnings("unused")
    @Mod.Instance
    public static ToolProgressionCoreMod INSTANCE;

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        logger.warn("Invalid fingerprint detected! The file " +
                    event.getSource().getName() +
                    " may have been tampered with. This version will NOT be supported by the author!");
    }
}
