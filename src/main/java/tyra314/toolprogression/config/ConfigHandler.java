package tyra314.toolprogression.config;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.harvest.HarvestLevelName;
import tyra314.toolprogression.proxy.CommonProxy;

public class ConfigHandler
{

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_COMPAT = "compat";
    private static final String CATEGORY_MINING_LEVEL = "mining_levels";
    private static final String CATEGORY_TOOLTIP = "tooltip";

    public static boolean all_blocks_destroyable = false;

    public static boolean tooltip_enabled = true;

    public static boolean waila_enabled = true;
    public static boolean waila_show_harvestable = false;

    public static boolean tconstruct_overwrite = true;

    public static boolean game_stages_compat = true;
    public static boolean dupe_fix_hacky_macky = false;

    public static boolean cofh_compat = true;

    public static final ToolOverwriteConfig toolOverwrites = new ToolOverwriteConfig(CommonProxy
            .tool_overwrites_config);

    public static final BlockOverwriteConfig blockOverwrites = new BlockOverwriteConfig(CommonProxy
            .block_overwrites_config);

    public static final MaterialOverwriteConfig matOverwrites = new MaterialOverwriteConfig(CommonProxy
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


        for (String name : names)
        {
            HarvestLevelName level = HarvestLevelName.readFromConfig(name);
            if (level != null)
            {
                HarvestLevelName.levels.put(level.getLevel(), level);

                ToolProgressionMod.logger.log(Level.INFO, String.format("Mining level: %d - %s",
                        level.getLevel(), level.getName()));
            }
            else
            {
                ToolProgressionMod.logger.log(Level.WARN, "Problem parsing harvest level: ", name);
            }
        }

        ToolProgressionMod.logger.log(Level.INFO, "Done applying mining levels.");
    }

    private static void initGeneralConfig(Configuration cfg)
    {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");

        tooltip_enabled =
                cfg.getBoolean("enabled",
                        CATEGORY_TOOLTIP,
                        tooltip_enabled,
                        "Set this to true if you like to get extended tooltips.");

        cfg.addCustomCategoryComment(CATEGORY_COMPAT, "Mod compatibility configuration");

        waila_enabled =
                cfg.getBoolean("enabled",
                        CATEGORY_COMPAT,
                        waila_enabled,
                        "Set this to true to enable the built-in WAILA plugin.");
        waila_show_harvestable =
                cfg.getBoolean("show_harvestable",
                        CATEGORY_COMPAT,
                        waila_show_harvestable,
                        "Set this to true to always show harvestability.");

        tconstruct_overwrite = cfg.getBoolean("tconstruct", CATEGORY_COMPAT,
                tconstruct_overwrite, "Set this to false, to leave Tinkers alone.");

        cofh_compat = cfg.getBoolean("cofh", CATEGORY_COMPAT,
                cofh_compat, "Set this to false, to leave CoFHCore alone.");

        game_stages_compat = cfg.getBoolean("gamestages", CATEGORY_COMPAT,
                game_stages_compat, "Set this to false, to leave GameStages alone.");

        dupe_fix_hacky_macky = cfg.getBoolean("dupe_fix_hacky_macky_voodoo_magic_wizardry", CATEGORY_GENERAL,
                dupe_fix_hacky_macky, "You have a dupe bug? You came in my issue tracker yelling at me? I cried and told you to active this? - If you can answer all these questions with yes, go ahead and set it to true. Otherwise, this setting is better off be left alone. Just like me. Forever alone. T__T");

        all_blocks_destroyable = cfg.getBoolean("all_blocks_destroyable", CATEGORY_GENERAL,
                all_blocks_destroyable, "Set this to true, if you want to be able to just destroy not harvestable blocks.");

    }
}
