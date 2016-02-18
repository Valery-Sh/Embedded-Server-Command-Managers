package org.netbeans.plugin.support.embedded.prefs;

import org.netbeans.plugin.support.embedded.jetty.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

public class InstancePreferences implements PreferencesProperties {

    private static final Logger LOG = Logger.getLogger(InstancePreferences.class.getName());

    private Preferences prefs;

    private final String id;

    public InstancePreferences(String id, Preferences prefs) {
        this.prefs = prefs;
        this.id = id;
    }

    public String[] keys() {
        try {
            return prefs.keys();
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
            return new String[]{};
        }
    }

    public Preferences getPreferences() {
        return prefs;
    }

    public String getId() {
        return id;
    }

    public boolean getBoolean(String key, boolean def) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            return prefs.getBoolean(key, def);
        }
    }

    public double getDouble(String key, double def) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            return prefs.getDouble(key, def);
        }
    }

    public float getFloat(String key, float def) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            return prefs.getFloat(key, def);
        }
    }

    public int getInt(String key, int def) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            return prefs.getInt(key, def);
        }
    }

    public long getLong(String key, long def) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            return prefs.getLong(key, def);
        }
    }

    public String getString(String key, String def) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            return prefs.get(key, def);
        }
    }

    public void putBoolean(String key, boolean value) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            prefs.putBoolean(key, value);
        }
    }

    public void putDouble(String key, double value) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            prefs.putDouble(key, value);
        }
    }

    public void putFloat(String key, float value) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            prefs.putFloat(key, value);
        }
    }

    public void putInt(String key, int value) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            prefs.putInt(key, value);
        }
    }

    public void putLong(String key, long value) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            prefs.putLong(key, value);
        }
    }

    public InstancePreferences setProperty(String propName, String value) {
        this.putString(propName, value);
        return this;
    }

    public String getProperty(String propName) {
        return this.getString(propName, null);
    }

    public void putString(String key, String value) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            prefs.put(key, value);
        }
    }

    @Override
    public void removeKey(String key) {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            prefs.remove(key);
        }
    }

    public boolean remove() {

        boolean success = false;

        try {
            synchronized (this) {
                if (prefs != null) {
                    prefs.removeNode();
                }
                success = !prefs.nodeExists("");
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
        }
        return success;
    }

    //
    //
    //
    public Map<String, String> toMap() {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            Map<String, String> map = new HashMap<>();
            for (String key : keys()) {
                map.put(key, prefs.get(key, null));
            }
            return map;
        }
    }
    public Properties toProperties() {
        synchronized (this) {
            if (prefs == null) {
                throw new IllegalStateException("Properties are not valid anymore");
            }
            Properties props = new Properties();
            for (String key : keys()) {
                props.put(key, prefs.get(key, null));
            }
            return props;
        }
    }

    public PreferencesProperties copyFrom(Properties props) {
        if (props == null || props.isEmpty()) {
            return this;
        }

        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String nm = (String) en.nextElement();
            putString(nm, props.getProperty(nm));
        }
        return this;
    }

    public PreferencesProperties copyFrom(Map<String,String> props) {
        if (props == null || props.isEmpty()) {
            return this;
        }
        props.forEach( (k,v) -> {
            putString(k,v);
        });
        
        return this;
    }
    
    @Override
    public void clear() {
        try {
            synchronized (this) {
                if (prefs != null) {
                    for (String key : prefs.keys()) {
                        prefs.remove(key);
                    }
                }
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
        }

    }

    public void removeKeys(Predicate<String> predicate) {
        for (String key : keys()) {
            if (predicate.test(key)) {
                removeKey(key);
            }
        }
    }

    public void forEach(BiConsumer<String, String> action) {
        String[] keys = keys();
        for (String key : keys) {
            action.accept(key, getString(key, null));
        }

    }
    @Override
    public Map<String, String> filter(BiPredicate<String, String> predicate) {
        Map<String, String> map = new HashMap<>();
        String[] keys = keys();
        for (String key : keys) {
            String value = getString(key, null);
            if (predicate.test(key, value)) {
                map.put(key, value);
            }
        }
        return map;
    }
    @Override
    public Stream<String> keyStream() {
        List<String> list = Arrays.asList(keys());
        return list.stream();
    }

    @Override
    public int size() {
        return keys().length;
    }
}
