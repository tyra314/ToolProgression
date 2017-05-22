package tyra314.toolprogression.harvest;


import net.minecraft.item.Item;

public class MaterialHelper
{
    public static String getConfig(Item.ToolMaterial mat)
    {
        return String.valueOf(mat.getHarvestLevel());
    }

    public static String getName(Item.ToolMaterial mat)
    {
        return mat.name().toLowerCase();
    }

    public static void setHarvestLevel(Item.ToolMaterial mat, int newHarvestLevel)
    {
        // harvestLevel is a private field of Item.ToolMaterial
        // I have an AccessTransformer for that field, but
        // still this is the only place, where it is accessed within this mod.
        mat.harvestLevel = newHarvestLevel;
    }
}
