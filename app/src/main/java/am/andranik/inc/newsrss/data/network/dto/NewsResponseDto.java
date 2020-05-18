package am.andranik.inc.newsrss.data.network.dto;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "rss", strict = false)
public class NewsResponseDto {
    @Element(name = "channel", required = false)
    private ChannelDto channelDto;

    @Nullable
    public ChannelDto getChannelDto() {
        return channelDto;
    }
}
