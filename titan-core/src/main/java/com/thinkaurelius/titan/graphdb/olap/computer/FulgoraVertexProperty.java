package com.thinkaurelius.titan.graphdb.olap.computer;

import com.thinkaurelius.titan.core.RelationType;
import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.core.TitanVertexProperty;
import com.tinkerpop.gremlin.structure.Direction;
import com.tinkerpop.gremlin.structure.Property;
import com.tinkerpop.gremlin.structure.Vertex;
import com.tinkerpop.gremlin.structure.VertexProperty;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Matthias Broecheler (me@matthiasb.com)
 */
public class FulgoraVertexProperty<V> implements TitanVertexProperty<V>, VertexProperty.Iterators {

    private final VertexMemoryHandler mixinParent;
    private final TitanVertex vertex;
    private final String key;
    private final V value;
    private boolean isRemoved = false;

    public FulgoraVertexProperty(VertexMemoryHandler mixinParent, TitanVertex vertex, String key, V value) {
        this.mixinParent = mixinParent;
        this.vertex = vertex;
        this.key = key;
        this.value = value;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public V value() throws NoSuchElementException {
        return value;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public TitanVertex element() {
        return vertex;
    }

    @Override
    public void remove() {
        mixinParent.removeKey(key);
        isRemoved=true;
    }

    @Override
    public Object id() {
        return null;
    }

    @Override
    public long longId() {
        throw new IllegalStateException("An id has not been set for this property");
    }

    @Override
    public boolean hasId() {
        return false;
    }

    @Override
    public <V> Property<V> property(String s, V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> V valueOrNull(RelationType key) {
        return (V)property(key.name()).orElse(null);
    }

    @Override
    public boolean isNew() {
        return !isRemoved;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean isRemoved() {
        return isRemoved;
    }

    @Override
    public VertexProperty.Iterators iterators() {
        return this;
    }

    @Override
    public <V> V value(String key) {
        throw Property.Exceptions.propertyDoesNotExist(key);
    }

    @Override
    public RelationType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Direction direction(Vertex vertex) {
        if (isIncidentOn(vertex)) return Direction.OUT;
        throw new IllegalArgumentException("Property is not incident on vertex");
    }

    @Override
    public boolean isIncidentOn(Vertex vertex) {
        return this.vertex.equals(vertex);
    }

    @Override
    public boolean isLoop() {
        return false;
    }

    @Override
    public boolean isProperty() {
        return true;
    }

    @Override
    public boolean isEdge() {
        return false;
    }

    @Override
    public <U> Iterator<Property<U>> propertyIterator(String... strings) {
        return Collections.emptyIterator();
    }
}
