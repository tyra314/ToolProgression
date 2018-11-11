package tyra314.toolprogression.harvest;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.api.OverwrittenContent;
import tyra314.toolprogression.compat.cofh.CoFHHelper;
import tyra314.toolprogression.config.ConfigHandler;

public class ToolHelper
{
    public static void setHarvestLevel(Item item, String tool_class, int level)
    {
        if (CoFHHelper.isLoaded() && CoFHHelper.isInstance(item))
        {
            CoFHHelper.setHarvestLevel(item, level);
            if(level >= 0)
            {
                CoFHHelper.addToolClass(item, tool_class);
            }
        }
        else
        {
            item.setHarvestLevel(tool_class, level);
        }
    }

    public static void applyToItem(Item item)
    {
        ToolOverwrite overwrite = ConfigHandler.toolOverwrites.get(item);

        if (overwrite != null)
        {
            overwrite.apply(item);
        }
        OverwrittenContent.tools.put(item.getUnlocalizedName(), overwrite);
    }

    public static ToolOverwrite createFromConfigString(String config)
    {
        ToolOverwrite overwrite = new ToolOverwrite();

        String[] tokens = config.split(",");

        for (String token : tokens)
        {
            String[] tok = token.split("=");

            if (tok.length == 2)
            {
                overwrite.addOverwrite(tok[0], Integer.parseInt(tok[1]));
            }
            else
            {
                ToolProgressionMod.logger.log(Level.WARN,
                        "Problem parsing tool overwrite: ",
                        config);
            }
        }

        return overwrite;
    }

    static String getConfigString(Item item)
    {
        StringBuilder config = new StringBuilder();

        for (String toolclass : item.getToolClasses(new ItemStack(item)))
        {
            int level = item.getHarvestLevel(new ItemStack(item), toolclass, null, null);

            String config_line = String.format("%s=%d", toolclass, level);

            if (config.length() > 0)
            {
                config.append(",").append(config_line);
            }
            else
            {
                config.append(config_line);
            }
        }

        return config.toString();
    }
}
