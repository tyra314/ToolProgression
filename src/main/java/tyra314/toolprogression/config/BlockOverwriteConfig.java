package tyra314.toolprogression.config;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.harvest.BlockHelper;
import tyra314.toolprogression.harvest.BlockOverwrite;

import java.util.HashMap;
import java.util.Map;

public class BlockOverwriteConfig
{
    private Configuration cfg;
    private final Map<String, BlockOverwrite> overwrites = new HashMap<>();


    BlockOverwriteConfig(Configuration file)
    {
        cfg = file;
    }

    public void load()
    {
        try
        {
            cfg.load();

            cfg.addCustomCategoryComment("block",
                    "To add any overwrites, simply copy them over from the block.cfg");

            overwrites.clear();

            for (Map.Entry<String, Property> tool : cfg.getCategory("block").entrySet())
            {
                ResourceLocation rl = new ResourceLocation(tool.getKey());
                BlockOverwrite
                        overwrite =
                        BlockOverwrite.readFromConfig(tool.getValue().getString());
                overwrites.put(tool.getKey(), overwrite);
            }
        }
        catch (Exception e)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem loading block overwrites file!", e);
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
            cfg.addCustomCategoryComment("block",
                    "To add any overwrites, simply copy them over from the block.cfg");

            ConfigCategory block = cfg.getCategory("block");

            block.clear();

            for (Map.Entry<String, BlockOverwrite> entry : overwrites.entrySet())
            {
                block.put(entry.getKey(),
                        new Property(entry.getKey(),
                                entry.getValue().getConfig(),
                                Property.Type.STRING));
            }
        }
        catch (Exception e)
        {
            ToolProgressionMod.logger.log(Level.ERROR, "Problem saving block overwrites file!", e);
        }
        finally
        {
            cfg.save();
        }
    }

    public BlockOverwrite get(String key)
    {
        if (overwrites.containsKey(key))
        {
            return overwrites.get(key);
        }

        return null;
    }


    public BlockOverwrite get(IBlockState state)
    {
        return get(BlockHelper.getKeyString(state));
    }


    public void unset(String key)
    {
        overwrites.remove(key);
    }


    public void set(String key, BlockOverwrite overwrite)
    {
        overwrites.put(key, overwrite);
    }
}
