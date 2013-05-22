package com.eloraam.redpower.core;

import com.eloraam.redpower.core.TagFile$1;
import com.eloraam.redpower.core.TagFile$Query$QueryIterator;
import java.util.Iterator;

public class TagFile$Query implements Iterable
{
    String[] pattern;

    final TagFile this$0;

    private TagFile$Query(TagFile var1, String var2)
    {
        this.this$0 = var1;
        this.pattern = var2.split("\\.");
    }

    public Iterator iterator()
    {
        return new TagFile$Query$QueryIterator(this, (TagFile$1)null);
    }

    TagFile$Query(TagFile var1, String var2, TagFile$1 var3)
    {
        this(var1, var2);
    }
}
