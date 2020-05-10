package  Project_AIUS.View;

public enum ColorTheme {
    LIGHT,
    DARK;

    public static String getCssPath(ColorTheme colorTheme){

        switch (colorTheme) {
            case LIGHT:
                return "/css/LightTheme.css";
            case DARK:
                return "/css/DarkTheme.css";
            default:
                return null;
        }
    }
}

