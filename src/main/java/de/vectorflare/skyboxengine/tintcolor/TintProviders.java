package de.vectorflare.skyboxengine.tintcolor;

import java.util.HashMap;
import java.util.Map;

public class TintProviders {

    private final Map<String,TintProvider> registeredProviders = new HashMap<>();

    public void registerTintProvider(TintProvider provider) {
        registeredProviders.put(provider.getName(),provider);
    }


    public TintProvider getTintProvider(String name) {
        return registeredProviders.get(name);
    }


}
