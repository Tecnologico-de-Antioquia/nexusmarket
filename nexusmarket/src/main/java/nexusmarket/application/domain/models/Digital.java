package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Digital extends Product {
    private String format;
    private String downloadUrl;

    public Digital(String id, String name, String description, String sellerId, String format, String downloadUrl) {
        super(id, name, description, sellerId);
        if (format == null || format.trim().isEmpty()) {
            throw new IllegalArgumentException("Format is required");
        }
        this.format = format.trim();
        this.downloadUrl = downloadUrl;
    }
}
