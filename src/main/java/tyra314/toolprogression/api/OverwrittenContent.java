package tyra314.toolprogression.api;

import tyra314.toolprogression.harvest.*;
import java.util.*;
import net.minecraft.item.Item.ToolMaterial;

public class OverwrittenContent
{
    public static final Map<String,BlockOverwrite> blocks = new HashMap<>();
    public static final Map<String,ToolOverwrite> tools = new HashMap<>();
    public static final Map<ToolMaterial,MaterialOverwrite> materials = new HashMap<>();
    public static final Map<Integer, HarvestLevelName> mining_level = new HashMap<>();
}
