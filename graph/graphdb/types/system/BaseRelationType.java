/*
 * Copyright (C) 2020 Grakn Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package grakn.core.graph.graphdb.types.system;

import com.google.common.base.Preconditions;
import grakn.core.graph.core.schema.ConsistencyModifier;
import grakn.core.graph.graphdb.idmanagement.IDManager;
import grakn.core.graph.graphdb.internal.JanusGraphSchemaCategory;
import grakn.core.graph.graphdb.internal.Token;
import org.apache.commons.lang.StringUtils;

public abstract class BaseRelationType extends EmptyRelationType implements SystemRelationType {

    private final String name;
    private final long id;

    BaseRelationType(String name, long id, JanusGraphSchemaCategory type) {
        Preconditions.checkArgument(StringUtils.isNotBlank(name));
        this.name = Token.systemETprefix + name;
        this.id = getSystemTypeId(id, type);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public long longId() {
        return id;
    }

    @Override
    public boolean hasId() {
        return true;
    }

    @Override
    public void setId(long id) {
        throw new IllegalStateException("SystemType has already been assigned an id");
    }

    @Override
    public ConsistencyModifier getConsistencyModifier() {
        return ConsistencyModifier.LOCK;
    }

    @Override
    public boolean isInvisibleType() {
        return true;
    }


    static long getSystemTypeId(long id, JanusGraphSchemaCategory type) {
        Preconditions.checkArgument(id > 0);
        Preconditions.checkArgument(type.isRelationType());
        switch (type) {
            case EDGELABEL:
                return IDManager.getSchemaId(IDManager.VertexIDType.SystemEdgeLabel, id);
            case PROPERTYKEY:
                return IDManager.getSchemaId(IDManager.VertexIDType.SystemPropertyKey, id);
            default:
                throw new AssertionError("Illegal argument: " + type);
        }
    }

}
