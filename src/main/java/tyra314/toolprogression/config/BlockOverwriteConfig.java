package tyra314.toolprogression.config;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.harvest.BlockHelper;
import tyra314.toolprogression.harvest.BlockOverwrite;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockOverwriteConfig
{
    private final Map<String, BlockOverwrite> overwrites = new HashMap<>();
    private final Configuration cfg;


    BlockOverwriteConfig(Configuration file)
    {
        cfg = file;
    }

    private void handleBlockOverwrite(String block, String config)
    {
        BlockOverwrite overwrite = BlockOverwrite.createFromConfig(config);
        if (overwrite == null)
        {
            ToolProgressionMod.logger.log(Level.WARN,
                    String.format("Invalid block overwrite config for block: %s (%s)",
                            block,
                            config));
        }
        overwrites.put(block, overwrite);
    }

    public void load()
    {
        try
        {
            cfg.load();

            cfg.addCustomCategoryComment("block",
                    "To add any overwrites, simply copy them over from the block.cfg");

            overwrites.clear();

            @SuppressWarnings("Annotator")
            final String regex = "^(?<modid>[^:]+):(?<block>[^:]+):(?<meta>[\\d\\*]{1,2})$";

            final Pattern pattern = Pattern.compile(regex);

            ConfigCategory category = cfg.getCategory("block");
            for (Map.Entry<String, Property> block_entry : category.entrySet())
            {
                final Matcher matcher = pattern.matcher(block_entry.getKey());

                if (matcher.find())
                {
                    if (matcher.group("meta").equals("*"))
                    {
                        // Wildcard here lets try stuff, cuz we trump
                        for (int i = 0; i < 16; i++)
                        {

                            String block = matcher.group("modid") + ":" +
                                           matcher.group("block") + ":" +
                                           i;

                            if (category.containsKey(block))
                            {
                                handleBlockOverwrite(block, category.get(block).getString());
                            }
                            else
                            {
                                handleBlockOverwrite(block, block_entry.getValue().getString());
                            }
                        }
                    }
                    else
                    {
                        handleBlockOverwrite(block_entry.getKey(),
                                block_entry.getValue().getString());
                    }
                }
                else
                {
                    ToolProgressionMod.logger.log(Level.WARN,
                            "Invalid block overwrite entry: ",
                            block_entry.getKey());
                }
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
