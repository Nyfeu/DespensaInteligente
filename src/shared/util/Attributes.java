package shared.util;

public enum Attributes {

    RESULT("RESULT"),
    STRATAGY("STRATEGY"),
    LIMIT("LIMIT"),
    OFFSET("OFFSET"),
    EMAIL("EMAIL"),
    NAME("NAME"),
    RECIPE_ID("RECIPE_ID"),
    USER("USER"),
    RECEITA("RECEITA"),
    INGREDIENTE("INGREDIENTE");

    private final String description;

    // Construtor do enum com valor em String
    Attributes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
