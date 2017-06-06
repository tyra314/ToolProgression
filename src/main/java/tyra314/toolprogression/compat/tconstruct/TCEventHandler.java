package tyra314.toolprogression.compat.tconstruct;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.MaterialEvent;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.proxy.CommonProxy;

public class TCEventHandler
{
    @SubscribeEvent
    public void handleMaterial(MaterialEvent.StatRegisterEvent<IMaterialStats> event)
    {
        String name = event.material.identifier;


        if (event.stats instanceof HeadMaterialStats)
        {
            HeadMaterialStats stats = (HeadMaterialStats) event.stats;

            CommonProxy.mats_config.getString(name, "material", String.valueOf(stats
                    .harvestLevel), name);

            ToolProgressionMod.logger.info("TiC Material registered: " + name);

            if (TCMaterial.hasOverwrite(event.material.identifier))
            {
                int newLevel = TCMaterial.getOverwrite(event.material.identifier);
                event.overrideResult(new HeadMaterialStats(stats.durability,
                        stats.miningspeed,
                        stats.attack,
                        newLevel));

                ToolProgressionMod.logger.info("TiC Material overwrite: " +
                                               String.valueOf(newLevel));
            }
        }
    }
}
