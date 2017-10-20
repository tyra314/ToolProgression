package tyra314.toolprogression.compat.tconstruct;

import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.repository.BookRepository;

import java.util.List;

public class ToolProgressionBookRepo extends BookRepository
{
    @Override
    public List<SectionData> getSections()
    {
        return null;
    }

    @Override
    public ResourceLocation getResourceLocation(String path, boolean safe)
    {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation loc)
    {
        return null;
    }

    @Override
    public boolean resourceExists(ResourceLocation location)
    {
        return false;
    }

    @Override
    public String resourceToString(IResource resource, boolean skipCommments)
    {
        return null;
    }
}
