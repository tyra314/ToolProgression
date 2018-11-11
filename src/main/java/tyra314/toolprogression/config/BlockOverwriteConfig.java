package tyra314.toolprogression.config;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
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

    private void handleBlockOverwrite(String block, String config,
                                      BlockOverwrite.OverwriteSource source, String sourceKey)
    {
        BlockOverwrite overwrite = BlockHelper.createFromConfigString(config, source, sourceKey);
        if (overwrite == null)
        {
            ToolProgressionMod.logger.log(Level.WARN,
                    String.format("Invalid block overwrite config for block: %s (%s)",
                            block,
                            config));
        }

        if (overwrites.containsKey(block))
        {
            if(overwrites.get(block).source.hasLessPriority(source))
            {
                overwrites.put(block, overwrite);
            }
        }
        else
        {
            overwrites.put(block, overwrite);
        }
    }

    public void load()
    {
        try
        {
            cfg.load();

            cfg.addCustomCategoryComment("block",
                    "To add any overwrites, simply copy them over from the block.cfg");

            cfg.addCustomCategoryComment("oreDict",
                    "Add overwrites to all blocks in the given OreDict entry");

            overwrites.clear();

            parseOreDictCategory(cfg.getCategory("oreDict"));
            parseBlockCategory(cfg.getCategory("block"));
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

    private void parseBlockCategory(ConfigCategory category)
    {
        @SuppressWarnings("Annotator") final String
                regex =
                "^(?<modid>[^:]+):(?<block>[^:]+):(?<meta>[\\d\\*]{1,2})$";

        final Pattern pattern = Pattern.compile(regex);

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

                        handleBlockOverwrite(block,
                                block_entry.getValue().getString(),
                                BlockOverwrite.OverwriteSource.Wildcard, block_entry.getKey());
                    }
                }
                else
                {
                    handleBlockOverwrite(block_entry.getKey(),
                            block_entry.getValue().getString(),
                            BlockOverwrite.OverwriteSource.Single, block_entry.getKey());
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

    private void parseOreDictCategory(ConfigCategory category)
    {
        @SuppressWarnings("Annotator") final String regex = "^ore:(?<entry>.+)$";

        final Pattern pattern = Pattern.compile(regex);

        for (Map.Entry<String, Property> ore_entry : category.entrySet())
        {
            final Matcher matcher = pattern.matcher(ore_entry.getKey());

            if (matcher.find())
            {
                String entry = matcher.group("entry");

                if (!OreDictionary.doesOreNameExist(entry))
                {
                    ToolProgressionMod.logger.log(Level.WARN,
                            "Invalid oreDict overwrite entry: ",
                            ore_entry.getKey());

                    continue;
                }

                NonNullList<ItemStack> ores = OreDictionary.getOres(entry);

                for (ItemStack ore : ores)
                {
                    if (ore.getItem() instanceof ItemBlock)
                    {
                        Block block = ((ItemBlock) ore.getItem()).getBlock();

                        if (ore.getMetadata() == OreDictionary.WILDCARD_VALUE)
                        {
                            for (IBlockState state : block.getBlockState().getValidStates())
                            {
                                String key = BlockHelper.getKeyString(state);

                                handleBlockOverwrite(key, ore_entry.getValue().getString(),
                                        BlockOverwrite.OverwriteSource.OreDict, ore_entry.getKey());

                            }
                        }
                        else
                        {
                            String key = BlockHelper.getKeyFromItemStack(ore);

                            handleBlockOverwrite(key, ore_entry.getValue().getString(),
                                    BlockOverwrite.OverwriteSource.OreDict, ore_entry.getKey());
                        }

                    }
                }
            }
            else
            {
                ToolProgressionMod.logger.log(Level.WARN,
                        "Invalid oreDict overwrite entry: ",
                        ore_entry.getKey());
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
                BlockOverwrite overwrite = entry.getValue();

                if (overwrite.source == BlockOverwrite.OverwriteSource.OreDict)
                {
                    // If it came from a oreDict, we don't write this.
                    // This would be messy AS FUCK
                    continue;
                }

                if (overwrite.source == BlockOverwrite.OverwriteSource.Wildcard)
                {
                    block.put(overwrite.sourceKey,
                            new Property(overwrite.sourceKey,
                                    overwrite.getConfig(),
                                    Property.Type.STRING));

                }
                else if (overwrite.source == BlockOverwrite.OverwriteSource.Single)
                {
                    block.put(entry.getKey(),
                            new Property(entry.getKey(),
                                    overwrite.getConfig(),
                                    Property.Type.STRING));
                }
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
