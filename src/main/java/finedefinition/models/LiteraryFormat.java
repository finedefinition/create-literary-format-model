package finedefinition.models;

public class LiteraryFormat {
    private Long id;
    private String format;

    public LiteraryFormat() {
    }

    public LiteraryFormat(Long id, String format) {
        this.id = id;
        this.format = format;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "LiteraryFormat{"
                + "id=" + id + ", format='" + format + '\'' + '}';
    }
}
