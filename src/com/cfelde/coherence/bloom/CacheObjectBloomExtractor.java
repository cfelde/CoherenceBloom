/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.cfelde.coherence.bloom;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import com.tangosol.net.BackingMapContext;
import com.tangosol.util.MapIndex;
import com.tangosol.util.extractor.IndexAwareExtractor;

/**
 *
 * @author Christian Felde (cfelde [at] cfelde [dot] com)
 */
public class CacheObjectBloomExtractor implements IndexAwareExtractor, Serializable {
    private static final long serialVersionUID = 7621963944351115266L;

    @Override
    public Object extract(Object target) {
        return ((CacheObject) target).getValue();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MapIndex createIndex(boolean ordered, Comparator comparator, Map mapIndex, BackingMapContext context) {
        MapIndex index = (MapIndex) mapIndex.get(this);

        if (index != null || ordered) {
            throw new IllegalArgumentException();
        }

        index = new BloomIndex(this);

        mapIndex.put(this, index);

        return index;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public MapIndex destroyIndex(Map mapIndex) {
        return (MapIndex) mapIndex.remove(this);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CacheObjectBloomExtractor;
    }
}
