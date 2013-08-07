CoherenceBloom
==============

An example Oracle Coherence Bloom filter based reverse index implementation.

To quote Wikipedia: A Bloom filter is a space-efficient probabilistic data structure that is used to test whether an element is a member of a set.

This is often used by systems in order to avoid accessing slow media, like a disk. Take HBase or Cassandra for instance: Instead of reading their data files in order to figure out if a particular key is present in a file, a file which might be huge, they can first consult their Bloom filters and get a true/false on if the key is likely to be in there. I say likely because a Bloom filter isn’t guaranteed to be correct. More formally: False positives are possible, but false negatives are not.

Given that false negatives can’t happen, the worst we’ll do is read a file we didn’t have to read. But we’re also guaranteed to read the file is the key is present, which is the type of guarantee we need.

Using a similar technique I managed to get almost a 2x performance gain on Oracle Coherence using a custom Bloom filter based index implementation (versus no index), using just a fraction of the memory a standard index instance would use (a few kB at most).

More info at http://blog.cfelde.com/2013/08/space-efficient-bloom-filter-index-2x-performance-gain/
