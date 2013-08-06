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
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tangosol.util.Binary;
import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMapHelper;
import com.tangosol.util.QueryMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.IndexAwareFilter;

/**
 *
 * @author Christian Felde (cfelde [at] cfelde [dot] com)
 */
public class BloomEqualsFilter implements IndexAwareFilter, Serializable {
    private static final long serialVersionUID = -4561522940909935752L;
    private ValueExtractor extractor;
    private String value;

    public BloomEqualsFilter() {
    }

    public BloomEqualsFilter(ValueExtractor extractor, String value) {
        this.extractor = extractor;
        this.value = value;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean evaluateEntry(Entry entry) {
        return evaluateExtracted((entry instanceof QueryMap.Entry) ? ((QueryMap.Entry) entry)
                .extract(extractor) : InvocableMapHelper.extractFromEntry(
                extractor, entry));
    }

    private boolean evaluateExtracted(Object target) {
        return value.equals(target);
    }

    @Override
    public boolean evaluate(Object target) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Filter applyIndex(Map indexMap, Set keySet) {
        BloomIndex index = (BloomIndex) indexMap.get(extractor);

        if (index == null) {
            return this;
        }

        Set removeKeys = new HashSet();
        for (Object oKey : keySet) {
            String key = ((Binary) oKey).toString();
            if (!index.getBloomFilter().contains(index.getBloomValue(key, value))) {
                removeKeys.add(oKey);
            }
        }

        keySet.removeAll(removeKeys);

        if (keySet.isEmpty()) {
            return null;
        }

        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int calculateEffectiveness(Map indexMap, Set keySet) {
        return 1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((extractor == null) ? 0 : extractor.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BloomEqualsFilter other = (BloomEqualsFilter) obj;
        if (extractor == null) {
            if (other.extractor != null) {
                return false;
            }
        } else if (!extractor.equals(other.extractor)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BloomEqualsFilter [extractor=" + extractor + ", value=" + value + "]";
    }
}
