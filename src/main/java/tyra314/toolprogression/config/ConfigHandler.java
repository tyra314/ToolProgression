package tyra314.toolprogression.config;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.handlers.TooltipEventHandler;
import tyra314.toolprogression.harvest.HarvestLevel;
import tyra314.toolprogression.proxy.CommonProxy;

public class ConfigHandler
{

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_MINING_LEVEL = "mining_levels";
    private static final String CATEGORY_TOOLTIP = "tooltip";


    public static void readBaseConfig()
    {
        Configuration base_cfg = CommonProxy.base_config;
        Configuration level_names_cfg = CommonProxy.mining_level_config;

        Configuration blocks_config = CommonProxy.blocks_config;
        Configuration block_overwrites_config = CommonProxy.block_overwrites_config;
        Configuration tools_config = CommonProxy.tools_config;
        Configuration tool_overwrites_config = CommonProxy.tool_overwrites_config;

        try
        {
            base_cfg.load();
            level_names_cfg.load();

            blocks_config.load();
            block_overwrites_config.load();
            tools_config.load();
            tool_overwrites_config.load();

            initGeneralConfig(base_cfg);
            initMiningLevelConfig(level_names_cfg);

        } catch (Exception e1)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally
        {
            if (base_cfg.hasChanged())
            {
                base_cfg.save();
            }

            if (level_names_cfg.hasChanged())
            {
                level_names_cfg.save();
            }

            if (block_overwrites_config.hasChanged())
            {
                block_overwrites_config.save();
            }

            if (tool_overwrites_config.hasChanged())
            {
                tool_overwrites_config.save();
            }
        }
    }

    private static void initMiningLevelConfig(Configuration cfg)
    {
        cfg.addCustomCategoryComment(CATEGORY_MINING_LEVEL, "The list of all mining level names");

        String[] names = {"0:Wood", "1:Stone:§8%s", "2:Iron:§f%s", "3:Diamond:§b%s"};
        names = cfg.getStringList("names", CATEGORY_MINING_LEVEL, names, "Canonical names");

        for (int i = 0; i < names.length; i++)
        {
            HarvestLevel level = HarvestLevel.readFromConfig(names[i]);
            if (level != null)
            {
                HarvestLevel.levels.put(level.getLevel(), level);
            } else
            {
                ToolProgressionMod.logger.log(Level.WARN, "Problem parsing harvest level: ", names[i]);
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg)
    {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");

        TooltipEventHandler.enabled = cfg.getBoolean("enabled", CATEGORY_TOOLTIP, TooltipEventHandler.enabled, "Set this to true if you like to get extended tooltips.");
    }

    private static void initBlockOverwriteConfig(Configuration cfg)
    {

    }

}
