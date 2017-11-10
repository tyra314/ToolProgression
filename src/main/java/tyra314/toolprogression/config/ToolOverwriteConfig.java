package tyra314.toolprogression.config;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.harvest.ToolOverwrite;

import java.util.HashMap;
import java.util.Map;

public class ToolOverwriteConfig
{
    private final Configuration cfg;
    private final Map<ResourceLocation, ToolOverwrite> overwrites = new HashMap<>();


    ToolOverwriteConfig(Configuration file)
    {
        cfg = file;
    }

    public void load()
    {
        try
        {
            cfg.load();

            cfg.addCustomCategoryComment("tool",
                    "To add any overwrites, simply copy them over from the tools.cfg");

            overwrites.clear();

            for (Map.Entry<String, Property> tool : cfg.getCategory("tool").entrySet())
            {
                ResourceLocation rl = new ResourceLocation(tool.getKey());

                ToolOverwrite overwrite = ToolOverwrite.readFromConfig(tool.getValue().getString());
                overwrites.put(rl, overwrite);
            }
        }
        catch (Exception e)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem loading tool overwrites file!", e);
        }
        finally
        {
            if (cfg.hasChanged())
            {
                cfg.save();
            }
        }
    }

    public void save()
    {
        try
        {
            cfg.addCustomCategoryComment("tool",
                    "To add any overwrites, simply copy them over from the tools.cfg");

            ConfigCategory tool = cfg.getCategory("tool");

            tool.clear();

            for (Map.Entry<ResourceLocation, ToolOverwrite> entry : overwrites.entrySet())
            {
                tool.put(entry.getKey().toString(),
                        new Property(entry.getKey().toString(),
                                entry.getValue().getConfig(),
                                Property.Type.STRING));
            }
        }
        catch (Exception e)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem saving tool overwrites file!", e);
        }
        finally
        {
            cfg.save();
        }
    }

    public ToolOverwrite get(Item item)
    {
        if (overwrites.containsKey(item.getRegistryName()))
        {
            return overwrites.get(item.getRegistryName());
        }

        return null;
    }

    public void unset(Item item)
    {
        overwrites.remove(item.getRegistryName());
    }


    public void set(Item item, ToolOverwrite overwrite)
    {
        overwrites.put(item.getRegistryName(), overwrite);
    }
}
