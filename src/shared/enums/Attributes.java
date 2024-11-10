package shared.enums;

public enum Attributes {

    RESULT("RESULT"),
    STRATEGY("STRATEGY"),
    LIMIT("LIMIT"),
    OFFSET("OFFSET"),
    EMAIL("EMAIL"),
    NAME("NAME"),
    RECIPE_ID("RECIPE_ID"),
    USER("USER"),
    RECEITA("RECEITA"),
    INGREDIENTE("INGREDIENTE"),
    AUTOR("AUTOR");

    private final String description;

    // Construtor do enum com valor em String
    Attributes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
