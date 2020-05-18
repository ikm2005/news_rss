package am.andranik.inc.newsrss.data.network.dto;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

public class ChannelDto {
    @Element(name = "title", required = false)
    private String title;

    @ElementList(required = false, inline = true, entry = "item")
    private List<NewsDto> items;

    @Nullable
    public List<NewsDto> getItems() {
        return items;
    }
}
