package tyra314.toolprogression.api;

import tyra314.toolprogression.harvest.*;
import java.util.*;
import net.minecraft.item.Item.ToolMaterial;

public class OverwrittenContent
{
    public static Map<String,BlockOverwrite> blocks = new HashMap<String,BlockOverwrite>();
    public static Map<String,ToolOverwrite> tools = new HashMap<String,ToolOverwrite>();
    public static Map<ToolMaterial,MaterialOverwrite> materials = new HashMap<ToolMaterial,MaterialOverwrite>();
}
