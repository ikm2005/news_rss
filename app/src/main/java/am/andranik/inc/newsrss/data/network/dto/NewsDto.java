package am.andranik.inc.newsrss.data.network.dto;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;

public class NewsDto {
    @Element(name = "title", required = false)
    private String title;
    @Element(name = "link", required = false)
    private String link;
    @Element(name = "pubDate", required = false)
    private String pubDate;

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getLink() {
        return link;
    }

    @Nullable
    public String getPubDate() {
        return pubDate;
    }
}
