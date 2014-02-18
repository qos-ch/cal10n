package ch.qos.cal10n;

public class Settings
{
    private static boolean rootLanguageFallbackEnabled = false;
    
    public static void setRootLanguageFallbackEnabled(boolean enabled)
    {
        rootLanguageFallbackEnabled = enabled;
    }
    
    public static boolean isRootLanguageFallbackEnabled()
    {
        return rootLanguageFallbackEnabled ;
    }
}
