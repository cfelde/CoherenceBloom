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

import java.util.Random;
import java.util.UUID;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.filter.EqualsFilter;

/**
 *
 * @author Christian Felde (cfelde [at] cfelde [dot] com)
 */
public class RunStandardIndexClient {
    private static final long NANO_TO_MILLISECONDS = 1000000;

    public static void main(String... args) throws Exception {
        System.setProperty("tangosol.coherence.cacheconfig", "client-config.xml");
        NamedCache cache = CacheFactory.getCache("dist-cache");

        int size = 100000;

        ValueExtractor extractor = new CacheObjectExtractor();
        cache.addIndex(extractor, false, null);

        System.out.println("Starting cache puts..");
        for (int i = 0; i < size; i++) {
            cache.put(UUID.randomUUID().toString(), new CacheObject("Value" + i));
        }

        Random random = new Random(0L);

        System.out.println("Starting random gets..");
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            String key = "Value" + random.nextInt(size);
            int test = (Integer) cache.aggregate(new EqualsFilter(extractor, key), new Count());
            if (test != 1) {
                throw new RuntimeException("Failed count, on key: " + key + ": " + test);
            }
        }
        long end = System.nanoTime();
        System.out.println("Get time: " + ((end - start) / NANO_TO_MILLISECONDS));
    }
}
