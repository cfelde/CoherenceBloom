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

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.skjegstad.utils.BloomFilter;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.InvocableMapHelper;
import com.tangosol.util.MapIndex;
import com.tangosol.util.ValueExtractor;

/**
 *
 * @author Christian Felde (cfelde [at] cfelde [dot] com)
 */
public class BloomIndex implements MapIndex {
    private final BloomFilter<String> bloomFilter = new BloomFilter<String>(0.1, 40000);
    private ValueExtractor extractor;

    public BloomIndex() {
    }

    public BloomIndex(ValueExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public Object get(Object key) {
        return MapIndex.NO_VALUE;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Comparator getComparator() {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getIndexContents() {
        return Collections.EMPTY_MAP;
    }

    public BloomFilter<String> getBloomFilter() {
        return bloomFilter;
    }

    public String getBloomValue(String key, String value) {
        return new StringBuilder(key).append(":::").append(value).toString();
    }

    @Override
    public ValueExtractor getValueExtractor() {
        return extractor;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    @Override
    public boolean isPartial() {
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void insert(Entry entry) {
        BinaryEntry be = (BinaryEntry) entry;

        String key = be.getBinaryKey().toString();
        String value = (String) InvocableMapHelper.extractFromEntry(extractor, entry);
        bloomFilter.add(getBloomValue(key, value));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void update(Entry entry) {
        // Not supported
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void delete(Entry entry) {
        // Not supported
        throw new UnsupportedOperationException();
    }
}
