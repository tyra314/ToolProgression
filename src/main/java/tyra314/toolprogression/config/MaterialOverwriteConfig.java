package tyra314.toolprogression.config;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.harvest.MaterialOverwrite;

import java.util.HashMap;
import java.util.Map;

public class MaterialOverwriteConfig
{
    private final Configuration cfg;
    private final Map<String, MaterialOverwrite> overwrites = new HashMap<>();


    MaterialOverwriteConfig(Configuration file)
    {
        cfg = file;
    }

    public void load()
    {
        try
        {
            cfg.load();

            cfg.addCustomCategoryComment("material",
                    "To add any overwrites, simply copy them over from the material.cfg");

            overwrites.clear();

            for (Map.Entry<String, Property> mat : cfg.getCategory("material").entrySet())
            {
                MaterialOverwrite overwrite = MaterialOverwrite.readFromConfig(mat.getValue()
                        .getString());
                overwrites.put(mat.getKey(), overwrite);
            }
        }
        catch (Exception e)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem loading material overwrites file!",
                    e);
        }
        finally
        {
            if (cfg.hasChanged())
            {
                cfg.save();
            }
        }
    }

    @SuppressWarnings("unused")
    public void save()
    {
        try
        {
            cfg.addCustomCategoryComment("material",
                    "To add any overwrites, simply copy them over from the material.cfg");

            ConfigCategory material = cfg.getCategory("material");

            material.clear();

            for (Map.Entry<String, MaterialOverwrite> entry : overwrites.entrySet())
            {
                material.put(entry.getKey(),
                        new Property(entry.getKey(),
                                entry.getValue().getConfig(),
                                Property.Type.STRING));
            }
        }
        catch (Exception e)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem saving material overwrites file!",
                    e);
        }
        finally
        {
            cfg.save();
        }
    }

    public MaterialOverwrite get(String key)
    {
        if (overwrites.containsKey(key))
        {
            return overwrites.get(key);
        }

        return null;
    }

    @SuppressWarnings("unused")
    public MaterialOverwrite get(Item.ToolMaterial mat)
    {
        return get(mat.name().toLowerCase());
    }

    @SuppressWarnings("unused")
    public void unset(String key)
    {
        overwrites.remove(key);
    }

    @SuppressWarnings("unused")
    public void set(String key, MaterialOverwrite overwrite)
    {
        overwrites.put(key, overwrite);
    }
}
