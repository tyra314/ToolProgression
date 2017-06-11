package tyra314.toolprogression.compat.tconstruct;

public class TiCEventHandler
{
/*    @SubscribeEvent
    public void handleMaterial(MaterialEvent.StatRegisterEvent<IMaterialStats> event)
    {
        String name = event.material.identifier;


        if (event.stats instanceof HeadMaterialStats)
        {
            HeadMaterialStats stats = (HeadMaterialStats) event.stats;

            CommonProxy.mats_config.getString(name, "material", String.valueOf(stats
                    .harvestLevel), name);

            ToolProgressionMod.logger.info("TiC Material registered: " + name);

            if (TiCMaterial.hasOverwrite(event.material.identifier))
            {
                int newLevel = TiCMaterial.getOverwrite(event.material.identifier);
                event.overrideResult(new HeadMaterialStats(stats.durability,
                        stats.miningspeed,
                        stats.attack,
                        newLevel));

                ToolProgressionMod.logger.info("TiC Material overwrite: " +
                                               String.valueOf(newLevel));
            }
        }
    }*/
}
