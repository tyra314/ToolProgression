package tyra314.toolprogression.harvest;

import net.minecraft.item.Item;
import tyra314.toolprogression.api.OverwrittenContent;
import tyra314.toolprogression.config.ConfigHandler;

public class MaterialOverwrite
{
    @SuppressWarnings("WeakerAccess")
    final int harvestLevel;

    @SuppressWarnings("WeakerAccess")
    public MaterialOverwrite(int harvestLevel)
    {
        this.harvestLevel = harvestLevel;
    }

    public static MaterialOverwrite readFromConfig(String config)
    {
        return new MaterialOverwrite(Integer.parseInt(config));
    }

    public static void applyToMaterial(Item.ToolMaterial mat)
    {
        MaterialOverwrite overwrite = ConfigHandler.matOverwrites.get(
                mat.name().toLowerCase()
        );

        if (overwrite != null)
        {
            overwrite.applyTo(mat);
        }
        OverwrittenContent.materials.put(mat, overwrite);
    }

    public String getConfig()
    {
        return String.valueOf(harvestLevel);
    }

    @SuppressWarnings("WeakerAccess")
    public void applyTo(Item.ToolMaterial mat)
    {
        // harvestLevel is a private field of Item.ToolMaterial
        // I have an AccessTransformer for that field, but
        // still this is the only place, where it is accessed within this mod.
        mat.harvestLevel = harvestLevel;
    }

    public int getHarvestLevel()
    {
        return harvestLevel;
    }
}
