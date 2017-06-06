package tyra314.toolprogression.harvest;

import net.minecraft.item.Item;
import tyra314.toolprogression.config.ConfigHandler;

public class MaterialOverwrite
{
    int harvestLevel;

    public MaterialOverwrite(int harvestLevel)
    {
        this.harvestLevel = harvestLevel;
    }

    public String getConfig()
    {
        return String.valueOf(harvestLevel);
    }

    public static MaterialOverwrite readFromConfig(String config)
    {
        return new MaterialOverwrite(Integer.parseInt(config));
    }

    public static void applyToMaterial(Item.ToolMaterial mat)
    {
        MaterialOverwrite overwrite = ConfigHandler.matOverwrites.get(MaterialHelper.getName(mat));

        if (overwrite != null)
        {
            overwrite.apply(mat);
        }
    }

    public void apply(Item.ToolMaterial mat)
    {
        MaterialHelper.setHarvestLevel(mat, harvestLevel);
    }

    public int getHarvestLevel()
    {
        return harvestLevel;
    }
}
