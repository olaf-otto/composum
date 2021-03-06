package com.composum.sling.core;

import com.composum.sling.core.util.PropertyUtil;
import com.composum.sling.core.util.ResourceUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.HashMap;

/**
 * The value map which extends the property retrieval to the context (parents,...) of a resource.
 */
public class InheritedValues extends HashMap<String, Object> implements ValueMap {

    public static final Object UNDEFINED = new String("");

    protected final Resource resource;
    protected final boolean nodeInheritance;
    protected final boolean restrictToSameContent;

    protected transient Resource exitPoint;
    protected transient Resource entryPoint;
    protected transient String relativePath;

    public InheritedValues(Resource resource) {
        this(resource, false);
    }

    public InheritedValues(Resource resource, boolean nodeInheritance) {
        this(resource, nodeInheritance, true);
    }

    public InheritedValues(Resource resource, boolean nodeInheritance, boolean restrictToSameContent) {
        this.resource = resource;
        this.nodeInheritance = nodeInheritance;
        this.restrictToSameContent = restrictToSameContent;
    }

    /**
     * Gets an inherited value.
     *
     * @param name the property name or path
     * @param <T>  the expected type of the value
     * @return inherited value if existing or <code>null</code>
     */
    @Override
    public <T> T get(String name, Class<T> type) {
        Object value = get(name);
        if (value == null) {
            value = findInherited(name, type);
            if (value == null) {
                value = UNDEFINED;
            }
            put(name, value); // cache the found value
        }
        return value != UNDEFINED ? type.cast(value) : null;
    }

    /**
     * Gets an inherited value.
     *
     * @param name         the property name or path
     * @param defaultValue the default value, must not be <code>null</code>
     * @param <T>          the expected type of the value
     * @return inherited value if existing, otherwise the default value
     */
    @Override
    public <T> T get(String name, T defaultValue) {
        Class<T> type = PropertyUtil.getType(defaultValue);
        T value = (T) get(name, type);
        return value != null ? value : defaultValue;
    }

    protected <T> T findInherited(String name, Class<T> type) {
        findEntryPoint();
        T value = scanNodeHierarchy(name, type);
        // TODO if not found scan in the 'design' or context definition
        return value;
    }

    /**
     * Searches the value along the repositories hierarchy by the entry point and path determined before.
     *
     * @param name the property name or path
     * @param <T>  the expected type of the value
     * @return the value found or <code>null</code> if no such value found
     * in one of the appropriate parent nodes
     */
    protected <T> T scanNodeHierarchy(String name, Class<T> type) {
        T value;
        String path = relativePath + name;
        for (Resource parent = entryPoint; parent != null; parent = parent.getParent()) {
            ValueMap parentProps = parent.adaptTo(ValueMap.class);
            if (parentProps != null) {
                value = parentProps.get(path, type);
                if (value != null) {
                    return value;
                }
            }
            if (exitPoint != null && parent.getPath().equals(exitPoint.getPath())) {
                break;
            }
        }
        return null;
    }

    /**
     * Retrieves the first parent node for inheritance traversal
     * and defines the relativePath of the property.
     */
    protected void findEntryPoint() {
        if (entryPoint == null) {
            StringBuilder path = new StringBuilder();
            Resource parent = resource.getParent();
            String name = null;
            while (parent != null && !ResourceUtil.CONTENT_NODE.equals(name = parent.getName())) {
                if (path.length() > 0) {
                    path.insert(0, '/');
                }
                path.insert(0, name);
                parent = parent.getParent();
            }
            if (!nodeInheritance && parent != null && ResourceUtil.CONTENT_NODE.equals(name)) {
                // the resource is a child of an element with a content subtree ('jcr:content/...')
                path.insert(0, '/');
                path.insert(0, ResourceUtil.CONTENT_NODE);
                relativePath = path.toString();
                entryPoint = parent.getParent();
            } else {
                // no content subtree ('jcr:content/...') detected, use node inheritance
                relativePath = "";
                entryPoint = resource.getParent();
                if (restrictToSameContent && parent != null && ResourceUtil.CONTENT_NODE.equals(name)) {
                    exitPoint = parent;
                }
            }
        }
    }
}
