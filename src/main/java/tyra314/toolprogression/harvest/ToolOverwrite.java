package tyra314.toolprogression.harvest;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ToolOverwrite
{
    public final Map<String, Integer> harvest_levels = new HashMap<>();

    public String getConfig()
    {
        StringBuilder res = new StringBuilder();
        String prefix = "";

        for (Map.Entry<String, Integer> entry : harvest_levels.entrySet())
        {
            res.append(prefix);
            res.append(entry.getKey()).append('=').append(entry.getValue());

            prefix = ",";
        }

        return res.toString();
    }

    public void addOverwrite(String toolclass, int level)
    {
        harvest_levels.put(toolclass, level);
    }

    public void apply(Item item)
    {
        // if there is an overwrite, we need to disable all old tool classes
        Set<String> toolClasses = new HashSet(item.getToolClasses(new ItemStack(item)));
        for (String entry : toolClasses)
        {
            ToolHelper.setHarvestLevel(item, entry, -1);
        }

        for (Map.Entry<String, Integer> entry : harvest_levels.entrySet())
        {
            ToolHelper.setHarvestLevel(item, entry.getKey(), entry.getValue());

            ToolProgressionMod.logger.log(Level.INFO,
                    String.format("Applying overwrite to item %s: %s %d",
                            item.getRegistryName(),
                            entry.getKey(),
                            entry.getValue()));

        }
    }
}
