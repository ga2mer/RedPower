package com.eloraam.redpower.core;

import com.eloraam.redpower.core.TagFile$1;
import com.eloraam.redpower.core.TagFile$Query;
import com.eloraam.redpower.core.TagFile$QueryEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class TagFile$Query$QueryIterator implements Iterator
{
    ArrayList path;
    String lastentry;

    final TagFile$Query this$1;

    private TagFile$Query$QueryIterator(TagFile$Query var1)
    {
        this.this$1 = var1;
        this.path = new ArrayList();
        TreeMap var2 = var1.this$0.contents;
        Object var3 = null;

        if (!this.step0(0, var1.this$0.contents, ""))
        {
            this.step();
        }
    }

    private void step()
    {
        while (true)
        {
            if (this.path != null)
            {
                if (!this.step1())
                {
                    continue;
                }

                return;
            }

            return;
        }
    }

    private boolean step1()
    {
        TagFile$QueryEntry var1 = (TagFile$QueryEntry)this.path.get(this.path.size() - 1);

        if (!var1.iter.hasNext())
        {
            this.path.remove(this.path.size() - 1);

            if (this.path.size() == 0)
            {
                this.path = null;
            }

            return false;
        }
        else
        {
            String var2 = (String)var1.iter.next();
            String var3 = var1.path.equals("") ? var2 : var1.path + "." + var2;

            if (var1.lvl == this.this$1.pattern.length - 1)
            {
                this.lastentry = var3;
                return true;
            }
            else
            {
                Object var4 = var1.tag.get(var2);
                return !(var4 instanceof TreeMap) ? false : this.step0(var1.lvl + 1, (TreeMap)var4, var3);
            }
        }
    }

    private boolean step0(int var1, TreeMap var2, String var3)
    {
        int var4 = var1;

        while (true)
        {
            if (var4 < this.this$1.pattern.length)
            {
                if (this.this$1.pattern[var4].equals("%"))
                {
                    TagFile$QueryEntry var6 = new TagFile$QueryEntry((TagFile$1)null);
                    var6.path = var3;
                    var6.tag = var2;
                    var6.lvl = var4;
                    var6.iter = var2.keySet().iterator();
                    this.path.add(var6);
                    return false;
                }

                Object var5 = var2.get(this.this$1.pattern[var4]);

                if (var3.equals(""))
                {
                    var3 = this.this$1.pattern[var4];
                }
                else
                {
                    var3 = var3 + "." + this.this$1.pattern[var4];
                }

                if (var5 instanceof TreeMap)
                {
                    var2 = (TreeMap)var5;
                    ++var4;
                    continue;
                }

                if (var4 == this.this$1.pattern.length - 1)
                {
                    this.lastentry = var3;
                    return true;
                }
            }

            this.path.remove(this.path.size() - 1);

            if (this.path.size() == 0)
            {
                this.path = null;
            }

            return false;
        }
    }

    public boolean hasNext()
    {
        return this.path != null;
    }

    public String next()
    {
        String var1 = this.lastentry;
        this.step();
        return var1;
    }

    public void remove() {}

    TagFile$Query$QueryIterator(TagFile$Query var1, TagFile$1 var2)
    {
        this(var1);
    }
}
