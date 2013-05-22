package com.eloraam.redpower.core;

public interface ICoverable
{
    boolean canAddCover(int var1, int var2);

    boolean tryAddCover(int var1, int var2);

    int tryRemoveCover(int var1);

    int getCover(int var1);

    int getCoverMask();
}
