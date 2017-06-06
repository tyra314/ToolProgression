package tyra314.toolprogression.config;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.compat.tconstruct.TCMiningLevels;
import tyra314.toolprogression.handlers.TooltipEventHandler;
import tyra314.toolprogression.harvest.HarvestLevel;
import tyra314.toolprogression.proxy.CommonProxy;

import java.util.Map;

public class ConfigHandler
{

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_COMPAT = "compat";
    private static final String CATEGORY_MINING_LEVEL = "mining_levels";
    private static final String CATEGORY_TOOLTIP = "tooltip";
    private static final String CATEGORY_WAILA = "waila";

    public static boolean waila_enabled = true;
    public static boolean waila_show_harvestable = false;

    public static boolean tconstruct_overwrite = true;

    public static ToolOverwriteConfig toolOverwrites = new ToolOverwriteConfig(CommonProxy
            .tool_overwrites_config);

    public static BlockOverwriteConfig blockOverwrites = new BlockOverwriteConfig(CommonProxy
            .block_overwrites_config);

    public static MaterialOverwriteConfig matOverwrites = new MaterialOverwriteConfig(CommonProxy
            .mat_overwrites_config);

    public static void readBaseConfig()
    {
        Configuration base_cfg = CommonProxy.base_config;
        Configuration level_names_cfg = CommonProxy.mining_level_config;

        Configuration block_overwrites_config = CommonProxy.block_overwrites_config;

        try
        {
            base_cfg.load();
            level_names_cfg.load();

            block_overwrites_config.load();

            initGeneralConfig(base_cfg);
            initMiningLevelConfig(level_names_cfg);

            blockOverwrites.load();
            toolOverwrites.load();
            matOverwrites.load();
        }
        catch (Exception e1)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem loading config file!", e1);
        }
        finally
        {
            if (base_cfg.hasChanged())
            {
                base_cfg.save();
            }

            if (level_names_cfg.hasChanged())
            {
                level_names_cfg.save();
            }
        }
    }

    private static void initMiningLevelConfig(Configuration cfg)
    {
        ToolProgressionMod.logger.info("Applying Mining levels.");


        cfg.addCustomCategoryComment(CATEGORY_MINING_LEVEL, "The list of all mining level names");

        String[] names = {"0:Wood", "1:Stone:§8%s", "2:Iron:§f%s", "3:Diamond:§b%s"};
        names = cfg.getStringList("names", CATEGORY_MINING_LEVEL, names, "Canonical names");

        Map<Integer, String> tcmininglevels = TCMiningLevels.getMiningLevels();

        if (tcmininglevels != null)
        {
            tcmininglevels.clear();
        }

        for (String name : names)
        {
            HarvestLevel level = HarvestLevel.readFromConfig(name);
            if (level != null)
            {
                HarvestLevel.levels.put(level.getLevel(), level);

                ToolProgressionMod.logger.log(Level.INFO, String.format("Mining level: %d - %s",
                        level.getLevel(), level.getName()));

                if (tcmininglevels != null)
                {
                    tcmininglevels.put(level.getLevel(), level.getFormatted());
                }
            }
            else
            {
                ToolProgressionMod.logger.log(Level.WARN, "Problem parsing harvest level: ", name);
            }
        }

        if (tcmininglevels != null)
        {
            ToolProgressionMod.logger.log(Level.INFO, "Applied compat for TiC mining levels");
        }

        ToolProgressionMod.logger.log(Level.INFO, "Done applying mining levels.");

    }

    private static void initGeneralConfig(Configuration cfg)
    {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");

        TooltipEventHandler.enabled =
                cfg.getBoolean("enabled",
                        CATEGORY_TOOLTIP,
                        TooltipEventHandler.enabled,
                        "Set this to true if you like to get extended tooltips.");

        waila_enabled =
                cfg.getBoolean("enabled",
                        CATEGORY_WAILA,
                        waila_enabled,
                        "Set this to true to enable the built-in WAILA plugin.");
        waila_show_harvestable =
                cfg.getBoolean("show_harvestable",
                        CATEGORY_WAILA,
                        waila_show_harvestable,
                        "Set this to true to always show harvestability.");

        cfg.addCustomCategoryComment(CATEGORY_COMPAT, "Mod compatibility configuration");

        tconstruct_overwrite = cfg.getBoolean("tconstruct", CATEGORY_COMPAT,
                tconstruct_overwrite, "Set this to false, to leave Tinkers alone.");
    }
}
